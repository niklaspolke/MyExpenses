package vu.de.npolke.myexpenses.servlets.util;

import static org.junit.Assert.*;

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
public class StatisticsPairTest {

	@Test
	public void constructorToGetterId() {
		StatisticsPair pair = new StatisticsPair(4l, "category", 3.3, true, false);

		assertEquals(4, pair.getId());
	}

	@Test
	public void constructorToGetterName() {
		StatisticsPair pair = new StatisticsPair(4l, "category", 3.3, true, false);

		assertEquals("category", pair.getName());
	}

	@Test
	public void constructorToGetterValue() {
		StatisticsPair pair = new StatisticsPair(4l, "category", 3.3, true, false);

		assertEquals(3.3, pair.getValue(), 0.01);
	}

	@Test
	public void constructorToGetterMonthly() {
		StatisticsPair pair = new StatisticsPair(4l, "category", 3.3, true, false);

		assertEquals(true, pair.isMonthly());
	}

	@Test
	public void constructorToGetterIncome() {
		StatisticsPair pair = new StatisticsPair(4l, "category", 3.3, true, false);

		assertEquals(false, pair.isIncome());
	}
}
