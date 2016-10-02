package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import vu.de.npolke.myexpenses.util.Month;

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
@WebServlet("/importmonthly.jsp")
public class ImportMonthlyServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	ExpenseDAO expenseDAO = (ExpenseDAO) DAOFactory.getDAO(Expense.class);

	@Override
	protected ServletReaction doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		final String month = request.getParameter("month");

		return duplicateMonthlyExpenses(account, month);
	}

	public ServletReaction duplicateMonthlyExpenses(final Account account, final String monthAsString) {
		ServletReaction reaction = new ServletReaction();
		Month month = Month.createMonth(monthAsString);
		String redirectToMonth = "";

		if (month != null) {
			final List<Expense> previousMonth = expenseDAO.readMonthlyByAccountAndMonth(account.getId(),
					month.previous());
			final List<Expense> currentMonth = expenseDAO.readMonthlyByAccountAndMonth(account.getId(), month);

			final List<Expense> expensesToCopy = findMissing(previousMonth, currentMonth);
			int countNewExpenses = 0;
			for (Expense toCopy : expensesToCopy) {
				Calendar date = toCopy.getDay();
				date.add(Calendar.MONTH, 1);
				toCopy.setDay(date.getTime());
				final Expense newExpense = expenseDAO.create(toCopy.getReadableDayAsString(), toCopy.getAmount(),
						toCopy.getReason(), true, toCopy.isIncome(), toCopy.getCategoryId(), account.getId());
				if (newExpense != null) {
					countNewExpenses++;
				}
			}
			if (countNewExpenses > 0) {
				reaction.setSessionAttribute("message",
						countNewExpenses + " monthly expenses imported from previous month");
			} else {
				reaction.setSessionAttribute("message", "no monthly expenses importable from previous month");
			}
			redirectToMonth = "&month=" + month.toString();
		}

		reaction.setRedirect("listexpenses?monthly=true" + redirectToMonth);
		return reaction;
	}

	protected List<Expense> findMissing(final List<Expense> potentiallyToCopyExpenses,
			final List<Expense> existingExpenses) {
		final List<Expense> toCopy = new ArrayList<Expense>();
		for (Expense potentialNew : potentiallyToCopyExpenses) {
			boolean copyExpense = true;
			for (Expense existing : existingExpenses) {
				if (existing.isIncome() == potentialNew.isIncome()
						&& existing.getCategoryId() == potentialNew.getCategoryId()
						&& existing.getReason().equals(potentialNew.getReason())) {
					copyExpense = false;
					break;
				}
			}
			if (copyExpense) {
				toCopy.add(potentialNew);
			}
		}
		return toCopy;
	}
}
