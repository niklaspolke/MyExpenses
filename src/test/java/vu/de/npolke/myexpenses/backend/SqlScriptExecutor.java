package vu.de.npolke.myexpenses.backend;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import vu.de.npolke.myexpenses.services.connections.ConnectionStrategy;

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
public class SqlScriptExecutor {

	public static final String	INITIALISE_DB_SCRIPT			= "src/test/resources/initialise_db.sql";
	public static final String	INSERT_TESTDATA_SCRIPT			= "src/test/resources/insert_testdata.sql";
	public static final String	DROP_TABLES_SCRIPT				= "src/test/resources/drop_tables.sql";
	private static final String	INITIALISE_DB_SCRIPT_ENCODING	= "UTF-8";
	private static final String	SQL_STATEMENT_END				= ";";

	private final ConnectionStrategy connectionStrategy;

	public SqlScriptExecutor(final ConnectionStrategy connectionStrategy) {
		this.connectionStrategy = connectionStrategy;
	}

	public static void executeSqlCommand(final Connection connection, final StringBuilder sqlScriptLine) {
		try {
			Statement updateQuery = connection.createStatement();
			updateQuery.executeUpdate(sqlScriptLine.toString());
			updateQuery.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static boolean isSqlStatementComplete(final StringBuilder sqlStatement) {
		return sqlStatement.toString().endsWith(SQL_STATEMENT_END);
	}

	public void executeSqlScript(final String sqlScriptFilename) {
		try (Connection connection = connectionStrategy.getConnection();
				BufferedReader scriptReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(sqlScriptFilename), INITIALISE_DB_SCRIPT_ENCODING));) {

			StringBuilder sqlStatement = new StringBuilder();
			for (String scriptLine = scriptReader.readLine(); scriptLine != null; scriptLine = scriptReader
					.readLine()) {
				sqlStatement.append(scriptLine);
				if (isSqlStatementComplete(sqlStatement)) {
					executeSqlCommand(connection, sqlStatement);
					sqlStatement = new StringBuilder();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
