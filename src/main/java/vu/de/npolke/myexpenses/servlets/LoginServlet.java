package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.AccountDAO;
import vu.de.npolke.myexpenses.services.DAOFactory;
import vu.de.npolke.myexpenses.services.StatisticsDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;
import vu.de.npolke.myexpenses.servlets.util.StatisticsPair;

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
@WebServlet("/login")
public class LoginServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	AccountDAO accountDAO = (AccountDAO) DAOFactory.getDAO(Account.class);
	StatisticsDAO statisticsDAO = (StatisticsDAO) DAOFactory.getDAO(StatisticsPair.class);

	@Override
	public ServletReaction doPost(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		final String login = request.getParameter("login");
		final String password = request.getParameter("password");
		final String redirectURL = (String) session.getAttribute("redirectAfterLogin");

		return login(login, password, redirectURL);
	}

	public ServletReaction login(final String login, final String password, final String redirectURL) {
		ServletReaction reaction = new ServletReaction();

		Account account = accountDAO.readByLogin(login, password);

		if (account != null) {
			List<Expense> topten = statisticsDAO.readTopTenByAccountId(account.getId());
			reaction.setSessionAttribute("account", account);
			reaction.setSessionAttribute("topten", topten);
			if (redirectURL != null && redirectURL.length() > 0) {
				reaction.setRedirect(redirectURL);
				reaction.setSessionAttribute("redirectAfterLogin", null);
			} else {
				reaction.setRedirect("listexpenses");
			}
		} else {
			reaction.setRequestAttribute("errorMessage", "unknown login or wrong password");
			reaction.setForward("index.jsp");
		}

		return reaction;
	}
}
