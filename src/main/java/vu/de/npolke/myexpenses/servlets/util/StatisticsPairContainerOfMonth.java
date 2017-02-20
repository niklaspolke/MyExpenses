package vu.de.npolke.myexpenses.servlets.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
public class StatisticsPairContainerOfMonth {

	private final String nameOfMonth;

	private final List<StatisticsPair> income;
	private final List<StatisticsPair> monthlyExpenses;
	private final List<StatisticsPair> expenses;

	public StatisticsPairContainerOfMonth(final String nameOfMonth, final List<StatisticsPair> statistics) {
		this.nameOfMonth = nameOfMonth;
		income = new ArrayList<StatisticsPair>();
		monthlyExpenses = new ArrayList<StatisticsPair>();
		expenses = new ArrayList<StatisticsPair>();

		for (StatisticsPair pair : statistics) {
			add(pair);
		}
	}

	public String getNameOfMonth() {
		return nameOfMonth;
	}

	public void add(final StatisticsPair pair) {
		if (pair != null) {
			if (pair.isIncome()) {
				income.add(pair);
			} else if (pair.isMonthly()) {
				monthlyExpenses.add(pair);
			} else {
				expenses.add(pair);
			}
		}
	}

	public List<StatisticsPair> getIncome() {
		Collections.sort(income);
		return income;
	}

	public List<StatisticsPair> getMonthlyExpenses() {
		Collections.sort(monthlyExpenses);
		return monthlyExpenses;
	}

	public List<StatisticsPair> getExpenses() {
		Collections.sort(expenses);
		return expenses;
	}
}
