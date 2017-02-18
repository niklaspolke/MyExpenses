package vu.de.npolke.myexpenses.servlets.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

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
public class StatisticsToCsvConverterTest {

	private static final StatisticsPair PAIR_MONTHLY_INCOME_1 = createIncome("1income", true);
	private static final StatisticsPair PAIR_MONTHLY_INCOME_2 = createIncome("2income", true);
	private static final StatisticsPair PAIR_INCOME_1 = createIncome("3income", false);
	private static final StatisticsPair PAIR_INCOME_2 = createIncome("4income", false);
	private static final StatisticsPair PAIR_MONTHLY_EXPENSE_1 = createExpense("1monthlyexpense", true);
	private static final StatisticsPair PAIR_MONTHLY_EXPENSE_2 = createExpense("2monthlyexpense", true);
	private static final StatisticsPair PAIR_EXPENSE_1 = createExpense("1expense", false);
	private static final StatisticsPair PAIR_EXPENSE_2 = createExpense("2expense", false);

	private static StatisticsPair createIncome(final String category, final boolean isMonthly) {
		return new StatisticsPair(1L, category, 2.3, isMonthly, true);
	}

	private static StatisticsPair createExpense(final String category, final boolean isMonthly) {
		return new StatisticsPair(1L, category, 2.3, isMonthly, false);
	}

	private StatisticsToCsvConverter converter;

	@Before
	public void setup() {
		StatisticsPairContainerOfMonth container = new StatisticsPairContainerOfMonth("test");
		container.add(PAIR_MONTHLY_INCOME_2);
		container.add(PAIR_EXPENSE_1);
		container.add(PAIR_MONTHLY_EXPENSE_2);
		container.add(PAIR_INCOME_1);
		container.add(PAIR_MONTHLY_INCOME_1);
		container.add(PAIR_MONTHLY_EXPENSE_1);
		container.add(PAIR_EXPENSE_2);
		container.add(PAIR_INCOME_2);

		converter = new StatisticsToCsvConverter(container);
	}

	@Test
	public void exportToCsv() {
		File tempFile = converter.convertToCsv();

		assertNotNull(tempFile);
		try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
			assertEquals("Income", reader.readLine());
			assertEquals("1income;2,30 €", reader.readLine());
			assertEquals("2income;2,30 €", reader.readLine());
			assertEquals("3income;2,30 €", reader.readLine());
			assertEquals("4income;2,30 €", reader.readLine());
			assertEquals("Monthly Expenses", reader.readLine());
			assertEquals("1monthlyexpense;2,30 €", reader.readLine());
			assertEquals("2monthlyexpense;2,30 €", reader.readLine());
			assertEquals("Expenses", reader.readLine());
			assertEquals("1expense;2,30 €", reader.readLine());
			assertEquals("2expense;2,30 €", reader.readLine());
		} catch (IOException e) {
			fail();
		} finally {
			tempFile.delete();
		}
	}
}
