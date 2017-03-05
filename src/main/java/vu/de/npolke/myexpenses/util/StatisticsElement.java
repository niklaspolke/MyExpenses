package vu.de.npolke.myexpenses.util;

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
public class StatisticsElement implements Comparable<StatisticsElement> {

	//@formatter:off
	private Month   month;
	private String  category;
	private Double  amount;
	private boolean isMonthly;
	private boolean isIncome;
	//@formatter:one

	private StatisticsElement(final Month month, final String category, final double amount, final boolean isMonthly,
			final boolean isIncome) {
		//@formatter:off
		this.month     = month;
		this.category  = category;
		this.amount    = amount;
		this.isMonthly = isMonthly;
		this.isIncome  = isIncome;
		//@formatter:on
	}

	public static StatisticsElement create(final Month month, final String category, final double amount,
			final boolean isMonthly, final boolean isIncome) {
		if (month != null && category != null) {
			return new StatisticsElement(month, category, amount, isMonthly, isIncome);
		} else {
			return null;
		}
	}

	public Month getMonth() {
		return month;
	}

	public String getCategory() {
		return category;
	}

	public double getAmount() {
		return amount;
	}

	public boolean isMonthly() {
		return isMonthly;
	}

	public boolean isIncome() {
		return isIncome;
	}

	@Override
	public boolean equals(final Object otherObject) {
		boolean isEqual = false;
		if (otherObject instanceof StatisticsElement && getMonth() != null && getCategory() != null) {
			StatisticsElement other = (StatisticsElement) otherObject;
			isEqual = month.equals(other.month) && category.equals(other.category) && isMonthly == other.isMonthly
					&& isIncome == other.isIncome;
		}
		return isEqual;
	}

	@Override
	public int compareTo(final StatisticsElement other) {
		int compareValue;
		if (other == null) {
			compareValue = -100;
		} else {
			compareValue = month.compareTo(other.month);
			compareValue = compareValue != 0 ? compareValue : compareMonthlyAndIncome(other.isMonthly, other.isIncome);
			compareValue = compareValue != 0 ? compareValue : category.compareTo(other.category);
		}
		return compareValue;
	}

	private int compareMonthlyAndIncome(boolean otherIsMonthly, boolean otherIsIncome) {
		if (isIncome && otherIsIncome == false)
			return -10;
		else if (isIncome == false && otherIsIncome)
			return 10;
		else {
			if (isMonthly && otherIsMonthly == false)
				return -1;
			else if (isMonthly == false && otherIsMonthly)
				return 1;
			else
				return 0;
		}
	}
}
