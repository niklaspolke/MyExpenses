package vu.de.npolke.myexpenses.backend;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaSqlScriptExecutor extends SqlScriptExecutor {

	private EntityManagerFactory dbConnectionPool;
	private EntityManager dbConnection;

	public JpaSqlScriptExecutor(final String jpaDataSource) {
		dbConnectionPool = Persistence.createEntityManagerFactory(jpaDataSource);
	}

	public EntityManagerFactory getConnectionPool() {
		return dbConnectionPool;
	}

	@Override
	protected void connectToDatabase() {
		dbConnection = dbConnectionPool.createEntityManager();
	}

	@Override
	protected void executeSqlCommand(final StringBuilder sqlScriptLine) {
		startTransaction();
		try {
			dbConnection.createNativeQuery(sqlScriptLine.toString()).executeUpdate();
			commit();
		} catch (Exception e) {
			rollback();
		}
	}

	@Override
	protected void closeDatabaseConnection() {
		dbConnection.close();
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
}
