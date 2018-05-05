package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;

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
public class SearchExpensesServletTest {

	private static final String SEARCHEXPENSES_JSP = "WEB-INF/searchexpenses.jsp";
	private static final long ACCOUNT_ID = 123;

	private Account account;
	private SearchExpensesServlet servlet;

	@Before
	public void init() {
		account = new Account();
		account.setId(ACCOUNT_ID);
		servlet = new SearchExpensesServlet();
	}

	@Test
	public void prepareListExpenses() {
		ServletReaction reaction = servlet.prepareListExpenses();

		assertNotNull(reaction);
		assertSame(SEARCHEXPENSES_JSP, reaction.getForward());
	}

	@Test
	public void searchExpenses() {
		ServletReaction reaction = servlet.searchExpenses(account, "test");

		assertNotNull(reaction);
		assertEquals("listexpenses.jsp?search=test", reaction.getRedirect());
	}

	@Test
	public void searchExpenses_WithUppercase() {
		ServletReaction reaction = servlet.searchExpenses(account, "TESt");

		assertNotNull(reaction);
		assertEquals("listexpenses.jsp?search=test", reaction.getRedirect());
	}

	@Test
	public void searchExpenses_tooShort() {
		ServletReaction reaction = servlet.searchExpenses(account, "te");

		assertNotNull(reaction);
		assertEquals(SEARCHEXPENSES_JSP, reaction.getForward());
	}

	@Test
	public void searchExpenses_withEmptySpacestooShort() {
		ServletReaction reaction = servlet.searchExpenses(account, " te ");

		assertNotNull(reaction);
		assertEquals(SEARCHEXPENSES_JSP, reaction.getForward());
	}
}
