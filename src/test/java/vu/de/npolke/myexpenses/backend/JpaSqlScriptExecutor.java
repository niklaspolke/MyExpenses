package vu.de.npolke.myexpenses.backend;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
		if (dbConnection.getTransaction().isActive()) {
			try {
				dbConnection.getTransaction().commit();
			} catch (Exception e) {
				// eclipselink bug --> Internal Exception: java.sql.SQLException: cannot rollback - no transaction is active
			}
		}
	}

	private void rollback() {
		if (dbConnection.getTransaction().isActive()) {
			try {
				dbConnection.getTransaction().rollback();
			} catch (Exception e) {
				// eclipselink bug --> Internal Exception: java.sql.SQLException: cannot rollback - no transaction is active
			}
		}
	}
}
