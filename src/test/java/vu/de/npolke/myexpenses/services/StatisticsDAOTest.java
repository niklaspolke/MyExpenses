package vu.de.npolke.myexpenses.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static vu.de.npolke.myexpenses.util.Month.createMonth;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.util.Month;
import vu.de.npolke.myexpenses.util.StatisticsElement;
import vu.de.npolke.myexpenses.util.StatisticsOfMonth;

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
@Category(vu.de.npolke.myexpenses.InMemory.class)
public class StatisticsDAOTest extends AbstractDAOTest {

	private static final String TESTDATA_INSERT_SCRIPT = "src/test/resources/insert_testdata_statistics.sql";
	private static final long ACCOUNT_ID = 2;
	private static final double DELTA = 0.001;

	private static StatisticsDAO statisticsDAO;

	private static long testCounter = 0;

	public StatisticsDAOTest() {
		super("StatisticsDAOTest" + ++testCounter);
	}

	@BeforeClass
	public static void initialise() {
		statisticsDAO = (StatisticsDAO) DAOFactory.getDAO(StatisticsElement.class);
	}

	@Before
	public void setup() {
		this.scriptExecutor.executeSqlScript(TESTDATA_INSERT_SCRIPT);
	}

	@Test
	public void readDistinctMonthsByAccountId_ValidAccount() {
		List<Month> months = statisticsDAO.readDistinctMonthsByAccountId(ACCOUNT_ID);

		assertNotNull(months);
		assertEquals(3, months.size());
		assertEquals("2015.07", months.get(0).toString());
		assertEquals("2015.06", months.get(1).toString());
		assertEquals("2015.05", months.get(2).toString());
	}

	@Test
	public void readDistinctMonthsByAccountId_InvalidAccount() {
		List<Month> months = statisticsDAO.readDistinctMonthsByAccountId(9);

		assertNotNull(months);
		assertTrue(months.isEmpty());
	}

	private void assertStatistics(final Month month, final String category, final double amount, final boolean isMonthly,
			final boolean isIncome, final StatisticsElement statistics) {
		assertEquals(month, statistics.getMonth());
		assertEquals(category, statistics.getCategory());
		assertEquals(amount, statistics.getAmount(), 0.01);
		assertEquals(isMonthly, statistics.isMonthly());
		assertEquals(isIncome, statistics.isIncome());
	}

	@Test
	public void readStatisticsByMonthsAndAccountId_201506() {
		Month month = createMonth("2015.06");
		StatisticsOfMonth statistics = statisticsDAO.readStatisticsByMonthAndAccountId(month,
				ACCOUNT_ID);

		assertNotNull(statistics);
		assertEquals(3, statistics.getExpenses().size());

		assertStatistics(month, "food", 35, false, false, statistics.getExpenses().get(0));
		assertStatistics(month, "luxury", 15, false, false, statistics.getExpenses().get(1));
		assertStatistics(month, "sports", 35, false, false, statistics.getExpenses().get(2));
	}

	@Test
	public void readStatisticsByMonthsAndAccountId_201505() {
		Month month = createMonth("2015.05");
		StatisticsOfMonth statistics = statisticsDAO.readStatisticsByMonthAndAccountId(month,
				ACCOUNT_ID);

		assertNotNull(statistics);

		assertEquals(1, statistics.getIncome().size());
		assertStatistics(month, "income", 2000, true, true, statistics.getIncome().get(0));

		assertEquals(1, statistics.getMonthlyExpenses().size());
		assertStatistics(month, "sports", 21, true, false, statistics.getMonthlyExpenses().get(0));

		assertEquals(3, statistics.getExpenses().size());
		assertStatistics(month, "food", 12, false, false, statistics.getExpenses().get(0));
		assertStatistics(month, "luxury", 27, false, false, statistics.getExpenses().get(1));
		assertStatistics(month, "sports", 11, false, false, statistics.getExpenses().get(2));
	}

	@Test
	public void readStatisticsByMonthsAndAccountId_InvalidMonth() {
		StatisticsOfMonth statistics = statisticsDAO.readStatisticsByMonthAndAccountId(createMonth("2002.01"),
				ACCOUNT_ID);

		assertNotNull(statistics);
		assertEquals(0, statistics.getIncome().size());
		assertEquals(0, statistics.getMonthlyExpenses().size());
		assertEquals(0, statistics.getExpenses().size());
	}

	@Test
	public void readStatisticsByMonthsAndAccountId_InvalidAccount() {
		StatisticsOfMonth statistics = statisticsDAO.readStatisticsByMonthAndAccountId(createMonth("2015.06"), 9);

		assertNotNull(statistics);
		assertEquals(0, statistics.getIncome().size());
		assertEquals(0, statistics.getMonthlyExpenses().size());
		assertEquals(0, statistics.getExpenses().size());
	}

	public static void assertExpense(final Expense expense, final String reason, final long categoryId,
			final String categoryName) {
		assertNotNull(expense);
		assertEquals(reason, expense.getReason());
		assertEquals(categoryName, expense.getCategoryName());
		assertEquals(categoryId, expense.getCategoryId());
	}

	@Test
	public void readTopTenByAccountId() {
		Calendar fakeActualDay = Calendar.getInstance(Locale.GERMANY);
		fakeActualDay.set(Calendar.YEAR, 2015);
		fakeActualDay.set(Calendar.MONTH, 6); // July
		fakeActualDay.set(Calendar.DAY_OF_MONTH, 4);
		StatisticsDAO statisticsDAOspy = spy(statisticsDAO);
		when(statisticsDAOspy.getToday()).thenReturn(fakeActualDay);

		List<Expense> expenses = statisticsDAOspy.readTopTenByAccountId(ACCOUNT_ID);

		assertNotNull(expenses);
		assertEquals(3, expenses.size());
		assertExpense(expenses.get(0), "climbing", 21, "sports");
		assertExpense(expenses.get(1), "supermarket", 22, "food");
		assertExpense(expenses.get(2), "jewels", 23, "luxury");
	}

	public static void assertExpense(final Expense expense, final String reason, final long categoryId,
			final String categoryName, final double amount) {
		assertNotNull(expense);
		assertEquals(reason, expense.getReason());
		assertEquals(categoryName, expense.getCategoryName());
		assertEquals(categoryId, expense.getCategoryId());
		assertEquals(amount, expense.getAmount(), DELTA);
	}

	@Test
	public void readTopXofExpensesByMonth_Top3Of4_NoIncome_NoMonthly() {
		List<Expense> expenses = statisticsDAO.readTopXofExpensesByMonth(ACCOUNT_ID, "2015.05", 3);

		assertNotNull(expenses);
		assertEquals(3, expenses.size());

		assertExpense(expenses.get(0), "climbing", 23, "luxury", 14);
		assertExpense(expenses.get(1), "jewels", 23, "luxury", 13);
		assertExpense(expenses.get(2), "supermarket", 22, "food", 12);
	}

	@Test
	public void readTopXofExpensesByMonth_Top3OfOne() {
		List<Expense> expenses = statisticsDAO.readTopXofExpensesByMonth(ACCOUNT_ID, "2015.07", 3);

		assertNotNull(expenses);
		assertEquals(1, expenses.size());

		assertExpense(expenses.get(0), "climbing", 21, "sports", 20);
	}

	@Test
	public void readTopXofExpensesByMonth_NoResult() {
		List<Expense> expenses = statisticsDAO.readTopXofExpensesByMonth(ACCOUNT_ID, "2020.05", 3);

		assertNotNull(expenses);
		assertEquals(0, expenses.size());
	}

	@Test
	public void readTopTenByMonthAndCategory() {
		List<Expense> expenses = statisticsDAO.readTopTenByMonthAndCategory(3, "2016.01", 31);

		assertNotNull(expenses);
		assertEquals(10, expenses.size());
		assertEquals("aaaa", expenses.get(0).getReason());
		assertEquals(323, expenses.get(1).getId());
		assertEquals(33.0, expenses.get(2).getAmount(), DELTA);
		assertEquals("bbbb", expenses.get(3).getReason());
		assertEquals(324, expenses.get(4).getId());
		assertEquals(6.0, expenses.get(5).getAmount(), DELTA);
		assertEquals("jjjj", expenses.get(6).getReason());
		assertEquals(328, expenses.get(7).getId());
		assertEquals(3.0, expenses.get(8).getAmount(), DELTA);
		assertEquals("gggg", expenses.get(9).getReason());
	}
}
