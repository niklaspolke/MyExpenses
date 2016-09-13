package vu.de.npolke.myexpenses.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
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
public class Expense implements Serializable {

	private static final long serialVersionUID = 1L;

	private final NumberFormat NUMBER_FORMATTER = NumberFormat.getCurrencyInstance(Locale.GERMANY);
	private final DateFormat DATA_FORMATTER = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);

	private long id;

	private Calendar day;

	private double amount;

	private String reason;

	private long categoryId;
	private String categoryName;

	private long accountId;

	private boolean monthly = false;

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public Calendar getDay() {
		return day;
	}

	public void setDay(final Date dayAsDate) {
		initialiseDay();
		day.setTime(dayAsDate);
	}

	private void setDay(final Calendar day) {
		this.day = day;
	}

	private void initialiseDay() {
		if (getDay() == null) {
			setDay(Calendar.getInstance(Locale.GERMANY));
		}
	}

	public void setReadableDayAsString(final String readableDate) {
		initialiseDay();
		try {
			getDay().setTime(DATA_FORMATTER.parse(readableDate));
		} catch (ParseException pe) {
		}
	}

	public String getReadableDayAsString() {
		initialiseDay();
		return DATA_FORMATTER.format(getDay().getTime());
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(final double amount) {
		this.amount = amount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(final String reason) {
		this.reason = reason;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(final long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(final String categoryName) {
		this.categoryName = categoryName;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(final long accountId) {
		this.accountId = accountId;
	}

	public boolean isMonthly() {
		return monthly;
	}

	public void setMonthly(final boolean monthly) {
		this.monthly = monthly;
	}

	@Override
	public String toString() {
		//@formatter:off
		StringBuilder text = new StringBuilder().append("Expense: ")
				.append("(").append(getReadableDayAsString()).append(") - ")
				.append(NUMBER_FORMATTER.format(getAmount())).append(" - ")
				.append(getCategoryName()).append(" for ")
				.append(getReason() != null ? getReason() : "")
				.append(isMonthly() ? " - monthly fixed" : "");
		return text.toString();
		//@formatter:on
	}
}
