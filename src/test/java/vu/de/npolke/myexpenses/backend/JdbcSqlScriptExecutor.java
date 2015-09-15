package vu.de.npolke.myexpenses.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Is able to create new initialised database without manipulating persistence.xml.
 */
public class JdbcSqlScriptExecutor extends SqlScriptExecutor {

	/**
	 * same jdbc driver as in META-INF/persistence.xml
	 */
	public static final String DRIVER = "org.sqlite.JDBC";
	/**
	 * same start of url as in META-INF/persistence.xml
	 */
	public static final String URL_PREFIX = "jdbc:sqlite:";

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
		} catch (SQLException sqle) {
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
