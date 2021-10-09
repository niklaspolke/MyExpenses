package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Expense;
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
@RunWith(MockitoJUnitRunner.class)
public class ImportMonthlyServletTest {

	private static final long ACCOUNT_ID = 123;
	private static final List<Expense> EMPTY_EXPENSELIST = new ArrayList<Expense>();

	private Account account;

	@Mock
	private ExpenseDAO expenseDAO;
	@InjectMocks
	private ImportMonthlyServlet servlet;

	@Before
	public void init() {
		account = new Account();
		account.setId(ACCOUNT_ID);
	}

	@Test
	public void findMissing_emptyLists() {
		List<Expense> result = servlet.findMissing(EMPTY_EXPENSELIST, EMPTY_EXPENSELIST);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	public void findMissing_equalLists() {
		List<Expense> potentiallyToCopy = new ArrayList<Expense>();
		List<Expense> existing = new ArrayList<Expense>();
		Expense exp1 = new Expense();
		exp1.setIncome(false);
		exp1.setCategoryId(4);
		exp1.setReason("reason");
		potentiallyToCopy.add(exp1);
		existing.add(exp1);

		List<Expense> result = servlet.findMissing(potentiallyToCopy, existing);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	public void findMissing_oneToCopy() {
		List<Expense> potentiallyToCopy = new ArrayList<Expense>();
		List<Expense> existing = new ArrayList<Expense>();
		Expense exp1 = new Expense();
		exp1.setIncome(false);
		exp1.setCategoryId(4);
		exp1.setReason("reason");
		Expense exp2 = new Expense();
		exp2.setIncome(false);
		exp2.setCategoryId(4);
		exp2.setReason("other reason");

		potentiallyToCopy.add(exp1);
		potentiallyToCopy.add(exp2);
		existing.add(exp1);

		List<Expense> result = servlet.findMissing(potentiallyToCopy, existing);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertSame(exp2, result.get(0));
	}

	@Test
	public void duplicateMonthlyExpenses_doNothing_whenMonthIsMissing() {
		ServletReaction reaction = servlet.duplicateMonthlyExpenses(account, null);

		assertNotNull(reaction);
		assertEquals("listexpenses.jsp?monthly=true", reaction.getRedirect());
		verify(expenseDAO, never()).create(anyString(), anyDouble(), anyString(), anyBoolean(), anyBoolean(),
				anyBoolean(), anyLong(), anyLong());
	}

	@Test
	public void duplicateMonthlyExpenses_doNothing_whenMonthIsIllegalMonth() {
		final String illegalMonth = "2016.13b";

		ServletReaction reaction = servlet.duplicateMonthlyExpenses(account, illegalMonth);

		assertNotNull(reaction);
		assertEquals("listexpenses.jsp?monthly=true", reaction.getRedirect());
		verify(expenseDAO, never()).create(anyString(), anyDouble(), anyString(), anyBoolean(), anyBoolean(),
				anyBoolean(), anyLong(), anyLong());
	}

	@Test
	public void duplicateMonthlyExpenses_noMonthToCopy() {
		final String MONTH = "2016.09";
		List<Expense> expenses = new ArrayList<Expense>();
		Expense exp1 = new Expense();
		exp1.setIncome(false);
		exp1.setCategoryId(4);
		exp1.setReason("reason");
		Expense exp2 = new Expense();
		exp2.setIncome(false);
		exp2.setCategoryId(4);
		exp2.setReason("other reason");
		expenses.add(exp1);
		expenses.add(exp2);
		when(expenseDAO.readMonthlyByAccountAndMonth(anyLong(), any(Month.class))).thenReturn(expenses);

		ServletReaction reaction = servlet.duplicateMonthlyExpenses(account, MONTH);

		assertNotNull(reaction);
		assertEquals("listexpenses.jsp?monthly=true&month=" + MONTH + "&message=warn.noimports",
				reaction.getRedirect());
		verify(expenseDAO, times(2)).readMonthlyByAccountAndMonth(anyLong(), any(Month.class));
		verify(expenseDAO, never()).create(anyString(), anyDouble(), anyString(), anyBoolean(), anyBoolean(),
				anyBoolean(), anyLong(), anyLong());
	}

	@Test
	public void duplicateMonthlyExpenses_withMonthToCopy() {
		final String MONTH = "2016.09";
		final String MONTH_PREVIOUS = "2016.08";
		List<Expense> expensesCurrentMonth = new ArrayList<Expense>();
		List<Expense> expensesPreviousMonth = new ArrayList<Expense>();
		Expense exp1 = new Expense();
		exp1.setIncome(false);
		exp1.setCategoryId(4);
		exp1.setReason("reason");
		Expense exp2 = new Expense();
		exp2.setReadableDayAsString("01.08.16");
		exp2.setIncome(false);
		exp2.setCategoryId(4);
		exp2.setReason("other reason");
		expensesCurrentMonth.add(exp1);
		expensesPreviousMonth.add(exp1);
		expensesPreviousMonth.add(exp2);
		when(expenseDAO.readMonthlyByAccountAndMonth(account.getId(), Month.create(MONTH)))
				.thenReturn(expensesCurrentMonth);
		when(expenseDAO.readMonthlyByAccountAndMonth(account.getId(), Month.create(MONTH_PREVIOUS)))
				.thenReturn(expensesPreviousMonth);
		when(expenseDAO.create(anyString(), anyDouble(), anyString(), anyBoolean(), anyBoolean(), anyBoolean(),
				anyLong(), anyLong())).thenReturn(new Expense());

		ServletReaction reaction = servlet.duplicateMonthlyExpenses(account, MONTH);

		assertNotNull(reaction);
		assertEquals("listexpenses.jsp?monthly=true&month=" + MONTH + "&message=info.imports&msgparam=1",
				reaction.getRedirect());
		verify(expenseDAO).create(eq("01.09.16"), anyDouble(), eq("other reason"), anyBoolean(), eq(false),
				anyBoolean(), eq(4l), anyLong());
	}
}
