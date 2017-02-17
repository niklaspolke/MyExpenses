package vu.de.npolke.myexpenses.servlets.util;

import static org.junit.Assert.*;

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
public class StatisticsPairContainerTest {

	private static final StatisticsPair PAIR_MONTHLY_INCOME_1 = createIncome("1income", true);
	private static final StatisticsPair PAIR_MONTHLY_INCOME_2 = createIncome("2income", true);
	private static final StatisticsPair PAIR_INCOME_1 = createIncome("1income", false);
	private static final StatisticsPair PAIR_INCOME_2 = createIncome("2income", false);
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

	private StatisticsPairContainer container = new StatisticsPairContainer();

	@Test
	public void addMonthlyIncome() {
		container.add(PAIR_MONTHLY_INCOME_2);
		container.add(PAIR_MONTHLY_INCOME_1);

		assertNotNull(container.getIncome());
		assertEquals(2, container.getIncome().size());
		assertSame(PAIR_MONTHLY_INCOME_1, container.getIncome().get(0));
		assertSame(PAIR_MONTHLY_INCOME_2, container.getIncome().get(1));
	}

	@Test
	public void addIncome() {
		container.add(PAIR_INCOME_2);
		container.add(PAIR_INCOME_1);

		assertNotNull(container.getIncome());
		assertEquals(2, container.getIncome().size());
		assertSame(PAIR_INCOME_1, container.getIncome().get(0));
		assertSame(PAIR_INCOME_2, container.getIncome().get(1));
	}

	@Test
	public void addMonthlyExpenses() {
		container.add(PAIR_MONTHLY_EXPENSE_2);
		container.add(PAIR_MONTHLY_EXPENSE_1);

		assertNotNull(container.getMonthlyExpenses());
		assertEquals(2, container.getMonthlyExpenses().size());
		assertSame(PAIR_MONTHLY_EXPENSE_1, container.getMonthlyExpenses().get(0));
		assertSame(PAIR_MONTHLY_EXPENSE_2, container.getMonthlyExpenses().get(1));
	}

	@Test
	public void addExpenses() {
		container.add(PAIR_EXPENSE_2);
		container.add(PAIR_EXPENSE_1);

		assertNotNull(container.getExpenses());
		assertEquals(2, container.getExpenses().size());
		assertSame(PAIR_EXPENSE_1, container.getExpenses().get(0));
		assertSame(PAIR_EXPENSE_2, container.getExpenses().get(1));
	}

	@Test
	public void addAll() {
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
	public void empty() {
		assertNotNull(container.getIncome());
		assertNotNull(container.getMonthlyExpenses());
		assertNotNull(container.getExpenses());
	}
}
