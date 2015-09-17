package vu.de.npolke.myexpenses.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Category;
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

	private static final String FILE = "target/test-classes/myexpenses_sqlite_initialised.db";

	private JdbcSqlScriptExecutor executor;

	@Before
	public void setup() {
		executor = new JdbcSqlScriptExecutor(FILE);
	}

	@Test
	public void dbFileCreation() {
		executor.executeSqlScript(JdbcSqlScriptExecutor.INITIALISE_DB_SCRIPT);
		File result = new File(FILE);
		assertTrue(result.isFile());
	}

	@Test
	public void initialisedDb() throws SQLException {
		executor.executeSqlScript(SqlScriptExecutor.INITIALISE_DB_SCRIPT);

		Connection connection = DriverManager.getConnection(JdbcSqlScriptExecutor.URL_PREFIX + FILE);
		Statement allExpensesQuery = connection.createStatement();
		ResultSet result = allExpensesQuery.executeQuery("select * from expenses");
		int countResults = 0;
		while (result.next()) {
			countResults++;
			Expense expense = new Expense();
			expense.setDatabaseDate(result.getString("date"));
			expense.setAmount(result.getDouble("amount"));
			expense.setReason(result.getString("reason"));
			expense.setId(result.getLong("id"));
			Category category = new Category();
			category.setName("<not read yet>");
			category.setId(result.getInt("category_id"));
			expense.setCategory(category);
			System.out.println(expense);
		}
		assertEquals(3, countResults);
	}
}
