package vu.de.npolke.myexpenses.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Category;

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
public class CategoryDAOTest extends AbstractDAOTest {

	private static CategoryDAO categoryDAO;

	private static long testCounter = 0;

	public CategoryDAOTest() {
		super("CategoryDAOTest" + ++testCounter);
	}

	@BeforeClass
	public static void initialise() {
		categoryDAO = (CategoryDAO) DAOFactory.getDAO(Category.class);
	}

	@Test
	public void read() {
		Category category = categoryDAO.read(11);

		assertNotNull(category);
		assertEquals(11, category.getId());
		assertEquals("food", category.getName());
	}

	@Test
	public void readNotExisting() {
		Category category = categoryDAO.read(99);

		assertNull(category);
	}

	@Test
	public void create() {
		Category category = categoryDAO.create("health", 1);

		assertNotNull(category);
		assertTrue(category.getId() > 0);
		assertEquals("health", category.getName());
	}

	@Test
	public void update() {
		Category category = categoryDAO.read(11);
		category.setName("junk food");

		boolean success = categoryDAO.update(category);

		assertTrue(success);

		category = categoryDAO.read(category.getId());
		assertEquals(11, category.getId());
		assertEquals("junk food", category.getName());
	}

	@Test
	public void updateNotExisting() {
		Category category = categoryDAO.read(11);
		category.setId(99);
		category.setName("junk food");

		boolean success = categoryDAO.update(category);

		assertFalse(success);

		category = categoryDAO.read(99);
		assertNull(category);
	}

	@Test
	public void readByAccountId() {
		List<Category> categories = categoryDAO.readByAccountId(1);

		assertNotNull(categories);
		assertEquals(4, categories.size());
		assertEquals("food", categories.get(0).getName());
		assertEquals("income", categories.get(1).getName());
		assertEquals("luxury", categories.get(2).getName());
		assertEquals("sports", categories.get(3).getName());
	}

	@Test
	public void delete() {
		boolean success = categoryDAO.deleteById(13);
		Category category = categoryDAO.read(13);

		assertTrue(success);
		assertNull(category);
	}

	@Test
	public void deleteWithExpenses() {
		boolean success = categoryDAO.deleteById(11);
		Category category = categoryDAO.read(11);

		assertFalse(success);
		assertNotNull(category);
	}

	@Test
	public void deleteNotExisting() {
		boolean success = categoryDAO.deleteById(99);

		assertFalse(success);
	}
}
