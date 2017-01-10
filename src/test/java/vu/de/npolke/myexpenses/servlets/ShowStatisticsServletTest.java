package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.services.StatisticsDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;
import vu.de.npolke.myexpenses.servlets.util.StatisticsPair;
import vu.de.npolke.myexpenses.util.Month;
import vu.de.npolke.myexpenses.util.TimerMock;

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
public class ShowStatisticsServletTest {

	private static final long ACCOUNT_ID = 123;
	private static final double DELTA = 0.001;

	public static final long FAKE_TIME = 1483999598469L; // within 2017.01

	public static final String JSON_BARCHART_OPTIONS = "{\"chartPadding\":{\"top\":5,\"right\":5,\"buttom\":5,\"left\":25},\"distributeSeries\":true,\"horizontalBars\":true,\"reverseData\":true}";
	public static final String JSON_CHART_TEMPLATE = "{\"labels\":[{1}],\"series\":[{2}]}";
	public static final String JSON_BARCHART_TEMPLATE = "{\"labels\":[\"Income\",\"Fixed Costs\",\"Expenses\"],\"series\":[{1}]}";

	private ShowStatisticsServlet servlet;

	private Account account;
	private List<Month> months;

	private String fillTemplate(String template, String... params) {
		int index = 0;
		for (String param : params) {
			index++;
			template = template.replaceAll("\\{" + index + "\\}", param);
		}
		return template;
	}

	@Before
	public void init() {
		servlet = new ShowStatisticsServlet();
		servlet.statisticsDAO = mock(StatisticsDAO.class);
		servlet.timer = new TimerMock(FAKE_TIME);

		account = new Account();
		account.setId(ACCOUNT_ID);

		months = new ArrayList<Month>();
	}

	@Test
	public void getSelectedMonths_NoMonth() {
		final Month MONTH = Month.createMonth(2017, 1);

		assertNull(servlet.getSelectedMonth(MONTH, months));
	}

	@Test
	public void getSelectedMonths_SmallerMonths() {
		final Month MONTH = Month.createMonth(2017, 1);
		months.add(Month.createMonth(2016, 10));
		months.add(Month.createMonth(2016, 11));
		Month nearestMonth = Month.createMonth(2016, 12);
		months.add(nearestMonth);

		assertEquals(nearestMonth, servlet.getSelectedMonth(MONTH, months));
	}

	@Test
	public void getSelectedMonths_BiggerMonths() {
		final Month MONTH = Month.createMonth(2017, 1);
		Month nearestMonth = Month.createMonth(2017, 2);
		months.add(nearestMonth);
		months.add(Month.createMonth(2017, 3));
		months.add(Month.createMonth(2017, 4));

		assertEquals(nearestMonth, servlet.getSelectedMonth(MONTH, months));
	}

	@Test
	public void getSelectedMonths_BiggerMonths_ReverseOrder() {
		final Month MONTH = Month.createMonth(2017, 1);
		months.add(Month.createMonth(2017, 4));
		months.add(Month.createMonth(2017, 3));
		Month nearestMonth = Month.createMonth(2017, 2);
		months.add(nearestMonth);

		assertEquals(nearestMonth, servlet.getSelectedMonth(MONTH, months));
	}

	@Test
	public void getSelectedMonths_SmallerAndBiggerMonths() {
		final Month MONTH = Month.createMonth(2017, 1);
		months.add(Month.createMonth(2016, 10));
		months.add(Month.createMonth(2016, 11));
		Month nearestMonth = Month.createMonth(2016, 12);
		months.add(nearestMonth);
		months.add(Month.createMonth(2017, 2));
		months.add(Month.createMonth(2017, 3));
		months.add(Month.createMonth(2017, 4));

		assertEquals(nearestMonth, servlet.getSelectedMonth(MONTH, months));
	}

	@Test
	public void getSelectedMonths_SmallerAndEqualAndBiggerMonths() {
		final Month MONTH = Month.createMonth(2017, 1);
		months.add(Month.createMonth(2016, 10));
		months.add(Month.createMonth(2016, 11));
		months.add(Month.createMonth(2016, 12));
		Month equalMonth = Month.createMonth(2017, 1);
		months.add(equalMonth);
		months.add(Month.createMonth(2017, 2));
		months.add(Month.createMonth(2017, 3));
		months.add(Month.createMonth(2017, 4));

		assertEquals(equalMonth, servlet.getSelectedMonth(MONTH, months));
	}

	@Test
	public void prepareStatistics_WithMonth() {
		servlet = spy(servlet);
		// equal to FAKE TIME, even if it is not used within this test
		final Month MONTH = Month.createMonth("2017.01");
		months.add(MONTH);
		months.add(Month.createMonth("2015.06"));
		months.add(Month.createMonth("2015.05"));
		when(servlet.statisticsDAO.readDistinctMonthsByAccountId(ACCOUNT_ID)).thenReturn(months);

		final ServletReaction reaction = servlet.prepareStatistics(account, MONTH.toString());

		verify(servlet).getSelectedMonth(MONTH, months);
		verify(servlet).readStatisticsForMonth(any(ServletReaction.class), eq(MONTH), eq(account));
		assertNotNull(reaction);
		assertEquals(months, reaction.getRequestAttributes().get("months"));
		assertEquals("WEB-INF/showstatistics.jsp", reaction.getForward());
	}

	@Test
	public void prepareStatistics_NoMonths() {
		servlet = spy(servlet);
		months.clear();
		when(servlet.statisticsDAO.readDistinctMonthsByAccountId(ACCOUNT_ID)).thenReturn(months);

		final ServletReaction reaction = servlet.prepareStatistics(account, null);

		verify(servlet, never()).readStatisticsForMonth(any(ServletReaction.class), any(Month.class), eq(account));
		assertNotNull(reaction);
		assertEquals(months, reaction.getRequestAttributes().get("months"));
		assertEquals("WEB-INF/showstatistics.jsp", reaction.getForward());
	}

	@Test
	public void showStatistics() {
		final Month MONTH = Month.createMonth("2015.05");

		final ServletReaction reaction = servlet.showStatistics(account, MONTH.toString());

		assertNotNull(reaction);
		assertEquals("showstatistics.jsp?month=2015.05", reaction.getRedirect());
	}

	private void assertStatisticsSizeAndSum(final ServletReaction reaction, final String sessionAttribute,
			final int expectedSize, final double exptectedSum) {
		assertNotNull(reaction.getRequestAttributes().get(sessionAttribute));

		@SuppressWarnings("unchecked")
		List<StatisticsPair> statistics = (List<StatisticsPair>) reaction.getRequestAttributes().get(sessionAttribute);
		assertEquals(expectedSize + 1, statistics.size());

		StatisticsPair sum = statistics.get(expectedSize);
		assertEquals(exptectedSum, sum.getValue(), DELTA);
	}

	@Test
	public void readStatisticsForMonth_NoStatistics_NullMonth() {
		final ServletReaction reaction = new ServletReaction();
		when(servlet.statisticsDAO.readStatisticsByMonthAndAccountId(null, ACCOUNT_ID))
				.thenReturn(new ArrayList<StatisticsPair>());

		servlet.readStatisticsForMonth(reaction, null, account);

		assertEquals(null, reaction.getRequestAttributes().get("month"));
		assertStatisticsSizeAndSum(reaction, "statistics", 0, 0.0);
		assertStatisticsSizeAndSum(reaction, "statisticsMonthlyCosts", 0, 0.0);
		assertStatisticsSizeAndSum(reaction, "statisticsIncome", 0, 0.0);
		assertEquals(fillTemplate(JSON_CHART_TEMPLATE, "", ""), reaction.getRequestAttributes().get("chart"));
		assertEquals(fillTemplate(JSON_BARCHART_TEMPLATE, "0.0,0.0,0.0"),
				reaction.getRequestAttributes().get("barchart"));
		assertEquals(JSON_BARCHART_OPTIONS, reaction.getRequestAttributes().get("barchartoptions"));
		assertEquals(0.0, (Double) reaction.getRequestAttributes().get("sum"), DELTA);
	}

	@Test
	public void readStatisticsForMonth_NoStatistics() {
		final ServletReaction reaction = new ServletReaction();
		final Month MONTH = Month.createMonth("2015.05");
		when(servlet.statisticsDAO.readStatisticsByMonthAndAccountId(MONTH, ACCOUNT_ID))
				.thenReturn(new ArrayList<StatisticsPair>());

		servlet.readStatisticsForMonth(reaction, MONTH, account);

		assertEquals(MONTH, reaction.getRequestAttributes().get("month"));
		assertStatisticsSizeAndSum(reaction, "statistics", 0, 0.0);
		assertStatisticsSizeAndSum(reaction, "statisticsMonthlyCosts", 0, 0.0);
		assertStatisticsSizeAndSum(reaction, "statisticsIncome", 0, 0.0);
		assertEquals(fillTemplate(JSON_CHART_TEMPLATE, "", ""), reaction.getRequestAttributes().get("chart"));
		assertEquals(fillTemplate(JSON_BARCHART_TEMPLATE, "0.0,0.0,0.0"),
				reaction.getRequestAttributes().get("barchart"));
		assertEquals(JSON_BARCHART_OPTIONS, reaction.getRequestAttributes().get("barchartoptions"));
		assertEquals(0.0, (Double) reaction.getRequestAttributes().get("sum"), DELTA);
	}

	@Test
	public void readStatisticsForMonth_WithStatistics() {
		final ServletReaction reaction = new ServletReaction();
		final Month MONTH = Month.createMonth("2015.05");
		final List<StatisticsPair> statistic = new ArrayList<StatisticsPair>();
		statistic.add(new StatisticsPair(1l, "Sports", 4d, false, false));
		statistic.add(new StatisticsPair(2l, "Income", 4d, true, true));
		statistic.add(new StatisticsPair(3l, "Food", 4d, false, false));
		statistic.add(new StatisticsPair(4l, "Income", 1d, false, true));
		statistic.add(new StatisticsPair(5l, "Home", 4d, false, false));
		statistic.add(new StatisticsPair(6l, "Insurances", 5d, true, false));
		statistic.add(new StatisticsPair(7l, "Flat", 5d, true, false));
		when(servlet.statisticsDAO.readStatisticsByMonthAndAccountId(MONTH, ACCOUNT_ID)).thenReturn(statistic);

		servlet.readStatisticsForMonth(reaction, MONTH, account);

		assertEquals(MONTH, reaction.getRequestAttributes().get("month"));
		assertStatisticsSizeAndSum(reaction, "statistics", 3, 12.0);
		assertStatisticsSizeAndSum(reaction, "statisticsMonthlyCosts", 2, 10.0);
		assertStatisticsSizeAndSum(reaction, "statisticsIncome", 2, 5.0);
		assertEquals(fillTemplate(JSON_CHART_TEMPLATE, "\"Sports\",\"Food\",\"Home\"", "4.0,4.0,4.0"),
				reaction.getRequestAttributes().get("chart"));
		assertEquals(fillTemplate(JSON_BARCHART_TEMPLATE, "5.0,10.0,12.0"),
				reaction.getRequestAttributes().get("barchart"));
		assertEquals(JSON_BARCHART_OPTIONS, reaction.getRequestAttributes().get("barchartoptions"));
		assertEquals(-17.0, (Double) reaction.getRequestAttributes().get("sum"), DELTA);
	}
}
