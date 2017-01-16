package vu.de.npolke.myexpenses.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

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
public class TimerMock extends Timer {

	private final DateFormat DATA_FORMATTER = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);

	private final Calendar calendar;

	public TimerMock(final long timeMillis) {
		calendar = Calendar.getInstance(Locale.GERMANY);
		calendar.setTimeInMillis(timeMillis);
	}

	public TimerMock(final String readableDate) {
		DATA_FORMATTER.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"), Locale.GERMANY);
		try {
			calendar.setTime(DATA_FORMATTER.parse(readableDate));
		} catch (ParseException pe) {
		}
	}

	@Override
	public long getCurrentTimeMillis() {
		return calendar.getTimeInMillis();
	}
}
