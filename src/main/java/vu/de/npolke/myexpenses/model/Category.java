package vu.de.npolke.myexpenses.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name="categories")
@NamedQueries(
	value={
	@NamedQuery(
		name="Category.findAll",
		query="SELECT c FROM Category c ORDER BY c.name ASC"),
	@NamedQuery(
		name="Category.findById",
		query="SELECT c FROM Category c WHERE c.id = :id")
	}
)
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "ID_SEQ", strategy = GenerationType.TABLE)
	private long id;

	private String name;

	@OneToMany(mappedBy="category")
	private Set<Expense> expenses;

	public Category() {
	}

	public Category(final long id) {
		setId(id);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Set<Expense> getExpenses() {
		return expenses;
	}

	@Override
	public String toString() {
		//@formatter:off
		StringBuilder text = new StringBuilder().append("Category: #")
				.append(getId() != 0 ? getId() : "").append(" - ")
				.append(getName());
		return text.toString();
		//@formatter:on
	}
}
