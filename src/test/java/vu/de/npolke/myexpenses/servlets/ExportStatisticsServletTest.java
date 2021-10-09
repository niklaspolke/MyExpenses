package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import vu.de.npolke.myexpenses.util.Month;
import vu.de.npolke.myexpenses.util.Statistics;
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
public class ExportStatisticsServletTest {

	private static final long ACCOUNT_ID = 112;

	private static final String MONTHLY_INCOME = "monthlyincome";
	private static final String INCOME = "income";
	private static final String MONTHLY_EXPENSE = "monthlyexpense";
	private static final String EXPENSE = "expense";
	private static final Month MONTH = Month.create(2015, 5);
	private static final StatisticsElement PAIR_MONTHLY_INCOME = createIncome(MONTH, 1, MONTHLY_INCOME, true);
	private static final StatisticsElement PAIR_INCOME = createIncome(MONTH, 2, INCOME, false);
	private static final StatisticsElement PAIR_MONTHLY_EXPENSE = createExpense(MONTH, 3, MONTHLY_EXPENSE, true);
	private static final StatisticsElement PAIR_EXPENSE = createExpense(MONTH, 4, EXPENSE, false);

	private static StatisticsElement createIncome(final Month month, final long categoryid, final String category,
			final boolean isMonthly) {
		return StatisticsElement.create(month, categoryid, category, 2.3, isMonthly, true);
	}

	private static StatisticsElement createExpense(final Month month, final long categoryid, final String category,
			final boolean isMonthly) {
		return StatisticsElement.create(month, categoryid, category, 2.3, isMonthly, false);
	}

	private static Account account = new Account();

	private static StatisticsOfMonth statisticsOfMonth;
	private static Statistics statisticsOfYear;
	private static List<Expense> topExpenses;

	@BeforeClass
	public static void setupClass() {
		account.setId(ACCOUNT_ID);
		List<StatisticsElement> income = new ArrayList<StatisticsElement>();
		List<StatisticsElement> monthlyExpenses = new ArrayList<StatisticsElement>();
		List<StatisticsElement> expenses = new ArrayList<StatisticsElement>();
		monthlyExpenses.add(PAIR_MONTHLY_EXPENSE);
		income.add(PAIR_MONTHLY_INCOME);
		income.add(PAIR_INCOME);
		expenses.add(PAIR_EXPENSE);
		topExpenses = new ArrayList<Expense>();
		topExpenses.add(createExpense("sports", "squash", 40));
		topExpenses.add(createExpense("food", "supermarket", 20.5));
		statisticsOfMonth = new StatisticsOfMonth(MONTH, income, monthlyExpenses, expenses, topExpenses);

		statisticsOfYear = new Statistics();
		statisticsOfYear.addTopExpenses(MONTH, new ArrayList<Expense>(topExpenses));
		statisticsOfYear.addTopExpenses(MONTH.next(), new ArrayList<Expense>(topExpenses));
		statisticsOfYear.add(PAIR_MONTHLY_EXPENSE);
		statisticsOfYear.add(PAIR_MONTHLY_INCOME);
		statisticsOfYear.add(PAIR_INCOME);
		statisticsOfYear.add(PAIR_EXPENSE);
		statisticsOfYear.add(createIncome(MONTH.next(), 1, MONTHLY_INCOME, true));
		statisticsOfYear.add(createIncome(MONTH.next(), 2, INCOME, false));
		statisticsOfYear.add(createExpense(MONTH.next(), 3, MONTHLY_EXPENSE, true));
		statisticsOfYear.add(createExpense(MONTH.next(), 4, EXPENSE, false));
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
	public void readStatisticsForMonth() throws IOException {
		PrintWriter writer = mock(PrintWriter.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(response.getWriter()).thenReturn(writer);

		servlet.statisticsDAO = mock(StatisticsDAO.class);
		when(servlet.statisticsDAO.readStatisticsByMonthAndAccountId(eq(MONTH), eq(ACCOUNT_ID), any(Integer.class)))
				.thenReturn(statisticsOfMonth);

		ServletReaction reaction = servlet.readStatistics(response, account, MONTH.toString(), null, "de");

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
		inOrder.verify(writer).println("Top20 Ausgaben");
		inOrder.verify(writer).println("sports - squash,\"40,00\"");
		inOrder.verify(writer).println("food - supermarket,\"20,50\"");
	}

	@Test
	public void readStatisticsForYear() throws IOException {
		PrintWriter writer = mock(PrintWriter.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(response.getWriter()).thenReturn(writer);

		servlet.statisticsDAO = mock(StatisticsDAO.class);
		when(servlet.statisticsDAO.readStatisticsByYearAndAccountId(eq(Month.create(MONTH.getYear(), 1)),
				eq(ACCOUNT_ID), any(Integer.class))).thenReturn(statisticsOfYear);

		ServletReaction reaction = servlet.readStatistics(response, account, null, "" + MONTH.getYear(), "de");

		assertNull(reaction);
		InOrder inOrder = inOrder(writer);
		inOrder.verify(writer).println(",2015.05,,2015.06");
		inOrder.verify(writer).println("Einnahmen");
		inOrder.verify(writer).println(INCOME + "," + "\"2,30\"," + "," + "\"2,30\",");
		inOrder.verify(writer).println(MONTHLY_INCOME + "," + "\"2,30\"," + "," + "\"2,30\",");
		inOrder.verify(writer).println("Fixkosten");
		inOrder.verify(writer).println(MONTHLY_EXPENSE + "," + "\"2,30\"," + "," + "\"2,30\",");
		inOrder.verify(writer).println("Ausgaben");
		inOrder.verify(writer).println(EXPENSE + "," + "\"2,30\"," + "," + "\"2,30\",");
		inOrder.verify(writer).println("");
		inOrder.verify(writer).println("Top20 Ausgaben");
		inOrder.verify(writer).println("sports - squash,\"40,00\"," + "sports - squash,\"40,00\"");
		inOrder.verify(writer).println("food - supermarket,\"20,50\"," + "food - supermarket,\"20,50\"");
	}
}
