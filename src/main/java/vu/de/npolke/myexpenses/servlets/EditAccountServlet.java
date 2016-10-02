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
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;
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
@WebServlet("/editaccount.jsp")
public class EditAccountServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	AccountDAO accountDAO = (AccountDAO) DAOFactory.getDAO(Account.class);

	@Override
	protected ServletReaction doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		return prepareEditAccount(account);
	}

	public ServletReaction prepareEditAccount(final Account account) {
		ServletReaction reaction = new ServletReaction();
		reaction.setForward("WEB-INF/editaccount.jsp");
		return reaction;
	}

	@Override
	public ServletReaction doPost(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		final String oldpassword = request.getParameter("oldpassword");
		final String newpassword1 = request.getParameter("newpassword1");
		final String newpassword2 = request.getParameter("newpassword2");
		final String login = request.getParameter("login");

		return editAccount(account, oldpassword, newpassword1, newpassword2, login);
	}

	public ServletReaction editAccount(final Account account, final String oldPassword, final String newPassword1,
			final String newPassword2, final String login) {
		ServletReaction reaction = new ServletReaction();
		boolean validationErrors = false;
		if (!account.getPassword().equals(HashUtil.toMD5(oldPassword))) {
			handleIncorrectInput(reaction, "old password wasn't correct");
			validationErrors = true;
		} else {
			Account accountUpdate = account.clone();
			if (!account.getLogin().equals(login)) {
				if (login != null && login.trim().length() >= 4) {
					accountUpdate.setLogin(login.trim());
				} else {
					handleIncorrectInput(reaction, "login has to be at least 4 characters long");
					validationErrors = true;
				}
			}
			if ((newPassword1 != null && newPassword1.length() > 0) || (newPassword2 != null && newPassword2.length() > 0)) {
				if (newPassword1 != null && newPassword1.trim().length() > 3 && newPassword1.equals(newPassword2)) {
					accountUpdate.setPassword(HashUtil.toMD5(newPassword1));
				} else {
					handleIncorrectInput(reaction, "new password 1 wasn't equal to new password 2");
					validationErrors = true;
				}
			}

			if (!validationErrors) {
				boolean success = accountDAO.update(accountUpdate);
				if (success) {
					account.setLogin(accountUpdate.getLogin());
					account.setPassword(accountUpdate.getPassword());
					reaction.setRedirect("listexpenses.jsp");
				} else {
					handleIncorrectInput(reaction, "login \"" + accountUpdate.getLogin() + "\" already in use");
				}
			}
		}

		return reaction;
	}

	private void handleIncorrectInput(final ServletReaction reaction, final String errorMessage) {
		if (reaction.getRequestAttributes().get("errorMessage") == null) {
			reaction.setRequestAttribute("errorMessage", errorMessage);
		} else {
			String formerErrorMessage = (String) reaction.getRequestAttributes().get("errorMessage");
			reaction.setRequestAttribute("errorMessage", formerErrorMessage + "<br/>" + errorMessage);
		}
		reaction.setForward("WEB-INF/editaccount.jsp");
	}
}
