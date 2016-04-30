package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Category;
import vu.de.npolke.myexpenses.services.CategoryDAO;
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
public class EditCategoryServletTest {
	private static final String NAME_OLD = "categoryName";
	private static final String NAME_NEW = "newCategoryName";

	private static final long ACCOUNT_ID = 123;
	private static final long ACCOUNT_ID_FOREIGN = 666;
	private static final long CATEGORY_ID = 456;
	private static final long CATEGORY_ID_NONEXISTING = 555;
	private static final long CATEGORY_ID_FOREIGN = 555;

	private EditCategoryServlet servlet;
	private Account account;
	private Category category;

	@Before
	public void init() {
		servlet = new EditCategoryServlet();
		servlet.categoryDAO = mock(CategoryDAO.class);
		account = new Account();
		account.setId(ACCOUNT_ID);
		category = new Category();
		category.setId(CATEGORY_ID);
		category.setAccountId(ACCOUNT_ID);
		category.setName(NAME_OLD);
	}

	@Test
	public void prepareEditCategory() {
		when(servlet.categoryDAO.read(CATEGORY_ID)).thenReturn(category);

		ServletReaction reaction = servlet.prepareEditCategory(account, String.valueOf(CATEGORY_ID));

		assertNotNull(reaction);
		// correct category in session
		assertEquals(category, reaction.getSessionAttributes().get("category"));
		// correct navigation
		assertEquals("editcategory.jsp", reaction.getRedirect());
	}

	@Test
	public void prepareEditCategory_nonExistingCategory() {
		when(servlet.categoryDAO.read(CATEGORY_ID)).thenReturn(category);

		ServletReaction reaction = servlet.prepareEditCategory(account, String.valueOf(CATEGORY_ID_NONEXISTING));

		assertNotNull(reaction);
		// correct error message
		assertEquals("You tried to edit a non existing category or a category that isn't yours!",
				reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("error.jsp", reaction.getForward());
	}

	@Test
	public void prepareEditCategory_foreignCategory() {
		Category foreignCategory = new Category();
		foreignCategory.setId(CATEGORY_ID_FOREIGN);
		foreignCategory.setAccountId(ACCOUNT_ID_FOREIGN);
		when(servlet.categoryDAO.read(CATEGORY_ID_FOREIGN)).thenReturn(foreignCategory);

		ServletReaction reaction = servlet.prepareEditCategory(account, String.valueOf(CATEGORY_ID_FOREIGN));

		assertNotNull(reaction);
		// correct error message
		assertEquals("You tried to edit a non existing category or a category that isn't yours!",
				reaction.getRequestAttributes().get("errorMessage"));
		// correct navigation
		assertEquals("error.jsp", reaction.getForward());
	}

	@Test
	public void editCategory() {
		ServletReaction reaction = servlet.editCategory(category, NAME_NEW);

		assertNotNull(reaction);
		// correct update
		assertEquals(NAME_NEW, category.getName());
		verify(servlet.categoryDAO).update(category);
		// correct navigation
		assertEquals("listcategories", reaction.getRedirect());
	}
}
