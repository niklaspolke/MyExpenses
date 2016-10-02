package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Category;
import vu.de.npolke.myexpenses.services.CategoryDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;

public class ListCategoriesServletTest {

	private static final long ACCOUNT_ID = 123;
	private static final Category CATEGORY_1 = new Category();
	private static final Category CATEGORY_2 = new Category();

	private Account account;
	private List<Category> categories;

	private ListCategoriesServlet servlet;

	@Before
	public void init() {
		servlet = new ListCategoriesServlet();
		servlet.categoryDAO = mock(CategoryDAO.class);
		account = new Account();
		account.setId(ACCOUNT_ID);
		categories = new ArrayList<Category>();
		categories.add(CATEGORY_1);
		categories.add(CATEGORY_2);
	}

	@Test
	public void prepareListCategories() {
		when(servlet.categoryDAO.readByAccountId(ACCOUNT_ID)).thenReturn(categories);

		ServletReaction reaction = servlet.prepareListCategories(account);

		assertNotNull(reaction);
		// correct categories in session
		assertEquals(categories, reaction.getRequestAttributes().get("categories"));
		// correct navigation
		assertEquals("WEB-INF/listcategories.jsp", reaction.getForward());
	}
}
