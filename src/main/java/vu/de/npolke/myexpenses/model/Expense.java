package vu.de.npolke.myexpenses.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

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
@Entity
public class Expense implements Serializable {

	private static final long serialVersionUID = 1L;

	@Transient
	private final NumberFormat NUMBER_FORMATTER = NumberFormat.getCurrencyInstance(Locale.GERMANY);
	@Transient
	private final DateFormat DATA_FORMATTER = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);

	@Id
	@GeneratedValue(generator = "ID_SEQ", strategy = GenerationType.TABLE)
	@TableGenerator(name="ID_SEQ",
		table="sequence",
		pkColumnName="SEQ_NAME", // Specify the name of the column of the primary key
		valueColumnName="SEQ_NUMBER", // Specify the name of the column that stores the last value generated
		pkColumnValue="ID_GENERATOR", // Specify the primary key column value that would be considered as a primary key generator
		allocationSize=1)
	private long id;

	@Column(name="DAY")
	private Calendar day;

	private Double amount;

	private String reason;

	@ManyToOne
	private Category category;

	@ManyToOne
	private Account account;

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public Calendar getDay() {
		return day;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(final Double amount) {
		this.amount = amount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(final String reason) {
		this.reason = reason;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(final Category category) {
		this.category = category;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(final Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		//@formatter:off
		StringBuilder text = new StringBuilder().append("Expense: ")
				.append("(").append(getReadableDayAsString()).append(") - ")
				.append(NUMBER_FORMATTER.format(getAmount().doubleValue())).append(" - ")
				.append(getCategory() != null ? getCategory().getName() : "null").append(" for ")
				.append(getReason() != null ? getReason() : "");
		return text.toString();
		//@formatter:on
	}
}
