package vu.de.npolke.myexpenses.servlets.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.util.StatisticsOfMonth;

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
public class StatisticsToCsvConverter {

	public static final String PROPERTIES = "messages";
	public static final String PROPERTY_INCOME = "statistics.income.title";
	public static final String PROPERTY_MONTHLYEXPENSES = "statistics.monthlycosts.title";
	public static final String PROPERTY_EXPENSES = "statistics.expenses.title";
	public static final String COLUMN_SEPARATOR = ",";
	public static final String QUOTATION_MARKS = "\"";
	public static final String CURRENCY = "€";

	private final StatisticsOfMonth container;
	private final List<Expense> topExpenses;
	private ResourceBundle properties;
	private boolean firstColumn = true;

	private BufferedWriter writer;

	public StatisticsToCsvConverter(final StatisticsOfMonth container) {
		this.container = container;
		this.topExpenses = null;
	}

	public StatisticsToCsvConverter(final StatisticsOfMonth container, final List<Expense> topExpenses) {
		this.container = container;
		this.topExpenses = topExpenses;
	}

	private String getProperty(final String property) {
		return properties.getString(property);
	}

	private void setProperties(Locale locale) {
		/*
		 * if messages_en.properties won't be found, an other properties based on the system specific default language
		 * would be taken - to avoid having to care for two equal property files (messages.properties and
		 * messages_en.properties), this workaround is used
		 */
		if ("en".equalsIgnoreCase(locale.getLanguage())) {
			locale = new Locale("");
		}
		this.properties = ResourceBundle.getBundle(PROPERTIES, locale);
	}

	private static String escapeColumn(String column) {
		if (column != null && (column.indexOf(COLUMN_SEPARATOR) != -1 || column.indexOf(QUOTATION_MARKS) != -1)) {
			column = column.replaceAll(QUOTATION_MARKS, QUOTATION_MARKS + QUOTATION_MARKS);
			column = QUOTATION_MARKS + column + QUOTATION_MARKS;
		}
		return column;
	}

	private StatisticsToCsvConverter writeColumn(final String textInColumn) throws IOException {
		if (firstColumn == false) {
			writer.write(COLUMN_SEPARATOR);
		}
		firstColumn = false;
		writer.write(escapeColumn(textInColumn));
		return this;
	}

	private void endLine() throws IOException {
		writer.newLine();
		firstColumn = true;
	}

	private void writeStatisticsPairsToFile(final List<StatisticsPair> pairs) throws IOException {
		for (StatisticsPair pair : pairs) {
			writeColumn(pair.getName());
			writeColumn(String.format(Locale.GERMANY, "%.2f", pair.getValue().doubleValue()));
			endLine();
		}
	}

	public synchronized File convertToCsv(Locale locale) {
		setProperties(locale);
		File tempFile = null;
		try {
			tempFile = File.createTempFile("csvexport", ".csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (tempFile != null) {
			try (BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"))) {
				this.writer = writer;
				writeColumn(getProperty(PROPERTY_INCOME)).endLine();
				writeStatisticsPairsToFile(container.getIncome());
				writeColumn(getProperty(PROPERTY_MONTHLYEXPENSES)).endLine();
				writeStatisticsPairsToFile(container.getMonthlyExpenses());
				writeColumn(getProperty(PROPERTY_EXPENSES)).endLine();
				writeStatisticsPairsToFile(container.getExpenses());
				if (topExpenses != null) {
					endLine();
					writeColumn("Top15 " + getProperty(PROPERTY_EXPENSES)).endLine();
					for (Expense expense : topExpenses) {
						writeColumn(expense.getCategoryName() + " - " + expense.getReason());
						writeColumn(String.format(Locale.GERMANY, "%.2f", expense.getAmount())).endLine();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tempFile;
	}
}
