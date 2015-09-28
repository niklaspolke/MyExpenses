package vu.de.npolke.myexpenses.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import vu.de.npolke.myexpenses.servlets.util.StatisticsPair;

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

	private static StatisticsDAO statisticsDAO;

	private static long testCounter = 0;

	public StatisticsDAOTest() {
		super("StatisticsDAOTest" + ++testCounter);
	}

	@BeforeClass
	public static void initialise() {
		statisticsDAO = (StatisticsDAO) DAOFactory.getDAO(StatisticsPair.class);
	}

	@Test
	public void readDistinctMonthsByAccountId_ValidAccount() {
		List<String> months = statisticsDAO.readDistinctMonthsByAccountId(1);

		assertNotNull(months);
		assertEquals(2, months.size());
		assertEquals("2015.06", months.get(0));
		assertEquals("2015.05", months.get(1));
	}

	@Test
	public void readDistinctMonthsByAccountId_InvalidAccount() {
		List<String> months = statisticsDAO.readDistinctMonthsByAccountId(9);

		assertNotNull(months);
		assertTrue(months.isEmpty());
	}

	@Test
	public void readStatisticsByMonthsAndAccountId_201506() {
		List<StatisticsPair> statistics = statisticsDAO.readStatisticsByMonthsAndAccountId("2015.06", 1);

		assertNotNull(statistics);
		assertEquals(3, statistics.size());
		assertEquals("food", statistics.get(0).getName());
		assertEquals(0.0, statistics.get(0).getValue(), 0.01);
		assertEquals("luxury", statistics.get(1).getName());
		assertEquals(80.0, statistics.get(1).getValue(), 0.01);
		assertEquals("sports", statistics.get(2).getName());
		assertEquals(0.0, statistics.get(2).getValue(), 0.01);
	}

	@Test
	public void readStatisticsByMonthsAndAccountId_201505() {
		List<StatisticsPair> statistics = statisticsDAO.readStatisticsByMonthsAndAccountId("2015.05", 1);

		assertNotNull(statistics);
		assertEquals(3, statistics.size());
		assertEquals("food", statistics.get(0).getName());
		assertEquals(9.05, statistics.get(0).getValue(), 0.01);
		assertEquals("luxury", statistics.get(1).getName());
		assertEquals(700.0, statistics.get(1).getValue(), 0.01);
		assertEquals("sports", statistics.get(2).getName());
		assertEquals(0.0, statistics.get(2).getValue(), 0.01);
	}

	@Test
	public void readStatisticsByMonthsAndAccountId_InvalidMonth() {
		List<StatisticsPair> statistics = statisticsDAO.readStatisticsByMonthsAndAccountId("2015.07", 1);

		assertNotNull(statistics);
		assertEquals(3, statistics.size());
		assertEquals("food", statistics.get(0).getName());
		assertEquals(0.0, statistics.get(0).getValue(), 0.01);
		assertEquals("luxury", statistics.get(1).getName());
		assertEquals(0.0, statistics.get(1).getValue(), 0.01);
		assertEquals("sports", statistics.get(2).getName());
		assertEquals(0.0, statistics.get(2).getValue(), 0.01);
	}

	@Test
	public void readStatisticsByMonthsAndAccountId_InvalidAccount() {
		List<StatisticsPair> statistics = statisticsDAO.readStatisticsByMonthsAndAccountId("2015.06", 9);

		assertNotNull(statistics);
		assertEquals(0, statistics.size());
	}
}
