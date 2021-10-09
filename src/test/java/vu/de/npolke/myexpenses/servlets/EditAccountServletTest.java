package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.services.AccountDAO;
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
public class EditAccountServletTest {

	private static final long ACCOUNT_ID = 1;

	private static final String LOGIN_OLD = "user";
	private static final String LOGIN_NEW = "new user";
	private static final String PASSWORD_OLD = "password";
	private static final String PASSWORD_NEW = "new password";
	private static final String BUDGET_OLD = "";
	private static final String BUDGET_NEW = "400";

	private EditAccountServlet servlet;
	private Account account;

	@Before
	public void init() {
		servlet = new EditAccountServlet();
		servlet.accountDAO = mock(AccountDAO.class);
		servlet.expenseDAO = mock(ExpenseDAO.class);
		account = new Account();
		account.setId(ACCOUNT_ID);
		account.setLogin(LOGIN_OLD);
		account.setPassword(HashUtil.toMD5(PASSWORD_OLD));
	}

	@Test
	public void prepareEditAccount() {
		final long AMOUNT_OF_EXPENSES = 14;
		when(servlet.expenseDAO.readAmountOfStandardExpenses(ACCOUNT_ID)).thenReturn(AMOUNT_OF_EXPENSES);

		ServletReaction reaction = servlet.prepareEditAccount(account);

		assertNotNull(reaction);
		// correct navigation
		assertEquals("WEB-INF/editaccount.jsp", reaction.getForward());
		// correct amount of expenses
		assertEquals(AMOUNT_OF_EXPENSES, reaction.getRequestAttributes().get("amountOfExpenses"));
	}

	@Test
	public void editAccount_Password() {
		Account accountToUpdate = account.clone();
		accountToUpdate.setPassword(HashUtil.toMD5(PASSWORD_NEW));
		when(servlet.accountDAO.update(eq(accountToUpdate))).thenReturn(true);

		ServletReaction reaction = servlet.editAccount(account, PASSWORD_OLD, PASSWORD_NEW, PASSWORD_NEW, LOGIN_OLD,
				BUDGET_OLD);

		assertNotNull(reaction);
		assertEquals(LOGIN_OLD, account.getLogin());
		assertEquals(HashUtil.toMD5(PASSWORD_NEW), account.getPassword());
		// correct persisting
		verify(servlet.accountDAO).update(eq(accountToUpdate));
		// correct navigation
		assertEquals("listexpenses.jsp", reaction.getRedirect());
	}

	@Test
	public void editAccount_Login() {
		Account accountToUpdate = account.clone();
		accountToUpdate.setLogin(LOGIN_NEW);
		when(servlet.accountDAO.update(eq(accountToUpdate))).thenReturn(true);

		ServletReaction reaction = servlet.editAccount(account, PASSWORD_OLD, "", "", LOGIN_NEW, BUDGET_OLD);

		assertNotNull(reaction);
		assertEquals(LOGIN_NEW, account.getLogin());
		assertEquals(HashUtil.toMD5(PASSWORD_OLD), account.getPassword());
		// correct persisting
		verify(servlet.accountDAO).update(eq(accountToUpdate));
		// correct navigation
		assertEquals("listexpenses.jsp", reaction.getRedirect());
	}

	@Test
	public void editAccount_Budget() {
		Account accountToUpdate = account.clone();
		accountToUpdate.setBudget(Double.parseDouble(BUDGET_NEW));
		when(servlet.accountDAO.update(eq(accountToUpdate))).thenReturn(true);

		ServletReaction reaction = servlet.editAccount(account, "", "", "", LOGIN_NEW, BUDGET_NEW);

		assertNotNull(reaction);
		assertEquals(BUDGET_NEW, "" + account.getBudget().intValue());
		// correct persisting
		verify(servlet.accountDAO).update(eq(accountToUpdate));
		// correct navigation
		assertEquals("editaccount.jsp", reaction.getRedirect());
	}

	@Test
	public void editAccount_LoginAndPassword() {
		Account accountToUpdate = account.clone();
		accountToUpdate.setLogin(LOGIN_NEW);
		accountToUpdate.setPassword(HashUtil.toMD5(PASSWORD_NEW));
		when(servlet.accountDAO.update(eq(accountToUpdate))).thenReturn(true);

		ServletReaction reaction = servlet.editAccount(account, PASSWORD_OLD, PASSWORD_NEW, PASSWORD_NEW, LOGIN_NEW,
				BUDGET_OLD);

		assertNotNull(reaction);
		assertEquals(LOGIN_NEW, account.getLogin());
		assertEquals(HashUtil.toMD5(PASSWORD_NEW), account.getPassword());
		// correct persisting
		verify(servlet.accountDAO).update(eq(accountToUpdate));
		// correct navigation
		assertEquals("listexpenses.jsp", reaction.getRedirect());
	}

	@Test
	public void editAccount_wrongPassword() {
		ServletReaction reaction = servlet.editAccount(account, "notOldPassword", PASSWORD_NEW, PASSWORD_NEW, LOGIN_OLD,
				BUDGET_OLD);

		assertNotNull(reaction);
		assertEquals(LOGIN_OLD, account.getLogin());
		assertEquals(HashUtil.toMD5(PASSWORD_OLD), account.getPassword());
		// correct persisting
		verify(servlet.accountDAO, never()).update(any(Account.class));
		// correct error message
		assertEquals("error.editaccount.wrongpassword", reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("WEB-INF/editaccount.jsp", reaction.getForward());
	}

	@Test
	public void editAccount_newPasswordsNotEqual() {
		ServletReaction reaction = servlet.editAccount(account, PASSWORD_OLD, PASSWORD_NEW, "notNewPassword", LOGIN_NEW,
				BUDGET_OLD);

		assertNotNull(reaction);
		assertEquals(LOGIN_OLD, account.getLogin());
		assertEquals(HashUtil.toMD5(PASSWORD_OLD), account.getPassword());
		// correct persisting
		verify(servlet.accountDAO, never()).update(any(Account.class));
		// correct error message
		assertEquals("error.editaccount.passwordsnotequal", reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("WEB-INF/editaccount.jsp", reaction.getForward());
	}

	@Test
	public void editAccount_emptyLogin() {
		ServletReaction reaction = servlet.editAccount(account, PASSWORD_OLD, PASSWORD_NEW, PASSWORD_NEW, "",
				BUDGET_OLD);

		assertNotNull(reaction);
		assertEquals(LOGIN_OLD, account.getLogin());
		assertEquals(HashUtil.toMD5(PASSWORD_OLD), account.getPassword());
		// correct persisting
		verify(servlet.accountDAO, never()).update(any(Account.class));
		// correct error message
		assertEquals("error.editaccount.logintooshort", reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("WEB-INF/editaccount.jsp", reaction.getForward());
	}

	@Test
	public void editAccount_LoginAlreadyInUse() {
		Account accountToUpdate = account.clone();
		accountToUpdate.setLogin(LOGIN_NEW);
		when(servlet.accountDAO.update(eq(accountToUpdate))).thenReturn(false);

		ServletReaction reaction = servlet.editAccount(account, PASSWORD_OLD, "", "", LOGIN_NEW, BUDGET_OLD);

		assertNotNull(reaction);
		assertEquals(LOGIN_OLD, account.getLogin());
		assertEquals(HashUtil.toMD5(PASSWORD_OLD), account.getPassword());
		// correct persisting
		verify(servlet.accountDAO).update(eq(accountToUpdate));
		// correct error message
		assertEquals("error.editaccount.logininuse", reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("WEB-INF/editaccount.jsp", reaction.getForward());
	}
}
