package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.DAOFactory;
import vu.de.npolke.myexpenses.services.ExpenseDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;

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
@WebServlet("/deleteexpense.jsp")
public class DeleteExpenseServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	ExpenseDAO expenseDAO = (ExpenseDAO) DAOFactory.getDAO(Expense.class);

	@Override
	protected ServletReaction doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		String expenseId = request.getParameter("id");
		String isDeleteConfirmed = request.getParameter("confirmed");

		return deleteExpense(account, expenseId, isDeleteConfirmed);
	}

	public ServletReaction deleteExpense(final Account account, final String idAsString, final String confirmed) {
		long expenseId = Long.parseLong(idAsString);

		ServletReaction reaction = new ServletReaction();
		Expense expense = expenseDAO.read(account.getId(), expenseId);
		if (expense != null && expense.isMonthly()) {
			reaction.setRedirect("listexpenses.jsp").add("monthly", true).add("back", "true");
		} else {
			reaction.setRedirect("listexpenses.jsp").add("back", "true");
		}

		if ("yes".equalsIgnoreCase(confirmed)) {
			if (expense != null) {
				expenseDAO.deleteById(expenseId);
			} else {
				reaction.setRequestAttribute("errorMessage",
						"You tried to delete a non existing expense or an expense that isn't yours!");
				reaction.setForward("WEB-INF/error.jsp");
			}
		}

		return reaction;
	}
}
