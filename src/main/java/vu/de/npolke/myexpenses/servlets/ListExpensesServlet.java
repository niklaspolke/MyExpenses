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
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.DAOFactory;
import vu.de.npolke.myexpenses.services.ExpenseDAO;
import vu.de.npolke.myexpenses.services.StatisticsDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;
import vu.de.npolke.myexpenses.util.Month;
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
@WebServlet("/listexpenses.jsp")
public class ListExpensesServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	protected static final String MODE_EXPENSES = "expenses";
	protected static final String MODE_TOP_10 = "topten";
	protected static final String MODE_MONTHLY = "monthly";

	private static final int AMOUNT_OF_ENTRIES_PER_PAGE = 10;

	ExpenseDAO expenseDAO = (ExpenseDAO) DAOFactory.getDAO(Expense.class);
	StatisticsDAO statisticsDAO = (StatisticsDAO) DAOFactory.getDAO(StatisticsElement.class);

	@Override
	public ServletReaction doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		final String requestedPage = request.getParameter("page");
		final String requestedMonth = request.getParameter("month");
		final String requestedCategoryId = request.getParameter("category");
		final String requestedMonthly = request.getParameter("monthly");
		final String message = (String) session.getAttribute("message");

		return prepareListExpenses(account, requestedPage, requestedMonth, requestedCategoryId, requestedMonthly,
				message);
	}

	protected Month getCurrentMonth() {
		final Calendar now = Calendar.getInstance(Locale.GERMANY);
		now.setTimeInMillis(System.currentTimeMillis());
		final int year = now.get(Calendar.YEAR);
		final int month = now.get(Calendar.MONTH) + 1;
		return Month.create(year, month);
	}

	protected Month calcMaxMonth(final Month current, final List<Month> all) {
		Month max = current;
		if (all.size() > 0 && all.get(0).compareTo(current) > 0) {
			max = all.get(0);
		}
		return max;
	}

	protected Month calcMinMonth(final Month current, final List<Month> all) {
		Month min = current;
		if (all.size() > 0 && all.get(all.size() - 1).compareTo(current) < 0) {
			min = all.get(all.size() - 1);
		}
		return min;
	}

	public ServletReaction prepareListExpenses(final Account account, final String requestedPage, final String month,
			final String categoryIdForTopTen, final String monthly, final String message) {
		ServletReaction reaction = new ServletReaction();
		List<Expense> expenses = null;
		boolean getMonthly = Boolean.parseBoolean(monthly);

		if (message != null && message.trim().length() > 0) {
			reaction.setRequestAttribute("message", message);
		}
		reaction.setSessionAttribute("message", null);

		if (getMonthly) {
			List<Month> monthsWithExpenses = statisticsDAO.readDistinctMonthsByAccountId(account.getId());
			Month currentMonth = getCurrentMonth();
			Month maxMonth = calcMaxMonth(currentMonth, monthsWithExpenses);
			Month minMonth = calcMinMonth(currentMonth, monthsWithExpenses);
			Month monthToShow = Month.createMonth(month);
			if (monthToShow == null || monthToShow.compareTo(maxMonth) > 0 || monthToShow.compareTo(minMonth) < 0) {
				monthToShow = currentMonth;
			}
			expenses = expenseDAO.readMonthlyByAccountAndMonth(account.getId(), monthToShow);
			reaction.setRequestAttribute("mode", MODE_MONTHLY);
			reaction.setRequestAttribute("monthMax", maxMonth);
			reaction.setRequestAttribute("monthCurrent", monthToShow);
			reaction.setRequestAttribute("monthMin", minMonth);
		} else if (month != null || categoryIdForTopTen != null) {
			final long parsedCategoryIdForTopTen = parseLongDefault0(categoryIdForTopTen);
			expenses = statisticsDAO.readTopTenByMonthAndCategory(account.getId(), month, parsedCategoryIdForTopTen);
			reaction.setRequestAttribute("mode", MODE_TOP_10);
			reaction.setRequestAttribute("month", month);
			reaction.setRequestAttribute("category",
					expenses.size() > 0 ? expenses.get(0).getCategoryName() : categoryIdForTopTen);
		} else {
			final long amountOfExpensesInFuture = expenseDAO.readAmountOfExpensesInFuture(account.getId());
			final long amountOfExpensesUpToNow = expenseDAO.readAmountOfExpensesUpToNow(account.getId());

			final int pageMax = calcAmountOfPages(amountOfExpensesInFuture, AMOUNT_OF_ENTRIES_PER_PAGE); // future on pages 1 -> x
			final int pageMin = Math.min(0, -1 * calcAmountOfPages(amountOfExpensesUpToNow, AMOUNT_OF_ENTRIES_PER_PAGE) + 1); // past/present on pages -x+1 -> 0
			final int page = parseRequestedPage(requestedPage, pageMin, pageMax);

			if (page >= 1) { // expenses of the future
				expenses = expenseDAO.readByAccountId(account.getId(), (page - 1) * AMOUNT_OF_ENTRIES_PER_PAGE + 1,
						page * AMOUNT_OF_ENTRIES_PER_PAGE, true);
			} else { // expenses of the past and the present
				expenses = expenseDAO.readByAccountId(account.getId(), Math.abs(page) * AMOUNT_OF_ENTRIES_PER_PAGE + 1,
						(Math.abs(page) + 1) * AMOUNT_OF_ENTRIES_PER_PAGE, false);
			}
			reaction.setRequestAttribute("mode", MODE_EXPENSES);
			reaction.setRequestAttribute("page", page);
			reaction.setRequestAttribute("pageMin", pageMin);
			reaction.setRequestAttribute("pageMax", pageMax);
		}

		reaction.setRequestAttribute("expenses", expenses);
		reaction.setForward("WEB-INF/listexpenses.jsp");

		return reaction;
	}

	private static long parseLongDefault0(final String longAsString) {
		long parsedLong = 0;
		try {
			parsedLong = Long.parseLong(longAsString);
		} catch (NumberFormatException nfe) {
		}
		return parsedLong;
	}

	public int parseRequestedPage(final String requestedPage, final int pageMin, final int pageMax) {
		int page = 0;
		try {
			page = Integer.parseInt(requestedPage);
		} catch (NumberFormatException nfe) {
		}
		if (page < pageMin) {
			page = pageMin;
		}
		if (page > pageMax) {
			page = pageMax;
		}
		return page;
	}

	public int calcAmountOfPages(final long amountOfEntries, final int amountOfEntriesPerPage) {
		if (amountOfEntries == 0)
			return 0;
		else
			return (int) Math.ceil((double) amountOfEntries / amountOfEntriesPerPage);
	}
}
