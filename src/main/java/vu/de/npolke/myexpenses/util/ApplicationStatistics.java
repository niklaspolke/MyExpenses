package vu.de.npolke.myexpenses.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

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
public class ApplicationStatistics {

	private static final DateFormat DATATIME_FORMATTER = DateFormat.getDateTimeInstance(DateFormat.SHORT,
			DateFormat.SHORT, Locale.GERMANY);

	private static final ApplicationStatistics singleton;

	static {
		singleton = new ApplicationStatistics();
	}

	private final Calendar applicationStart;

	private final HashMap<ApplicationStatisticTypes, Long> statistics = new HashMap<ApplicationStatisticTypes, Long>();

	protected ApplicationStatistics() {
		applicationStart = Calendar.getInstance(Locale.GERMANY);
		applicationStart.setTimeInMillis(System.currentTimeMillis());
		for (ApplicationStatisticTypes type : ApplicationStatisticTypes.values()) {
			statistics.put(type, 0l);
		}
	}

	public String getStartOfApplication() {
		return DATATIME_FORMATTER.format(applicationStart.getTime());
	}

	public long getCounterForStatisticType(final ApplicationStatisticTypes type) {
		return statistics.get(type);
	}

	public long increaseCounterForStatisticType(final ApplicationStatisticTypes type) {
		final long newCount = statistics.get(type) + 1;
		statistics.put(type, newCount);
		return newCount;
	}

	public static ApplicationStatistics getSingleton() {
		return singleton;
	}
}
