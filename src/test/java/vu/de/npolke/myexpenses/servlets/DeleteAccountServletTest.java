package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.services.AccountDAO;
import vu.de.npolke.myexpenses.services.CategoryDAO;
import vu.de.npolke.myexpenses.services.ExpenseDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;
import vu.de.npolke.myexpenses.util.HashUtil;

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
public class DeleteAccountServletTest {

	private static final long ACCOUNT_ID = 1;

	private static final String PASSWORD = "password";

	private DeleteAccountServlet servlet;
	private Account account;

	@Before
	public void init() {
		servlet = new DeleteAccountServlet();
		servlet.accountDAO = mock(AccountDAO.class);
		servlet.categoryDAO = mock(CategoryDAO.class);
		servlet.expenseDAO = mock(ExpenseDAO.class);
		account = new Account();
		account.setId(ACCOUNT_ID);
		account.setPassword(HashUtil.toMD5(PASSWORD));
	}

	@Test
	public void deleteAccount() {
		when(servlet.expenseDAO.deleteByAccountId(eq(ACCOUNT_ID))).thenReturn(10l);
		when(servlet.categoryDAO.deleteByAccountId(eq(ACCOUNT_ID))).thenReturn(4l);
		when(servlet.accountDAO.deleteByAccountId(eq(ACCOUNT_ID))).thenReturn(1l);

		ServletReaction reaction = servlet.deleteAccount(account, PASSWORD);

		assertNotNull(reaction);
		// correct persisting
		verify(servlet.expenseDAO).deleteByAccountId(eq(ACCOUNT_ID));
		verify(servlet.categoryDAO).deleteByAccountId(eq(ACCOUNT_ID));
		verify(servlet.accountDAO).deleteByAccountId(eq(ACCOUNT_ID));
		// correct navigation
		assertEquals("login.jsp", reaction.getRedirect());
	}

	@Test
	public void deleteAccount_wrongPassword() {
		when(servlet.expenseDAO.deleteByAccountId(eq(ACCOUNT_ID))).thenReturn(10l);
		when(servlet.categoryDAO.deleteByAccountId(eq(ACCOUNT_ID))).thenReturn(4l);
		when(servlet.accountDAO.deleteByAccountId(eq(ACCOUNT_ID))).thenReturn(1l);

		ServletReaction reaction = servlet.deleteAccount(account, "WRONG password WRONG");

		assertNotNull(reaction);
		// no persisting
		verify(servlet.expenseDAO, never()).deleteByAccountId(any(Long.class));
		verify(servlet.categoryDAO, never()).deleteByAccountId(any(Long.class));
		verify(servlet.accountDAO, never()).deleteByAccountId(any(Long.class));
		// correct error message
		assertEquals("error.deleteaccount.wrongpassword", reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("WEB-INF/editaccount.jsp", reaction.getForward());
	}

	@Test
	public void deleteAccount_unknownError() {
		when(servlet.expenseDAO.deleteByAccountId(eq(ACCOUNT_ID))).thenReturn(10l);
		when(servlet.categoryDAO.deleteByAccountId(eq(ACCOUNT_ID))).thenReturn(4l);
		when(servlet.accountDAO.deleteByAccountId(eq(ACCOUNT_ID))).thenReturn(0l);

		ServletReaction reaction = servlet.deleteAccount(account, PASSWORD);

		assertNotNull(reaction);
		// no persisting
		verify(servlet.accountDAO).deleteByAccountId(eq(ACCOUNT_ID));
		// correct error message
		assertEquals("error.deleteaccount.unknownerror", reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("WEB-INF/editaccount.jsp", reaction.getForward());
	}
}
