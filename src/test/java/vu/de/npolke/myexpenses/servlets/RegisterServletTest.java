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
		assertEquals("register.jsp?error=password1+wasn%27t+equal+to+password2", reaction.getRedirect());
	}

	@Test
	public void register_differentPasswordsAndLoginAlreadyExists() {
		when(servlet.accountDAO.create(LOGIN, PASSWORD)).thenReturn(null);

		final ServletReaction reaction = servlet.registerUser(LOGIN, PASSWORD, OTHER_PASSWORD);

		assertNotNull(reaction);
		// correct account in session
		assertNull(reaction.getSessionAttributes().get("account"));
		// correct navigation
		assertEquals("register.jsp?error=password1+wasn%27t+equal+to+password2", reaction.getRedirect());
	}

	@Test
	public void register_equalPasswordsButLoginAlreadyExists() {
		when(servlet.accountDAO.create(LOGIN, PASSWORD)).thenReturn(null);

		final ServletReaction reaction = servlet.registerUser(LOGIN, PASSWORD, PASSWORD);

		assertNotNull(reaction);
		// correct account in session
		assertNull(reaction.getSessionAttributes().get("account"));
		// correct navigation
		assertEquals("register.jsp?error=user+%22" + LOGIN + "%22+already+exists", reaction.getRedirect());
	}
}
