package vu.de.npolke.myexpenses.servlets.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

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
public class CategoryComparatorTest {

	private static Category createCategory(final String name) {
		Category c = new Category();
		c.setName(name);
		return c;
	}

	@Test
	public void sortByName() {
		Category c1 = createCategory("sport");
		Category c2 = createCategory("food");
		Category c3 = createCategory("health");
		Category c4 = createCategory("car");

		List<Category> categories = new ArrayList<Category>();
		categories.add(c1);
		categories.add(c2);
		categories.add(c3);
		categories.add(c4);

		categories.sort(new CategoryComparator<>());

		assertEquals(c4, categories.get(0));
		assertEquals(c2, categories.get(1));
		assertEquals(c3, categories.get(2));
		assertEquals(c1, categories.get(3));
	}
}
