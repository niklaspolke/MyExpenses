package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.backend.DatabaseConnection;
import vu.de.npolke.myexpenses.model.Account;
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
public class ShowStatisticsServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	//@formatter:off
	private static final String SELECT_DISTINCT_MONTHS =
		"SELECT DISTINCT year(e.day)+'.'+lpad(month(e.day),2,'0') AS month " +
				"FROM expense e " +
				"WHERE e.account_id = ?1 " +
				"ORDER BY month DESC";

	private static final String SELECT_STATISTICS_FOR_MONTH =
		"SELECT c.name as category, sum(e.amount) as sumofamount " +
		"FROM category c " +
		"LEFT OUTER JOIN ( " +
				"SELECT category_id, amount " +
				"FROM expense " +
				"WHERE year(day)+'.'+lpad(month(day),2,'0') = ?1 AND account_id = ?2 ) e " +
		"ON e.category_id = c.id " +
		"WHERE c.account_id = ?2 " +
		"GROUP BY c.name " +
		"ORDER BY c.name ASC";
	//@formatter:on

	private final DatabaseConnection DB_CONNECT = new DatabaseConnection();

	private static void readStatisticsForMonth(final String month, final HttpServletRequest request,
			final EntityManager dbConnection, final Account account) {
		Query statisticsQuery = dbConnection.createNativeQuery(SELECT_STATISTICS_FOR_MONTH);
		statisticsQuery.setParameter(1, month);
		statisticsQuery.setParameter(2, account.getId());
		@SuppressWarnings("unchecked")
		List<Object[]> queryResultStatistics = statisticsQuery.getResultList();
		List<StatisticsPair> list4Table = new ArrayList<StatisticsPair>();
		List<String> labels = new ArrayList<String>();
		List<Double> values = new ArrayList<Double>();
		for (Object[] queryResultStatistic : queryResultStatistics) {
			String category = (String) queryResultStatistic[0];
			Double value = (Double) queryResultStatistic[1];
			value = value != null ? value : 0.0;

			labels.add(category);
			values.add(value);
			list4Table.add(new StatisticsPair(category, value));
		}

		JsonObject json = new JsonObject();
		json.addParameter("labels", labels);
		json.addParameter("series", values);

		HttpSession session = request.getSession();
		session.setAttribute("chart", json.toString());
		session.setAttribute("statistics", list4Table);
		session.setAttribute("month", month);
	}

	@Override
	public void doGet(final HttpServletRequest request, final HttpServletResponse response, final HttpSession session,
			Account account) throws ServletException, IOException {

		EntityManager dbConnection = DB_CONNECT.connect();
		dbConnection.getTransaction().setRollbackOnly();

		Query monthsQuery = dbConnection.createNativeQuery(SELECT_DISTINCT_MONTHS);
		monthsQuery.setParameter(1, account.getId());
		@SuppressWarnings("unchecked")
		List<Object> queryResultMonths = monthsQuery.getResultList();
		List<String> months = new ArrayList<String>();
		for (Object queryResultMonth : queryResultMonths) {
			months.add((String) queryResultMonth);
		}

		readStatisticsForMonth(months.get(0), request, dbConnection, account);

		DB_CONNECT.rollback();
		DB_CONNECT.close();

		session.setAttribute("months", months);

		response.sendRedirect("showstatistics.jsp");
	}

	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		String month = request.getParameter("month");

		EntityManager dbConnection = DB_CONNECT.connect();
		dbConnection.getTransaction().setRollbackOnly();

		readStatisticsForMonth(month, request, dbConnection, account);

		DB_CONNECT.rollback();
		DB_CONNECT.close();

		response.sendRedirect("showstatistics.jsp");
	}
}
