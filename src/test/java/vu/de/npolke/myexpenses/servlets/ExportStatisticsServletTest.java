package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InOrder;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.StatisticsDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;
import vu.de.npolke.myexpenses.servlets.util.StatisticsPair;
import vu.de.npolke.myexpenses.util.Month;
import vu.de.npolke.myexpenses.util.StatisticsOfMonth;
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
public class ExportStatisticsServletTest {

	private static final long ACCOUNT_ID = 112;

	private static final String MONTHLY_INCOME = "monthlyincome";
	private static final String INCOME = "income";
	private static final String MONTHLY_EXPENSE = "monthlyexpense";
	private static final String EXPENSE = "expense";
	private static final StatisticsPair PAIR_MONTHLY_INCOME = createIncome(MONTHLY_INCOME, true);
	private static final StatisticsPair PAIR_INCOME = createIncome(INCOME, false);
	private static final StatisticsPair PAIR_MONTHLY_EXPENSE = createExpense(MONTHLY_EXPENSE, true);
	private static final StatisticsPair PAIR_EXPENSE = createExpense(EXPENSE, false);

	private static StatisticsPair createIncome(final String category, final boolean isMonthly) {
		return new StatisticsPair(1L, category, 2.3, isMonthly, true);
	}

	private static StatisticsPair createExpense(final String category, final boolean isMonthly) {
		return new StatisticsPair(1L, category, 2.3, isMonthly, false);
	}

	private static Account account = new Account();

	private static final String MONTH = "2015.05";
	private static StatisticsOfMonth statistics;
	private static List<Expense> topExpenses;

	@BeforeClass
	public static void setupClass() {
		account.setId(ACCOUNT_ID);
		statistics = new StatisticsOfMonth(MONTH);
		statistics.add(PAIR_MONTHLY_EXPENSE);
		statistics.add(PAIR_INCOME);
		statistics.add(PAIR_EXPENSE);
		statistics.add(PAIR_MONTHLY_INCOME);
		topExpenses = new ArrayList<Expense>();
		topExpenses.add(createExpense("sports", "squash", 40));
		topExpenses.add(createExpense("food", "supermarket", 20.5));
	}

	private static Expense createExpense(final String categoryName, final String reason, final double amount) {
		Expense exp = new Expense();
		exp.setCategoryName(categoryName);
		exp.setReason(reason);
		exp.setAmount(amount);
		return exp;
	}

	private ExportStatisticsServlet servlet = new ExportStatisticsServlet();

	private File createTempFile(final String prefix, final String suffix, final String... lines) {
		File tempFile = null;
		try {
			tempFile = File.createTempFile(prefix, suffix);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"));
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
		}
		return tempFile;
	}

	@Test
	public void streamFileToResponse_Header() throws IOException {
		final String MONTH = "MONTH_NAME";
		final String FIRST_LINE = "first line";
		final String SECOND_LINE = "second line";
		PrintWriter writer = mock(PrintWriter.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(response.getWriter()).thenReturn(writer);
		File tempFile = createTempFile("testcsvexport", ".csv", FIRST_LINE, SECOND_LINE);

		servlet.streamFileToResponse(tempFile, response, MONTH);

		verify(response).setContentType("application/octet-stream; charset=UTF-8");
		verify(response).setCharacterEncoding("UTF-8");
		verify(response).setContentLength((int) tempFile.length());
		verify(response).setHeader("Content-Disposition", "attachment; filename=\"" + MONTH + ".csv\"");

		InOrder inOrder = inOrder(writer);
		inOrder.verify(writer).println(FIRST_LINE);
		inOrder.verify(writer).println(SECOND_LINE);
	}

	@Test
	public void extractRegularMonth() {
		Month month = servlet.extractMonth("2015-05");

		assertEquals(2015, month.getYear());
		assertEquals(5, month.getMonth());
	}

	@Test
	public void extractIrregularMonth() {
		servlet.timer = new TimerMock("20.06.2016");
		Month month = servlet.extractMonth("201505");

		assertEquals(2016, month.getYear());
		assertEquals(6, month.getMonth());
	}

	@Test
	public void readStatistics() throws IOException {
		PrintWriter writer = mock(PrintWriter.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(response.getWriter()).thenReturn(writer);

		servlet.statisticsDAO = mock(StatisticsDAO.class);
		when(servlet.statisticsDAO.readStatisticsByMonthAndAccountId(eq(Month.createMonth(MONTH)), eq(ACCOUNT_ID)))
				.thenReturn(statistics);
		when(servlet.statisticsDAO.readTopXofExpensesByMonth(eq(ACCOUNT_ID), eq(MONTH), eq(15)))
				.thenReturn(topExpenses);

		ServletReaction reaction = servlet.readStatisticsForMonth(response, account, MONTH, "de");

		assertNull(reaction);
		InOrder inOrder = inOrder(writer);
		inOrder.verify(writer).println("Einnahmen");
		inOrder.verify(writer).println(MONTHLY_INCOME + "," + "\"2,30\"");
		inOrder.verify(writer).println(INCOME + "," + "\"2,30\"");
		inOrder.verify(writer).println("Fixkosten");
		inOrder.verify(writer).println(MONTHLY_EXPENSE + "," + "\"2,30\"");
		inOrder.verify(writer).println("Ausgaben");
		inOrder.verify(writer).println(EXPENSE + "," + "\"2,30\"");
		inOrder.verify(writer).println("");
		inOrder.verify(writer).println("Top15 Ausgaben");
		inOrder.verify(writer).println("sports - squash,\"40,00\"");
		inOrder.verify(writer).println("food - supermarket,\"20,50\"");
	}
}
