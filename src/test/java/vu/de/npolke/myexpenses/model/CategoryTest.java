package vu.de.npolke.myexpenses.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

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
public class CategoryTest {

	private Category category;

	@Before
	public void setup() {
		category = new Category();
	}

	@Test
	public void toString_NormalValues() {
		category.setId(4);
		category.setName("Food");
		category.expenses = new HashSet<Expense>();

		assertEquals("Category: #4 - <Food>", category.toString());
		assertNotNull(category.getExpenses());
	}

	@Test
	public void toString_NullValues() {
		assertEquals("Category: # - <>", category.toString());
		assertNull(category.getExpenses());
	}
}
