package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
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

	private ShowStatisticsServlet servlet;

	private Account account;
	private List<String> months;

	@Before
	public void init() {
		servlet = new ShowStatisticsServlet();
		servlet.statisticsDAO = mock(StatisticsDAO.class);

		account = new Account();
		account.setId(ACCOUNT_ID);

		months = new ArrayList<String>();
	}

	@Test
	public void prepareStatistics() {
		final String MONTH = "2015.06";
		months.add(MONTH);
		months.add("2015.05");
		final List<StatisticsPair> statistic = new ArrayList<StatisticsPair>();
		statistic.add(new StatisticsPair("category1", 1.1));
		statistic.add(new StatisticsPair("category2", 22.22));
		when(servlet.statisticsDAO.readDistinctMonthsByAccountId(ACCOUNT_ID)).thenReturn(months);
		when(servlet.statisticsDAO.readStatisticsByMonthsAndAccountId(MONTH, ACCOUNT_ID)).thenReturn(statistic);

		final ServletReaction reaction = servlet.prepareStatistics(account);

		assertNotNull(reaction);
		// correct values in session
		assertEquals(months, reaction.getSessionAttributes().get("months"));
		assertEquals(MONTH, reaction.getSessionAttributes().get("month"));
		assertEquals(statistic, reaction.getSessionAttributes().get("statistics"));
		assertEquals("{\"labels\":[\"category1\",\"category2\"],\"series\":[1.1,22.22]}",
				reaction.getSessionAttributes().get("chart"));
		// correct navigation
		assertEquals("showstatistics.jsp", reaction.getRedirect());
	}

	@Test
	public void prepareStatistics_noMonths() {
		final List<StatisticsPair> statistic = new ArrayList<StatisticsPair>();
		when(servlet.statisticsDAO.readDistinctMonthsByAccountId(ACCOUNT_ID)).thenReturn(months);
		when(servlet.statisticsDAO.readStatisticsByMonthsAndAccountId(null, ACCOUNT_ID)).thenReturn(statistic);

		final ServletReaction reaction = servlet.prepareStatistics(account);

		assertNotNull(reaction);
		// correct values in session
		assertEquals(months, reaction.getSessionAttributes().get("months"));
		assertEquals(null, reaction.getSessionAttributes().get("month"));
		assertEquals(statistic, reaction.getSessionAttributes().get("statistics"));
		assertEquals("{\"labels\":[],\"series\":[]}", reaction.getSessionAttributes().get("chart"));
		// correct navigation
		assertEquals("showstatistics.jsp", reaction.getRedirect());
	}

	@Test
	public void showStatistics() {
		final String MONTH = "2015.06";
		final List<StatisticsPair> statistic = new ArrayList<StatisticsPair>();
		statistic.add(new StatisticsPair("category1", 1.1));
		statistic.add(new StatisticsPair("category2", 22.22));
		when(servlet.statisticsDAO.readStatisticsByMonthsAndAccountId(MONTH, ACCOUNT_ID)).thenReturn(statistic);

		final ServletReaction reaction = servlet.showStatistics(account, MONTH);

		assertNotNull(reaction);
		// correct values in session
		assertEquals(MONTH, reaction.getSessionAttributes().get("month"));
		assertEquals(statistic, reaction.getSessionAttributes().get("statistics"));
		assertEquals("{\"labels\":[\"category1\",\"category2\"],\"series\":[1.1,22.22]}",
				reaction.getSessionAttributes().get("chart"));
		// correct navigation
		assertEquals("showstatistics.jsp", reaction.getRedirect());
	}
}
