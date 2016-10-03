package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
public class DeleteCategoryServletTest {

	private DeleteCategoryServlet servlet;

	private Account account;
	private static final long ACCOUNT_ID = 123;

	@Before
	public void init() {
		servlet = new DeleteCategoryServlet();
		servlet.categoryDAO = mock(CategoryDAO.class);

		account = new Account();
		account.setId(ACCOUNT_ID);
	}

	@Test
	public void deleteCategory() {
		long categoryId = 22;
		Category category = new Category();
		category.setId(categoryId);
		category.setAccountId(ACCOUNT_ID);
		when(servlet.categoryDAO.read(ACCOUNT_ID, categoryId)).thenReturn(category);
		when(servlet.categoryDAO.deleteById(categoryId)).thenReturn(true);

		ServletReaction reaction = servlet.deleteCategory(account, Long.toString(categoryId), "yes");

		assertNotNull(reaction);
		// correct deletion
		verify(servlet.categoryDAO).deleteById(categoryId);
		// correct navigation
		assertEquals("listcategories.jsp", reaction.getRedirect());
	}

	@Test
	public void deleteCategory_notAllowedWithExpenses() {
		long categoryId = 22;
		Category category = new Category();
		category.setId(categoryId);
		category.setAccountId(ACCOUNT_ID);
		when(servlet.categoryDAO.read(ACCOUNT_ID, categoryId)).thenReturn(category);
		when(servlet.categoryDAO.deleteById(categoryId)).thenReturn(false);

		ServletReaction reaction = servlet.deleteCategory(account, Long.toString(categoryId), "yes");

		assertNotNull(reaction);
		// correct deletion
		verify(servlet.categoryDAO).deleteById(categoryId);
		// correct navigation
		assertEquals("listcategories.jsp?error=error.deletecategory.stillinuse", reaction.getRedirect());
	}

	@Test
	public void deleteCategory_notConfirmed() {
		long categoryId = 22;
		Category category = new Category();
		category.setId(categoryId);
		category.setAccountId(ACCOUNT_ID);
		when(servlet.categoryDAO.read(ACCOUNT_ID, categoryId)).thenReturn(category);

		ServletReaction reaction = servlet.deleteCategory(account, Long.toString(categoryId), null);

		assertNotNull(reaction);
		// no deletion
		verify(servlet.categoryDAO, never()).deleteById(anyLong());
		// correct navigation
		assertEquals("listcategories.jsp", reaction.getRedirect());
	}

	@Test
	public void deleteCategory_nonExisting() {
		long categoryId = 22;
		Category category = new Category();
		category.setId(categoryId);
		category.setAccountId(ACCOUNT_ID);
		when(servlet.categoryDAO.read(ACCOUNT_ID, categoryId)).thenReturn(category);

		ServletReaction reaction = servlet.deleteCategory(account, "44", "yes");

		assertNotNull(reaction);
		// no deletion
		verify(servlet.categoryDAO, never()).deleteById(anyLong());
		// correct navigation
		assertEquals("error.jsp", reaction.getForward());
		assertEquals(1, reaction.getRequestAttributes().size());
		assertEquals("error.deletecategory.wrongid", reaction.getRequestAttributes().get("errorMessage"));
	}

	@Test
	public void deleteCategory_foreign() {
		long categoryId = 22;
		Category category = new Category();
		category.setId(categoryId);
		category.setAccountId(666);
		when(servlet.categoryDAO.read(ACCOUNT_ID, categoryId)).thenReturn(category);

		ServletReaction reaction = servlet.deleteCategory(account, Long.toString(categoryId), "yes");

		assertNotNull(reaction);
		// no deletion
		verify(servlet.categoryDAO, never()).deleteById(anyLong());
		// correct navigation
		assertEquals("error.jsp", reaction.getForward());
		assertEquals(1, reaction.getRequestAttributes().size());
		assertEquals("error.deletecategory.wrongid", reaction.getRequestAttributes().get("errorMessage"));
	}
}
