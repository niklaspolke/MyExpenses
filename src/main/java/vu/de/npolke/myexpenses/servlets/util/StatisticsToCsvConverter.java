package vu.de.npolke.myexpenses.servlets.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.util.Month;
import vu.de.npolke.myexpenses.util.Statistics;
import vu.de.npolke.myexpenses.util.StatisticsElement;
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

	private static final int INDEX_INCOME = 0;
	private static final int INDEX_MONTHLY = 1;
	private static final int INDEX_EXPENSES = 2;

	private final Statistics yearContainer;
	private final StatisticsOfMonth monthContainer;
	private ResourceBundle properties;
	private boolean firstColumn = true;

	// private TreeMap<String, List<String>>

	private BufferedWriter writer;

	public StatisticsToCsvConverter(final StatisticsOfMonth container) {
		this.monthContainer = container;
		this.yearContainer = null;
	}

	public StatisticsToCsvConverter(final Statistics container) {
		this.monthContainer = null;
		this.yearContainer = container;
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

	private void writeStatisticsPairsToFile(final List<StatisticsElement> pairs) throws IOException {
		for (StatisticsElement pair : pairs) {
			writeColumn(pair.getCategoryName());
			writeColumn(String.format(Locale.GERMANY, "%.2f", pair.getAmount()));
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
				if (monthContainer != null) {
					convertMonthToCsv();
				} else {
					convertYearToCsv();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tempFile;
	}

	private void convertMonthToCsv() throws IOException {
		writeColumn(getProperty(PROPERTY_INCOME)).endLine();
		writeStatisticsPairsToFile(monthContainer.getIncome());
		writeColumn(getProperty(PROPERTY_MONTHLYEXPENSES)).endLine();
		writeStatisticsPairsToFile(monthContainer.getMonthlyExpenses());
		writeColumn(getProperty(PROPERTY_EXPENSES)).endLine();
		writeStatisticsPairsToFile(monthContainer.getExpenses());
		if (monthContainer.getTopExpenses().size() > 0) {
			endLine();
			writeColumn("Top20 " + getProperty(PROPERTY_EXPENSES)).endLine();
			for (Expense expense : monthContainer.getTopExpenses()) {
				writeColumn(expense.getCategoryName() + " - " + expense.getReason());
				writeColumn(String.format(Locale.GERMANY, "%.2f", expense.getAmount())).endLine();
			}
		}
	}

	private void writeMonthsLine() throws IOException {
		for (Month month : yearContainer.getMonths()) {
			writeColumn("");
			writeColumn(month.toString());
		}
		endLine();
	}

	public void convertYearToCsv() throws IOException {
		List<List<List<StatisticsElement>>> listOfStatisticsElements = createListOfStatisticsElementsPerMonth();
		writeMonthsLine();
		writeColumn(getProperty(PROPERTY_INCOME)).endLine();
		writePerMonth(listOfStatisticsElements.get(INDEX_INCOME));
		writeColumn(getProperty(PROPERTY_MONTHLYEXPENSES)).endLine();
		writePerMonth(listOfStatisticsElements.get(INDEX_MONTHLY));
		writeColumn(getProperty(PROPERTY_EXPENSES)).endLine();
		writePerMonth(listOfStatisticsElements.get(INDEX_EXPENSES));
		endLine();
		List<List<Expense>> listOfTopExpensesPerMonth = createListOfTopExpensesPerMonth();
		final int maxTopExpenses = countMaxTopExpenses(listOfTopExpensesPerMonth);
		writeColumn("Top20 " + getProperty(PROPERTY_EXPENSES)).endLine();
		writeTopExpensesPerMonth(listOfTopExpensesPerMonth, maxTopExpenses);
	}

	/**
	 * first level is: 0 == income, 1 == monthlyExpenses, 2 == expenses; second level is month; third level is category
	 *
	 * @return 3 level List for StatisticsElements
	 */
	private List<List<List<StatisticsElement>>> createListOfStatisticsElementsPerMonth() {
		List<List<List<StatisticsElement>>> listOfStatisticsElementsPerMonth = new ArrayList<List<List<StatisticsElement>>>();
		listOfStatisticsElementsPerMonth.add(new ArrayList<List<StatisticsElement>>()); // income
		listOfStatisticsElementsPerMonth.add(new ArrayList<List<StatisticsElement>>()); // monthly expenses
		listOfStatisticsElementsPerMonth.add(new ArrayList<List<StatisticsElement>>()); // expenses

		for (Month month : yearContainer.getMonths()) {
			StatisticsOfMonth statisticsOfMonth = yearContainer.filter(month, true);
			listOfStatisticsElementsPerMonth.get(INDEX_INCOME).add(statisticsOfMonth.getIncome());
			listOfStatisticsElementsPerMonth.get(INDEX_MONTHLY).add(statisticsOfMonth.getMonthlyExpenses());
			listOfStatisticsElementsPerMonth.get(INDEX_EXPENSES).add(statisticsOfMonth.getExpenses());
		}
		return listOfStatisticsElementsPerMonth;
	}

	private List<List<Expense>> createListOfTopExpensesPerMonth() {
		List<List<Expense>> listOfTopExpensesPerMonth = new ArrayList<List<Expense>>();

		for (Month month : yearContainer.getMonths()) {
			StatisticsOfMonth statisticsOfMonth = yearContainer.filter(month, true);
			listOfTopExpensesPerMonth.add(statisticsOfMonth.getTopExpenses());
		}
		return listOfTopExpensesPerMonth;
	}

	private void writeTopExpensesPerMonth(final List<List<Expense>> listOfTopExpensesPerMonth, final int maxTopExpenses)
			throws IOException {
		for (int topIndex = 0; topIndex < maxTopExpenses; topIndex++) {
			for (List<Expense> topExpensesOfMonth : listOfTopExpensesPerMonth) {
				if (topIndex < topExpensesOfMonth.size()) {
					Expense expense = topExpensesOfMonth.get(topIndex);
					writeColumn(expense.getCategoryName() + " - " + expense.getReason());
					writeColumn(String.format(Locale.GERMANY, "%.2f", expense.getAmount()));
				} else {
					writeColumn("").writeColumn("");
				}
			}
			endLine();
		}
	}

	private int countMaxTopExpenses(List<List<Expense>> listOfTopExpensesPerMonth) {
		int maxTopExpenses = 0;
		for (List<Expense> listOfMonth : listOfTopExpensesPerMonth) {
			maxTopExpenses = Math.max(maxTopExpenses, listOfMonth.size());
		}
		return maxTopExpenses;
	}

	private void writePerMonth(final List<List<StatisticsElement>> statistics) throws IOException {
		for (int categoryIndex = 0; categoryIndex < statistics.get(0).size(); categoryIndex++) {
			writeColumn(statistics.get(0).get(categoryIndex).getCategoryName());
			for (List<StatisticsElement> statisticsOfMonth : statistics) {
				writeColumn(String.format(Locale.GERMANY, "%.2f", statisticsOfMonth.get(categoryIndex).getAmount()));
				writeColumn("");
			}
			endLine();
		}
	}
}
