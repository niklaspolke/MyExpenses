package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.services.AccountDAO;
import vu.de.npolke.myexpenses.services.DAOFactory;

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
@WebServlet("/register")
public class RegisterServlet extends AbstractBasicServletOld {

	private static final long serialVersionUID = 1L;

	private AccountDAO accountDAO = (AccountDAO) DAOFactory.getDAO(Account.class);

	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response, final HttpSession session,
			Account account) throws ServletException, IOException {

		final String login = request.getParameter("login");
		final String password1 = request.getParameter("password1");
		final String password2 = request.getParameter("password2");

		if (password1.equals(password2)) {
			account = accountDAO.create(login, password1);
		}

		if (account != null) {
			session.setAttribute("account", account);
			response.sendRedirect("listexpenses");
		} else {
			request.setAttribute("errorMessage", "password1 wasn't equal to password2");
			request.getRequestDispatcher("register.jsp").forward(request, response);
		}
	}
}
