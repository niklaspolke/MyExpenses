package vu.de.npolke.myexpenses.servlets.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.util.Month;
import vu.de.npolke.myexpenses.util.Statistics;
import vu.de.npolke.myexpenses.util.StatisticsElement;

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
public class StatisticsToCsvConverterTest {

	private static final Month MONTH = Month.create(2015, 5);
	private static final String CATEGORY_MONTHLYINCOME = "income";
	private static final String CATEGORY_MONTHLYEXPENSE = "monthlyexpense";
	private static final String CATEGORY_EXPENSE = "1expense";

	private static final String CATEGORY_SPECIALCHARS_COLON = "4expe,nse";
	private static final String CATEGORY_SPECIALCHARS_QUOTATIONMARK = "5expe\"nse";
	private static final String CATEGORY_SPECIALCHARS_QUOTATIONMARK_AND_COLON = "6e\"xp\"e,nse";

	private static StatisticsElement createIncome(final Month month, final long categoryid, final String category) {
		return StatisticsElement.create(month, categoryid, category, 2.3, true, true);
	}

	private static StatisticsElement createExpense(final Month month, final long categoryid, final String category,
			final boolean isMonthly) {
		return StatisticsElement.create(month, categoryid, category, 2.3, isMonthly, false);
	}

	private Statistics stats = new Statistics();

	private StatisticsToCsvConverter converter;

	@Before
	public void setup() {
		stats.add(createIncome(MONTH, 1, CATEGORY_MONTHLYINCOME));
		stats.add(createExpense(MONTH, 2, CATEGORY_MONTHLYEXPENSE, true));
		stats.add(createExpense(MONTH, 3, CATEGORY_EXPENSE, false));

		converter = new StatisticsToCsvConverter(stats.filter(MONTH));
	}

	@Test
	public void exportToCsv_DE() {
		File tempFile = converter.convertToCsv(Locale.GERMAN);

		assertNotNull(tempFile);
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(tempFile), "UTF-8"));) {
			assertEquals("Einnahmen", reader.readLine());
			assertEquals(CATEGORY_MONTHLYINCOME + ",\"2,30\"", reader.readLine());
			assertEquals("Fixkosten", reader.readLine());
			assertEquals(CATEGORY_MONTHLYEXPENSE + ",\"2,30\"", reader.readLine());
			assertEquals("Ausgaben", reader.readLine());
			assertEquals(CATEGORY_EXPENSE + ",\"2,30\"", reader.readLine());
		} catch (IOException e) {
			fail();
		} finally {
			tempFile.delete();
		}
	}

	@Test
	public void exportToCsv_EN() {
		File tempFile = converter.convertToCsv(Locale.ENGLISH);

		assertNotNull(tempFile);
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(tempFile), "UTF-8"));) {
			assertEquals("Income", reader.readLine());
			assertEquals(CATEGORY_MONTHLYINCOME + ",\"2,30\"", reader.readLine());
			assertEquals("Monthly Costs", reader.readLine());
			assertEquals(CATEGORY_MONTHLYEXPENSE + ",\"2,30\"", reader.readLine());
			assertEquals("Expenses", reader.readLine());
			assertEquals(CATEGORY_EXPENSE + ",\"2,30\"", reader.readLine());
		} catch (IOException e) {
			fail();
		} finally {
			tempFile.delete();
		}
	}

	private static Expense createExpense(final String categoryName, final String reason, final double amount) {
		Expense exp = new Expense();
		exp.setCategoryName(categoryName);
		exp.setReason(reason);
		exp.setAmount(amount);
		return exp;
	}

	@Test
	public void exportToCsv_DE_WithTopExpenses() {
		ArrayList<Expense> topExpenses = new ArrayList<Expense>();
		topExpenses.add(createExpense("sports", "squash", 40));
		topExpenses.add(createExpense("food", "supermarket", 20.5));
		stats.addTopExpenses(MONTH, topExpenses);
		converter = new StatisticsToCsvConverter(stats.filter(MONTH));

		File tempFile = converter.convertToCsv(Locale.GERMAN);

		assertNotNull(tempFile);
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(tempFile), "UTF-8"));) {
			assertEquals("Einnahmen", reader.readLine());
			assertEquals(CATEGORY_MONTHLYINCOME + ",\"2,30\"", reader.readLine());
			assertEquals("Fixkosten", reader.readLine());
			assertEquals(CATEGORY_MONTHLYEXPENSE + ",\"2,30\"", reader.readLine());
			assertEquals("Ausgaben", reader.readLine());
			assertEquals(CATEGORY_EXPENSE + ",\"2,30\"", reader.readLine());
			assertEquals("", reader.readLine());
			assertEquals("Top20 Ausgaben", reader.readLine());
			assertEquals("sports - squash,\"40,00\"", reader.readLine());
			assertEquals("food - supermarket,\"20,50\"", reader.readLine());
		} catch (IOException e) {
			fail();
		} finally {
			tempFile.delete();
		}
	}

	@Test
	public void exportYearToCsv_DE_WithTopExpenses() {
		ArrayList<Expense> topExpenses = new ArrayList<Expense>();
		topExpenses.add(createExpense("sports", "squash", 40));
		topExpenses.add(createExpense("food", "supermarket", 20.5));
		stats.addTopExpenses(MONTH, topExpenses);

		stats.add(createIncome(MONTH.next(), 1, CATEGORY_MONTHLYINCOME));
		stats.add(createExpense(MONTH.next(), 2, CATEGORY_MONTHLYEXPENSE, true));
		stats.add(createExpense(MONTH.next(), 4, CATEGORY_EXPENSE, false));
		topExpenses = new ArrayList<Expense>(topExpenses);
		topExpenses.add(createExpense("2food", "2supermarket", 33.5));
		stats.addTopExpenses(MONTH.next(), topExpenses);
		converter = new StatisticsToCsvConverter(stats);

		File tempFile = converter.convertToCsv(Locale.GERMAN);

		assertNotNull(tempFile);
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(tempFile), "UTF-8"));) {
			assertEquals(",2015.05,,2015.06", reader.readLine());
			assertEquals("Einnahmen", reader.readLine());
			assertEquals(CATEGORY_MONTHLYINCOME + ",\"2,30\",,\"2,30\",", reader.readLine());
			assertEquals("Fixkosten", reader.readLine());
			assertEquals(CATEGORY_MONTHLYEXPENSE + ",\"2,30\",,\"2,30\",", reader.readLine());
			assertEquals("Ausgaben", reader.readLine());
			assertEquals(CATEGORY_EXPENSE + ",\"2,30\",,\"2,30\",", reader.readLine());
			assertEquals("", reader.readLine());
			assertEquals("Top20 Ausgaben", reader.readLine());
			assertEquals("sports - squash,\"40,00\",sports - squash,\"40,00\"", reader.readLine());
			assertEquals("food - supermarket,\"20,50\",food - supermarket,\"20,50\"", reader.readLine());
			assertEquals(",,2food - 2supermarket,\"33,50\"", reader.readLine());
		} catch (IOException e) {
			fail();
		} finally {
			tempFile.delete();
		}
	}

	@Test
	public void exportToCsv_DE_WithTopExpenses_SpecialCharacter() {
		stats.add(createExpense(MONTH, 4, CATEGORY_SPECIALCHARS_COLON, false));
		stats.add(createExpense(MONTH, 5, CATEGORY_SPECIALCHARS_QUOTATIONMARK, false));
		stats.add(createExpense(MONTH, 6, CATEGORY_SPECIALCHARS_QUOTATIONMARK_AND_COLON, false));
		converter = new StatisticsToCsvConverter(stats.filter(MONTH));

		File tempFile = converter.convertToCsv(Locale.GERMAN);

		assertNotNull(tempFile);
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(tempFile), "UTF-8"));) {
			assertEquals("Einnahmen", reader.readLine());
			assertEquals(CATEGORY_MONTHLYINCOME + ",\"2,30\"", reader.readLine());
			assertEquals("Fixkosten", reader.readLine());
			assertEquals(CATEGORY_MONTHLYEXPENSE + ",\"2,30\"", reader.readLine());
			assertEquals("Ausgaben", reader.readLine());
			assertEquals(CATEGORY_EXPENSE + ",\"2,30\"", reader.readLine());
			assertEquals("\"4expe,nse\",\"2,30\"", reader.readLine());
			assertEquals("\"5expe\"\"nse\",\"2,30\"", reader.readLine());
			assertEquals("\"6e\"\"xp\"\"e,nse\",\"2,30\"", reader.readLine());
		} catch (IOException e) {
			fail();
		} finally {
			tempFile.delete();
		}
	}
}
