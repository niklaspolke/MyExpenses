package vu.de.npolke.myexpenses.services.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
public class JdbcInMemoryConnectionStrategy implements ConnectionStrategy {

	public static final String DRIVER = "org.hsqldb.jdbc.JDBCDriver";
	public static final String URL_PREFIX = "jdbc:hsqldb:mem:";
	public static final String URL_SUFFIX = ";user=SA;shutdown=true";

	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private final String databaseName;

	public JdbcInMemoryConnectionStrategy(final String databaseName) {
		this.databaseName = databaseName;
	}

	@Override
	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(URL_PREFIX + databaseName + URL_SUFFIX);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return connection;
	}
}
