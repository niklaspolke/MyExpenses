package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.AccountDAO;
import vu.de.npolke.myexpenses.services.StatisticsDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;
import vu.de.npolke.myexpenses.util.ApplicationStatisticTypes;
import vu.de.npolke.myexpenses.util.ApplicationStatistics;

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
public class LoginServletTest {

	private static final long ACCOUNT_ID = 123;
	private static final String LOGIN = "username";
	private static final String PASSWORD = "passwordOfUser";
	private static final String COOKIE_KEY = "locale";
	private static final String COOKIE_VALUE = "de";
	private static final Cookie COOKIE = new Cookie("locale", COOKIE_VALUE);

	private Account account;
	private LoginServlet servlet;
	private List<Expense> topten;

	@Before
	public void init() {
		account = new Account();
		account.setId(ACCOUNT_ID);
		topten = new ArrayList<Expense>();
		topten.add(new Expense());
		topten.add(new Expense());
		servlet = new LoginServlet();
		servlet.accountDAO = mock(AccountDAO.class);
		servlet.statisticsDAO = mock(StatisticsDAO.class);
		servlet.statistics = spy(ApplicationStatistics.getSingleton());
	}

	@Test
	public void doGet_alreadyLoggedIn() throws Exception {
		final ServletReaction reaction = servlet.doGet(null, null, null, new Account());

		assertNotNull(reaction);
		assertEquals("listexpenses.jsp?back=true", reaction.getRedirect());
	}

	@Test
	public void initialiseLocale_noCookie() {
		final ServletReaction reaction = servlet.initialiseLocale(null);

		assertNotNull(reaction);
		assertNull(reaction.getSessionAttributes().get(COOKIE_KEY));
		assertEquals("WEB-INF/login.jsp", reaction.getForward());
	}

	@Test
	public void initialiseLocale_withCookie() {
		final ServletReaction reaction = servlet.initialiseLocale(COOKIE);

		assertNotNull(reaction);
		assertEquals(COOKIE_VALUE, reaction.getSessionAttributes().get(COOKIE_KEY));
		assertEquals("WEB-INF/login.jsp", reaction.getForward());
	}

	@Test
	public void login_success() {
		when(servlet.accountDAO.readByLogin(LOGIN, PASSWORD)).thenReturn(account);
		when(servlet.statisticsDAO.readTopTenByAccountId(ACCOUNT_ID)).thenReturn(topten);

		final ServletReaction reaction = servlet.login(LOGIN, PASSWORD, null, COOKIE);

		assertNotNull(reaction);
		// correct account in session
		assertSame(account, reaction.getSessionAttributes().get("account"));
		// correct topten in session
		assertSame(topten, reaction.getSessionAttributes().get("topten"));
		assertEquals(COOKIE_VALUE, reaction.getSessionAttributes().get(COOKIE_KEY));
		// correct navigation
		assertEquals("listexpenses.jsp", reaction.getRedirect());
		verify(servlet.statistics).increaseCounterForStatisticType(ApplicationStatisticTypes.LOGINS);
	}

	@Test
	public void login_success_withSimpleRedirect() {
		when(servlet.accountDAO.readByLogin(LOGIN, PASSWORD)).thenReturn(account);
		when(servlet.statisticsDAO.readTopTenByAccountId(ACCOUNT_ID)).thenReturn(topten);

		final ServletReaction reaction = servlet.login(LOGIN, PASSWORD, "addExpense", COOKIE);

		assertNotNull(reaction);
		assertTrue(reaction.getSessionAttributes().containsKey("redirectAfterLogin"));
		assertNull(reaction.getSessionAttributes().get("redirectAfterLogin"));
		assertEquals(COOKIE_VALUE, reaction.getSessionAttributes().get(COOKIE_KEY));
		// correct navigation
		assertEquals("addExpense", reaction.getRedirect());
		verify(servlet.statistics).increaseCounterForStatisticType(ApplicationStatisticTypes.LOGINS);
	}

	@Test
	public void login_success_withComplexRedirect() {
		when(servlet.accountDAO.readByLogin(LOGIN, PASSWORD)).thenReturn(account);
		when(servlet.statisticsDAO.readTopTenByAccountId(ACCOUNT_ID)).thenReturn(topten);

		final ServletReaction reaction = servlet.login(LOGIN, PASSWORD, "addExpense?id=445&test=true", COOKIE);

		assertNotNull(reaction);
		assertTrue(reaction.getSessionAttributes().containsKey("redirectAfterLogin"));
		assertNull(reaction.getSessionAttributes().get("redirectAfterLogin"));
		assertEquals(COOKIE_VALUE, reaction.getSessionAttributes().get(COOKIE_KEY));
		// correct navigation
		assertEquals("addExpense?id=445&test=true", reaction.getRedirect());
		verify(servlet.statistics).increaseCounterForStatisticType(ApplicationStatisticTypes.LOGINS);
	}

	@Test
	public void login_failure() {
		when(servlet.accountDAO.readByLogin(LOGIN, PASSWORD)).thenReturn(account);

		final ServletReaction reaction = servlet.login("nologin", "wrongpassword", null, COOKIE);

		assertNotNull(reaction);
		// no account in session
		assertNull(reaction.getSessionAttributes().get("account"));
		assertEquals(COOKIE_VALUE, reaction.getSessionAttributes().get(COOKIE_KEY));
		// correct navigation
		assertEquals("login.jsp?error=error.login.notfound", reaction.getRedirect());
		verify(servlet.statistics, never()).increaseCounterForStatisticType(ApplicationStatisticTypes.LOGINS);
	}
}
