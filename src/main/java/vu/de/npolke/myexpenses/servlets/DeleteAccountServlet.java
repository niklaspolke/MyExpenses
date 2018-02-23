package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Category;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.AccountDAO;
import vu.de.npolke.myexpenses.services.CategoryDAO;
import vu.de.npolke.myexpenses.services.DAOFactory;
import vu.de.npolke.myexpenses.services.ExpenseDAO;
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
@WebServlet("/deleteaccount.jsp")
public class DeleteAccountServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	AccountDAO accountDAO = (AccountDAO) DAOFactory.getDAO(Account.class);
	CategoryDAO categoryDAO = (CategoryDAO) DAOFactory.getDAO(Category.class);
	ExpenseDAO expenseDAO = (ExpenseDAO) DAOFactory.getDAO(Expense.class);

	@Override
	public ServletReaction doPost(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		final String password = request.getParameter("password");

		final ServletReaction reaction = deleteAccount(account, password);

		if (reaction.getDoRedirect()) {
			session.invalidate();
		}

		return reaction;
	}

	public ServletReaction deleteAccount(final Account account, final String password) {
		ServletReaction reaction = new ServletReaction();
		if (!account.getPassword().equals(HashUtil.toMD5(password))) {
			handleIncorrectInput(reaction, "error.deleteaccount.wrongpassword");
		} else {
			expenseDAO.deleteByAccountId(account.getId());
			categoryDAO.deleteByAccountId(account.getId());
			boolean success = 1 == accountDAO.deleteByAccountId(account.getId());
			if (success) {
				reaction.setRedirect("login.jsp");
			} else {
				handleIncorrectInput(reaction, "error.deleteaccount.unknownerror");
			}
		}

		return reaction;
	}

	private void handleIncorrectInput(final ServletReaction reaction, final String errorMessage) {
		reaction.setRequestAttribute("errorMessage", errorMessage);
		reaction.setForward("WEB-INF/editaccount.jsp");
	}
}
