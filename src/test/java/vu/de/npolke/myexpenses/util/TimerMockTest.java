package vu.de.npolke.myexpenses.util;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
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
public class TimerMockTest {

	private static final long FAKE_TIME = 123456L;

	@Test
	public void getCurrentTimeMillis() {
		Timer fakeTime = new TimerMock(FAKE_TIME);

		assertEquals(FAKE_TIME, fakeTime.getCurrentTimeMillis());
	}

	// should be corrected, when no work with currentTimeMillis is needed any more
	// either use of Java8 Time classes or String for date
	@Ignore
	@Test
	public void getCurrentTimeMillis_FromDate() {
		Timer fakeTime = new TimerMock("08.05.1981");

		assertEquals(358120800000l, fakeTime.getCurrentTimeMillis());
	}
}
