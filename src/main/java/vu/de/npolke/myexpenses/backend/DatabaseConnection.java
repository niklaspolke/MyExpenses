package vu.de.npolke.myexpenses.backend;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DatabaseConnection {
	/**
	 * persistence-unit as defined in META-INF/persistence.xml
	 */
	public static String PERSISTENCE_UNIT = "myexpenses-db";

	private EntityManagerFactory dbConnectionPool;
	private EntityManager dbConnection;

	public DatabaseConnection() {
		dbConnectionPool = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
	}

	public EntityManager connect() {
		dbConnection = dbConnectionPool.createEntityManager();
		dbConnection.getTransaction().begin();
		return dbConnection;
	}

	public void close() {
		dbConnection.close();
	}

	public void commit() {
		if (dbConnection.getTransaction().isActive()) {
			if (dbConnection.getTransaction().getRollbackOnly()) {
				rollback();
			} else {
				dbConnection.getTransaction().commit();
			}
		}
	}

	public void rollback() {
		if (dbConnection.getTransaction().isActive()) {
			try {
				dbConnection.getTransaction().rollback();
			} catch (Exception e) {
				// eclipselink bug --> Internal Exception: java.sql.SQLException: cannot rollback - no transaction is active
			}
		}
	}
}
