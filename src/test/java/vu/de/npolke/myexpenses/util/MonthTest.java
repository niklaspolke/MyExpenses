package vu.de.npolke.myexpenses.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static vu.de.npolke.myexpenses.util.Month.*;

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
public class MonthTest {

	private Month month = createMonth(2015, 5);

	@Test
	public void getYear() {
		assertEquals(2015, month.getYear());
	}

	@Test
	public void getMonth() {
		assertEquals(5, month.getMonth());
	}

	@Test
	public void nextMonth() {
		assertEquals("2015.06", month.next().toString());
	}

	@Test
	public void nextMonthInNextYear() {
		assertEquals("2016.01", createMonth(2015, 12).next().toString());
	}

	@Test
	public void previousMonth() {
		assertEquals("2015.04", month.previous().toString());
	}

	@Test
	public void previousMonthInPreviousYear() {
		assertEquals("2015.12", createMonth(2016, 1).previous().toString());
	}

	@Test
	public void testToString() {
		assertEquals("2015.05", month.toString());
	}

	@Test
	public void equalsEqual() {
		assertTrue(month.equals(createMonth(month.getYear(), month.getMonth())));
	}

	@Test
	public void equalsNotEqual() {
		assertFalse(month.equals(createMonth(month.getYear() + 1, month.getMonth())));
	}

	@Test
	public void equalsOtherType() {
		assertFalse(month.equals(4));
	}

	@Test
	public void equalsEqualString() {
		assertTrue(month.equals(month.toString()));
	}

	@Test
	public void equalsNull() {
		assertFalse(month.equals(null));
	}

	@Test
	public void creationFailure() {
		Month month2 = createMonth(2015, 0);

		assertNull(month2);
	}

	@Test
	public void createFromString() {
		Month month2 = createMonth("2015.05");
		assertNotNull(month2);
		assertEquals(month, month2);
	}

	@Test
	public void createFromIllegalString() {
		Month month2 = createMonth("2015.5");

		assertNull(month2);
	}

	@Test
	public void compareEqual() {
		assertEquals(0, month.compareTo(createMonth(month.getYear(), month.getMonth())));
	}

	@Test
	public void compareSmallerMonth() {
		assertTrue(month.compareTo(createMonth(month.getYear(), month.getMonth() + 1)) < 0);
	}

	@Test
	public void compareBiggerYear() {
		assertTrue(month.compareTo(createMonth(month.getYear() - 1, month.getMonth())) > 0);
	}

	@Test
	public void createMonthFromTimeMillis1() {
		assertEquals("2017.01", createMonthFromTimeMillis(1483999598469L).toString());
	}
}
