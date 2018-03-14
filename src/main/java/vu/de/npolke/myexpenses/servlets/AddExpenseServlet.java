package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
import vu.de.npolke.myexpenses.util.ApplicationStatisticTypes;
import vu.de.npolke.myexpenses.util.ApplicationStatistics;

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
@WebServlet("/addexpense.jsp")
public class AddExpenseServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	protected static final int MAX_LENGTH_REASON = 40;

	ExpenseDAO expenseDAO = (ExpenseDAO) DAOFactory.getDAO(Expense.class);
	CategoryDAO categoryDAO = (CategoryDAO) DAOFactory.getDAO(Category.class);
	ApplicationStatistics statistics = ApplicationStatistics.getSingleton();

	@Override
	protected ServletReaction doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		final String id = request.getParameter("id");
		final String reason = request.getParameter("reason");
		final String categoryId = request.getParameter("category");

		return prepareAddExpense(account, id, reason, categoryId);
	}

	public ServletReaction prepareAddExpense(final Account account, final String expenseId, final String reason,
			final String categoryId) {
		List<Category> categories = categoryDAO.readByAccountId(account.getId());
		Calendar now = Calendar.getInstance(Locale.GERMANY);
		now.setTimeInMillis(System.currentTimeMillis());

		boolean errorOccured = false;
		ServletReaction reaction = new ServletReaction();
		reaction.setRequestAttribute("categoryPreset", null);

		try {
			long id = Long.parseLong(expenseId);
			Expense expense = expenseDAO.read(account.getId(), id);
			if (expense == null) {
				errorOccured = true;
			} else {
				expense.setId(0);
				Calendar originalDay = (Calendar) expense.getDay().clone();
				expense.setDay(now.getTime());
				reaction.setRequestAttribute("expense", expense);
				reaction.setRequestAttribute("originalday", originalDay);
				reaction.setRequestAttribute("categoryPreset", Boolean.TRUE);
			}
		} catch (NumberFormatException nfe) {
			Expense defaultExpense = new Expense();
			defaultExpense.setDay(now.getTime());
			if (reason != null) {
				defaultExpense.setReason(
						reason.length() > MAX_LENGTH_REASON ? reason.substring(0, MAX_LENGTH_REASON) : reason);
			}
			try {
				final long categoryIdAsLong = Long.parseLong(categoryId);
				for (Category category : categories) {
					if (category.getId() == categoryIdAsLong) {
						defaultExpense.setCategoryId(category.getId());
						reaction.setRequestAttribute("categoryPreset", Boolean.TRUE);
						break;
					}
				}
			} catch (NumberFormatException nfe2) {
			}
			reaction.setRequestAttribute("expense", defaultExpense);
		}

		if (errorOccured) {
			reaction.setRequestAttribute("errorMessage", "error.addexpense.wrongid");
			reaction.setForward("WEB-INF/error.jsp");
		} else {
			reaction.setRequestAttribute("categories", categories);
			reaction.setForward("WEB-INF/addexpense.jsp");
		}

		return reaction;
	}

	@Override
	public ServletReaction doPost(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		String amount = request.getParameter("amount").replaceAll(",", ".");
		String reason = request.getParameter("reason");
		String monthly = request.getParameter("monthly");
		String income = request.getParameter("income");
		String day = request.getParameter("day");
		String month = request.getParameter("month");
		String year = request.getParameter("year");
		String categoryId = request.getParameter("category");

		return addExpense(account, amount, reason, monthly, income, day, month, year, categoryId);
	}

	public ServletReaction addExpense(final Account account, final String amountAsString, final String reason,
			final String monthly, final String income, final String day, final String month, final String year,
			final String categoryIdAsString) {
		double amount = Double.parseDouble(amountAsString);
		boolean isMonthly = Boolean.parseBoolean(monthly);
		boolean isIncome = Boolean.parseBoolean(income);
		long categoryId = Long.valueOf(categoryIdAsString);
		String readableDate = day + "." + month + "." + year;

		expenseDAO.create(readableDate, amount, reason, isMonthly, isIncome, categoryId, account.getId());
		statistics.increaseCounterForStatisticType(ApplicationStatisticTypes.NEW_EXPENSES);

		ServletReaction reaction = new ServletReaction();
		if (isMonthly) {
			reaction.setRedirect("listexpenses.jsp").add("monthly", true);
		} else {
			reaction.setRedirect("listexpenses.jsp").add("back", "true");
		}

		return reaction;
	}
}
