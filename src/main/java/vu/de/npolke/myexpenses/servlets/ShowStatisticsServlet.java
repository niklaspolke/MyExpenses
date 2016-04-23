package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.services.DAOFactory;
import vu.de.npolke.myexpenses.services.StatisticsDAO;
import vu.de.npolke.myexpenses.servlets.util.JsonObject;
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
@WebServlet("/showstatistics")
public class ShowStatisticsServlet extends AbstractBasicServletOld {

	private static final long serialVersionUID = 1L;

	private StatisticsDAO statisticsDAO = (StatisticsDAO) DAOFactory.getDAO(StatisticsPair.class);

	private void readStatisticsForMonth(final HttpServletRequest request, final String month, final Account account) {
		HttpSession session = request.getSession();

		List<StatisticsPair> statistics = statisticsDAO.readStatisticsByMonthsAndAccountId(month, account.getId());

		List<String> labels = new ArrayList<String>();
		List<Double> values = new ArrayList<Double>();
		Double sum = new Double(0);
		for (StatisticsPair pair : statistics) {
			labels.add(pair.getName());
			values.add(pair.getValue());
			sum += pair.getValue();
		}
		statistics.add(new StatisticsPair("Summe", sum));

		JsonObject json = new JsonObject();
		json.addParameter("labels", labels);
		json.addParameter("series", values);

		session.setAttribute("chart", json.toString());
		session.setAttribute("statistics", statistics);
		session.setAttribute("month", month);
	}

	@Override
	public void doGet(final HttpServletRequest request, final HttpServletResponse response, final HttpSession session,
			Account account) throws ServletException, IOException {

		List<String> months = statisticsDAO.readDistinctMonthsByAccountId(account.getId());
		readStatisticsForMonth(request, months.get(0), account);

		session.setAttribute("months", months);
		response.sendRedirect("showstatistics.jsp");
	}

	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		String month = request.getParameter("month");

		readStatisticsForMonth(request, month, account);

		response.sendRedirect("showstatistics.jsp");
	}
}
