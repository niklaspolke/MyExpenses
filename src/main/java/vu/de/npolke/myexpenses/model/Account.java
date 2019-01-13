package vu.de.npolke.myexpenses.model;

import java.io.Serializable;

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
public class Account implements Serializable, Cloneable {

	public static final double DELTA_NOT_ZERO = 0.001;

	private static final long serialVersionUID = 1L;

	private long id;

	private String login;

	// MD5-Hashvalue
	private String password;

	private Double budget;

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public Double getBudget() {
		return budget;
	}

	public Integer getBudgetAsInteger() {
		if (getBudget() == null) {
			return null;
		} else {
			return getBudget().intValue();
		}
	}

	public void setBudget(final Double budget) {
		this.budget = budget;
	}

	public boolean equalBudget(final Double otherBudget) {
		if (getBudget() == null || otherBudget == null) {
			return getBudget() == otherBudget;
		} else {
			return Math.abs(getBudget() - otherBudget) <= DELTA_NOT_ZERO;
		}
	}

	@Override
	public Account clone() {
		final Account clone = new Account();
		clone.setId(getId());
		clone.setLogin(getLogin());
		clone.setPassword(getPassword());
		clone.setBudget(getBudget());
		return clone;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Account) {
			Account other = (Account) obj;
			boolean equal = getId() == other.getId();
			if (getLogin() == null) {
				equal &= other.getLogin() == null;
			} else {
				equal &= getLogin().equals(other.getLogin());
			}
			if (getPassword() == null) {
				equal &= other.getPassword() == null;
			} else {
				equal &= getPassword().equals(other.getPassword());
			}
			equal &= equalBudget(other.getBudget());
			return equal;
		} else {
			return false;
		}
	};

	@Override
	public String toString() {
		//@formatter:off
		StringBuilder text = new StringBuilder().append("Account: ")
				.append(getLogin());
		return text.toString();
		//@formatter:on
	}
}
