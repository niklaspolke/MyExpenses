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
@WebServlet("/deleteexpense")
public class DeleteExpenseServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	private ExpenseDAO expenseDAO = (ExpenseDAO) DAOFactory.getDAO(Expense.class);

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		long id = Long.parseLong(request.getParameter("id"));
		String confirmed = request.getParameter("confirmed");

		boolean errorOccured = false;

		if ("yes".equalsIgnoreCase(confirmed)) {
			Expense expense = expenseDAO.read(id);
			if (expense != null && expense.getAccountId() == account.getId()) {
				expenseDAO.deleteById(id);
			} else {
				errorOccured = true;
			}
		}

		if (errorOccured) {
			request.setAttribute("errorMessage",
					"You tried to delete a non existing expense or an expense that isn't yours!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("listexpenses").forward(request, response);
		}
	}
}
