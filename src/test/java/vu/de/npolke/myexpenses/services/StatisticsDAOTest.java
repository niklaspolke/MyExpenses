package vu.de.npolke.myexpenses.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static vu.de.npolke.myexpenses.util.Month.createMonth;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.servlets.util.StatisticsPair;
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
		statisticsDAO = (StatisticsDAO) DAOFactory.getDAO(StatisticsPair.class);
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

	@Test
	public void readStatisticsByMonthsAndAccountId_201506() {
		List<StatisticsPair> statistics = statisticsDAO.readStatisticsByMonthAndAccountId(createMonth("2015.06"),
				ACCOUNT_ID);

		assertNotNull(statistics);
		assertEquals(4, statistics.size());
		assertEquals(22, statistics.get(0).getId());
		assertEquals("food", statistics.get(0).getName());
		assertEquals(35, statistics.get(0).getValue(), 0.01);
		assertEquals(false, statistics.get(0).isMonthly());
		assertEquals(false, statistics.get(0).isIncome());
		assertEquals(24, statistics.get(1).getId());
		assertEquals("income", statistics.get(1).getName());
		assertEquals(0, statistics.get(1).getValue(), 0.01);
		assertEquals(false, statistics.get(1).isMonthly());
		assertEquals(false, statistics.get(2).isIncome());
		assertEquals(23, statistics.get(2).getId());
		assertEquals("luxury", statistics.get(2).getName());
		assertEquals(15, statistics.get(2).getValue(), 0.01);
		assertEquals(false, statistics.get(2).isMonthly());
		assertEquals(false, statistics.get(2).isIncome());
		assertEquals(21, statistics.get(3).getId());
		assertEquals("sports", statistics.get(3).getName());
		assertEquals(35, statistics.get(3).getValue(), 0.01);
		assertEquals(false, statistics.get(3).isMonthly());
		assertEquals(false, statistics.get(3).isIncome());
	}

	@Test
	public void readStatisticsByMonthsAndAccountId_201505() {
		List<StatisticsPair> statistics = statisticsDAO.readStatisticsByMonthAndAccountId(createMonth("2015.05"),
				ACCOUNT_ID);

		assertNotNull(statistics);
		assertEquals(5, statistics.size());
		assertEquals(22, statistics.get(0).getId());
		assertEquals("food", statistics.get(0).getName());
		assertEquals(12, statistics.get(0).getValue(), 0.01);
		assertEquals(false, statistics.get(0).isMonthly());
		assertEquals(false, statistics.get(0).isIncome());
		assertEquals(24, statistics.get(1).getId());
		assertEquals("income", statistics.get(1).getName());
		assertEquals(2000, statistics.get(1).getValue(), 0.01);
		assertEquals(true, statistics.get(1).isMonthly());
		assertEquals(true, statistics.get(1).isIncome());
		assertEquals(23, statistics.get(2).getId());
		assertEquals("luxury", statistics.get(2).getName());
		assertEquals(27, statistics.get(2).getValue(), 0.01);
		assertEquals(false, statistics.get(2).isMonthly());
		assertEquals(false, statistics.get(2).isIncome());
		assertEquals(21, statistics.get(3).getId());
		assertEquals("sports", statistics.get(3).getName());
		assertEquals(11, statistics.get(3).getValue(), 0.01);
		assertEquals(false, statistics.get(3).isMonthly());
		assertEquals(false, statistics.get(3).isIncome());
		assertEquals(21, statistics.get(4).getId());
		assertEquals("sports", statistics.get(4).getName());
		assertEquals(21, statistics.get(4).getValue(), 0.01);
		assertEquals(true, statistics.get(4).isMonthly());
		assertEquals(false, statistics.get(4).isIncome());
	}

	@Test
	public void readStatisticsByMonthsAndAccountId_InvalidMonth() {
		List<StatisticsPair> statistics = statisticsDAO.readStatisticsByMonthAndAccountId(createMonth("2002.01"),
				ACCOUNT_ID);

		assertNotNull(statistics);
		assertEquals(4, statistics.size());
		assertEquals(22, statistics.get(0).getId());
		assertEquals("food", statistics.get(0).getName());
		assertEquals(0.0, statistics.get(0).getValue(), 0.01);
		assertEquals(24, statistics.get(1).getId());
		assertEquals("income", statistics.get(1).getName());
		assertEquals(0, statistics.get(1).getValue(), 0.01);
		assertEquals(23, statistics.get(2).getId());
		assertEquals("luxury", statistics.get(2).getName());
		assertEquals(0.0, statistics.get(2).getValue(), 0.01);
		assertEquals(21, statistics.get(3).getId());
		assertEquals("sports", statistics.get(3).getName());
		assertEquals(0.0, statistics.get(3).getValue(), 0.01);
	}

	@Test
	public void readStatisticsByMonthsAndAccountId_InvalidAccount() {
		List<StatisticsPair> statistics = statisticsDAO.readStatisticsByMonthAndAccountId(createMonth("2015.06"), 9);

		assertNotNull(statistics);
		assertEquals(0, statistics.size());
	}

	@Test
	public void readTopTenByAccountId() {
		List<Expense> expenses = statisticsDAO.readTopTenByAccountId(ACCOUNT_ID);

		assertNotNull(expenses);
		assertEquals(4, expenses.size());
		assertEquals("climbing", expenses.get(0).getReason());
		assertEquals("sports", expenses.get(0).getCategoryName());
		assertEquals(21, expenses.get(0).getCategoryId());
		assertEquals("supermarket", expenses.get(1).getReason());
		assertEquals("food", expenses.get(1).getCategoryName());
		assertEquals(22, expenses.get(1).getCategoryId());
		assertEquals("jewels", expenses.get(2).getReason());
		assertEquals("luxury", expenses.get(2).getCategoryName());
		assertEquals(23, expenses.get(2).getCategoryId());
		assertEquals("climbing", expenses.get(3).getReason());
		assertEquals("luxury", expenses.get(3).getCategoryName());
		assertEquals(23, expenses.get(3).getCategoryId());
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
