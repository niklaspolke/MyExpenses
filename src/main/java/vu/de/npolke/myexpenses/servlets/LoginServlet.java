package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.AccountDAO;
import vu.de.npolke.myexpenses.services.DAOFactory;
import vu.de.npolke.myexpenses.services.StatisticsDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;
import vu.de.npolke.myexpenses.util.StatisticsElement;

/**
 * Copyright 2015 Niklas Polke
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author Niklas Polke
 */
@WebServlet("/login.jsp")
public class LoginServlet extends AbstractBasicServlet {

	public static final String COOKIE_LOCALE = "locale";
	public static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 30;

	private static final long serialVersionUID = 1L;

	AccountDAO accountDAO = (AccountDAO) DAOFactory.getDAO(Account.class);
	StatisticsDAO statisticsDAO = (StatisticsDAO) DAOFactory.getDAO(StatisticsElement.class);

	@Override
	public ServletReaction doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		Cookie[] cookies = request.getCookies();
		Cookie localeCookie = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (COOKIE_LOCALE.equals(cookie.getName())) {
					localeCookie = cookie;
					break;
				}
			}
		}

		return initialiseLocale(localeCookie);
	}

	public ServletReaction initialiseLocale(final Cookie localeCookie) {
		ServletReaction reaction = new ServletReaction();
		reaction.setForward("WEB-INF/login.jsp");
		if (localeCookie != null && COOKIE_LOCALE.equals(localeCookie.getName())) {
			reaction.setSessionAttribute(COOKIE_LOCALE, localeCookie.getValue());
		}
		return reaction;
	}

	@Override
	public ServletReaction doPost(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		final String login = request.getParameter("login");
		final String password = request.getParameter("password");
		final String redirectURL = (String) session.getAttribute("redirectAfterLogin");
		final String locale = request.getParameter(COOKIE_LOCALE);
		Cookie localeCookie = new Cookie(COOKIE_LOCALE, locale);
		localeCookie.setMaxAge(COOKIE_MAX_AGE);
		response.addCookie(localeCookie);

		return login(login, password, redirectURL, localeCookie);
	}

	public ServletReaction login(final String login, final String password, final String redirectURL,
			final Cookie localeCookie) {
		ServletReaction reaction = new ServletReaction();
		reaction.setSessionAttribute(COOKIE_LOCALE, localeCookie.getValue());

		Account account = accountDAO.readByLogin(login, password);

		if (account != null) {
			List<Expense> topten = statisticsDAO.readTopTenByAccountId(account.getId());
			reaction.setSessionAttribute("account", account);
			reaction.setSessionAttribute("topten", topten);
			if (redirectURL != null && redirectURL.length() > 0) {
				reaction.setRedirect(redirectURL);
				reaction.setSessionAttribute("redirectAfterLogin", null);
			} else {
				reaction.setRedirect("listexpenses.jsp");
			}
		} else {
			reaction.setRedirect("login.jsp").add("error", "error.login.notfound");
		}

		return reaction;
	}
}
