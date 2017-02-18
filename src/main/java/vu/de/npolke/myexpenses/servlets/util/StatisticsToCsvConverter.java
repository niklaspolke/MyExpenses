package vu.de.npolke.myexpenses.servlets.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
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
public class StatisticsToCsvConverter {

	public static final String COLUMN_SEPARATOR = ";";
	public static final String CURRENCY = "€";

	private final StatisticsPairContainerOfMonth container;

	private BufferedWriter writer;

	public StatisticsToCsvConverter(final StatisticsPairContainerOfMonth container) {
		this.container = container;
	}

	private void writeLine(final String line) throws IOException {
		writer.write(line);
		writer.newLine();
	}

	private void writeStatisticsPairsToFile(final List<StatisticsPair> pairs) throws IOException {
		for (StatisticsPair pair : pairs) {
			writer.write(pair.getName());
			writer.write(COLUMN_SEPARATOR);
			writer.write(String.format(Locale.GERMANY, "%.2f", pair.getValue().doubleValue()));
			writer.write(" " + CURRENCY);
			writer.newLine();
		}
	}

	public synchronized File convertToCsv() {
		File tempFile = null;
		try {
			tempFile = File.createTempFile("csvexport", ".csv");
			writer = new BufferedWriter(new FileWriter(tempFile));
			writeLine("Income");
			writeStatisticsPairsToFile(container.getIncome());
			writeLine("Monthly Expenses");
			writeStatisticsPairsToFile(container.getMonthlyExpenses());
			writeLine("Expenses");
			writeStatisticsPairsToFile(container.getExpenses());
			writer.close();
		} catch (IOException e) {
		}
		return tempFile;
	}
}
