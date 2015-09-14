package vu.de.npolke.myexpenses.backend;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Expense;

public class SqlScriptExecutorTest {

	private SqlScriptExecutor executor;

	@Before
	public void setup() {
		executor = new SqlScriptExecutor(DatabaseConnectionTest.TEST_PERSISTENCE_UNIT);
	}

	@Test
	public void testSqlScript() {
		executor.executeSqlScript(SqlScriptExecutor.INITIALIZE_DB_SCRIPT);
	}

	@Test
	public void testReuseOfDatabaseConnection() {
		executor.executeSqlScript(SqlScriptExecutor.INITIALIZE_DB_SCRIPT);
		EntityManager connection = executor.getConnectionPool().createEntityManager();
		connection.getTransaction().begin();
		TypedQuery<Expense> allExpensesQuery = connection.createNamedQuery("Expense.findAll", Expense.class);
		List<Expense> allExpenses = allExpensesQuery.getResultList();
		for (Expense expense : allExpenses) {
			System.out.println(expense);
		}
		assertNotNull(allExpenses);
		assertTrue(allExpenses.size() > 0);
		connection.getTransaction().commit();
		connection.close();
	}
}
