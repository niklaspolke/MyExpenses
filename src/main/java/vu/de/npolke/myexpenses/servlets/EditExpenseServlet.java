package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Category;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.CategoryDAO;
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
@WebServlet("/editexpense")
public class EditExpenseServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	ExpenseDAO expenseDAO = (ExpenseDAO) DAOFactory.getDAO(Expense.class);
	CategoryDAO categoryDAO = (CategoryDAO) DAOFactory.getDAO(Category.class);

	@Override
	protected ServletReaction doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		String expenseId = request.getParameter("id");

		return prepareEditExpense(account, expenseId);
	}

	public ServletReaction prepareEditExpense(final Account account, final String expenseId) {
		ServletReaction reaction = new ServletReaction();
		long id = Long.parseLong(expenseId);

		List<Category> categories = categoryDAO.readByAccountId(account.getId());
		Expense expense = expenseDAO.read(id);

		if (expense == null || expense.getAccountId() != account.getId()) {
			reaction.setRequestAttribute("errorMessage",
					"You tried to edit a non existing expense or an expense that isn't yours!");
			reaction.setForward("error.jsp");
		} else {
			reaction.setSessionAttribute("expense", expense);
			reaction.setSessionAttribute("categories", categories);
			reaction.setRedirect("editexpense.jsp");
		}
		return reaction;
	}

	@Override
	public ServletReaction doPost(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		String amount = request.getParameter("amount");
		String reason = request.getParameter("reason");
		String day = request.getParameter("day");
		String month = request.getParameter("month");
		String year = request.getParameter("year");
		String categoryId = request.getParameter("category");

		Expense expense = (Expense) session.getAttribute("expense");

		return editExpense(expense, amount, reason, day, month, year, categoryId);
	}

	public ServletReaction editExpense(final Expense expense, final String amountAsString, final String reason,
			final String day, final String month, final String year, final String categoryIdAsString) {
		double amount = Double.parseDouble(amountAsString.replaceAll(",", "."));
		long categoryId = Long.parseLong(categoryIdAsString);

		expense.setAmount(amount);
		expense.setReason(reason);
		expense.setReadableDayAsString(day + "." + month + "." + year);
		expense.setCategoryId(categoryId);
		expenseDAO.update(expense);

		ServletReaction reaction = new ServletReaction();
		reaction.setRedirect("listexpenses");
		return reaction;
	}
}
