package vu.de.npolke.myexpenses.backend;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Expense;

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
