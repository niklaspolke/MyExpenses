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
@WebServlet("/listexpenses")
public class ListExpensesServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	private static final int AMOUNT_OF_ENTRIES_PER_PAGE = 10;

	ExpenseDAO expenseDAO = (ExpenseDAO) DAOFactory.getDAO(Expense.class);

	@Override
	public ServletReaction doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		final String requestedPage = request.getParameter("page");

		return prepareListExpenses(account, requestedPage);
	}

	public ServletReaction prepareListExpenses(final Account account, final String requestedPage) {
		ServletReaction reaction = new ServletReaction();

		final long amountOfExpenses = expenseDAO.readAmountOfExpenses(account.getId());

		final int pageMax = calcAmountOfPages(amountOfExpenses, AMOUNT_OF_ENTRIES_PER_PAGE);
		final int page = parseRequestedPage(requestedPage, pageMax);

		final List<Expense> expenses = expenseDAO.readByAccountId(account.getId(),
				(page - 1) * AMOUNT_OF_ENTRIES_PER_PAGE + 1, page * AMOUNT_OF_ENTRIES_PER_PAGE);

		reaction.setRequestAttribute("page", page);
		reaction.setRequestAttribute("pageMax", pageMax);
		reaction.setSessionAttribute("expenses", expenses);
		reaction.setForward("listexpenses.jsp");

		return reaction;
	}

	public int parseRequestedPage(final String requestedPage, final int pageMax) {
		int page = 1;
		try {
			page = Integer.parseInt(requestedPage);
		} catch (NumberFormatException nfe) {
		}
		if (page < 1) {
			page = 1;
		}
		if (page > pageMax) {
			page = pageMax;
		}
		return page;
	}

	public int calcAmountOfPages(final long amountOfEntries, final int amountOfEntriesPerPage) {
		return (int) Math.max(1, Math.ceil((double) amountOfEntries / amountOfEntriesPerPage));
	}
}
