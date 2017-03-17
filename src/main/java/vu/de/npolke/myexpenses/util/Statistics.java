package vu.de.npolke.myexpenses.util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import vu.de.npolke.myexpenses.model.Expense;

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
public class Statistics {

	/**
	 * Map of months and their top expenses.
	 */
	private TreeMap<Month, List<Expense>> topExpenses = new TreeMap<Month, List<Expense>>();
	//@formatter:off
	private StatisticsGroup income          = new StatisticsGroup();
	private StatisticsGroup monthlyExpenses = new StatisticsGroup();
	private StatisticsGroup expenses        = new StatisticsGroup();
	//@formatter:on

	/**
	 * Add StatisticElements to the Statistics. If different elements of the same month/category/income,monthlyExp,exp
	 * exist, the values will be added.
	 *
	 * @param month
	 *            month of the given newPairs
	 * @param elements
	 *            input for the statistics
	 */
	public void add(final List<StatisticsElement> elements) {
		for (StatisticsElement element : elements) {
			add(element);
		}
	}

	public void add(StatisticsElement element) {
		if (!getMonths().contains(element.getMonth())) {
			topExpenses.put(element.getMonth(), null);
		}
		if (element.isIncome()) {
			income.add(element);
		} else if (element.isMonthly()) {
			monthlyExpenses.add(element);
		} else {
			expenses.add(element);
		}
	}

	/**
	 * Extracts the statistics of the given month
	 *
	 * @param month
	 *            Month (yyyy.mm) for that the values should be extracted
	 * @param withEmptyCategories
	 *            if true, all categories from all months are in the result even if no input for this month and this
	 *            category exists (in this case with the value 0.0).
	 * @return Statistics for the given month
	 */
	public StatisticsOfMonth filter(final Month month, final boolean withEmptyCategories) {
		//@formatter:off
		List<StatisticsElement>          incomeOfMonth =          income.filter(month, withEmptyCategories);
		List<StatisticsElement> monthlyExpensesOfMonth = monthlyExpenses.filter(month, withEmptyCategories);
		List<StatisticsElement>        expensesOfMonth =        expenses.filter(month, withEmptyCategories);
		//@formatter:on
		return new StatisticsOfMonth(month, incomeOfMonth, monthlyExpensesOfMonth, expensesOfMonth,
				topExpenses.get(month));
	}

	public StatisticsOfMonth filter(final Month month) {
		return filter(month, false);
	}

	public void addTopExpenses(final Month month, final List<Expense> topExpenses) {
		this.topExpenses.put(month, topExpenses);
	}

	public List<Month> getMonths() {
		return new ArrayList<Month>(topExpenses.keySet());
	}
}
