package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.services.AccountDAO;
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
public class RegisterServletTest {

	private static final String LOGIN = "username";
	private static final String PASSWORD = "passwordOfUser";
	private static final String OTHER_PASSWORD = "otherPassword";

	private Account account;
	private RegisterServlet servlet;

	@Before
	public void init() {
		servlet = new RegisterServlet();
		servlet.accountDAO = mock(AccountDAO.class);

		account = new Account();
	}

	@Test
	public void register() {
		when(servlet.accountDAO.create(LOGIN, PASSWORD)).thenReturn(account);

		final ServletReaction reaction = servlet.registerUser(LOGIN, PASSWORD, PASSWORD);

		assertNotNull(reaction);
		// correct account in session
		assertSame(account, reaction.getSessionAttributes().get("account"));
		// correct navigation
		assertEquals("listexpenses.jsp", reaction.getRedirect());
	}

	@Test
	public void register_differentPasswords() {
		when(servlet.accountDAO.create(LOGIN, PASSWORD)).thenReturn(account);

		final ServletReaction reaction = servlet.registerUser(LOGIN, PASSWORD, OTHER_PASSWORD);

		assertNotNull(reaction);
		// correct account in session
		assertNull(reaction.getSessionAttributes().get("account"));
		// correct navigation
		assertEquals("register.jsp?error=error.createaccount.passwordsnotequal", reaction.getRedirect());
	}

	@Test
	public void register_differentPasswordsAndLoginAlreadyExists() {
		when(servlet.accountDAO.create(LOGIN, PASSWORD)).thenReturn(null);

		final ServletReaction reaction = servlet.registerUser(LOGIN, PASSWORD, OTHER_PASSWORD);

		assertNotNull(reaction);
		// correct account in session
		assertNull(reaction.getSessionAttributes().get("account"));
		// correct navigation
		assertEquals("register.jsp?error=error.createaccount.passwordsnotequal", reaction.getRedirect());
	}

	@Test
	public void register_equalPasswordsButLoginAlreadyExists() {
		when(servlet.accountDAO.create(LOGIN, PASSWORD)).thenReturn(null);

		final ServletReaction reaction = servlet.registerUser(LOGIN, PASSWORD, PASSWORD);

		assertNotNull(reaction);
		// correct account in session
		assertNull(reaction.getSessionAttributes().get("account"));
		// correct navigation
		assertEquals("register.jsp?error=error.createaccount.logininuse", reaction.getRedirect());
	}
}
