package vu.de.npolke.myexpenses.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
public class StatisticsDAO extends AbstractConnectionDAO {

	private final DecimalFormat FORMAT_2DIGIT = new DecimalFormat("00");
	private static final int TOPTENPERIOD_STARTBEFOREDAYS = -35; // 5 Weeks

	// @formatter:off
	private static final String SQL_SELECT_DISTINCT_MONTHS = "SELECT DISTINCT year(e.day)+'.'+lpad(month(e.day),2,'0') AS month "
			+ "FROM expense e " + "WHERE e.account_id = ? " + "ORDER BY month DESC";

	private static final String SQL_SELECT_STATISTICS_FOR_MONTH = "SELECT c.name as category, sum(e.amount) as sumofamount, monthly, income "
			+ "FROM category c " + "JOIN ( " + "SELECT category_id, amount, monthly, income " + "FROM expense "
			+ "WHERE year(day)+'.'+lpad(month(day),2,'0') = ? AND account_id = ? ) e " + "ON e.category_id = c.id "
			+ "WHERE c.account_id = ? " + "GROUP BY c.id, c.name, e.monthly, e.income "
			+ "ORDER BY c.name ASC, e.monthly ASC, e.income ASC";

	private static final String SQL_READ_TOPTEN_BY_ACCOUNT_ID = "SELECT * FROM ( "
			+ "SELECT COUNT(e.reason), e.reason, e.category_id, c.name " + "FROM Expense e " + "JOIN Category c "
			+ "ON e.category_id = c.id "
			+ "WHERE account_id = ? AND income = false AND monthly = false AND year(e.day)+'.'+lpad(month(e.day),2,'0')+'.'+lpad(day(e.day),2,'0') >= ? "
			+ "GROUP BY e.category_id, c.name, e.reason " + "ORDER BY COUNT(e.reason) DESC " + ") WHERE rownum() <= 10";

	private static final String SQL_READ_TOPTEN_BY_MONTH_AND_CATEGORY = "SELECT * FROM ( " + "SELECT e.*, c.name "
			+ "FROM Expense e " + "JOIN Category c " + "ON e.category_id = c.id "
			+ "WHERE e.account_id = ? AND c.id = ? AND year(e.day)+'.'+lpad(month(e.day),2,'0') = ? "
			+ "ORDER BY e.amount DESC " + ") WHERE rownum() <= 10";

	private static final String SQL_READ_TOPX_BY_MONTH_AND_ACCOUNT = "SELECT * FROM ( "
			+ "SELECT e.*, c.name, c.id as categoryid " + "FROM Expense e " + "JOIN Category c "
			+ "ON e.category_id = c.id "
			+ "WHERE e.monthly = false AND e.income = false AND e.account_id = ? AND year(e.day)+'.'+lpad(month(e.day),2,'0') = ? "
			+ "ORDER BY e.amount DESC " + ") WHERE rownum() <= ?";
	// @formatter:on

	public List<Month> readDistinctMonthsByAccountId(final long accountId) {
		List<Month> months = new ArrayList<Month>();

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_SELECT_DISTINCT_MONTHS);
			readStatement.setLong(1, accountId);
			ResultSet result = readStatement.executeQuery();
			while (result.next()) {
				months.add(Month.createMonth(result.getString("month")));
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return months;
	}

	public StatisticsOfMonth readStatisticsByMonthAndAccountId(final Month month, final long accountId) {
		Statistics stats = new Statistics();
		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_SELECT_STATISTICS_FOR_MONTH);
			readStatement.setString(1, month.toString());
			readStatement.setLong(2, accountId);
			readStatement.setLong(3, accountId);
			ResultSet result = readStatement.executeQuery();
			while (result.next()) {
				String category = result.getString("category");
				Double amount = result.getDouble("sumofamount");
				Boolean monthly = result.getBoolean("monthly");
				Boolean income = result.getBoolean("income");
				stats.add(StatisticsElement.create(month, category, amount, monthly, income));
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return stats.filter(month);
	}

	protected Calendar getToday() {
		return Calendar.getInstance(Locale.GERMANY);
	}

	public List<Expense> readTopTenByAccountId(final long accountId) {
		List<Expense> expenses = new ArrayList<Expense>();

		Calendar startOfPeriod = getToday();
		startOfPeriod.add(Calendar.DAY_OF_MONTH, TOPTENPERIOD_STARTBEFOREDAYS);
		final String STARTDAYOFPERIOD = startOfPeriod.get(Calendar.YEAR) + "."
				+ FORMAT_2DIGIT.format(startOfPeriod.get(Calendar.MONTH) + 1) + "."
				+ FORMAT_2DIGIT.format(startOfPeriod.get(Calendar.DAY_OF_MONTH));

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_READ_TOPTEN_BY_ACCOUNT_ID);
			readStatement.setLong(1, accountId);
			readStatement.setString(2, STARTDAYOFPERIOD);
			ResultSet result = readStatement.executeQuery();
			while (result.next()) {
				Expense expense = new Expense();
				expense.setReason(result.getString("reason"));
				expense.setCategoryId(result.getLong("category_id"));
				expense.setAccountId(accountId);
				expense.setCategoryName(result.getString("name"));
				expenses.add(expense);
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return expenses;
	}

	/**
	 * returning no income and no monthly expenses but normal expenses with the biggest amount
	 *
	 * @param accountId
	 *            Account of the user
	 * @param month
	 *            month that is searched within
	 * @param topX
	 *            amount of expenses with the biggest amount
	 * @return List of Expenses ordered by the amount (descending) (exclusively monthly expenses and income)
	 */
	public List<Expense> readTopXofExpensesByMonth(final long accountId, final String month, final int topX) {
		List<Expense> expenses = new ArrayList<Expense>();

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_READ_TOPX_BY_MONTH_AND_ACCOUNT);
			readStatement.setLong(1, accountId);
			readStatement.setString(2, month);
			readStatement.setLong(3, topX);
			ResultSet result = readStatement.executeQuery();
			while (result.next()) {
				Expense expense = new Expense();
				expense.setId(result.getLong("id"));
				expense.setDay(result.getDate("day"));
				expense.setAmount(result.getDouble("amount"));
				expense.setReason(result.getString("reason"));
				expense.setAccountId(accountId);
				expense.setCategoryId(result.getLong("categoryId"));
				expense.setCategoryName(result.getString("name"));
				expenses.add(expense);
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return expenses;
	}

	public List<Expense> readTopTenByMonthAndCategory(final long accountId, final String month, final long categoryId) {
		List<Expense> expenses = new ArrayList<Expense>();

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_READ_TOPTEN_BY_MONTH_AND_CATEGORY);
			readStatement.setLong(1, accountId);
			readStatement.setLong(2, categoryId);
			readStatement.setString(3, month);
			ResultSet result = readStatement.executeQuery();
			while (result.next()) {
				Expense expense = new Expense();
				expense.setId(result.getLong("id"));
				expense.setDay(result.getDate("day"));
				expense.setAmount(result.getDouble("amount"));
				expense.setReason(result.getString("reason"));
				expense.setAccountId(accountId);
				expense.setCategoryId(categoryId);
				expense.setCategoryName(result.getString("name"));
				expenses.add(expense);
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return expenses;
	}
}
