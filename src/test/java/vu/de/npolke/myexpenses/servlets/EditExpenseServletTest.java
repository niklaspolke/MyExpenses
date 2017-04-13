package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
public class EditExpenseServletTest {
	private static final String AMOUNT_OLD = "4";
	private static final String AMOUNT_NEW = "6";
	private static final String REASON_OLD = "shopping";
	private static final String REASON_NEW = "clothes";
	private static final long CATEGORY_ID_OLD = 111;
	private static final long CATEGORY_ID_NEW = 222;

	private static final long ACCOUNT_ID = 123;
	private static final long ACCOUNT_ID_FOREIGN = 666;
	private static final long EXPENSE_ID = 456;
	private static final long EXPENSE_ID_NONEXISTING = 555;
	private static final long EXPENSE_ID_FOREIGN = 555;
	private static final Category CATEGORY_1 = new Category();
	private static final Category CATEGORY_2 = new Category();

	private EditExpenseServlet servlet;
	private Account account;
	private Expense expense;
	private List<Category> categories;

	@Before
	public void init() {
		CATEGORY_1.setId(CATEGORY_ID_OLD);
		CATEGORY_2.setId(CATEGORY_ID_NEW);
		categories = new ArrayList<Category>();
		categories.add(CATEGORY_1);
		categories.add(CATEGORY_2);
		servlet = new EditExpenseServlet();
		servlet.categoryDAO = mock(CategoryDAO.class);
		servlet.expenseDAO = mock(ExpenseDAO.class);
		account = new Account();
		account.setId(ACCOUNT_ID);
		expense = new Expense();
		expense.setId(EXPENSE_ID);
		expense.setAccountId(ACCOUNT_ID);
		expense.setAmount(Double.parseDouble(AMOUNT_OLD.replaceAll(",", ".")));
		expense.setReason(REASON_OLD);
		expense.setCategoryId(CATEGORY_ID_OLD);
	}

	@Test
	public void prepareEditExpense() {
		when(servlet.categoryDAO.readByAccountId(ACCOUNT_ID)).thenReturn(categories);
		when(servlet.expenseDAO.read(ACCOUNT_ID, EXPENSE_ID)).thenReturn(expense);

		ServletReaction reaction = servlet.prepareEditExpense(account, String.valueOf(EXPENSE_ID));

		assertNotNull(reaction);
		// correct expense in session
		assertEquals(expense, reaction.getRequestAttributes().get("expense"));
		// correct categories in session
		assertEquals(categories, reaction.getRequestAttributes().get("categories"));
		// correct navigation
		assertEquals("WEB-INF/editexpense.jsp", reaction.getForward());
	}

	@Test
	public void prepareEditExpense_nonExistingExpense() {
		when(servlet.categoryDAO.readByAccountId(ACCOUNT_ID)).thenReturn(categories);
		when(servlet.expenseDAO.read(ACCOUNT_ID, EXPENSE_ID)).thenReturn(expense);

		ServletReaction reaction = servlet.prepareEditExpense(account, String.valueOf(EXPENSE_ID_NONEXISTING));

		assertNotNull(reaction);
		// correct error message
		assertEquals("error.editexpense.wrongid", reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("WEB-INF/error.jsp", reaction.getForward());
	}

	@Test
	public void prepareEditExpense_foreignExpense() {
		Expense foreignExpense = new Expense();
		foreignExpense.setId(EXPENSE_ID_FOREIGN);
		foreignExpense.setAccountId(ACCOUNT_ID_FOREIGN);
		when(servlet.expenseDAO.read(ACCOUNT_ID_FOREIGN, EXPENSE_ID_FOREIGN)).thenReturn(foreignExpense);

		ServletReaction reaction = servlet.prepareEditExpense(account, String.valueOf(EXPENSE_ID_FOREIGN));

		assertNotNull(reaction);
		// correct error message
		assertEquals("error.editexpense.wrongid", reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("WEB-INF/error.jsp", reaction.getForward());
	}

	@Test
	public void editExpense() {
		final int DAY_NEW = 15;
		final int MONTH_NEW = 12;
		final int YEAR_NEW = 2000;
		when(servlet.expenseDAO.read(ACCOUNT_ID, EXPENSE_ID)).thenReturn(expense);

		ServletReaction reaction = servlet.editExpense(account, "" + EXPENSE_ID, AMOUNT_NEW, REASON_NEW, null, null,
				String.valueOf(DAY_NEW), String.valueOf(MONTH_NEW), String.valueOf(YEAR_NEW),
				String.valueOf(CATEGORY_ID_NEW));

		assertNotNull(reaction);
		// correct update
		assertEquals(Double.valueOf(AMOUNT_NEW), expense.getAmount(), 0.01);
		assertEquals(CATEGORY_ID_NEW, expense.getCategoryId());
		assertEquals(REASON_NEW, expense.getReason());
		assertEquals(false, expense.isMonthly());
		assertEquals(DAY_NEW + "." + MONTH_NEW + "." + "00", expense.getReadableDayAsString());
		verify(servlet.expenseDAO).update(expense);
		// correct navigation
		assertEquals("listexpenses.jsp?back=true", reaction.getRedirect());
	}

	@Test
	public void editExpense_Monthly() {
		final int DAY_NEW = 15;
		final int MONTH_NEW = 12;
		final int YEAR_NEW = 2000;
		when(servlet.expenseDAO.read(ACCOUNT_ID, EXPENSE_ID)).thenReturn(expense);

		ServletReaction reaction = servlet.editExpense(account, "" + EXPENSE_ID, AMOUNT_NEW, REASON_NEW, "true", "true",
				String.valueOf(DAY_NEW), String.valueOf(MONTH_NEW), String.valueOf(YEAR_NEW),
				String.valueOf(CATEGORY_ID_NEW));

		assertNotNull(reaction);
		// correct update
		assertEquals(Double.valueOf(AMOUNT_NEW), expense.getAmount(), 0.01);
		assertEquals(CATEGORY_ID_NEW, expense.getCategoryId());
		assertEquals(REASON_NEW, expense.getReason());
		assertEquals(true, expense.isMonthly());
		assertEquals(true, expense.isIncome());
		assertEquals(DAY_NEW + "." + MONTH_NEW + "." + "00", expense.getReadableDayAsString());
		verify(servlet.expenseDAO).update(expense);
		// correct navigation
		assertEquals("listexpenses.jsp?monthly=true", reaction.getRedirect());
	}
}
