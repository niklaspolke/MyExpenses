package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import vu.de.npolke.myexpenses.servlets.util.StatisticsPair;
import vu.de.npolke.myexpenses.util.Month;
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

	public static final String TEXT_TOTAL = "Total";

	private static final long serialVersionUID = 1L;

	StatisticsDAO statisticsDAO = (StatisticsDAO) DAOFactory.getDAO(StatisticsPair.class);

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

	protected void readStatisticsForMonth(final ServletReaction reaction, final Month month, final Account account) {
		List<StatisticsPair> statisticsAll = statisticsDAO.readStatisticsByMonthAndAccountId(month, account.getId());
		List<StatisticsPair> statistics = new ArrayList<StatisticsPair>();
		List<StatisticsPair> statisticsMonthlyCosts = new ArrayList<StatisticsPair>();
		List<StatisticsPair> statisticsIncome = new ArrayList<StatisticsPair>();

		List<String> labels = new ArrayList<String>();
		List<Double> values = new ArrayList<Double>();
		Double sum = new Double(0);
		Double sumMonthlyCosts = new Double(0);
		Double sumIncome = new Double(0);
		for (StatisticsPair pair : statisticsAll) {
			if (pair.isIncome()) {
				statisticsIncome.add(pair);
				sumIncome += pair.getValue();
			} else if (pair.isMonthly() && !pair.isIncome()) {
				statisticsMonthlyCosts.add(pair);
				sumMonthlyCosts += pair.getValue();
			} else {
				statistics.add(pair);
				labels.add(pair.getName());
				values.add(pair.getValue());
				sum += pair.getValue();
			}
		}
		statistics.add(new StatisticsPair(0l, TEXT_TOTAL, sum, false, false));
		statisticsMonthlyCosts.add(new StatisticsPair(0l, TEXT_TOTAL, sumMonthlyCosts, true, false));
		statisticsIncome.add(new StatisticsPair(0l, TEXT_TOTAL, sumIncome, true, true));

		JsonObject json = new JsonObject();
		json.addParameter("labels", labels);
		json.addParameter("series", values);

		JsonObject barchart = new JsonObject();
		barchart.addParameter("labels", new String[] { "Income", "Fixed Costs", "Expenses" });
		barchart.addParameter("series", new Double[] { sumIncome, sumMonthlyCosts, sum });

		reaction.setRequestAttribute("chart", json.toString());
		reaction.setRequestAttribute("barchart", barchart.toString());
		setBarChartOptions(reaction);
		reaction.setRequestAttribute("statistics", statistics);
		reaction.setRequestAttribute("statisticsMonthlyCosts", statisticsMonthlyCosts);
		reaction.setRequestAttribute("statisticsIncome", statisticsIncome);
		reaction.setRequestAttribute("sum", sumIncome - sumMonthlyCosts - sum);
		reaction.setRequestAttribute("month", month);
	}

	@Override
	public ServletReaction doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		final String month = request.getParameter("month");

		return prepareStatistics(account, month);
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

	public ServletReaction prepareStatistics(final Account account, final String monthAsString) {
		ServletReaction reaction = new ServletReaction();
		Month month = Month.createMonth(monthAsString);
		if (month == null) {
			month = Month.createMonthFromTimeMillis(timer.getCurrentTimeMillis());
		}

		List<Month> months = statisticsDAO.readDistinctMonthsByAccountId(account.getId());
		month = getSelectedMonth(month, months);
		if (month != null) {
			readStatisticsForMonth(reaction, month, account);
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
