package vu.de.npolke.myexpenses.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vu.de.npolke.myexpenses.model.Expense;

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

	private static final String SQL_INSERT = "INSERT INTO Expense (id, day, amount, reason, monthly, category_id, account_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

	private static final String SQL_READ_BY_ID = "SELECT e.day, e.amount, e.reason, monthly, e.category_id, e.account_id, c.name FROM Expense e JOIN Category c ON e.category_id = c.id WHERE e.id = ?";

	private static final String SQL_READ_BY_ACCOUNT_ID = "SELECT * FROM (SELECT e.id, e.day, e.amount, e.reason, e.monthly, e.category_id, c.name FROM Expense e JOIN Category c ON e.category_id = c.id WHERE account_id = ? ORDER BY day DESC, id DESC) WHERE rownum() <= ?";

	private static final String SQL_READ_AMOUNT_BY_ACCOUNT_ID = "SELECT COUNT(id) as amountofexpenses FROM Expense WHERE account_id = ?";

	private static final String SQL_READ_BY_CATEGORY_ID = "SELECT e.id, e.day, e.amount, e.reason, e.monthly, e.account_id, c.name FROM Expense e JOIN Category c ON e.category_id = c.id WHERE category_id = ? ORDER BY day DESC";

	private static final String SQL_UPDATE_BY_ID = "UPDATE Expense SET day = ?, amount = ?, reason = ?, monthly = ?, category_id = ? WHERE id = ?";

	private static final String SQL_DELETE_BY_ID = "DELETE FROM Expense WHERE id = ?";

	private SequenceDAO sequenceDAO;

	private CategoryDAO categoryDAO;

	public ExpenseDAO(final SequenceDAO sequenceDAO) {
		this.sequenceDAO = sequenceDAO;
	}

	protected void setCategoryDAO(final CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	public Expense create(final String readableDate, final double amount, final String reason, final boolean monthly,
			final long categoryId, final long accountId) {
		Expense expense = new Expense();
		expense.setId(sequenceDAO.getNextPrimaryKey());
		expense.setReadableDayAsString(readableDate);
		expense.setAmount(amount);
		expense.setReason(reason);
		expense.setMonthly(monthly);
		expense.setCategoryId(categoryId);
		expense.setCategoryName(categoryDAO.read(categoryId).getName());
		expense.setAccountId(accountId);

		try (Connection connection = getConnection()) {
			PreparedStatement createStatement;
			createStatement = connection.prepareStatement(SQL_INSERT);
			createStatement.setLong(1, expense.getId());
			createStatement.setDate(2, new java.sql.Date(expense.getDay().getTimeInMillis()), expense.getDay());
			createStatement.setDouble(3, expense.getAmount());
			createStatement.setString(4, expense.getReason());
			createStatement.setBoolean(5, expense.isMonthly());
			createStatement.setLong(6, categoryId);
			createStatement.setLong(7, accountId);
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

	public Expense read(final long id) {
		Expense expense = null;

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_READ_BY_ID);
			readStatement.setLong(1, id);
			ResultSet result = readStatement.executeQuery();
			if (result.next()) {
				expense = new Expense();
				expense.setId(id);
				expense.setDay(result.getDate("day"));
				expense.setAmount(result.getDouble("amount"));
				expense.setReason(result.getString("reason"));
				expense.setMonthly(result.getBoolean("monthly"));
				expense.setCategoryId(result.getLong("category_id"));
				expense.setAccountId(result.getLong("account_id"));
				expense.setCategoryName(result.getString("name"));
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
			updateStatement.setLong(5, expense.getCategoryId());
			String categoryName = categoryDAO.read(expense.getCategoryId()).getName();
			expense.setCategoryName(categoryName);
			updateStatement.setLong(6, expense.getId());
			updated = 1 == updateStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return updated;
	}

	public List<Expense> readByAccountId(final long accountId, final long startIndex, final long endIndex) {
		List<Expense> expenses = new ArrayList<Expense>();

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_READ_BY_ACCOUNT_ID);
			readStatement.setLong(1, accountId);
			readStatement.setLong(2, endIndex);
			ResultSet result = readStatement.executeQuery();
			while (result.next()) {
				if (result.getRow() >= startIndex) {
					Expense expense = new Expense();
					expense.setId(result.getLong("id"));
					expense.setDay(result.getDate("day"));
					expense.setAmount(result.getDouble("amount"));
					expense.setReason(result.getString("reason"));
					expense.setMonthly(result.getBoolean("monthly"));
					expense.setCategoryId(result.getLong("category_id"));
					expense.setAccountId(accountId);
					expense.setCategoryName(result.getString("name"));
					expenses.add(expense);
				}
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return expenses;
	}

	public long readAmountOfExpenses(final long accountId) {
		long amountOfExpenses = -1;

		try (Connection connection = getConnection()) {
			PreparedStatement readStatement;
			readStatement = connection.prepareStatement(SQL_READ_AMOUNT_BY_ACCOUNT_ID);
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
				Expense expense = new Expense();
				expense.setId(result.getLong("id"));
				expense.setDay(result.getDate("day"));
				expense.setAmount(result.getDouble("amount"));
				expense.setReason(result.getString("reason"));
				expense.setMonthly(result.getBoolean("monthly"));
				expense.setCategoryId(categoryId);
				expense.setAccountId(result.getLong("account_id"));
				expense.setCategoryName(result.getString("name"));
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
}
