package vu.de.npolke.myexpenses.backend;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Expense;

public class JpaSqlScriptExecutorTest {

	private JpaSqlScriptExecutor executor;

	@Before
	public void setup() {
		executor = new JpaSqlScriptExecutor(DatabaseConnectionTest.TEST_PERSISTENCE_UNIT);
	}

	@Test
	public void sqlScript() {
		executor.executeSqlScript(JpaSqlScriptExecutor.INITIALISE_DB_SCRIPT);
	}

	@Test
	public void initialisedDb() {
		executor.executeSqlScript(JpaSqlScriptExecutor.INITIALISE_DB_SCRIPT);
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
