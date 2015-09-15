package vu.de.npolke.myexpenses.backend;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Expense;

public class DatabaseConnectionTest {
	/**
	 * persistence-unit for test as defined in META-INF/persistence.xml
	 */
	public static final String TEST_PERSISTENCE_UNIT = "myexpenses-testdb";

	private static final String DATABASECONNECTION_CONNECTIONPOOL_ATTRIBUTE = "dbConnectionPool";

	private DatabaseConnection dbConnect;

	/*
	 * The EntityManagerFactory within DatabaseConnection won't be closed. When a new
	 * EntityManagerFactory is created within the constructor of SqlScriptExecutor
	 * (the creation of the EntityManagerFactory within the constructor of DatabaseConnection
	 * will be overriden within setup) with the same persistenceUnitName, the connection
	 * of the 'DatabaseConnectionPool' won't be closed and even within an in-memory database
	 * the data won't be lost.
	 * Nevertheless the execution of the sql script before every test will reset the data
	 * within the database.
	 */
	@Before
	public void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		dbConnect = new DatabaseConnection();
		JpaSqlScriptExecutor executor = new JpaSqlScriptExecutor(TEST_PERSISTENCE_UNIT);
		executor.executeSqlScript(JpaSqlScriptExecutor.INITIALISE_DB_SCRIPT);

		Field privateField = DatabaseConnection.class.getDeclaredField(DATABASECONNECTION_CONNECTIONPOOL_ATTRIBUTE);
		privateField.setAccessible(true);
		privateField.set(dbConnect, executor.getConnectionPool());
	}

	@Test
	public void getNewEntityManager() {
		EntityManager em = dbConnect.connect();
		assertNotNull(em);
		assertTrue(em.isOpen());
		assertNotNull(em.getTransaction());
		assertTrue(em.getTransaction().isActive());
		assertFalse(em.getTransaction().getRollbackOnly());
		dbConnect.rollback();
		dbConnect.close();
	}

	@Test
	public void selectRollback() {
		EntityManager em = dbConnect.connect();
		TypedQuery<Expense> allExpensesQuery = em.createNamedQuery("Expense.findAll", Expense.class);
		List<Expense> allExpenses = allExpensesQuery.getResultList();
		assertNotNull(allExpenses);
		assertTrue(allExpenses.size() > 0);
		dbConnect.rollback();
		dbConnect.close();
	}

	@Test
	public void selectCommit() {
		EntityManager em = dbConnect.connect();
		TypedQuery<Expense> allExpensesQuery = em.createNamedQuery("Expense.findAll", Expense.class);
		List<Expense> allExpenses = allExpensesQuery.getResultList();
		assertNotNull(allExpenses);
		assertTrue(allExpenses.size() > 0);
		dbConnect.commit();
		dbConnect.close();
	}

	@Test
	public void errorRollback() {
		EntityManager em = dbConnect.connect();
		Query incorrectQuery = em.createNativeQuery("SELECT * FROM expectError");
		try {
			incorrectQuery.getResultList();
			fail();
		} catch (PersistenceException e) {
		}
		dbConnect.rollback();
		dbConnect.close();
	}

	@Test
	public void errorCommit() {
		EntityManager em = dbConnect.connect();
		Query incorrectQuery = em.createNativeQuery("SELECT * FROM expectError");
		try {
			incorrectQuery.getResultList();
			fail();
		} catch (PersistenceException e) {
		}
		dbConnect.commit();
		dbConnect.close();
	}
}
