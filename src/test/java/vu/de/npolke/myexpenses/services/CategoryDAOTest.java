package vu.de.npolke.myexpenses.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Category;
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
public class CategoryDAOTest {

	private static CategoryDAO categoryDAO;
	private static Connection connection;

	@BeforeClass
	public static void initialise() {
		ConnectionStrategy connectionStrategy = new JdbcInMemoryConnectionStrategy(CategoryDAOTest.class.getName());
		connection = connectionStrategy.getConnection();
		categoryDAO = (CategoryDAO) DAOFactory.getDAO(Category.class);
		categoryDAO.setConnectionStrategy(connectionStrategy);
		SequenceDAO sequenceDAO = (SequenceDAO) DAOFactory.getDAO(Long.class);
		sequenceDAO.setConnectionStrategy(connectionStrategy);

		try {
			Statement createTable = connection.createStatement();
			createTable.executeUpdate("CREATE TABLE category (id INTEGER PRIMARY KEY, name VARCHAR(40) NOT NULL, account_id INTEGER NOT NULL)");
			createTable = connection.createStatement();
			createTable.executeUpdate("CREATE TABLE sequence (seq_name VARCHAR(40) PRIMARY KEY, seq_number INTEGER NOT NULL)");
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setup() {
		try {
			Statement clearTable = connection.createStatement();
			clearTable.executeUpdate("DELETE FROM category");
			Statement insertStatement = connection.createStatement();
			insertStatement.executeUpdate("INSERT INTO category (id, name, account_id) VALUES (10, 'food', 1)");

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
		Category category = categoryDAO.read(10);

		assertNotNull(category);
		assertEquals(10, category.getId());
		assertEquals("food", category.getName());
	}

	@Test
	public void create() {
		Category category = categoryDAO.create("health", 1);

		assertNotNull(category);
		assertTrue(category.getId() > 10);
		assertEquals("health", category.getName());
	}

	@Test
	public void update() {
		Category category = categoryDAO.read(10);
		category.setName("junk food");

		boolean success = categoryDAO.update(category);

		assertTrue(success);

		category = categoryDAO.read(category.getId());
		assertEquals(10, category.getId());
		assertEquals("junk food", category.getName());
	}
}
