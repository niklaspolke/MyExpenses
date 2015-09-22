package vu.de.npolke.myexpenses.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

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
@NamedQueries(
	value={
	@NamedQuery(
		name="Account.checkLogin",
		query="SELECT a FROM Account a WHERE a.login = :login AND a.password = :password")
	}
)
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "ID_SEQ", strategy = GenerationType.TABLE)
	private long id;

	private String login;

	private String password;

	@OneToMany(mappedBy="account")
	@OrderBy("name ASC")
	List<Category> categories;

	@OneToMany(mappedBy="account")
	@OrderBy("day DESC")
	List<Expense> expenses;

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

	public void add(final Category category) {
		if (getCategories() == null) {
			categories = new ArrayList<Category>();
		}
		category.setAccount(this);
		getCategories().add(category);
	}

	public void remove(final Category category) {
		if (getCategories() == null) {
			categories = new ArrayList<Category>();
		}
		category.setAccount(null);
		getCategories().remove(category);
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void add(final Expense expense) {
		if (getExpenses() == null) {
			expenses = new ArrayList<Expense>();
		}
		expense.setAccount(this);
		getExpenses().add(expense);
	}

	public void remove(final Expense expense) {
		if (getExpenses() == null) {
			expenses = new ArrayList<Expense>();
		}
		expense.setAccount(null);
		getExpenses().remove(expense);
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	@Override
	public String toString() {
		//@formatter:off
		StringBuilder text = new StringBuilder().append("Account: ")
				.append(getLogin());
		return text.toString();
		//@formatter:on
	}
}
