package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.services.AccountDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;

public class LoginServletTest {

	private static final long ACCOUNT_ID = 123;
	private static final String LOGIN = "username";
	private static final String PASSWORD = "passwordOfUser";

	private Account account;
	private LoginServlet servlet;

	@Before
	public void init() {
		account = new Account();
		account.setId(ACCOUNT_ID);
		servlet = new LoginServlet();
		servlet.accountDAO = mock(AccountDAO.class);
	}

	@Test
	public void login_success() {
		when(servlet.accountDAO.readByLogin(LOGIN, PASSWORD)).thenReturn(account);

		final ServletReaction reaction = servlet.login(LOGIN, PASSWORD);

		assertNotNull(reaction);
		// correct account in session
		assertSame(account, reaction.getSessionAttributes().get("account"));
		// correct navigation
		assertEquals("listexpenses", reaction.getRedirect());
	}

	@Test
	public void login_failure() {
		when(servlet.accountDAO.readByLogin(LOGIN, PASSWORD)).thenReturn(account);

		final ServletReaction reaction = servlet.login("nologin", "wrongpassword");

		assertNotNull(reaction);
		// no account in session
		assertNull(reaction.getSessionAttributes().get("account"));
		// correct error in request
		assertEquals("unknown login or wrong password", reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("index.jsp", reaction.getForward());
	}
}
