package vu.de.npolke.myexpenses.services;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.services.connections.ConnectionStrategy;
import vu.de.npolke.myexpenses.services.connections.JdbcInMemoryConnectionStrategy;
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
public class AccountDAOTest {

	private static AccountDAO accountDAO;
	private static Connection connection;

	@BeforeClass
	public static void initialise() {
		ConnectionStrategy connectionStrategy = new JdbcInMemoryConnectionStrategy(AccountDAOTest.class.getName());
		connection = connectionStrategy.getConnection();
		accountDAO = (AccountDAO) DAOFactory.getDAO(Account.class);
		accountDAO.setConnectionStrategy(connectionStrategy);

		try {
			Statement createTable = connection.createStatement();
			createTable.executeUpdate("CREATE TABLE account (id INTEGER PRIMARY KEY, login VARCHAR(40) NOT NULL UNIQUE, password VARCHAR(40) NOT NULL)");
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setup() {
		try {
			Statement clearTable = connection.createStatement();
			clearTable.executeUpdate("DELETE FROM account");
			Statement insertAccount = connection.createStatement();
			insertAccount.executeUpdate("INSERT INTO account (id, login, password) VALUES (1, 'user', '" + HashUtil.toMD5("password") + "')");
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void readAccountFromLogin_ValidLogin() {
		Account account = accountDAO.readAccountFromLogin("user", "password");

		assertNotNull(account);
		assertEquals(1, account.getId());
		assertEquals("user", account.getLogin());
	}

	@Test
	public void readAccountFromLogin_InvalidPassword() {
		Account account = accountDAO.readAccountFromLogin("user", "wrong password");

		assertNull(account);
	}

	@Test
	public void readAccountFromLogin_InvalidUser() {
		Account account = accountDAO.readAccountFromLogin("non existing user", "password");

		assertNull(account);
	}
}
