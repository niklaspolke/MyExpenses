package vu.de.npolke.myexpenses.util;

import java.util.ArrayList;
import java.util.List;

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
public class StatisticsOfMonth {

	private final Month month;

	private final List<StatisticsElement> income;
	private final List<StatisticsElement> monthlyExpenses;
	private final List<StatisticsElement> expenses;

	private final List<Expense> topExpenses;

	private final double sumIncome;
	private final double sumMonthlyExpenses;
	private final double sumExpenses;

	private static double calcSum(final List<StatisticsElement> elements) {
		double sum = 0.0;
		for (StatisticsElement element : elements)
			sum += element.getAmount();
		return sum;
	}

	public StatisticsOfMonth(final Month month, final List<StatisticsElement> income,
			final List<StatisticsElement> monthlyExpenses, final List<StatisticsElement> expenses,
			final List<Expense> topExpenses) {
		this.month = month;
		this.income = income;
		this.sumIncome = calcSum(income);
		this.monthlyExpenses = monthlyExpenses;
		this.sumMonthlyExpenses = calcSum(monthlyExpenses);
		this.expenses = expenses;
		this.sumExpenses = calcSum(expenses);

		this.topExpenses = topExpenses;
	}

	public Month getMonth() {
		return month;
	}

	private ArrayList<StatisticsElement> cloneSorted(final List<StatisticsElement> original) {
		return new ArrayList<StatisticsElement>(original);
	}

	public List<StatisticsElement> getIncome() {
		return cloneSorted(income);
	}

	public List<StatisticsElement> getMonthlyExpenses() {
		return cloneSorted(monthlyExpenses);
	}

	public List<StatisticsElement> getExpenses() {
		return cloneSorted(expenses);
	}

	public double getSumIncome() {
		return sumIncome;
	}

	public double getSumMonthlyExpenses() {
		return sumMonthlyExpenses;
	}

	public double getSumExpenses() {
		return sumExpenses;
	}

	public List<Expense> getTopExpenses() {
		ArrayList<Expense> copyTopExpenses = new ArrayList<Expense>();
		if (topExpenses != null) {
			copyTopExpenses.addAll(topExpenses);
		}
		return copyTopExpenses;
	}
}
