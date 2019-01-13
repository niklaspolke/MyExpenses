package vu.de.npolke.myexpenses.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.util.HashUtil;

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
@Category(vu.de.npolke.myexpenses.InMemory.class)
public class AccountDAOTest extends AbstractDAOTest {

	private static AccountDAO accountDAO;
	private static CategoryDAO categoryDAO;
	private static ExpenseDAO expenseDAO;

	private static long testCounter = 0;

	public AccountDAOTest() {
		super("AccountDAOTest" + ++testCounter);
	}

	@BeforeClass
	public static void initialise() {
		accountDAO = (AccountDAO) DAOFactory.getDAO(Account.class);
		categoryDAO = (CategoryDAO) DAOFactory.getDAO(vu.de.npolke.myexpenses.model.Category.class);
		expenseDAO = (ExpenseDAO) DAOFactory.getDAO(Expense.class);
	}

	@Test
	public void readAccountFromLogin_ValidLogin() {
		Account account = accountDAO.readByLogin("test1", "password");

		assertNotNull(account);
		assertEquals(1, account.getId());
		assertEquals("test1", account.getLogin());
		assertEquals(HashUtil.toMD5("password"), account.getPassword());
		assertNull(account.getBudget());
	}

	@Test
	public void readAccountFromLogin_InvalidPassword() {
		Account account = accountDAO.readByLogin("test1", "wrong password");

		assertNull(account);
	}

	@Test
	public void readAccountFromLogin_InvalidUser() {
		Account account = accountDAO.readByLogin("non existing user", "password");

		assertNull(account);
	}

	@Test
	public void readAccountFromLogin_WithBudget() {
		Account account = accountDAO.readByLogin("test2", "password");

		assertNotNull(account);
		assertEquals(10, account.getId());
		assertEquals("test2", account.getLogin());
		assertEquals(HashUtil.toMD5("password"), account.getPassword());
		assertNotNull(account.getBudget());
		assertTrue(400 - account.getBudget() <= Account.DELTA_NOT_ZERO);
	}

	@Test
	public void create() {
		Account account = accountDAO.create("user1", "password1");

		assertNotNull(account);
		assertTrue(account.getId() > 0);
		assertEquals("user1", account.getLogin());
		assertEquals(HashUtil.toMD5("password1"), account.getPassword());
	}

	@Test
	public void update() {
		Account account = accountDAO.readByLogin("test1", "password");
		account.setLogin("user111");
		account.setPassword(HashUtil.toMD5("password2"));
		account.setBudget(400.0);
		long oldId = account.getId();

		accountDAO.update(account);

		account = accountDAO.readByLogin("user111", "password2");

		assertNotNull(account);
		assertEquals(oldId, account.getId());
		assertEquals("user111", account.getLogin());
		assertEquals(HashUtil.toMD5("password2"), account.getPassword());
		assertNotNull(account.getBudget());
		assertTrue(400 - account.getBudget() <= Account.DELTA_NOT_ZERO);
	}

	@Test
	public void delete_success() {
		final long deletedAccounts = accountDAO.deleteByAccountId(1);

		assertEquals(1, deletedAccounts);
	}

	@Test
	public void delete_invalidUser() {
		final long deletedAccounts = accountDAO.deleteByAccountId(666);

		assertEquals(0, deletedAccounts);
	}

	@Test
	public void delete_integration() {
		final long deletedExpenses = expenseDAO.deleteByAccountId(1);
		final long deletedCategorys = categoryDAO.deleteByAccountId(1);
		final long deletedAccounts = accountDAO.deleteByAccountId(1);

		assertEquals(6, deletedExpenses);
		assertEquals(4, deletedCategorys);
		assertEquals(1, deletedAccounts);
	}
}
