package vu.de.npolke.myexpenses.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Category;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.connections.ConnectionStrategy;
import vu.de.npolke.myexpenses.services.connections.JdbcInMemoryConnectionStrategy;

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
public class ExpenseDAOTest {

	private static ExpenseDAO	expenseDAO;
	private static Connection	connection;

	@BeforeClass
	public static void initialise() {
		ConnectionStrategy connectionStrategy = new JdbcInMemoryConnectionStrategy(ExpenseDAOTest.class.getName());
		connection = connectionStrategy.getConnection();
		expenseDAO = (ExpenseDAO) DAOFactory.getDAO(Expense.class);
		expenseDAO.setConnectionStrategy(connectionStrategy);
		CategoryDAO categoryDAO = (CategoryDAO) DAOFactory.getDAO(Category.class);
		categoryDAO.setConnectionStrategy(connectionStrategy);
		SequenceDAO sequenceDAO = (SequenceDAO) DAOFactory.getDAO(Long.class);
		sequenceDAO.setConnectionStrategy(connectionStrategy);

		try {
			Statement createTable = connection.createStatement();
			createTable.executeUpdate(
					"CREATE TABLE expense (id INTEGER PRIMARY KEY, day DATE NOT NULL, amount DOUBLE NOT NULL, reason VARCHAR(40) NOT NULL, category_id INTEGER NOT NULL, account_id INTEGER NOT NULL)");
			createTable = connection.createStatement();
			createTable.executeUpdate(
					"CREATE TABLE category (id INTEGER PRIMARY KEY, name VARCHAR(40) NOT NULL, account_id INTEGER NOT NULL)");
			createTable = connection.createStatement();
			createTable.executeUpdate(
					"CREATE TABLE sequence (seq_name VARCHAR(40) PRIMARY KEY, seq_number INTEGER NOT NULL)");
			Statement insertStatement = connection.createStatement();
			insertStatement.executeUpdate("INSERT INTO category (id, name, account_id) VALUES (11, 'food', 1)");
			insertStatement = connection.createStatement();
			insertStatement.executeUpdate("INSERT INTO category (id, name, account_id) VALUES (12, 'health', 1)");
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setup() {
		try {
			Statement clearTable = connection.createStatement();
			clearTable.executeUpdate("DELETE FROM expense");
			Statement insertStatement = connection.createStatement();
			insertStatement.executeUpdate(
					"INSERT INTO Expense (id, day, amount, reason, category_id, account_id) VALUES (101, '2015-09-27', 25.5, 'shopping', 11, 1)");

			clearTable = connection.createStatement();
			clearTable.executeUpdate("DELETE FROM sequence");
			insertStatement = connection.createStatement();
			insertStatement.executeUpdate("INSERT INTO sequence (seq_name, seq_number) VALUES ('ID_GENERATOR', 10)");

			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void read() {
		Expense expense = expenseDAO.read(101);

		assertNotNull(expense);
		assertEquals(101, expense.getId());
		assertEquals("27.09.15", expense.getReadableDayAsString());
		assertEquals(25.5, expense.getAmount(), 0.01);
		assertEquals("shopping", expense.getReason());
		assertEquals(11, expense.getCategoryId());
		assertEquals("food", expense.getCategoryName());
		assertEquals(1, expense.getAccountId());
	}

	@Test
	public void readNotExisting() {
		Expense expense = expenseDAO.read(1101);

		assertNull(expense);
	}

	@Test
	public void create() {
		Expense expense = expenseDAO.create("25.05.15", 13.3, "junk food", 11, 1);

		assertNotNull(expense);
		assertTrue(expense.getId() > 0);
		assertEquals("25.05.15", expense.getReadableDayAsString());
		assertEquals(13.3, expense.getAmount(), 0.01);
		assertEquals("junk food", expense.getReason());
		assertEquals(11, expense.getCategoryId());
		assertEquals("food", expense.getCategoryName());
		assertEquals(1, expense.getAccountId());
	}

	@Test
	public void update() {
		Expense expense = expenseDAO.read(101);
		expense.setAmount(12.2);
		expense.setCategoryId(12);
		expense.setReadableDayAsString("01.01.15");
		expense.setReason("gone swimming");

		boolean success = expenseDAO.update(expense);

		assertTrue(success);
		assertEquals("health", expense.getCategoryName());

		expense = expenseDAO.read(expense.getId());
		assertEquals(101, expense.getId());
		assertEquals("01.01.15", expense.getReadableDayAsString());
		assertEquals(12.2, expense.getAmount(), 0.01);
		assertEquals("gone swimming", expense.getReason());
		assertEquals(12, expense.getCategoryId());
		assertEquals("health", expense.getCategoryName());
		assertEquals(1, expense.getAccountId());
	}

	@Test
	public void updateNotExisting() {
		Expense expense = expenseDAO.read(101);
		expense.setId(1101);
		expense.setAmount(12.2);
		expense.setCategoryId(12);
		expense.setReadableDayAsString("01.01.15");
		expense.setReason("gone swimming");

		boolean success = expenseDAO.update(expense);
		expense = expenseDAO.read(1101);

		assertFalse(success);
		assertNull(expense);
	}

	@Test
	public void readByAccountId() {
		expenseDAO.create("25.01.15", 17.3, "just4fun", 11, 2);
		expenseDAO.create("25.01.15", 17.3, "just4fun", 11, 1);
		expenseDAO.create("25.01.15", 17.3, "just4fun", 11, 2);
		expenseDAO.create("25.01.15", 17.3, "just4fun", 11, 1);
		expenseDAO.create("25.01.15", 17.3, "just4fun", 11, 2);

		List<Expense> expenses = expenseDAO.readByAccountId(2);

		assertNotNull(expenses);
		assertEquals(3, expenses.size());
		for (Expense expense : expenses) {
			assertTrue(expense.getId() > 0);
			assertEquals("25.01.15", expense.getReadableDayAsString());
			assertEquals(17.3, expense.getAmount(), 0.01);
			assertEquals("just4fun", expense.getReason());
			assertEquals(11, expense.getCategoryId());
			assertEquals("food", expense.getCategoryName());
			assertEquals(2, expense.getAccountId());
		}
	}

	@Test
	public void readByCategoryId() {
		expenseDAO.create("25.01.15", 17.3, "just4fun", 12, 1);
		expenseDAO.create("25.01.15", 17.3, "just4fun", 11, 1);
		expenseDAO.create("25.01.15", 17.3, "just4fun", 12, 1);
		expenseDAO.create("25.01.15", 17.3, "just4fun", 11, 1);
		expenseDAO.create("25.01.15", 17.3, "just4fun", 12, 1);

		List<Expense> expenses = expenseDAO.readByCategoryId(12);

		assertNotNull(expenses);
		assertEquals(3, expenses.size());
		for (Expense expense : expenses) {
			assertTrue(expense.getId() > 0);
			assertEquals("25.01.15", expense.getReadableDayAsString());
			assertEquals(17.3, expense.getAmount(), 0.01);
			assertEquals("just4fun", expense.getReason());
			assertEquals(12, expense.getCategoryId());
			assertEquals("health", expense.getCategoryName());
			assertEquals(1, expense.getAccountId());
		}
	}

	@Test
	public void delete() {
		boolean success = expenseDAO.deleteById(101);
		Expense expense = expenseDAO.read(101);

		assertTrue(success);
		assertNull(expense);
	}

	@Test
	public void deleteNotExisting() {
		boolean success = expenseDAO.deleteById(1101);

		assertFalse(success);
	}
}
