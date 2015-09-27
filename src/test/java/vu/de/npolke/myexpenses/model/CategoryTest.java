package vu.de.npolke.myexpenses.model;

import static org.junit.Assert.assertEquals;

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

	private static final int	ID			= 4;
	private static final String	NAME		= "food";
	private static final long	ACCOUNTID	= 1;

	private Category category;

	@Before
	public void setup() {
		category = new Category();
	}

	@Test
	public void normalValues() {
		category.setId(ID);
		category.setName(NAME);
		category.setAccountId(ACCOUNTID);

		assertEquals(ID, category.getId());
		assertEquals(NAME, category.getName());
		assertEquals(ACCOUNTID, category.getAccountId());

		assertEquals("Category: food", category.toString());
	}
}
