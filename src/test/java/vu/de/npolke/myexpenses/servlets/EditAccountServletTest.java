package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.services.AccountDAO;
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
public class EditAccountServletTest {

	private static final String PASSWORD_OLD = "password";
	private static final String PASSWORD_NEW = "new password";

	private EditAccountServlet servlet;
	private Account account;

	@Before
	public void init() {
		servlet = new EditAccountServlet();
		servlet.accountDAO = mock(AccountDAO.class);
		account = new Account();
		account.setPassword(HashUtil.toMD5(PASSWORD_OLD));
	}

	@Test
	public void prepareEditAccount() {
		ServletReaction reaction = servlet.prepareEditAccount(account);

		assertNotNull(reaction);
		// correct session attribute: account to edit
		Object accountObject = reaction.getSessionAttributes().get("account");
		assertTrue(accountObject instanceof Account);
		Account accountInSession = (Account) accountObject;
		assertEquals(account, accountInSession);
		// correct navigation
		assertEquals("editaccount.jsp", reaction.getRedirect());
	}

	@Test
	public void editAccount() {
		ServletReaction reaction = servlet.editAccount(account, PASSWORD_OLD, PASSWORD_NEW, PASSWORD_NEW);

		assertNotNull(reaction);
		// correct new account password
		assertEquals(HashUtil.toMD5(PASSWORD_NEW), account.getPassword());
		// correct persisting
		verify(servlet.accountDAO).update(account);
		// correct navigation
		assertEquals("listexpenses", reaction.getRedirect());
	}

	@Test
	public void editAccount_wrongPassword() {
		ServletReaction reaction = servlet.editAccount(account, "notOldPassword", PASSWORD_NEW, PASSWORD_NEW);

		assertNotNull(reaction);
		// correct persisting
		verify(servlet.accountDAO, never()).update(account);
		// correct error message
		assertEquals("old password wasn't correct", reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("editaccount.jsp", reaction.getForward());
	}

	@Test
	public void editAccount_newPasswordsNotEqual() {
		ServletReaction reaction = servlet.editAccount(account, PASSWORD_OLD, PASSWORD_NEW, "notNewPassword");

		assertNotNull(reaction);
		// correct persisting
		verify(servlet.accountDAO, never()).update(account);
		// correct error message
		assertEquals("new password 1 wasn't equal to new password 2", reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("editaccount.jsp", reaction.getForward());
	}
}
