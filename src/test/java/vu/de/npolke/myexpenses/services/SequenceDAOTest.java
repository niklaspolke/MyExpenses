package vu.de.npolke.myexpenses.services;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
public class SequenceDAOTest {

	private static SequenceDAO sequenceDAO;
	private static Connection connection;

	private long seq_value;

	@BeforeClass
	public static void initialise() {
		ConnectionStrategy connectionStrategy = new JdbcInMemoryConnectionStrategy(SequenceDAOTest.class.getName());
		connection = connectionStrategy.getConnection();
		sequenceDAO = (SequenceDAO) DAOFactory.getDAO(Long.class);
		sequenceDAO.setConnectionStrategy(connectionStrategy);

		try {
			Statement createTable = connection.createStatement();
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
			clearTable.executeUpdate("DELETE FROM sequence");
			seq_value = 1000;
			Statement insertSequence = connection.createStatement();
			insertSequence.executeUpdate("INSERT INTO sequence (seq_name, seq_number) VALUES ('ID_GENERATOR', '" + seq_value + "')");
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getNextPrimaryKey() {
		assertEquals(seq_value + 1, sequenceDAO.getNextPrimaryKey());
		assertEquals(seq_value + 2, sequenceDAO.getNextPrimaryKey());
		assertEquals(seq_value + 3, sequenceDAO.getNextPrimaryKey());
	}
}
