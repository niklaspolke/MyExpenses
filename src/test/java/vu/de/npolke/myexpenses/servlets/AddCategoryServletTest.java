package vu.de.npolke.myexpenses.servlets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
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
