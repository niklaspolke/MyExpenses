package vu.de.npolke.myexpenses.servlets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.services.CategoryDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;

public class AddCategoryServletTest {

	private AddCategoryServlet servlet;

	@Before
	public void init() {
		servlet = new AddCategoryServlet();
		servlet.categoryDAO = mock(CategoryDAO.class);
	}

	@Test
	public void forward() {
		ServletReaction reaction = servlet.forward();

		assertEquals("WEB-INF/addcategory.jsp", reaction.getForward());
	}

	@Test
	public void addCategory() {
		final Account account = new Account();
		account.setId(123);
		final String categoryName = "newCategory";

		ServletReaction reaction = servlet.addCategory(account, categoryName);

		verify(servlet.categoryDAO).create(categoryName, account.getId());
		assertNotNull(reaction);
		assertEquals("listcategories.jsp", reaction.getRedirect());
	}
}
