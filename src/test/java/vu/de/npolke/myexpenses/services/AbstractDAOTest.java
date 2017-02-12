package vu.de.npolke.myexpenses.services;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.experimental.categories.Category;

import vu.de.npolke.myexpenses.backend.SqlScriptExecutor;
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
@Category(vu.de.npolke.myexpenses.InMemory.class)
public abstract class AbstractDAOTest {

	protected SqlScriptExecutor scriptExecutor;

	private ConnectionStrategy connectionStrategy;

	public AbstractDAOTest(final String databaseName) {
		connectionStrategy = new JdbcInMemoryConnectionStrategy(databaseName);
		DAOFactory.changeConnectionStrategy(connectionStrategy);
		scriptExecutor = new SqlScriptExecutor(connectionStrategy);
	}

	@Before
	public void superSetup() {
		scriptExecutor.executeSqlScript(SqlScriptExecutor.INITIALISE_DB_SCRIPT);
		scriptExecutor.executeSqlScript(SqlScriptExecutor.INSERT_TESTDATA_SCRIPT);
	}

	@After
	public void destroyDatabase() {
		try (Connection connection = connectionStrategy.getConnection()) {
			SqlScriptExecutor.executeSqlCommand(connection, new StringBuilder("SHUTDOWN"));
			connection.commit();
		} catch (SQLException e) {
		}
	}
}
