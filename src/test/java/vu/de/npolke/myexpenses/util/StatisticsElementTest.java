package vu.de.npolke.myexpenses.util;

import static org.junit.Assert.*;
import static vu.de.npolke.myexpenses.util.StatisticsElement.*;

import org.junit.Test;

import vu.de.npolke.myexpenses.util.Month;
import vu.de.npolke.myexpenses.util.StatisticsElement;

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
public class StatisticsElementTest {

	private static final double DELTA = 0.001;

	private static final Month MONTH = Month.create(2015, 5);
	private static final double AMOUNT = 1.1;

	@Test
	public void createToGetters() {
		//@formatter:off
		final String  CATEGORY_NAME = "category";
		final double  AMOUNT        = 3.3;
		final boolean ISMONTHLY     = true;
		final boolean ISINCOME      = false;
		StatisticsElement pair      = StatisticsElement.create(MONTH, CATEGORY_NAME, AMOUNT, ISMONTHLY, ISINCOME);

		assertEquals(MONTH,         pair.getMonth());
		assertEquals(CATEGORY_NAME, pair.getCategory());
		assertEquals(AMOUNT,        pair.getAmount(), DELTA);
		assertEquals(ISMONTHLY,     pair.isMonthly());
		assertEquals(ISINCOME,      pair.isIncome());
		//@formatter:on
	}

	private static void assertBiggerThan(final StatisticsElement element1, final StatisticsElement element2) {
		assertTrue(element1.compareTo(element2) > 0);
		assertTrue(element2.compareTo(element1) < 0);
		assertFalse(element1.equals(element2));
		assertFalse(element2.equals(element1));
	}

	@Test
	public void compareToNull() {
		StatisticsElement element = create(MONTH, "a", AMOUNT, true, true);
		assertTrue(element.compareTo(null) < 0);
		assertFalse(element.equals(null));
	}

	@Test
	public void compareDifferentMonths() {
		assertBiggerThan(create(MONTH.next(), "a", AMOUNT, true, true), create(MONTH, "b", AMOUNT, false, false));
	}

	@Test
	public void compareDifferentIsIncome() {
		assertBiggerThan(create(MONTH, "a", AMOUNT, true, false), create(MONTH, "b", AMOUNT, false, true));
	}

	@Test
	public void compareDifferentIsMonthly() {
		assertBiggerThan(create(MONTH, "a", AMOUNT, false, false), create(MONTH, "b", AMOUNT, true, false));
	}

	@Test
	public void compareDifferentCategory() {
		assertBiggerThan(create(MONTH, "b", AMOUNT, false, false), create(MONTH, "a", AMOUNT, false, false));
	}

	private static void assertEqual(final StatisticsElement element1, final StatisticsElement element2) {
		assertTrue(element1.compareTo(element2) == 0);
		assertTrue(element2.compareTo(element1) == 0);
		assertTrue(element1.equals(element2));
		assertTrue(element2.equals(element1));
	}

	@Test
	public void compareEqual() {
		assertEqual(create(MONTH, "a", AMOUNT, false, false), create(MONTH, "a", AMOUNT, false, false));
	}
}
