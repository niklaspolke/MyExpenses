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
import vu.de.npolke.myexpenses.util.HashUtil;

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
@WebServlet("/editaccount")
public class EditAccountServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	private AccountDAO accountDAO = (AccountDAO) DAOFactory.getDAO(Account.class);

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		session.setAttribute("account", account);
		response.sendRedirect("editaccount.jsp");
	}

	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response, final HttpSession session,
			Account account) throws ServletException, IOException {

		final String oldpassword = request.getParameter("oldpassword");
		final String newpassword1 = request.getParameter("newpassword1");
		final String newpassword2 = request.getParameter("newpassword2");

		if (!account.getPassword().equals(HashUtil.toMD5(oldpassword))) {
			request.setAttribute("errorMessage", "oldpassword wasn't correct");
			request.getRequestDispatcher("editaccount.jsp").forward(request, response);
		} else if (newpassword1 != null && newpassword1.trim().length() > 3 && newpassword1.equals(newpassword2)) {
			account.setPassword(HashUtil.toMD5(newpassword1));
			accountDAO.update(account);
			response.sendRedirect("listexpenses");
		} else {
			request.setAttribute("errorMessage", "password1 wasn't equal to password2");
			request.getRequestDispatcher("editaccount.jsp").forward(request, response);
		}
	}
}
