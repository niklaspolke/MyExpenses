package vu.de.npolke.myexpenses.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Category;
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

	private static final String JPQL_ALL_ACCOUNTS = "FROM Account a";

	private static final String JPQL_ONE_CATEGORY = "SELECT c FROM Category c JOIN c.account a WHERE a.login='test' AND c.id=11";

	private static final String JPQL_ONE_EXPENSE = "SELECT e FROM Expense e JOIN e.account a WHERE a.login='test' AND e.id=103";

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
	public void account() {
		executor.executeSqlScript(JpaSqlScriptExecutor.INITIALISE_DB_SCRIPT);
		EntityManager connection = executor.getConnectionPool().createEntityManager();
		connection.getTransaction().begin();
		TypedQuery<Account> allAccountsQuery = connection.createQuery(JPQL_ALL_ACCOUNTS, Account.class);
		List<Account> allAccounts = allAccountsQuery.getResultList();
		connection.getTransaction().commit();
		connection.close();

		assertEquals(1, allAccounts.size());
		assertEquals(1, allAccounts.get(0).getId());
		assertEquals("test", allAccounts.get(0).getLogin());
		assertEquals("5f4dcc3b5aa765d61d8327deb882cf99", allAccounts.get(0).getPassword());
		assertEquals(2, allAccounts.get(0).getCategories().size());
		assertEquals(3, allAccounts.get(0).getExpenses().size());
	}

	@Test
	public void category() {
		executor.executeSqlScript(JpaSqlScriptExecutor.INITIALISE_DB_SCRIPT);
		EntityManager connection = executor.getConnectionPool().createEntityManager();
		connection.getTransaction().begin();
		TypedQuery<Category> singleCategoryQuery = connection.createQuery(JPQL_ONE_CATEGORY, Category.class);
		Category category = singleCategoryQuery.getSingleResult();
		connection.getTransaction().commit();
		connection.close();

		assertNotNull(category);
		assertEquals(11, category.getId());
		assertEquals("food", category.getName());
		assertNotNull(category.getAccount());
		assertEquals("test", category.getAccount().getLogin());
		assertEquals(2, category.getExpenses().size());
	}

	@Test
	public void expense() {
		executor.executeSqlScript(JpaSqlScriptExecutor.INITIALISE_DB_SCRIPT);
		EntityManager connection = executor.getConnectionPool().createEntityManager();
		connection.getTransaction().begin();
		TypedQuery<Expense> singleExpenseQuery = connection.createQuery(JPQL_ONE_EXPENSE, Expense.class);
		Expense expense = singleExpenseQuery.getSingleResult();
		connection.getTransaction().commit();
		connection.close();

		assertNotNull(expense);
		assertEquals(103, expense.getId());
		assertEquals("20.07.15", expense.getReadableDayAsString());
		assertEquals("french fries", expense.getReason());
		assertEquals(3.55, expense.getAmount(), 0.01);
		assertNotNull(expense.getAccount());
		assertEquals("test", expense.getAccount().getLogin());
		assertNotNull(expense.getCategory());
		assertEquals("food", expense.getCategory().getName());
	}

	@Test
	public void addExpense() {
		executor.executeSqlScript(JpaSqlScriptExecutor.INITIALISE_DB_SCRIPT);
		EntityManager connection = executor.getConnectionPool().createEntityManager();
		connection.getTransaction().begin();
		Account account = connection.find(Account.class, 1l);
		Category category = connection.find(Category.class, 11l);
		Expense expense = new Expense();
		expense.setId(104);
		expense.setAmount(14.5d);
		expense.setReason("Meine Gründe");
		expense.setReadableDayAsString("27.02.15");
		account.add(expense);
		category.add(expense);
		connection.persist(expense);
		connection.getTransaction().commit();
		connection.close();

		executor.getConnectionPool().close();
	}
}
