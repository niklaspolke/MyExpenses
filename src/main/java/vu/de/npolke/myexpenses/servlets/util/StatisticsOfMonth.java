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
public class StatisticsOfMonth {

	private final String nameOfMonth;

	private final List<StatisticsPair> income = new ArrayList<StatisticsPair>();
	private double sumIncome = 0.0;
	private final List<StatisticsPair> monthlyExpenses = new ArrayList<StatisticsPair>();
	private double sumMonthlyExpenses = 0.0;
	private final List<StatisticsPair> expenses = new ArrayList<StatisticsPair>();
	private double sumExpenses = 0.0;
	private boolean sorted = true;

	public StatisticsOfMonth(final String nameOfMonth) {
		this.nameOfMonth = nameOfMonth;
	}

	public String getNameOfMonth() {
		return nameOfMonth;
	}

	public void add(final StatisticsPair pair) {
		if (pair != null) {
			if (pair.isIncome()) {
				income.add(pair);
				sumIncome += pair.getValue();
			} else if (pair.isMonthly()) {
				monthlyExpenses.add(pair);
				sumMonthlyExpenses += pair.getValue();
			} else {
				expenses.add(pair);
				sumExpenses += pair.getValue();
			}
			sorted = false;
		}
	}

	private void sort() {
		if (sorted == false) {
			Collections.sort(income);
			Collections.sort(monthlyExpenses);
			Collections.sort(expenses);
			sorted = true;
		}
	}

	public List<StatisticsPair> getIncome() {
		sort();
		return income;
	}

	public List<StatisticsPair> getMonthlyExpenses() {
		sort();
		return monthlyExpenses;
	}

	public List<StatisticsPair> getExpenses() {
		sort();
		return expenses;
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
}
