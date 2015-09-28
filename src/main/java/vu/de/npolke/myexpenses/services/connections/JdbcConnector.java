package vu.de.npolke.myexpenses.services.connections;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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
public class JdbcConnector {
	public static final String DATASOURE = "jdbc/myexpenses-db";
	public static final String JNDI_PREFIX = "java:/comp/env/";

	private static DataSource datasource;

	static {
		try {
			InitialContext context = new InitialContext();
			datasource = (DataSource) context.lookup(JNDI_PREFIX + DATASOURE);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		Connection connection = null;
		try {
			connection = datasource.getConnection();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return connection;
	}
}
