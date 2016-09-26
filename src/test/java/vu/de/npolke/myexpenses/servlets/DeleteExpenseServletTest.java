package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Expense;
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
public class DeleteExpenseServletTest {

	private DeleteExpenseServlet servlet;

	private Account account;
	private static final long ACCOUNT_ID = 123;

	@Before
	public void init() {
		servlet = new DeleteExpenseServlet();
		servlet.expenseDAO = mock(ExpenseDAO.class);

		account = new Account();
		account.setId(ACCOUNT_ID);
	}

	@Test
	public void deleteExpense() {
		long expenseId = 22;
		Expense expense = new Expense();
		expense.setId(expenseId);
		expense.setAccountId(ACCOUNT_ID);
		when(servlet.expenseDAO.read(ACCOUNT_ID, expenseId)).thenReturn(expense);

		ServletReaction reaction = servlet.deleteExpense(account, Long.toString(expenseId), "yes");

		assertNotNull(reaction);
		// correct deletion
		verify(servlet.expenseDAO).deleteById(expenseId);
		// correct navigation
		assertEquals("listexpenses", reaction.getForward());
	}

	@Test
	public void deleteExpense_notConfirmed() {
		long expenseId = 22;
		Expense expense = new Expense();
		expense.setId(expenseId);
		expense.setAccountId(ACCOUNT_ID);
		when(servlet.expenseDAO.read(ACCOUNT_ID, expenseId)).thenReturn(expense);

		ServletReaction reaction = servlet.deleteExpense(account, Long.toString(expenseId), null);

		assertNotNull(reaction);
		// no deletion
		verify(servlet.expenseDAO, never()).deleteById(anyLong());
		// correct navigation
		assertEquals("listexpenses", reaction.getForward());
	}

	@Test
	public void deleteExpense_nonExisting() {
		long expenseId = 22;
		Expense expense = new Expense();
		expense.setId(expenseId);
		expense.setAccountId(ACCOUNT_ID);
		when(servlet.expenseDAO.read(ACCOUNT_ID, expenseId)).thenReturn(expense);

		ServletReaction reaction = servlet.deleteExpense(account, "44", "yes");

		assertNotNull(reaction);
		// no deletion
		verify(servlet.expenseDAO, never()).deleteById(anyLong());
		// correct navigation
		assertEquals("error.jsp", reaction.getForward());
		assertEquals(1, reaction.getRequestAttributes().size());
		assertEquals("You tried to delete a non existing expense or an expense that isn't yours!",
				reaction.getRequestAttributes().get("errorMessage"));
	}

	@Test
	public void deleteExpense_foreign() {
		long expenseId = 22;
		Expense expense = new Expense();
		expense.setId(expenseId);
		expense.setAccountId(666);
		when(servlet.expenseDAO.read(666, expenseId)).thenReturn(expense);

		ServletReaction reaction = servlet.deleteExpense(account, Long.toString(expenseId), "yes");

		assertNotNull(reaction);
		// no deletion
		verify(servlet.expenseDAO, never()).deleteById(anyLong());
		// correct navigation
		assertEquals("error.jsp", reaction.getForward());
		assertEquals(1, reaction.getRequestAttributes().size());
		assertEquals("You tried to delete a non existing expense or an expense that isn't yours!",
				reaction.getRequestAttributes().get("errorMessage"));
	}
}
