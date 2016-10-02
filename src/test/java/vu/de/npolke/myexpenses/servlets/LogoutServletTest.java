package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.servlets.util.ServletReaction;

public class LogoutServletTest {

	private LogoutServlet servlet;

	@Before
	public void init() {
		servlet = new LogoutServlet();
	}

	@Test
	public void logout() {
		final ServletReaction reaction = servlet.logout();

		assertNotNull(reaction);
		// correct navigation
		assertEquals("login.jsp?info=You%27ve+successfully+logged+out.", reaction.getRedirect());
	}
}
