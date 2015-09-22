package vu.de.npolke.myexpenses.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Is able to create new initialised database without manipulating persistence.xml.
 *
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
public class JdbcSqlScriptExecutor extends SqlScriptExecutor {

	/**
	 * same jdbc driver as in META-INF/persistence.xml
	 */
	public static final String DRIVER = "org.hsqldb.jdbc.JDBCDriver";
	/**
	 * same start of url as in META-INF/persistence.xml
	 */
	public static final String URL_PREFIX = "jdbc:hsqldb:file:";

	private String database;
	private Connection connection;

	public JdbcSqlScriptExecutor(String database) {
		this.database = database;
	}

	@Override
	protected void connectToDatabase() {
		try {
			Class.forName(DRIVER);
			connection = DriverManager.getConnection(URL_PREFIX + database);
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	@Override
	protected void executeSqlCommand(final StringBuilder sqlScriptLine) {
		try {
			Statement updateQuery = connection.createStatement();
			updateQuery.executeUpdate(sqlScriptLine.toString());
			updateQuery.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void closeDatabaseConnection() {
		try {
			connection.close();
		} catch (SQLException sqle) {
		}
	}
}
