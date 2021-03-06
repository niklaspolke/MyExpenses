package vu.de.npolke.myexpenses.servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.services.DAOFactory;
import vu.de.npolke.myexpenses.services.StatisticsDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;
import vu.de.npolke.myexpenses.servlets.util.StatisticsToCsvConverter;
import vu.de.npolke.myexpenses.util.Month;
import vu.de.npolke.myexpenses.util.Statistics;
import vu.de.npolke.myexpenses.util.StatisticsElement;
import vu.de.npolke.myexpenses.util.StatisticsOfMonth;
import vu.de.npolke.myexpenses.util.Timer;

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
@WebServlet("/exportstatistics.jsp")
public class ExportStatisticsServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	StatisticsDAO statisticsDAO = (StatisticsDAO) DAOFactory.getDAO(StatisticsElement.class);

	Timer timer = new Timer();

	protected void streamFileToResponse(final File fileToStream, final HttpServletResponse response,
			final String filename) {
		response.setContentType("application/octet-stream; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentLength((int) fileToStream.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".csv\"");

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileToStream), "UTF-8"));
				PrintWriter writer = response.getWriter()) {
			String line;
			while ((line = br.readLine()) != null) {
				writer.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected Month extractMonth(String monthAsString) {
		if (monthAsString.length() <= 4) {
			monthAsString = monthAsString + ".01";
		}
		Month month = Month.create(monthAsString);
		if (month == null) {
			month = Month.createMonthFromTimeMillis(timer.getCurrentTimeMillis());
		}
		return month;
	}

	protected ServletReaction readStatisticsForMonth(final HttpServletResponse response, final Account account,
			final String monthAsString, final String locale) {
		Month month = extractMonth(monthAsString);

		StatisticsOfMonth container = statisticsDAO.readStatisticsByMonthAndAccountId(month, account.getId(), 20);

		File tempCsvFile = new StatisticsToCsvConverter(container).convertToCsv(new Locale(locale));
		streamFileToResponse(tempCsvFile, response, container.getMonth().toString());
		tempCsvFile.delete();

		return null;
	}

	protected ServletReaction readStatisticsForYear(final HttpServletResponse response, final Account account,
			final String yearAsString, final String locale) {
		Month year = extractMonth(yearAsString);

		Statistics container = statisticsDAO.readStatisticsByYearAndAccountId(year, account.getId(), 20);

		File tempCsvFile = new StatisticsToCsvConverter(container).convertToCsv(new Locale(locale));
		streamFileToResponse(tempCsvFile, response, "" + year.getYear());
		tempCsvFile.delete();

		return null;
	}

	public ServletReaction readStatistics(final HttpServletResponse response, final Account account,
			final String monthAsString, final String yearAsString, final String locale) {
		if (yearAsString != null && yearAsString.length() > 0) {
			return readStatisticsForYear(response, account, yearAsString, locale);
		} else {
			return readStatisticsForMonth(response, account, monthAsString, locale);
		}
	}

	@Override
	public ServletReaction doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		final String month = request.getParameter("month");
		final String year = request.getParameter("year");
		final String locale = (String) session.getAttribute(LoginServlet.COOKIE_LOCALE);

		return readStatistics(response, account, month, year, locale);
	}
}
