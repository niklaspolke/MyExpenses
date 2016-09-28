package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.AccountDAO;
import vu.de.npolke.myexpenses.services.StatisticsDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;

public class LoginServletTest {

	private static final long ACCOUNT_ID = 123;
	private static final String LOGIN = "username";
	private static final String PASSWORD = "passwordOfUser";

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
	}

	@Test
	public void login_success() {
		when(servlet.accountDAO.readByLogin(LOGIN, PASSWORD)).thenReturn(account);
		when(servlet.statisticsDAO.readTopTenByAccountId(ACCOUNT_ID)).thenReturn(topten);

		final ServletReaction reaction = servlet.login(LOGIN, PASSWORD, null);

		assertNotNull(reaction);
		// correct account in session
		assertSame(account, reaction.getSessionAttributes().get("account"));
		// correct topten in session
		assertSame(topten, reaction.getSessionAttributes().get("topten"));
		// correct navigation
		assertEquals("listexpenses", reaction.getRedirect());
	}

	@Test
	public void login_success_withSimpleRedirect() {
		when(servlet.accountDAO.readByLogin(LOGIN, PASSWORD)).thenReturn(account);
		when(servlet.statisticsDAO.readTopTenByAccountId(ACCOUNT_ID)).thenReturn(topten);

		final ServletReaction reaction = servlet.login(LOGIN, PASSWORD, "addExpense");

		assertNotNull(reaction);
		assertTrue(reaction.getSessionAttributes().containsKey("redirectAfterLogin"));
		assertNull(reaction.getSessionAttributes().get("redirectAfterLogin"));
		// correct navigation
		assertEquals("addExpense", reaction.getRedirect());
	}

	@Test
	public void login_success_withComplexRedirect() {
		when(servlet.accountDAO.readByLogin(LOGIN, PASSWORD)).thenReturn(account);
		when(servlet.statisticsDAO.readTopTenByAccountId(ACCOUNT_ID)).thenReturn(topten);

		final ServletReaction reaction = servlet.login(LOGIN, PASSWORD, "addExpense?id=445&test=true");

		assertNotNull(reaction);
		assertTrue(reaction.getSessionAttributes().containsKey("redirectAfterLogin"));
		assertNull(reaction.getSessionAttributes().get("redirectAfterLogin"));
		// correct navigation
		assertEquals("addExpense?id=445&test=true", reaction.getRedirect());
	}

	@Test
	public void login_failure() {
		when(servlet.accountDAO.readByLogin(LOGIN, PASSWORD)).thenReturn(account);

		final ServletReaction reaction = servlet.login("nologin", "wrongpassword", null);

		assertNotNull(reaction);
		// no account in session
		assertNull(reaction.getSessionAttributes().get("account"));
		// correct error in request
		assertEquals("unknown login or wrong password", reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("index.jsp", reaction.getForward());
	}
}
