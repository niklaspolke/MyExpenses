package vu.de.npolke.myexpenses.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Expense;

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
public class JdbcSqlScriptExecutorTest {

	private static final String FILE = "target/myexpenses_hsqldb";

	private final DateFormat DATA_FORMATTER = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);

	private JdbcSqlScriptExecutor executor;

	@Before
	public void setup() {
		executor = new JdbcSqlScriptExecutor(FILE + ";shutdown=true;hsqldb.lock_file=false");
	}

	@After
	public void shutdown() {

		executor.closeDatabaseConnection();
	}

	@Test
	public void dbFileCreation() {
		executor.executeSqlScript(JdbcSqlScriptExecutor.INITIALISE_DB_SCRIPT);
		File result = new File(FILE + ".script");
		assertTrue(result.isFile());
	}

	@Test
	public void initialisedDb() throws SQLException {
		executor.executeSqlScript(SqlScriptExecutor.INITIALISE_DB_SCRIPT);

		Connection connection = DriverManager
				.getConnection(JdbcSqlScriptExecutor.URL_PREFIX + FILE + ";shutdown=true;hsqldb.lock_file=false");
		Statement allExpensesQuery = connection.createStatement();
		ResultSet result = allExpensesQuery.executeQuery(
				"select id, amount, reason, category_id, account_id, day from expense where account_id = 1");
		int countResults = 0;
		while (result.next()) {
			countResults++;
			Expense expense = new Expense();
			Calendar calendar = Calendar.getInstance(Locale.GERMANY);
			calendar.setTime(result.getDate("day"));
			expense.setReadableDayAsString(DATA_FORMATTER.format(calendar.getTime()));
			expense.setAmount(result.getDouble("amount"));
			expense.setReason(result.getString("reason"));
			expense.setId(result.getLong("id"));
			expense.setCategoryId(result.getLong("category_id"));
		}
		assertEquals(3, countResults);

		PreparedStatement insert = connection.prepareStatement(
				"INSERT INTO expense(id, amount, reason, category_id, account_id, day) VALUES(104, 44.4, 'spezieller Test2s', 11, 1, ?)");
		insert.setDate(1, new java.sql.Date(System.currentTimeMillis()), Calendar.getInstance(Locale.GERMANY));
		insert.executeUpdate();
		connection.commit();
		connection.close();
	}
}
