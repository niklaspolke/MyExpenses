package vu.de.npolke.myexpenses.backend;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SqlScriptExecutor {

	public static final String INITIALIZE_DB_SCRIPT = "target/test-classes/initialise_db.sql";
	private static final String SQL_STATEMENT_END = ";";

	private EntityManagerFactory dbConnectionPool;
	private EntityManager dbConnection;

	public SqlScriptExecutor(final String jpaDataSource) {
		dbConnectionPool = Persistence.createEntityManagerFactory(jpaDataSource);
	}

	public EntityManagerFactory getConnectionPool() {
		return dbConnectionPool;
	}

	private void connectToDatabase() {
		dbConnection = dbConnectionPool.createEntityManager();
	}

	private void startTransaction() {
		dbConnection.getTransaction().begin();
	}

	private void commit() {
		dbConnection.getTransaction().commit();
	}

	private void rollback() {
		dbConnection.getTransaction().rollback();
	}

	private void closeDatabaseConnection() {
		dbConnection.close();
	}

	private void executeSqlCommand(final StringBuilder sqlScriptLine) {
		startTransaction();
		try {
			dbConnection.createNativeQuery(sqlScriptLine.toString()).executeUpdate();
			commit();
		} catch (Exception e) {
			rollback();
		}
	}

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

	public void executeSqlScript(final String sqlScriptFilename) {
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
