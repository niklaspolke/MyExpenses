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
public class AccountTest {

	private static final int ID = 4;
	private static final String LOGIN = "testuser";
	private static final String PASSWORD = "password";

	private Account account;

	@Before
	public void setup() {
		account = new Account();
	}

	@Test
	public void normalValues() {
		account.setId(ID);
		account.setLogin(LOGIN);
		account.setPassword(PASSWORD);

		assertEquals(ID, account.getId());
		assertEquals(LOGIN, account.getLogin());
		assertEquals(PASSWORD, account.getPassword());

		assertEquals("Account: testuser", account.toString());
	}

	@Test
	public void addCategory() {
		Category category = new Category();
		Category category2 = new Category();
		account.add(category);
		account.add(category2);

		assertEquals(2, account.getCategories().size());
		assertEquals(category, account.getCategories().get(0));
		assertEquals(category2, account.getCategories().get(1));
		assertEquals(account, category.getAccount());
		assertEquals(account, category2.getAccount());
	}

	@Test
	public void removeCategory() {
		Category category = new Category();
		account.remove(category);
		account.add(category);
		account.remove(category);

		assertEquals(0, account.getCategories().size());
		assertEquals(null, category.getAccount());
	}

	@Test
	public void addExpense() {
		Expense expense = new Expense();
		Expense expense2 = new Expense();
		account.add(expense);
		account.add(expense2);

		assertEquals(2, account.getExpenses().size());
		assertEquals(expense, account.getExpenses().get(0));
		assertEquals(expense2, account.getExpenses().get(1));
		assertEquals(account, expense.getAccount());
		assertEquals(account, expense2.getAccount());
	}

	@Test
	public void removeExpense() {
		Expense expense = new Expense();
		account.remove(expense);
		account.add(expense);
		account.remove(expense);

		assertEquals(0, account.getExpenses().size());
		assertEquals(null, expense.getAccount());
	}
}
