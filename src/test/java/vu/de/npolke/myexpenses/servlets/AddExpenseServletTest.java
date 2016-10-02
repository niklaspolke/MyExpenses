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

	private static final long ACCOUNT_ID = 123;
	private static final String REASON = "correctReason";
	private static final String REASON_2_IGNORE = "wrongReason";
	private static final long CATEGORY_ID = 14;
	private static final long NON_EXISTING_CATEGORY_ID = 15;

	private AddExpenseServlet servlet;

	@Before
	public void init() {
		servlet = new AddExpenseServlet();
		servlet.categoryDAO = mock(CategoryDAO.class);
		servlet.expenseDAO = mock(ExpenseDAO.class);
	}

	@Test
	public void prepareAddExpense() {
		Account account = new Account();
		account.setId(ACCOUNT_ID);
		String expenseId = null;
		List<Category> categories = new ArrayList<Category>();
		when(servlet.categoryDAO.readByAccountId(ACCOUNT_ID)).thenReturn(categories);

		ServletReaction reaction = servlet.prepareAddExpense(account, expenseId, null, null);

		assertNotNull(reaction);
		assertEquals(3, reaction.getRequestAttributes().size());
		// correct session attribute: prepared expense
		Object expenseObject = reaction.getRequestAttributes().get("expense");
		assertTrue(expenseObject instanceof Expense);
		Expense expense = (Expense) expenseObject;
		assertTrue(System.currentTimeMillis() - expense.getDay().getTimeInMillis() < 10000);
		// correct session attribute: categories
		assertSame(categories, reaction.getRequestAttributes().get("categories"));
		// no preset of category
		assertEquals(null, reaction.getRequestAttributes().get("categoryPreset"));
		// correct navigation
		assertEquals("WEB-INF/addexpense.jsp", reaction.getForward());
	}

	@Test
	public void prepareAddExpense_WithReason() {
		Account account = new Account();
		account.setId(ACCOUNT_ID);
		String expenseId = null;
		List<Category> categories = new ArrayList<Category>();
		when(servlet.categoryDAO.readByAccountId(ACCOUNT_ID)).thenReturn(categories);

		ServletReaction reaction = servlet.prepareAddExpense(account, expenseId, REASON, null);

		assertNotNull(reaction);
		assertEquals(3, reaction.getRequestAttributes().size());
		// correct session attribute: prepared expense
		Object expenseObject = reaction.getRequestAttributes().get("expense");
		assertTrue(expenseObject instanceof Expense);
		Expense expense = (Expense) expenseObject;
		assertTrue(System.currentTimeMillis() - expense.getDay().getTimeInMillis() < 10000);
		assertEquals(REASON, expense.getReason());
		// correct session attribute: categories
		assertSame(categories, reaction.getRequestAttributes().get("categories"));
		// no preset of category
		assertEquals(null, reaction.getRequestAttributes().get("categoryPreset"));
		// correct navigation
		assertEquals("WEB-INF/addexpense.jsp", reaction.getForward());
	}

	@Test
	public void prepareAddExpense_WithCategoryId() {
		Account account = new Account();
		account.setId(ACCOUNT_ID);
		String expenseId = null;
		List<Category> categories = new ArrayList<Category>();
		Category category = new Category();
		category.setAccountId(ACCOUNT_ID);
		category.setId(CATEGORY_ID);
		categories.add(category);
		when(servlet.categoryDAO.readByAccountId(ACCOUNT_ID)).thenReturn(categories);

		ServletReaction reaction = servlet.prepareAddExpense(account, expenseId, null, "" + CATEGORY_ID);

		assertNotNull(reaction);
		assertEquals(3, reaction.getRequestAttributes().size());
		// correct session attribute: prepared expense
		Object expenseObject = reaction.getRequestAttributes().get("expense");
		assertTrue(expenseObject instanceof Expense);
		Expense expense = (Expense) expenseObject;
		assertTrue(System.currentTimeMillis() - expense.getDay().getTimeInMillis() < 10000);
		assertEquals(CATEGORY_ID, expense.getCategoryId());
		// correct session attribute: categories
		assertSame(categories, reaction.getRequestAttributes().get("categories"));
		// preset of category
		assertEquals(Boolean.TRUE, reaction.getRequestAttributes().get("categoryPreset"));
		// correct navigation
		assertEquals("WEB-INF/addexpense.jsp", reaction.getForward());
	}

	@Test
	public void prepareAddExpense_WithWrongCategoryId() {
		Account account = new Account();
		account.setId(ACCOUNT_ID);
		String expenseId = null;
		List<Category> categories = new ArrayList<Category>();
		when(servlet.categoryDAO.readByAccountId(ACCOUNT_ID)).thenReturn(categories);

		ServletReaction reaction = servlet.prepareAddExpense(account, expenseId, null, "" + NON_EXISTING_CATEGORY_ID);

		assertNotNull(reaction);
		assertEquals(3, reaction.getRequestAttributes().size());
		// correct session attribute: prepared expense
		Object expenseObject = reaction.getRequestAttributes().get("expense");
		assertTrue(expenseObject instanceof Expense);
		Expense expense = (Expense) expenseObject;
		assertTrue(System.currentTimeMillis() - expense.getDay().getTimeInMillis() < 10000);
		assertEquals(0, expense.getCategoryId());
		// correct session attribute: categories
		assertSame(categories, reaction.getRequestAttributes().get("categories"));
		// no preset of category
		assertEquals(null, reaction.getRequestAttributes().get("categoryPreset"));
		// correct navigation
		assertEquals("WEB-INF/addexpense.jsp", reaction.getForward());
	}

	@Test
	public void prepareAddExpense_WithReasonAndCategoryId() {
		Account account = new Account();
		account.setId(ACCOUNT_ID);
		String expenseId = null;
		List<Category> categories = new ArrayList<Category>();
		Category category = new Category();
		category.setAccountId(ACCOUNT_ID);
		category.setId(CATEGORY_ID);
		categories.add(category);
		when(servlet.categoryDAO.readByAccountId(ACCOUNT_ID)).thenReturn(categories);

		ServletReaction reaction = servlet.prepareAddExpense(account, expenseId, REASON, "" + CATEGORY_ID);

		assertNotNull(reaction);
		assertEquals(3, reaction.getRequestAttributes().size());
		// correct session attribute: prepared expense
		Object expenseObject = reaction.getRequestAttributes().get("expense");
		assertTrue(expenseObject instanceof Expense);
		Expense expense = (Expense) expenseObject;
		assertTrue(System.currentTimeMillis() - expense.getDay().getTimeInMillis() < 10000);
		assertEquals(REASON, expense.getReason());
		assertEquals(CATEGORY_ID, expense.getCategoryId());
		// correct session attribute: categories
		assertSame(categories, reaction.getRequestAttributes().get("categories"));
		// preset of category
		assertEquals(Boolean.TRUE, reaction.getRequestAttributes().get("categoryPreset"));
		// correct navigation
		assertEquals("WEB-INF/addexpense.jsp", reaction.getForward());
	}

	@Test
	public void prepareCopyExpense() {
		Account account = new Account();
		account.setId(ACCOUNT_ID);
		long expenseId = 444;
		Expense expense = new Expense();
		expense.setAccountId(ACCOUNT_ID);
		expense.setId(expenseId);
		List<Category> categories = new ArrayList<Category>();
		when(servlet.expenseDAO.read(ACCOUNT_ID, expenseId)).thenReturn(expense);
		when(servlet.categoryDAO.readByAccountId(ACCOUNT_ID)).thenReturn(categories);

		ServletReaction reaction = servlet.prepareAddExpense(account, Long.toString(expenseId), null, null);

		assertNotNull(reaction);
		assertEquals(3, reaction.getRequestAttributes().size());
		// correct session attribute: prepared expense
		assertEquals(expense, reaction.getRequestAttributes().get("expense"));
		// correct session attribute: categories
		assertSame(categories, reaction.getRequestAttributes().get("categories"));
		// preset of category
		assertEquals(Boolean.TRUE, reaction.getRequestAttributes().get("categoryPreset"));
		// correct navigation
		assertEquals("WEB-INF/addexpense.jsp", reaction.getForward());
	}

	@Test
	public void prepareCopyExpense_WithReasonAndCategory() {
		Account account = new Account();
		account.setId(ACCOUNT_ID);
		long expenseId = 444;
		Expense expense = new Expense();
		expense.setAccountId(ACCOUNT_ID);
		expense.setId(expenseId);
		expense.setReason(REASON);
		expense.setCategoryId(CATEGORY_ID);
		List<Category> categories = new ArrayList<Category>();
		when(servlet.expenseDAO.read(ACCOUNT_ID, expenseId)).thenReturn(expense);
		when(servlet.categoryDAO.readByAccountId(ACCOUNT_ID)).thenReturn(categories);

		ServletReaction reaction = servlet.prepareAddExpense(account, Long.toString(expenseId), REASON_2_IGNORE,
				"" + NON_EXISTING_CATEGORY_ID);

		assertNotNull(reaction);
		assertEquals(3, reaction.getRequestAttributes().size());
		// correct session attribute: prepared expense
		assertEquals(expense, reaction.getRequestAttributes().get("expense"));
		final Expense sessionExpense = (Expense) reaction.getRequestAttributes().get("expense");
		// ignore other parameters
		assertEquals(CATEGORY_ID, sessionExpense.getCategoryId());
		assertEquals(REASON, sessionExpense.getReason());
		// correct session attribute: categories
		assertSame(categories, reaction.getRequestAttributes().get("categories"));
		// preset of category
		assertEquals(Boolean.TRUE, reaction.getRequestAttributes().get("categoryPreset"));
		// correct navigation
		assertEquals("WEB-INF/addexpense.jsp", reaction.getForward());
	}

	@Test
	public void prepareCopyNonExistingExpense() {
		Account account = new Account();
		account.setId(ACCOUNT_ID);
		long nonExistingExpenseId = 123;
		List<Category> categories = new ArrayList<Category>();
		when(servlet.expenseDAO.read(ACCOUNT_ID, nonExistingExpenseId)).thenReturn(null);
		when(servlet.categoryDAO.readByAccountId(ACCOUNT_ID)).thenReturn(categories);

		ServletReaction reaction = servlet.prepareAddExpense(account, Long.toString(nonExistingExpenseId), null, null);

		assertNotNull(reaction);
		// correct request attribute: error message
		assertEquals("You tried to clone a non existing expense or an expense that isn't yours!",
				reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("WEB-INF/error.jsp", reaction.getForward());
	}

	@Test
	public void prepareCopyForeignExpense() {
		long foreignAccountId = 666;
		Account account = new Account();
		account.setId(ACCOUNT_ID);
		long expenseId = 444;
		Expense expense = new Expense();
		expense.setAccountId(foreignAccountId);
		expense.setId(expenseId);
		List<Category> categories = new ArrayList<Category>();
		when(servlet.expenseDAO.read(foreignAccountId, expenseId)).thenReturn(expense);
		when(servlet.categoryDAO.readByAccountId(ACCOUNT_ID)).thenReturn(categories);

		ServletReaction reaction = servlet.prepareAddExpense(account, Long.toString(expenseId), null, null);

		assertNotNull(reaction);
		// correct request attribute: error message
		assertEquals("You tried to clone a non existing expense or an expense that isn't yours!",
				reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("WEB-INF/error.jsp", reaction.getForward());
	}

	@Test
	public void addExpense() {
		Account account = new Account();
		account.setId(ACCOUNT_ID);
		double amount = 123.1;
		String reason = "reason";
		long categoryId = 12;
		int day = 15;
		int month = 6;
		int year = 2000;

		ServletReaction reaction = servlet.addExpense(account, Double.toString(amount), reason, null, null,
				Integer.toString(day), Integer.toString(month), Integer.toString(year), Long.toString(categoryId));

		assertNotNull(reaction);
		// correct navigation
		assertEquals("listexpenses.jsp", reaction.getRedirect());
		// correct creation of Expense
		verify(servlet.expenseDAO).create(day + "." + month + "." + year, amount, reason, false, false, categoryId,
				ACCOUNT_ID);
	}

	@Test
	public void addExpense_Monthly() {
		Account account = new Account();
		account.setId(ACCOUNT_ID);
		double amount = 123.1;
		String reason = "reason";
		long categoryId = 12;
		int day = 15;
		int month = 6;
		int year = 2000;

		ServletReaction reaction = servlet.addExpense(account, Double.toString(amount), reason, "true", "true",
				Integer.toString(day), Integer.toString(month), Integer.toString(year), Long.toString(categoryId));

		assertNotNull(reaction);
		// correct navigation
		assertEquals("listexpenses.jsp?monthly=true", reaction.getRedirect());
		// correct creation of Expense
		verify(servlet.expenseDAO).create(day + "." + month + "." + year, amount, reason, true, true, categoryId,
				ACCOUNT_ID);
	}
}
