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
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.services.StatisticsDAO;
import vu.de.npolke.myexpenses.servlets.util.JsonObject;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;
import vu.de.npolke.myexpenses.util.Month;
import vu.de.npolke.myexpenses.util.StatisticsElement;
import vu.de.npolke.myexpenses.util.StatisticsOfMonth;
import vu.de.npolke.myexpenses.util.TimerMock;

/**
 * Copyright 2015 Niklas Polke
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * @author Niklas Polke
 */
public class ShowStatisticsServletTest {

	private static final long ACCOUNT_ID = 123;
	private static final double DELTA = 0.001;

	public static final long FAKE_TIME = 1483999598469L; // within 2017.01

	private static final Month MONTH = Month.create(2015, 5);

	public static final String JSON_BARCHART_OPTIONS = "{\"chartPadding\":{\"top\":5,\"right\":5,\"buttom\":5,\"left\":25},\"distributeSeries\":true,\"horizontalBars\":true,\"reverseData\":true}";
	public static final String JSON_CHART_TEMPLATE = "{\"labels\":[{1}],\"series\":[{2}]}";
	public static final String JSON_BARCHART_TEMPLATE = "{\"labels\":[\"Income\",\"Monthly Costs\",\"Expenses\"],\"series\":[{1}]}";

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
		final Month MONTH = Month.create(2017, 1);

		assertNull(servlet.getSelectedMonth(MONTH, months));
	}

	@Test
	public void getSelectedMonths_SmallerMonths() {
		final Month MONTH = Month.create(2017, 1);
		months.add(Month.create(2016, 10));
		months.add(Month.create(2016, 11));
		Month nearestMonth = Month.create(2016, 12);
		months.add(nearestMonth);

		assertEquals(nearestMonth, servlet.getSelectedMonth(MONTH, months));
	}

	@Test
	public void getSelectedMonths_BiggerMonths() {
		final Month MONTH = Month.create(2017, 1);
		Month nearestMonth = Month.create(2017, 2);
		months.add(nearestMonth);
		months.add(Month.create(2017, 3));
		months.add(Month.create(2017, 4));

		assertEquals(nearestMonth, servlet.getSelectedMonth(MONTH, months));
	}

	@Test
	public void getSelectedMonths_BiggerMonths_ReverseOrder() {
		final Month MONTH = Month.create(2017, 1);
		months.add(Month.create(2017, 4));
		months.add(Month.create(2017, 3));
		Month nearestMonth = Month.create(2017, 2);
		months.add(nearestMonth);

		assertEquals(nearestMonth, servlet.getSelectedMonth(MONTH, months));
	}

	@Test
	public void getSelectedMonths_SmallerAndBiggerMonths() {
		final Month MONTH = Month.create(2017, 1);
		months.add(Month.create(2016, 10));
		months.add(Month.create(2016, 11));
		Month nearestMonth = Month.create(2016, 12);
		months.add(nearestMonth);
		months.add(Month.create(2017, 2));
		months.add(Month.create(2017, 3));
		months.add(Month.create(2017, 4));

		assertEquals(nearestMonth, servlet.getSelectedMonth(MONTH, months));
	}

	@Test
	public void getSelectedMonths_SmallerAndEqualAndBiggerMonths() {
		final Month MONTH = Month.create(2017, 1);
		months.add(Month.create(2016, 10));
		months.add(Month.create(2016, 11));
		months.add(Month.create(2016, 12));
		Month equalMonth = Month.create(2017, 1);
		months.add(equalMonth);
		months.add(Month.create(2017, 2));
		months.add(Month.create(2017, 3));
		months.add(Month.create(2017, 4));

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
		when(servlet.statisticsDAO.readStatisticsByMonthAndAccountId(eq(MONTH), eq(ACCOUNT_ID)))
				.thenReturn(new StatisticsOfMonth(MONTH, new ArrayList<StatisticsElement>(),
						new ArrayList<StatisticsElement>(), new ArrayList<StatisticsElement>()));
		when(servlet.statisticsDAO.readDistinctMonthsByAccountId(ACCOUNT_ID)).thenReturn(months);

		final ServletReaction reaction = servlet.prepareStatistics(account, MONTH.toString(), "en");

		verify(servlet).getSelectedMonth(MONTH, months);
		verify(servlet).readStatisticsForMonth(any(ServletReaction.class), eq(MONTH), eq(account), eq(new Locale("")));
		assertNotNull(reaction);
		assertEquals(months, reaction.getRequestAttributes().get("months"));
		assertEquals("WEB-INF/showstatistics.jsp", reaction.getForward());
	}

	@Test
	public void prepareStatistics_NoMonths() {
		servlet = spy(servlet);
		months.clear();
		when(servlet.statisticsDAO.readDistinctMonthsByAccountId(ACCOUNT_ID)).thenReturn(months);

		final ServletReaction reaction = servlet.prepareStatistics(account, null, "en");

		verify(servlet, never()).readStatisticsForMonth(any(ServletReaction.class), any(Month.class), eq(account),
				eq(new Locale("")));
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
		List<StatisticsElement> statistics = (List<StatisticsElement>) reaction.getRequestAttributes()
				.get(sessionAttribute);
		assertEquals(expectedSize + 1, statistics.size());

		StatisticsElement sum = statistics.get(expectedSize);
		assertEquals(exptectedSum, sum.getAmount(), DELTA);
	}

	@Test
	public void exportToPieChart_empty() {
		JsonObject json = servlet.exportToPieChart(new ArrayList<StatisticsElement>());

		assertEquals(fillTemplate(JSON_CHART_TEMPLATE, "", ""), json.toString());
	}

	@Test
	public void exportToPieChart() {
		final List<StatisticsElement> statistic = new ArrayList<StatisticsElement>();
		statistic.add(StatisticsElement.create(MONTH, 1, "Sports", 4d, false, false));
		statistic.add(StatisticsElement.create(MONTH, 2, "Food", 4d, false, false));
		statistic.add(StatisticsElement.create(MONTH, 3, "Home", 4d, false, false));

		JsonObject json = servlet.exportToPieChart(statistic);

		assertEquals(fillTemplate(JSON_CHART_TEMPLATE, "\"Sports\",\"Food\",\"Home\"", "4.0,4.0,4.0"), json.toString());
	}

	@Test
	public void readStatisticsForMonth_NoStatistics() {
		final ServletReaction reaction = new ServletReaction();
		final Month MONTH = Month.createMonth("2015.05");
		when(servlet.statisticsDAO.readStatisticsByMonthAndAccountId(MONTH, ACCOUNT_ID))
				.thenReturn(new StatisticsOfMonth(MONTH, new ArrayList<StatisticsElement>(),
						new ArrayList<StatisticsElement>(), new ArrayList<StatisticsElement>()));

		servlet.readStatisticsForMonth(reaction, MONTH, account, new Locale(""));

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
		List<StatisticsElement> income = new ArrayList<StatisticsElement>();
		List<StatisticsElement> monthlyExpenses = new ArrayList<StatisticsElement>();
		List<StatisticsElement> expenses = new ArrayList<StatisticsElement>();
		income.add(StatisticsElement.create(MONTH, 1, "Income", 4d, true, true));
		expenses.add(StatisticsElement.create(MONTH, 2, "Food", 4d, false, false));
		expenses.add(StatisticsElement.create(MONTH, 3, "Home", 5d, false, false));
		expenses.add(StatisticsElement.create(MONTH, 4, "Sports", 6d, false, false));
		income.add(StatisticsElement.create(MONTH, 1, "Income", 1d, false, true));
		monthlyExpenses.add(StatisticsElement.create(MONTH, 5, "Insurances", 5d, true, false));
		monthlyExpenses.add(StatisticsElement.create(MONTH, 6, "Flat", 5d, true, false));
		final StatisticsOfMonth statistic = new StatisticsOfMonth(MONTH, income, monthlyExpenses, expenses);
		when(servlet.statisticsDAO.readStatisticsByMonthAndAccountId(MONTH, ACCOUNT_ID)).thenReturn(statistic);

		servlet.readStatisticsForMonth(reaction, MONTH, account, new Locale(""));

		assertEquals(MONTH, reaction.getRequestAttributes().get("month"));
		assertStatisticsSizeAndSum(reaction, "statistics", 3, 15.0);
		assertStatisticsSizeAndSum(reaction, "statisticsMonthlyCosts", 2, 10.0);
		assertStatisticsSizeAndSum(reaction, "statisticsIncome", 2, 5.0);
		assertEquals(fillTemplate(JSON_CHART_TEMPLATE, "\"Food\",\"Home\",\"Sports\"", "4.0,5.0,6.0"),
				reaction.getRequestAttributes().get("chart"));
		assertEquals(fillTemplate(JSON_BARCHART_TEMPLATE, "5.0,10.0,15.0"),
				reaction.getRequestAttributes().get("barchart"));
		assertEquals(JSON_BARCHART_OPTIONS, reaction.getRequestAttributes().get("barchartoptions"));
		assertEquals(-20.0, (Double) reaction.getRequestAttributes().get("sum"), DELTA);
	}
}
