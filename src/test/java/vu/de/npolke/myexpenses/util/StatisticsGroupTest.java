package vu.de.npolke.myexpenses.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

/**
 * Copyright 2015 Niklas Polke
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * @author Niklas Polke
 */
public class StatisticsGroupTest {

	private static final double DELTA = 0.001;

	private static final Month MONTH = Month.create(2017, 3);
	private static final long CATEGORY_ID = 14;
	private static final String CATEGORY_NAME = "category";
	private static final double AMOUNT = 1.1;
	private static final boolean NONSENSE_DEFAULT = false;

	private StatisticsGroup statGroup = new StatisticsGroup();

	@Test
	public void addOne() {
		StatisticsElement pair = StatisticsElement.create(MONTH, CATEGORY_ID, CATEGORY_NAME, AMOUNT, NONSENSE_DEFAULT,
				NONSENSE_DEFAULT);

		statGroup.add(pair);
		List<StatisticsElement> result = statGroup.filter(MONTH, false);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(pair, result.get(0));
	}

	@Test
	public void addTwo_filterOne() {
		StatisticsElement pair = StatisticsElement.create(MONTH, CATEGORY_ID, CATEGORY_NAME, AMOUNT, NONSENSE_DEFAULT,
				NONSENSE_DEFAULT);
		StatisticsElement pair2 = StatisticsElement.create(MONTH.next(), CATEGORY_ID + 1, "other category", AMOUNT,
				NONSENSE_DEFAULT, NONSENSE_DEFAULT);

		statGroup.add(pair);
		statGroup.add(pair2);
		List<StatisticsElement> result = statGroup.filter(MONTH, false);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(pair, result.get(0));
	}

	@Test
	public void addTwoEqual_addValues_filterOne() {
		StatisticsElement pair = StatisticsElement.create(MONTH, CATEGORY_ID, CATEGORY_NAME, AMOUNT, NONSENSE_DEFAULT,
				NONSENSE_DEFAULT);
		StatisticsElement pair2 = StatisticsElement.create(MONTH, CATEGORY_ID, CATEGORY_NAME, AMOUNT, NONSENSE_DEFAULT,
				NONSENSE_DEFAULT);

		statGroup.add(pair);
		statGroup.add(pair2);
		List<StatisticsElement> result = statGroup.filter(MONTH, false);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(MONTH, result.get(0).getMonth());
		assertEquals(CATEGORY_NAME, result.get(0).getCategoryName());
		assertEquals(2 * AMOUNT, result.get(0).getAmount(), DELTA);
	}

	@Test
	public void addTwo_filterOne_withEmptyCategories() {
		StatisticsElement pair = StatisticsElement.create(MONTH, CATEGORY_ID, CATEGORY_NAME, AMOUNT, NONSENSE_DEFAULT,
				NONSENSE_DEFAULT);
		final String EMPTY_CATEGORY = "emptyCategory";
		StatisticsElement pair2 = StatisticsElement.create(MONTH.next(), 0, EMPTY_CATEGORY, AMOUNT, NONSENSE_DEFAULT,
				NONSENSE_DEFAULT);

		statGroup.add(pair);
		statGroup.add(pair2);
		List<StatisticsElement> result = statGroup.filter(MONTH, true);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(pair, result.get(0));
		assertEquals(EMPTY_CATEGORY, result.get(1).getCategoryName());
		assertEquals(0.0, result.get(1).getAmount(), DELTA);
	}
}
