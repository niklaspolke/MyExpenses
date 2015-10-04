package vu.de.npolke.myexpenses.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
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
public class AccountDAOTest extends AbstractDAOTest {

	private static AccountDAO accountDAO;

	private static long testCounter = 0;

	public AccountDAOTest() {
		super("AccountDAOTest" + ++testCounter);
	}

	@BeforeClass
	public static void initialise() {
		accountDAO = (AccountDAO) DAOFactory.getDAO(Account.class);
	}

	@Test
	public void readAccountFromLogin_ValidLogin() {
		Account account = accountDAO.readByLogin("test1", "password");

		assertNotNull(account);
		assertEquals(1, account.getId());
		assertEquals("test1", account.getLogin());
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
	public void create() {
		Account account = accountDAO.create("user1", "password1");

		assertNotNull(account);
		assertTrue(account.getId() > 0);
		assertEquals("user1", account.getLogin());
		assertEquals(HashUtil.toMD5("password1"), account.getPassword());
	}
}
