package vu.de.npolke.myexpenses.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Entity
@Table(name="category")
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "ID_SEQ", strategy = GenerationType.TABLE)
	private long id;

	private String name;

	@OneToMany(mappedBy="category")
	List<Expense> expenses;

	@ManyToOne
	private Account account;

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void add(final Expense expense) {
		if (getExpenses() == null) {
			expenses = new ArrayList<Expense>();
		}
		expense.setCategory(this);
		getExpenses().add(expense);
	}

	public void remove(final Expense expense) {
		if (getExpenses() == null) {
			expenses = new ArrayList<Expense>();
		}
		expense.setCategory(null);
		getExpenses().remove(expense);
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(final Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("Category: ").append(getName()).toString();
	}
}
