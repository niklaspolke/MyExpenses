package vu.de.npolke.myexpenses.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

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
public class CategoryTest {

	private static final int ID = 4;
	private static final String NAME = "food";
	private static final Account ACCOUNT = new Account();

	private Category category;

	@Before
	public void setup() {
		category = new Category();
	}

	@Test
	public void normalValues() {
		category.setId(ID);
		category.setName(NAME);
		category.setAccount(ACCOUNT);

		assertEquals(ID, category.getId());
		assertEquals(NAME, category.getName());
		assertEquals(ACCOUNT, category.getAccount());

		assertEquals("Category: food", category.toString());
	}

	@Test
	public void addExpense() {
		Expense expense = new Expense();
		Expense expense2 = new Expense();
		category.add(expense);
		category.add(expense2);

		assertEquals(2, category.getExpenses().size());
		assertEquals(expense, category.getExpenses().get(0));
		assertEquals(expense2, category.getExpenses().get(1));
		assertEquals(category, expense.getCategory());
		assertEquals(category, expense2.getCategory());
	}

	@Test
	public void removeExpense() {
		Expense expense = new Expense();
		category.remove(expense);
		category.add(expense);
		category.remove(expense);

		assertEquals(0, category.getExpenses().size());
		assertEquals(null, expense.getCategory());
	}
}
