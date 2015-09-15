package vu.de.npolke.myexpenses.backend;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public abstract class SqlScriptExecutor {

	public static final String INITIALISE_DB_SCRIPT = "target/test-classes/initialise_db.sql";
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
			scriptReader = new BufferedReader(new FileReader(new File(sqlScriptFilename)));
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
