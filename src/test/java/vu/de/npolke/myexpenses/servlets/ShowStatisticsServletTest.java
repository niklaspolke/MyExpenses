package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
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

public class ShowStatisticsServletTest {

	private static final long ACCOUNT_ID = 123;
	private static final double DELTA = 0.001;

	public static final String JSON_BARCHART_OPTIONS = "{\"chartPadding\":{\"top\":5,\"right\":5,\"buttom\":5,\"left\":25},\"distributeSeries\":true,\"horizontalBars\":true,\"reverseData\":true}";
	public static final String JSON_CHART_TEMPLATE = "{\"labels\":[{1}],\"series\":[{2}]}";
	public static final String JSON_BARCHART_TEMPLATE = "{\"labels\":[\"Income\",\"Fixed Costs\",\"Expenses\"],\"series\":[{1}]}";

	private ShowStatisticsServlet servlet;

	private Account account;
	private List<String> months;

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

		account = new Account();
		account.setId(ACCOUNT_ID);

		months = new ArrayList<String>();
	}

	@Test
	public void prepareStatistics_WithMonths() {
		servlet = spy(servlet);
		final String MONTH = "2015.06";
		months.add(MONTH);
		months.add("2015.05");
		when(servlet.statisticsDAO.readDistinctMonthsByAccountId(ACCOUNT_ID)).thenReturn(months);

		final ServletReaction reaction = servlet.prepareStatistics(account);

		verify(servlet).readStatisticsForMonth(any(ServletReaction.class), eq(MONTH), eq(account));
		assertNotNull(reaction);
		assertEquals(months, reaction.getSessionAttributes().get("months"));
		assertEquals("showstatistics.jsp", reaction.getRedirect());
	}

	@Test
	public void prepareStatistics_NoMonths() {
		servlet = spy(servlet);
		months.clear();
		when(servlet.statisticsDAO.readDistinctMonthsByAccountId(ACCOUNT_ID)).thenReturn(months);

		final ServletReaction reaction = servlet.prepareStatistics(account);

		verify(servlet).readStatisticsForMonth(any(ServletReaction.class), isNull(String.class), eq(account));
		assertNotNull(reaction);
		assertEquals(months, reaction.getSessionAttributes().get("months"));
		assertEquals("showstatistics.jsp", reaction.getRedirect());
	}

	@Test
	public void showStatistics() {
		servlet = spy(servlet);
		final String MONTH = "2015.05";

		final ServletReaction reaction = servlet.showStatistics(account, MONTH);

		verify(servlet).readStatisticsForMonth(any(ServletReaction.class), eq(MONTH), eq(account));
		assertNotNull(reaction);
		assertEquals("showstatistics.jsp", reaction.getRedirect());
	}

	private void assertStatisticsSizeAndSum(final ServletReaction reaction, final String sessionAttribute,
			final int expectedSize, final double exptectedSum) {
		assertNotNull(reaction.getSessionAttributes().get(sessionAttribute));

		@SuppressWarnings("unchecked")
		List<StatisticsPair> statistics = (List<StatisticsPair>) reaction.getSessionAttributes().get(sessionAttribute);
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

		assertEquals(null, reaction.getSessionAttributes().get("month"));
		assertStatisticsSizeAndSum(reaction, "statistics", 0, 0.0);
		assertStatisticsSizeAndSum(reaction, "statisticsMonthlyCosts", 0, 0.0);
		assertStatisticsSizeAndSum(reaction, "statisticsIncome", 0, 0.0);
		assertEquals(fillTemplate(JSON_CHART_TEMPLATE, "", ""), reaction.getSessionAttributes().get("chart"));
		assertEquals(fillTemplate(JSON_BARCHART_TEMPLATE, "0.0,0.0,0.0"),
				reaction.getSessionAttributes().get("barchart"));
		assertEquals(JSON_BARCHART_OPTIONS, reaction.getSessionAttributes().get("barchartoptions"));
		assertEquals(0.0, (Double) reaction.getSessionAttributes().get("sum"), DELTA);
	}

	@Test
	public void readStatisticsForMonth_NoStatistics() {
		final ServletReaction reaction = new ServletReaction();
		final String MONTH = "2015.05";
		when(servlet.statisticsDAO.readStatisticsByMonthAndAccountId(MONTH, ACCOUNT_ID))
				.thenReturn(new ArrayList<StatisticsPair>());

		servlet.readStatisticsForMonth(reaction, MONTH, account);

		assertEquals(MONTH, reaction.getSessionAttributes().get("month"));
		assertStatisticsSizeAndSum(reaction, "statistics", 0, 0.0);
		assertStatisticsSizeAndSum(reaction, "statisticsMonthlyCosts", 0, 0.0);
		assertStatisticsSizeAndSum(reaction, "statisticsIncome", 0, 0.0);
		assertEquals(fillTemplate(JSON_CHART_TEMPLATE, "", ""), reaction.getSessionAttributes().get("chart"));
		assertEquals(fillTemplate(JSON_BARCHART_TEMPLATE, "0.0,0.0,0.0"),
				reaction.getSessionAttributes().get("barchart"));
		assertEquals(JSON_BARCHART_OPTIONS, reaction.getSessionAttributes().get("barchartoptions"));
		assertEquals(0.0, (Double) reaction.getSessionAttributes().get("sum"), DELTA);
	}

	@Test
	public void readStatisticsForMonth_WithStatistics() {
		final ServletReaction reaction = new ServletReaction();
		final String MONTH = "2015.05";
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

		assertEquals(MONTH, reaction.getSessionAttributes().get("month"));
		assertStatisticsSizeAndSum(reaction, "statistics", 3, 12.0);
		assertStatisticsSizeAndSum(reaction, "statisticsMonthlyCosts", 2, 10.0);
		assertStatisticsSizeAndSum(reaction, "statisticsIncome", 2, 5.0);
		assertEquals(fillTemplate(JSON_CHART_TEMPLATE, "\"Sports\",\"Food\",\"Home\"", "4.0,4.0,4.0"),
				reaction.getSessionAttributes().get("chart"));
		assertEquals(fillTemplate(JSON_BARCHART_TEMPLATE, "5.0,10.0,12.0"),
				reaction.getSessionAttributes().get("barchart"));
		assertEquals(JSON_BARCHART_OPTIONS, reaction.getSessionAttributes().get("barchartoptions"));
		assertEquals(-17.0, (Double) reaction.getSessionAttributes().get("sum"), DELTA);
	}
}
