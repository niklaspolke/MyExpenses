package vu.de.npolke.myexpenses.services;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;

import vu.de.npolke.myexpenses.backend.SqlScriptExecutor;
import vu.de.npolke.myexpenses.services.connections.ConnectionStrategy;
import vu.de.npolke.myexpenses.services.connections.JdbcInMemoryConnectionStrategy;

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
