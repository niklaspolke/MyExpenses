package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Category;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.CategoryDAO;
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
public class AddExpenseServletTest {

	private AddExpenseServlet servlet;

	@Before
	public void init() {
		servlet = new AddExpenseServlet();
		servlet.categoryDAO = mock(CategoryDAO.class);
		servlet.expenseDAO = mock(ExpenseDAO.class);
	}

	@Test
	public void prepareAddExpense() {
		long accountId = 123;
		Account account = new Account();
		account.setId(accountId);
		String expenseId = null;
		List<Category> categories = new ArrayList<Category>();
		when(servlet.categoryDAO.readByAccountId(accountId)).thenReturn(categories);

		ServletReaction reaction = servlet.prepareAddExpense(account, expenseId);

		assertNotNull(reaction);
		assertEquals(2, reaction.getSessionAttributes().size());
		// correct session attribute: prepared expense
		Object expenseObject = reaction.getSessionAttributes().get("expense");
		assertTrue(expenseObject instanceof Expense);
		Expense expense = (Expense) expenseObject;
		assertTrue(System.currentTimeMillis() - expense.getDay().getTimeInMillis() < 10000);
		// correct session attribute: categories
		assertSame(categories, reaction.getSessionAttributes().get("categories"));
		// correct navigation
		assertTrue(reaction.getDoRedirect());
		assertEquals("addexpense.jsp", reaction.getNextLocation());
	}

	@Test
	public void prepareCopyExpense() {
		long accountId = 123;
		Account account = new Account();
		account.setId(accountId);
		long expenseId = 444;
		Expense expense = new Expense();
		expense.setAccountId(accountId);
		expense.setId(expenseId);
		List<Category> categories = new ArrayList<Category>();
		when(servlet.expenseDAO.read(expenseId)).thenReturn(expense);
		when(servlet.categoryDAO.readByAccountId(accountId)).thenReturn(categories);

		ServletReaction reaction = servlet.prepareAddExpense(account, Long.toString(expenseId));

		assertNotNull(reaction);
		assertEquals(2, reaction.getSessionAttributes().size());
		// correct session attribute: prepared expense
		assertEquals(expense, reaction.getSessionAttributes().get("expense"));
		// correct session attribute: categories
		assertSame(categories, reaction.getSessionAttributes().get("categories"));
		// correct navigation
		assertTrue(reaction.getDoRedirect());
		assertEquals("addexpense.jsp", reaction.getNextLocation());
	}

	@Test
	public void prepareCopyNonExistingExpense() {
		long accountId = 123;
		Account account = new Account();
		account.setId(accountId);
		long nonExistingExpenseId = 123;
		List<Category> categories = new ArrayList<Category>();
		when(servlet.expenseDAO.read(nonExistingExpenseId)).thenReturn(null);
		when(servlet.categoryDAO.readByAccountId(accountId)).thenReturn(categories);

		ServletReaction reaction = servlet.prepareAddExpense(account, Long.toString(nonExistingExpenseId));

		assertNotNull(reaction);
		assertEquals(1, reaction.getRequestAttributes().size());
		// correct request attribute: error message
		assertEquals("You tried to clone a non existing expense or an expense that isn't yours!",
				reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertFalse(reaction.getDoRedirect());
		assertEquals("error.jsp", reaction.getNextLocation());
	}

	@Test
	public void prepareCopyForeignExpense() {
		long accountId = 123;
		long foreignAccountId = 666;
		Account account = new Account();
		account.setId(accountId);
		long expenseId = 444;
		Expense expense = new Expense();
		expense.setAccountId(foreignAccountId);
		expense.setId(expenseId);
		List<Category> categories = new ArrayList<Category>();
		when(servlet.expenseDAO.read(expenseId)).thenReturn(expense);
		when(servlet.categoryDAO.readByAccountId(accountId)).thenReturn(categories);

		ServletReaction reaction = servlet.prepareAddExpense(account, Long.toString(expenseId));

		assertNotNull(reaction);
		assertEquals(1, reaction.getRequestAttributes().size());
		// correct request attribute: error message
		assertEquals("You tried to clone a non existing expense or an expense that isn't yours!",
				reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertFalse(reaction.getDoRedirect());
		assertEquals("error.jsp", reaction.getNextLocation());
	}

	@Test
	public void addExpense() {
		Account account = new Account();
		account.setId(123);
		double amount = 123.1;
		String reason = "reason";
		long categoryId = 12;
		int day = 15;
		int month = 6;
		int year = 2000;

		ServletReaction reaction = servlet.addExpense(account, Double.toString(amount), reason, Integer.toString(day),
				Integer.toString(month), Integer.toString(year), Long.toString(categoryId));

		assertNotNull(reaction);
		// correct navigation
		assertTrue(reaction.getDoRedirect());
		assertEquals("listexpenses", reaction.getNextLocation());
		// correct creation of Expense
		verify(servlet.expenseDAO).create(day + "." + month + "." + year, amount, reason, categoryId, account.getId());
	}
}
