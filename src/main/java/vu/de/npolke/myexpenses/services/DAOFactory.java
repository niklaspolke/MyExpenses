package vu.de.npolke.myexpenses.services;

import java.util.HashMap;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Category;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.connections.ConnectionStrategy;
import vu.de.npolke.myexpenses.services.connections.JdbcConnectionStrategy;
import vu.de.npolke.myexpenses.servlets.util.StatisticsPair;

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
public class DAOFactory {

	private static HashMap<Class<?>, AbstractConnectionDAO> daoRegistry = new HashMap<Class<?>, AbstractConnectionDAO>();

	static {
		//@formatter:off
		  SequenceDAO   sequenceDAO = new   SequenceDAO();
		   AccountDAO    accountDAO = new    AccountDAO(sequenceDAO);
		  CategoryDAO   categoryDAO = new   CategoryDAO(sequenceDAO);
		   ExpenseDAO    expenseDAO = new    ExpenseDAO(sequenceDAO);
		StatisticsDAO statisticsDAO = new StatisticsDAO();

		categoryDAO.setExpenseDAO(expenseDAO);
		expenseDAO.setCategoryDAO(categoryDAO);

		daoRegistry.put(          Long.class, sequenceDAO);
		daoRegistry.put(       Account.class, accountDAO);
		daoRegistry.put(      Category.class, categoryDAO);
		daoRegistry.put(       Expense.class, expenseDAO);
		daoRegistry.put(StatisticsPair.class, statisticsDAO);
		//@formatter:off

		changeConnectionStrategy(new JdbcConnectionStrategy());
	}

	public static AbstractConnectionDAO getDAO(final Class<?> entity) {
		return daoRegistry.get(entity);
	}

	public static void changeConnectionStrategy(final ConnectionStrategy connectionStrategy) {
		for (AbstractConnectionDAO dao : daoRegistry.values()) {
			dao.setConnectionStrategy(connectionStrategy);
		}
	}
}
