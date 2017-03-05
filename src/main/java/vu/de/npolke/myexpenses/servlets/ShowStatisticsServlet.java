package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.services.DAOFactory;
import vu.de.npolke.myexpenses.services.StatisticsDAO;
import vu.de.npolke.myexpenses.servlets.util.JsonObject;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;
import vu.de.npolke.myexpenses.util.Month;
import vu.de.npolke.myexpenses.util.StatisticsElement;
import vu.de.npolke.myexpenses.util.StatisticsOfMonth;
import vu.de.npolke.myexpenses.util.Timer;

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
@WebServlet("/showstatistics.jsp")
public class ShowStatisticsServlet extends AbstractBasicServlet {

	public static final String PROPERTIES = "messages";
	public static final String PROPERTY_INCOME = "statistics.income.title";
	public static final String PROPERTY_MONTHLYEXPENSES = "statistics.monthlycosts.title";
	public static final String PROPERTY_EXPENSES = "statistics.expenses.title";

	public static final String TEXT_TOTAL = "Total";

	private static final long serialVersionUID = 1L;

	StatisticsDAO statisticsDAO = (StatisticsDAO) DAOFactory.getDAO(StatisticsElement.class);

	Timer timer = new Timer();

	private void setBarChartOptions(final ServletReaction reaction) {
		JsonObject barchartOptions = new JsonObject();
		barchartOptions.addParameter("horizontalBars", Boolean.TRUE);
		barchartOptions.addParameter("reverseData", Boolean.TRUE);
		barchartOptions.addParameter("distributeSeries", Boolean.TRUE);
		Map<String, Integer> chartPadding = new LinkedHashMap<String, Integer>();
		chartPadding.put("top", 5);
		chartPadding.put("right", 5);
		chartPadding.put("buttom", 5);
		chartPadding.put("left", 25);
		barchartOptions.addParameter("chartPadding", chartPadding);

		reaction.setRequestAttribute("barchartoptions", barchartOptions.toString());
	}

	protected JsonObject exportToPieChart(final List<StatisticsElement> statistics) {
		List<String> labels = new ArrayList<String>();
		List<Double> values = new ArrayList<Double>();

		for (StatisticsElement pair : statistics) {
			labels.add(pair.getCategory());
			values.add(pair.getAmount());
		}

		JsonObject json = new JsonObject();
		json.addParameter("labels", labels);
		json.addParameter("series", values);
		return json;
	}

	protected void readStatisticsForMonth(final ServletReaction reaction, final Month month, final Account account,
			final Locale locale) {
		StatisticsOfMonth statistics = statisticsDAO.readStatisticsByMonthAndAccountId(month, account.getId());

		List<StatisticsElement> statsExpenses = statistics.getExpenses();
		statsExpenses.add(StatisticsElement.create(month, TEXT_TOTAL, statistics.getSumExpenses(), false, false));
		List<StatisticsElement> statsMonthlyExpenses = statistics.getMonthlyExpenses();
		statsMonthlyExpenses.add(StatisticsElement.create(month, TEXT_TOTAL, statistics.getSumMonthlyExpenses(), true, false));
		List<StatisticsElement> statsIncome = statistics.getIncome();
		statsIncome.add(StatisticsElement.create(month, TEXT_TOTAL, statistics.getSumIncome(), true, true));

		JsonObject barchart = new JsonObject();
		ResourceBundle properties = ResourceBundle.getBundle(PROPERTIES, locale);
		barchart.addParameter("labels", new String[] { properties.getString(PROPERTY_INCOME),
				properties.getString(PROPERTY_MONTHLYEXPENSES), properties.getString(PROPERTY_EXPENSES) });
		barchart.addParameter("series", new Double[] { statistics.getSumIncome(), statistics.getSumMonthlyExpenses(),
				statistics.getSumExpenses() });

		reaction.setRequestAttribute("chart", exportToPieChart(statistics.getExpenses()).toString());
		reaction.setRequestAttribute("barchart", barchart.toString());
		setBarChartOptions(reaction);
		reaction.setRequestAttribute("statistics", statsExpenses);
		reaction.setRequestAttribute("statisticsMonthlyCosts", statsMonthlyExpenses);
		reaction.setRequestAttribute("statisticsIncome", statsIncome);
		reaction.setRequestAttribute("sum",
				statistics.getSumIncome() - statistics.getSumMonthlyExpenses() - statistics.getSumExpenses());
		reaction.setRequestAttribute("month", month);
	}

	@Override
	public ServletReaction doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		final String month = request.getParameter("month");
		final String locale = (String) session.getAttribute(LoginServlet.COOKIE_LOCALE);

		return prepareStatistics(account, month, locale);
	}

	Month getSelectedMonth(final Month wantedMonth, final List<Month> existingMonths) {
		Month selectedMonth = null;
		List<Month> sortedMonths = new ArrayList<Month>(existingMonths);
		Collections.sort(sortedMonths);
		for (Month month : sortedMonths) {
			if (selectedMonth == null || month.compareTo(wantedMonth) <= 0) {
				selectedMonth = month;
			} else {
				break;
			}
		}
		return selectedMonth;
	}

	public ServletReaction prepareStatistics(final Account account, final String monthAsString,
			final String localeAsString) {
		ServletReaction reaction = new ServletReaction();
		Month month = Month.createMonth(monthAsString);
		if (month == null) {
			month = Month.createMonthFromTimeMillis(timer.getCurrentTimeMillis());
		}

		List<Month> months = statisticsDAO.readDistinctMonthsByAccountId(account.getId());
		month = getSelectedMonth(month, months);
		if (month != null) {
			Locale locale;
			if (localeAsString != null && !localeAsString.equalsIgnoreCase("en")) {
				locale = new Locale(localeAsString);
			} else {
				locale = new Locale("");
			}
			readStatisticsForMonth(reaction, month, account, locale);
		}

		reaction.setRequestAttribute("months", months);
		reaction.setForward("WEB-INF/showstatistics.jsp");

		return reaction;
	}

	@Override
	public ServletReaction doPost(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		final String month = request.getParameter("month");

		return showStatistics(account, month);
	}

	public ServletReaction showStatistics(final Account account, final String month) {
		ServletReaction reaction = new ServletReaction();

		reaction.setRedirect("showstatistics.jsp").add("month", month);

		return reaction;
	}
}
