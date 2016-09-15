package vu.de.npolke.myexpenses.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Expense;

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
public class ExpenseDAOTest extends AbstractDAOTest {

	private static ExpenseDAO expenseDAO;

	private static long testCounter = 0;

	public ExpenseDAOTest() {
		super("ExpenseDAOTest" + ++testCounter);
	}

	@BeforeClass
	public static void initialise() {
		expenseDAO = (ExpenseDAO) DAOFactory.getDAO(Expense.class);
	}

	@Test
	public void read() {
		Expense expense = expenseDAO.read(101);

		assertNotNull(expense);
		assertEquals(101, expense.getId());
		assertEquals("01.05.15", expense.getReadableDayAsString());
		assertEquals(5.5, expense.getAmount(), 0.01);
		assertEquals("burger", expense.getReason());
		assertEquals(false, expense.isMonthly());
		assertEquals(11, expense.getCategoryId());
		assertEquals("food", expense.getCategoryName());
		assertEquals(1, expense.getAccountId());
	}

	@Test
	public void readNotExisting() {
		Expense expense = expenseDAO.read(999);

		assertNull(expense);
	}

	@Test
	public void create() {
		Expense expense = expenseDAO.create("25.05.15", 13.3, "junk food", true, 11, 1);

		assertNotNull(expense);
		assertTrue(expense.getId() > 0);
		assertEquals("25.05.15", expense.getReadableDayAsString());
		assertEquals(13.3, expense.getAmount(), 0.01);
		assertEquals("junk food", expense.getReason());
		assertEquals(true, expense.isMonthly());
		assertEquals(11, expense.getCategoryId());
		assertEquals("food", expense.getCategoryName());
		assertEquals(1, expense.getAccountId());
	}

	@Test
	public void update() {
		Expense expense = expenseDAO.read(101);
		expense.setAmount(12.2);
		expense.setCategoryId(13);
		expense.setReadableDayAsString("01.01.15");
		expense.setReason("gone swimming");
		expense.setMonthly(true);

		boolean success = expenseDAO.update(expense);

		assertTrue(success);
		assertEquals("sports", expense.getCategoryName());

		expense = expenseDAO.read(expense.getId());
		assertEquals(101, expense.getId());
		assertEquals("01.01.15", expense.getReadableDayAsString());
		assertEquals(12.2, expense.getAmount(), 0.01);
		assertEquals("gone swimming", expense.getReason());
		assertEquals(true, expense.isMonthly());
		assertEquals(13, expense.getCategoryId());
		assertEquals("sports", expense.getCategoryName());
		assertEquals(1, expense.getAccountId());
	}

	@Test
	public void updateNotExisting() {
		Expense expense = expenseDAO.read(101);
		expense.setId(999);
		expense.setAmount(12.2);
		expense.setCategoryId(13);
		expense.setReadableDayAsString("01.01.15");
		expense.setReason("gone swimming");
		expense.setMonthly(true);

		boolean success = expenseDAO.update(expense);
		expense = expenseDAO.read(999);

		assertFalse(success);
		assertNull(expense);
	}

	@Test
	public void readByAccountId() {
		List<Expense> expenses = expenseDAO.readByAccountId(1, 1, 10);

		assertNotNull(expenses);
		assertEquals(4, expenses.size());
		for (Expense expense : expenses) {
			assertTrue(expense.getId() > 0);
			assertTrue(expense.getReadableDayAsString().length() == 8);
			assertTrue(expense.getAmount() > 0);
			assertTrue(expense.getReason().trim().length() > 0);
			assertFalse(expense.isMonthly());
			assertTrue(expense.getCategoryId() == 11 || expense.getCategoryId() == 12);
			assertTrue("food".equals(expense.getCategoryName()) || "luxury".equals(expense.getCategoryName()));
			assertEquals(1, expense.getAccountId());
		}
	}

	@Test
	public void readMonthlyByAccountId() {
		List<Expense> expenses = expenseDAO.readMonthlyByAccountId(1);

		assertNotNull(expenses);
		assertEquals(1, expenses.size());
		for (Expense expense : expenses) {
			assertTrue(expense.getId() > 0);
			assertTrue(expense.getReadableDayAsString().length() == 8);
			assertTrue(expense.getAmount() > 0);
			assertEquals("flat", expense.getReason());
			assertTrue(expense.isMonthly());
			assertEquals(12, expense.getCategoryId());
			assertEquals("luxury", expense.getCategoryName());
			assertEquals(1, expense.getAccountId());
		}
	}

	@Test
	public void readByAccountId_NotAllEntries() {
		List<Expense> expenses = expenseDAO.readByAccountId(1, 2, 3);

		assertNotNull(expenses);
		assertEquals(2, expenses.size());
		for (Expense expense : expenses) {
			assertTrue(expense.getId() == 102 || expense.getId() == 103);
			assertTrue(expense.getReadableDayAsString().length() == 8);
			assertTrue(expense.getAmount() > 0);
			assertTrue(expense.getReason().equals("jewels") || expense.getReason().equals("french fries"));
			assertFalse(expense.isMonthly());
			assertTrue(expense.getCategoryId() == 11 || expense.getCategoryId() == 12);
			assertTrue("food".equals(expense.getCategoryName()) || "luxury".equals(expense.getCategoryName()));
			assertEquals(1, expense.getAccountId());
		}
	}

	@Test
	public void readAmountOfExpenses() {
		assertEquals(4, expenseDAO.readAmountOfExpenses(1));

		expenseDAO.create("25.05.15", 13.3, "junk food", false, 11, 1);
		expenseDAO.create("25.05.15", 13.3, "junk food", false, 11, 1);
		Expense expense = expenseDAO.create("25.05.15", 13.3, "junk food", false, 11, 1);

		assertEquals(7, expenseDAO.readAmountOfExpenses(1));

		expenseDAO.deleteById(expense.getId());

		assertEquals(6, expenseDAO.readAmountOfExpenses(1));
	}

	@Test
	public void readByCategoryId() {
		List<Expense> expenses = expenseDAO.readByCategoryId(11);

		assertNotNull(expenses);
		assertEquals(2, expenses.size());
		for (Expense expense : expenses) {
			assertTrue(expense.getId() > 0);
			assertTrue(expense.getReadableDayAsString().length() == 8);
			assertTrue(expense.getAmount() > 0);
			assertTrue(expense.getReason().trim().length() > 0);
			assertFalse(expense.isMonthly());
			assertEquals(11, expense.getCategoryId());
			assertEquals("food", expense.getCategoryName());
			assertEquals(1, expense.getAccountId());
		}
	}

	@Test
	public void delete() {
		boolean success = expenseDAO.deleteById(101);
		Expense expense = expenseDAO.read(101);

		assertTrue(success);
		assertNull(expense);
	}

	@Test
	public void deleteNotExisting() {
		boolean success = expenseDAO.deleteById(999);

		assertFalse(success);
	}
}
