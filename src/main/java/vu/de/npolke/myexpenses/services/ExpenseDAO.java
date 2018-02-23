package vu.de.npolke.myexpenses.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.util.Month;
import vu.de.npolke.myexpenses.util.Timer;

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
public class ExpenseDAO extends AbstractConnectionDAO {

	private static final String SQL_READ_TEMPLATE = "SELECT e.id, e.day, e.amount, e.reason, e.monthly, e.income, e.category_id, e.account_id, c.name FROM Expense e JOIN Category c ON e.category_id = c.id WHERE e.account_id = ?";

	private static final String SQL_INSERT = "INSERT INTO Expense (id, day, amount, reason, monthly, income, category_id, account_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SQL_READ_BY_ID = SQL_READ_TEMPLATE + "AND e.id = ?";

	private static final String SQL_READ_BY_ACCOUNT_ID_INFUTURE = "SELECT * FROM (" + SQL_READ_TEMPLATE
			+ "AND e.monthly = false AND e.day > ? ORDER BY e.day ASC, e.id DESC) WHERE rownum() <= ?";

	private static final String SQL_READ_BY_ACCOUNT_ID_UPTONOW = "SELECT * FROM (" + SQL_READ_TEMPLATE
			+ "AND monthly = false AND day <= ? ORDER BY day DESC, id DESC) WHERE rownum() <= ?";

	private static final String SQL_READ_MONTHLY_BY_ACCOUNT_AND_MONTH = SQL_READ_TEMPLATE
			+ "AND year(e.day)+'.'+lpad(month(e.day),2,'0') = ? AND monthly = true ORDER BY amount DESC, id DESC";

	private static final String SQL_READ_AMOUNT_IN_FUTURE_BY_ACCOUNT_ID = "SELECT COUNT(id) as amountofexpenses FROM Expense WHERE account_id = ? AND monthly = false AND day > ?";

	private static final String SQL_READ_AMOUNT_UP_TO_NOW_BY_ACCOUNT_ID = "SELECT COUNT(id) as amountofexpenses FROM Expense WHERE account_id = ? AND monthly = false AND day <= ?";

	private static final String SQL_READ_AMOUNT_STANDARD_EXPENSE_BY_ACCOUNT_ID = "SELECT COUNT(id) as amountofexpenses FROM Expense WHERE account_id = ? AND monthly = false AND income = false";

	private static final String SQL_READ_BY_CATEGORY_ID = "SELECT e.id, e.day, e.amount, e.reason, e.monthly, e.income, e.category_id, e.account_id, c.name FROM Expense e JOIN Category c ON e.category_id = c.id WHERE e.category_id = ? ORDER BY day DESC";

	private static final String SQL_UPDATE_BY_ID = "UPDATE Expense SET day = ?, amount = ?, reason = ?, monthly = ?, income = ?, category_id = ? WHERE id = ?";

	private static final String SQL_DELETE_BY_ID = "DELETE FROM Expense WHERE id = ?";

	private static final String SQL_DELETE_BY_ACCOUNTID = "DELETE FROM Expense WHERE account_id = ?";

	private SequenceDAO sequenceDAO;

	private CategoryDAO categoryDAO;

	Timer timer = new Timer();

	public ExpenseDAO(final SequenceDAO sequenceDAO) {
		this.sequenceDAO = sequenceDAO;
	}

	protected void setCategoryDAO(final CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	public Expense create(final String readableDate, final double amount, final String reason, final boolean monthly,
			final boolean income, final long categoryId, final long accountId) {
		Expense expense = new Expense();
		expense.setId(sequenceDAO.getNextPrimaryKey());
		expense.setReadableDayAsString(readableDate);
		expense.setAmount(amount);
		expense.setReason(reason);
		expense.setMonthly(monthly);
		expense.setIncome(income);
		expense.setCategoryId(categoryId);
		expense.setCategoryName(categoryDAO.read(accountId, categoryId).getName());
		expense.setAccountId(accountId);

		try (Connection connection = getConnection()) {
			PreparedStatement createStatement;
			createStatement = connection.prepareStatement(SQL_INSERT);
			createStatement.setLong(1, expense.getId());
			createStatement.setDate(2, new java.sql.Date(expense.getDay().getTimeInMillis()), expense.getDay());
			createStatement.setDouble(3, expense.getAmount());
			createStatement.setString(4, expense.getReason());
			createStatement.setBoolean(5, expense.isMonthly());
			createStatement.setBoolean(6, expense.isIncome());
			createStatement.setLong(7, categoryId);
			createStatement.setLong(8, accountId);
			boolean created = 1 == createStatement.executeUpdate();
			if (!created) {
				expense = null;
			}
			connection.commit();
		} catch (SQLException e) {
			expense = null;
			e.printStackTrace();
		}

		return expense;
	}

	public Expense read(final long accountId, final long id) {
		Expense expense = null;

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_READ_BY_ID);
			readStatement.setLong(1, accountId);
			readStatement.setLong(2, id);
			ResultSet result = readStatement.executeQuery();
			if (result.next()) {
				expense = mapResultRow(result);
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return expense;
	}

	private Expense mapResultRow(ResultSet result) throws SQLException {
		Expense expense = new Expense();
		expense.setId(result.getLong("id"));
		expense.setDay(result.getDate("day"));
		expense.setAmount(result.getDouble("amount"));
		expense.setReason(result.getString("reason"));
		expense.setMonthly(result.getBoolean("monthly"));
		expense.setIncome(result.getBoolean("income"));
		expense.setCategoryId(result.getLong("category_id"));
		expense.setAccountId(result.getLong("account_id"));
		expense.setCategoryName(result.getString("name"));
		return expense;
	}

	public boolean update(final Expense expense) {
		boolean updated = false;

		try (Connection connection = getConnection()) {
			PreparedStatement updateStatement;
			updateStatement = connection.prepareStatement(SQL_UPDATE_BY_ID);
			updateStatement.setDate(1, new java.sql.Date(expense.getDay().getTimeInMillis()), expense.getDay());
			updateStatement.setDouble(2, expense.getAmount());
			updateStatement.setString(3, expense.getReason());
			updateStatement.setBoolean(4, expense.isMonthly());
			updateStatement.setBoolean(5, expense.isIncome());
			updateStatement.setLong(6, expense.getCategoryId());
			String categoryName = categoryDAO.read(expense.getAccountId(), expense.getCategoryId()).getName();
			expense.setCategoryName(categoryName);
			updateStatement.setLong(7, expense.getId());
			updated = 1 == updateStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return updated;
	}

	public List<Expense> readByAccountId(final long accountId, final long startIndex, final long endIndex,
			final boolean inFuture) {
		List<Expense> expenses = new ArrayList<Expense>();

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection
					.prepareStatement(inFuture ? SQL_READ_BY_ACCOUNT_ID_INFUTURE : SQL_READ_BY_ACCOUNT_ID_UPTONOW);
			readStatement.setLong(1, accountId);
			readStatement.setDate(2, new java.sql.Date(timer.getCurrentTimeMillis()));
			readStatement.setLong(3, endIndex);
			ResultSet result = readStatement.executeQuery();
			while (result.next()) {
				if (result.getRow() >= startIndex) {
					Expense expense = mapResultRow(result);
					if (inFuture) {
						// reverse order needed
						expenses.add(0, expense);
					} else {
						expenses.add(expense);
					}
				}
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return expenses;
	}

	public List<Expense> readMonthlyByAccountAndMonth(final long accountId, final Month month) {
		List<Expense> monthlyCosts = new ArrayList<Expense>();

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_READ_MONTHLY_BY_ACCOUNT_AND_MONTH);
			readStatement.setLong(1, accountId);
			readStatement.setString(2, month.toString());
			ResultSet result = readStatement.executeQuery();
			while (result.next()) {
				Expense expense = mapResultRow(result);
				monthlyCosts.add(expense);
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return monthlyCosts;
	}

	public long readAmountOfExpensesInFuture(final long accountId) {
		long amountOfExpenses = -1;

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_READ_AMOUNT_IN_FUTURE_BY_ACCOUNT_ID);
			readStatement.setLong(1, accountId);
			readStatement.setDate(2, new java.sql.Date(timer.getCurrentTimeMillis()));
			ResultSet result = readStatement.executeQuery();
			if (result.next()) {
				amountOfExpenses = result.getLong("amountofexpenses");
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return amountOfExpenses;
	}

	public long readAmountOfExpensesUpToNow(final long accountId) {
		long amountOfExpenses = -1;

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_READ_AMOUNT_UP_TO_NOW_BY_ACCOUNT_ID);
			readStatement.setLong(1, accountId);
			readStatement.setDate(2, new java.sql.Date(timer.getCurrentTimeMillis()));
			ResultSet result = readStatement.executeQuery();
			if (result.next()) {
				amountOfExpenses = result.getLong("amountofexpenses");
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return amountOfExpenses;
	}

	public long readAmountOfStandardExpenses(final long accountId) {
		long amountOfExpenses = -1;

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_READ_AMOUNT_STANDARD_EXPENSE_BY_ACCOUNT_ID);
			readStatement.setLong(1, accountId);
			ResultSet result = readStatement.executeQuery();
			if (result.next()) {
				amountOfExpenses = result.getLong("amountofexpenses");
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return amountOfExpenses;
	}

	public List<Expense> readByCategoryId(final long categoryId) {
		List<Expense> expenses = new ArrayList<Expense>();

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_READ_BY_CATEGORY_ID);
			readStatement.setLong(1, categoryId);
			ResultSet result = readStatement.executeQuery();
			while (result.next()) {
				Expense expense = mapResultRow(result);
				expenses.add(expense);
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return expenses;
	}

	public boolean deleteById(final long expenseId) {
		boolean deleted = false;

		try (Connection connection = getConnection()) {
			PreparedStatement deleteStatement;
			deleteStatement = connection.prepareStatement(SQL_DELETE_BY_ID);
			deleteStatement.setLong(1, expenseId);
			deleted = 1 == deleteStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return deleted;
	}

	public long deleteByAccountId(final long accountId) {
		long deleted = 0;
		try (Connection connection = getConnection()) {
			PreparedStatement deleteStatement;
			deleteStatement = connection.prepareStatement(SQL_DELETE_BY_ACCOUNTID);
			deleteStatement.setLong(1, accountId);
			deleted = deleteStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			deleted = 0;
		}
		return deleted;
	}
}
