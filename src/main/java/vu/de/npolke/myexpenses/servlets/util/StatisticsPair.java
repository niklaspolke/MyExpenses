package vu.de.npolke.myexpenses.servlets.util;

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
public class StatisticsPair implements Comparable<StatisticsPair> {

	private Long id;
	private String name;
	private Double value;
	private Boolean monthly;
	private Boolean income;

	public StatisticsPair(final Long id, final String name, final Double value, final Boolean monthly,
			final Boolean income) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.monthly = monthly;
		this.income = income;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Double getValue() {
		return value;
	}

	public Boolean isMonthly() {
		return monthly;
	}

	public Boolean isIncome() {
		return income;
	}

	@Override
	/**
	 * Note: this class has a natural ordering that is inconsistent with equals.
	 */
	public int compareTo(final StatisticsPair other) {
		if (isMonthly() && other.isMonthly() == false) {
			return -100;
		} else if (isMonthly() == false && other.isMonthly()) {
			return 100;
		} else {
			return getName().compareTo(other.getName());
		}
	}
}
