package vu.de.npolke.myexpenses.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static vu.de.npolke.myexpenses.util.ApplicationStatisticTypes.LOGINS;
import static vu.de.npolke.myexpenses.util.ApplicationStatisticTypes.NEW_EXPENSES;

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
public class ApplicationStatisticsTest {

	private ApplicationStatistics statistics = new ApplicationStatistics();

	@Test
	public void getSingleton() {
		assertNotNull(ApplicationStatistics.getSingleton());
	}

	@Test
	public void getStartOfApplication() {
		assertNotNull(statistics.getStartOfApplication());
		assertTrue(statistics.getStartOfApplication().trim().length() > 0);
	}

	@Test
	public void getCounterForStatisticType_initialValues() {
		assertEquals(0, statistics.getCounterForStatisticType(LOGINS));
		assertEquals(0, statistics.getCounterForStatisticType(NEW_EXPENSES));
	}

	@Test
	public void increaseCounterForStatisticType() {
		statistics.increaseCounterForStatisticType(LOGINS);
		statistics.increaseCounterForStatisticType(NEW_EXPENSES);
		statistics.increaseCounterForStatisticType(LOGINS);

		assertEquals(2, statistics.getCounterForStatisticType(LOGINS));
		assertEquals(1, statistics.getCounterForStatisticType(NEW_EXPENSES));
	}
}
