package vu.de.npolke.myexpenses.servlets.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

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
public class StatisticsPairContainerOfMonthTest {

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

	private static final String NAME_OF_MONTH = "2017-02";

	private List<StatisticsPair> statistics = new ArrayList<StatisticsPair>();
	private StatisticsPairContainerOfMonth container;

	@Test
	public void nameOfMonth() {
		container = new StatisticsPairContainerOfMonth(NAME_OF_MONTH, statistics);

		assertEquals(NAME_OF_MONTH, container.getNameOfMonth());
	}

	@Test
	public void addMonthlyIncome() {
		container = new StatisticsPairContainerOfMonth(NAME_OF_MONTH, statistics);
		container.add(PAIR_MONTHLY_INCOME_2);
		container.add(PAIR_MONTHLY_INCOME_1);

		assertNotNull(container.getIncome());
		assertEquals(2, container.getIncome().size());
		assertSame(PAIR_MONTHLY_INCOME_1, container.getIncome().get(0));
		assertSame(PAIR_MONTHLY_INCOME_2, container.getIncome().get(1));
	}

	@Test
	public void addIncome() {
		container = new StatisticsPairContainerOfMonth(NAME_OF_MONTH, statistics);
		container.add(PAIR_INCOME_2);
		container.add(PAIR_INCOME_1);

		assertNotNull(container.getIncome());
		assertEquals(2, container.getIncome().size());
		assertSame(PAIR_INCOME_1, container.getIncome().get(0));
		assertSame(PAIR_INCOME_2, container.getIncome().get(1));
	}

	@Test
	public void addMonthlyExpenses() {
		container = new StatisticsPairContainerOfMonth(NAME_OF_MONTH, statistics);
		container.add(PAIR_MONTHLY_EXPENSE_2);
		container.add(PAIR_MONTHLY_EXPENSE_1);

		assertNotNull(container.getMonthlyExpenses());
		assertEquals(2, container.getMonthlyExpenses().size());
		assertSame(PAIR_MONTHLY_EXPENSE_1, container.getMonthlyExpenses().get(0));
		assertSame(PAIR_MONTHLY_EXPENSE_2, container.getMonthlyExpenses().get(1));
	}

	@Test
	public void addExpenses() {
		container = new StatisticsPairContainerOfMonth(NAME_OF_MONTH, statistics);
		container.add(PAIR_EXPENSE_2);
		container.add(PAIR_EXPENSE_1);

		assertNotNull(container.getExpenses());
		assertEquals(2, container.getExpenses().size());
		assertSame(PAIR_EXPENSE_1, container.getExpenses().get(0));
		assertSame(PAIR_EXPENSE_2, container.getExpenses().get(1));
	}

	@Test
	public void addAll() {
		container = new StatisticsPairContainerOfMonth(NAME_OF_MONTH, statistics);
		container.add(PAIR_MONTHLY_INCOME_2);
		container.add(PAIR_EXPENSE_1);
		container.add(PAIR_MONTHLY_EXPENSE_2);
		container.add(PAIR_INCOME_1);
		container.add(PAIR_MONTHLY_INCOME_1);
		container.add(PAIR_MONTHLY_EXPENSE_1);
		container.add(PAIR_EXPENSE_2);
		container.add(PAIR_INCOME_2);

		assertNotNull(container.getIncome());
		assertEquals(4, container.getIncome().size());
		assertNotNull(container.getMonthlyExpenses());
		assertEquals(2, container.getMonthlyExpenses().size());
		assertNotNull(container.getExpenses());
		assertEquals(2, container.getExpenses().size());
		assertSame(PAIR_MONTHLY_INCOME_1, container.getIncome().get(0));
		assertSame(PAIR_MONTHLY_INCOME_2, container.getIncome().get(1));
		assertSame(PAIR_INCOME_1, container.getIncome().get(2));
		assertSame(PAIR_INCOME_2, container.getIncome().get(3));
		assertSame(PAIR_MONTHLY_EXPENSE_1, container.getMonthlyExpenses().get(0));
		assertSame(PAIR_MONTHLY_EXPENSE_2, container.getMonthlyExpenses().get(1));
		assertSame(PAIR_EXPENSE_1, container.getExpenses().get(0));
		assertSame(PAIR_EXPENSE_2, container.getExpenses().get(1));
	}

	@Test
	public void constructorAll() {
		statistics.add(PAIR_MONTHLY_INCOME_2);
		statistics.add(PAIR_EXPENSE_1);
		statistics.add(PAIR_MONTHLY_EXPENSE_2);
		statistics.add(PAIR_INCOME_1);
		statistics.add(PAIR_MONTHLY_INCOME_1);
		statistics.add(PAIR_MONTHLY_EXPENSE_1);
		statistics.add(PAIR_EXPENSE_2);
		statistics.add(PAIR_INCOME_2);
		container = new StatisticsPairContainerOfMonth(NAME_OF_MONTH, statistics);

		assertNotNull(container.getIncome());
		assertEquals(4, container.getIncome().size());
		assertNotNull(container.getMonthlyExpenses());
		assertEquals(2, container.getMonthlyExpenses().size());
		assertNotNull(container.getExpenses());
		assertEquals(2, container.getExpenses().size());
		assertSame(PAIR_MONTHLY_INCOME_1, container.getIncome().get(0));
		assertSame(PAIR_MONTHLY_INCOME_2, container.getIncome().get(1));
		assertSame(PAIR_INCOME_1, container.getIncome().get(2));
		assertSame(PAIR_INCOME_2, container.getIncome().get(3));
		assertSame(PAIR_MONTHLY_EXPENSE_1, container.getMonthlyExpenses().get(0));
		assertSame(PAIR_MONTHLY_EXPENSE_2, container.getMonthlyExpenses().get(1));
		assertSame(PAIR_EXPENSE_1, container.getExpenses().get(0));
		assertSame(PAIR_EXPENSE_2, container.getExpenses().get(1));
	}

	@Test
	public void empty() {
		container = new StatisticsPairContainerOfMonth(NAME_OF_MONTH, statistics);

		assertNotNull(container.getIncome());
		assertNotNull(container.getMonthlyExpenses());
		assertNotNull(container.getExpenses());
	}
}
