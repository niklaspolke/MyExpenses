package vu.de.npolke.myexpenses.util;

import java.text.DecimalFormat;
import java.util.Calendar;
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
public class Month implements Comparable<Month> {

	private static final DecimalFormat FORMAT_YEAR = new DecimalFormat("0000");
	private static final DecimalFormat FORMAT_MONTH = new DecimalFormat("00");
	private static final String DELIMITER_YEAR_MONTH = ".";
	private static final String STRING_PATTERN = "\\d{4}" + DELIMITER_YEAR_MONTH + "\\d{2}";

	private int year;
	private int month;

	Month(final int year, final int month) {
		this.year = year;
		this.month = month;
	}

	public static Month createMonthFromTimeMillis(final long timeMillis) {
		Calendar now = Calendar.getInstance(Locale.GERMANY);
		now.setTimeInMillis(timeMillis);
		return new Month(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
	}

	public static Month create(final int year, final int month) {
		if (month >= 1 && month <= 12) {
			return new Month(year, month);
		} else {
			return null;
		}
	}

	public static Month createMonth(final String yearMonth) {
		if (yearMonth != null && yearMonth.matches(STRING_PATTERN)) {
			int year = Integer.parseInt(yearMonth.substring(0, 4));
			int month = Integer.parseInt(yearMonth.substring(5, 7));
			return new Month(year, month);
		} else {
			return null;
		}
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public Month next() {
		if (month == 12) {
			return new Month(year + 1, 1);
		} else {
			return new Month(year, month + 1);
		}
	}

	public Month previous() {
		if (month == 1) {
			return new Month(year - 1, 12);
		} else {
			return new Month(year, month - 1);
		}
	}

	@Override
	public String toString() {
		return FORMAT_YEAR.format(year) + DELIMITER_YEAR_MONTH + FORMAT_MONTH.format(month);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Month) {
			Month otherMonth = (Month) o;
			return getYear() == otherMonth.getYear() && getMonth() == otherMonth.getMonth();
		} else if (o instanceof String) {
			String otherMonth = (String) o;
			return toString().equals(otherMonth);
		} else {
			return false;
		}
	}

	@Override
	public int compareTo(Month otherMonth) {
		if (otherMonth == null) {
			return 1;
		} else {
			if (year == otherMonth.getYear()) {
				return month - otherMonth.getMonth();
			} else {
				return year - otherMonth.getYear();
			}
		}
	}
}
