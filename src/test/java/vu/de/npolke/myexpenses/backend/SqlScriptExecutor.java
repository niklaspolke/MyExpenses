package vu.de.npolke.myexpenses.backend;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
public abstract class SqlScriptExecutor {

	public static final String INITIALISE_DB_SCRIPT = "src/test/resources/initialise_db.sql";
	public static final String INITIALISE_DB_SCRIPT_ENCODING = "UTF-8";
	private static final String SQL_STATEMENT_END = ";";

	protected abstract void connectToDatabase();

	protected abstract void closeDatabaseConnection();

	protected abstract void executeSqlCommand(final StringBuilder sqlScriptLine);

	private static void closeIgnoreExceptions(final Closeable input) {
		if (input != null) {
			try {
				input.close();
			} catch (IOException ioe) {
			}
		}
	}

	private boolean isSqlStatementComplete(final StringBuilder sqlStatement) {
		return sqlStatement.toString().endsWith(SQL_STATEMENT_END);
	}

	public final void executeSqlScript(final String sqlScriptFilename) {
		connectToDatabase();

		BufferedReader scriptReader = null;
		try {
			scriptReader = new BufferedReader(new InputStreamReader(new FileInputStream(sqlScriptFilename), INITIALISE_DB_SCRIPT_ENCODING));
			StringBuilder sqlStatement = new StringBuilder();
			for (String scriptLine = scriptReader.readLine(); scriptLine != null; scriptLine = scriptReader.readLine()) {
				sqlStatement.append(scriptLine);
				if (isSqlStatementComplete(sqlStatement)) {
					executeSqlCommand(sqlStatement);
					sqlStatement = new StringBuilder();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeIgnoreExceptions(scriptReader);
		}

		closeDatabaseConnection();
	}
}
