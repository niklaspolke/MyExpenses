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
public class StatisticsToCsvConverter {

	public static final String PROPERTIES = "messages";
	public static final String PROPERTY_INCOME = "statistics.income.title";
	public static final String PROPERTY_MONTHLYEXPENSES = "statistics.monthlycosts.title";
	public static final String PROPERTY_EXPENSES = "statistics.expenses.title";
	public static final String COLUMN_SEPARATOR = ";";
	public static final String CURRENCY = "€";

	private final StatisticsOfMonth container;
	private final List<Expense> topExpenses;

	private BufferedWriter writer;

	public StatisticsToCsvConverter(final StatisticsOfMonth container) {
		this.container = container;
		this.topExpenses = null;
	}

	public StatisticsToCsvConverter(final StatisticsOfMonth container, final List<Expense> topExpenses) {
		this.container = container;
		this.topExpenses = topExpenses;
	}

	private void writeLine(final String line) throws IOException {
		writer.write(line);
		writer.newLine();
	}

	private void writeStatisticsPairsToFile(final List<StatisticsPair> pairs) throws IOException {
		for (StatisticsPair pair : pairs) {
			writer.write(pair.getName());
			writer.write(COLUMN_SEPARATOR);
			writeLine(String.format(Locale.GERMANY, "%.2f", pair.getValue().doubleValue()));
		}
	}

	public synchronized File convertToCsv(Locale locale) {
		if ("en".equalsIgnoreCase(locale.getLanguage())) {
			locale = new Locale("");
		}
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
				ResourceBundle properties = ResourceBundle.getBundle(PROPERTIES, locale);
				writeLine(properties.getString(PROPERTY_INCOME));
				writeStatisticsPairsToFile(container.getIncome());
				writeLine(properties.getString(PROPERTY_MONTHLYEXPENSES));
				writeStatisticsPairsToFile(container.getMonthlyExpenses());
				writeLine(properties.getString(PROPERTY_EXPENSES));
				writeStatisticsPairsToFile(container.getExpenses());
				if (topExpenses != null) {
					writeLine("");
					writeLine("Top10 " + properties.getString(PROPERTY_EXPENSES));
					for (Expense expense : topExpenses) {
						writeLine(expense.getCategoryName() + " - " + expense.getReason() + COLUMN_SEPARATOR
								+ String.format(Locale.GERMANY, "%.2f", expense.getAmount()));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tempFile;
	}
}
