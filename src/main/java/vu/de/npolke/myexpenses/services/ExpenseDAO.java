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

	private static final String SQL_INSERT = "INSERT INTO Expense (id, day, amount, reason, category_id, account_id) VALUES (?, ?, ?, ?, ?, ?)";

	private static final String SQL_READ_BY_ID = "SELECT e.day, e.amount, e.reason, e.category_id, e.account_id, c.name FROM Expense e JOIN Category c ON e.category_id = c.id WHERE e.id = ?";

	private static final String SQL_READ_BY_ACCOUNT_ID = "SELECT e.id, e.day, e.amount, e.reason, e.category_id, c.name FROM Expense e JOIN Category c ON e.category_id = c.id WHERE account_id = ? ORDER BY day DESC";

	private static final String SQL_UPDATE_BY_ID = "UPDATE Expense SET day = ?, amount = ?, reason = ?, category_id = ? WHERE id = ?";

	private static final String SQL_DELETE_BY_ID = "DELETE FROM Expense WHERE id = ?";

	private SequenceDAO sequenceDAO;

	private CategoryDAO categoryDAO;

	public ExpenseDAO(final SequenceDAO sequenceDAO, final CategoryDAO categoryDAO) {
		this.sequenceDAO = sequenceDAO;
		this.categoryDAO = categoryDAO;
	}

	public Expense create(final String readableDate, final double amount, final String reason, final long categoryId,
			final long accountId) {
		Expense expense = new Expense();
		expense.setId(sequenceDAO.getNextPrimaryKey());
		expense.setReadableDayAsString(readableDate);
		expense.setAmount(amount);
		expense.setReason(reason);
		expense.setCategoryId(categoryId);
		expense.setCategoryName(categoryDAO.read(categoryId).getName());
		expense.setAccountId(accountId);

		Connection connection = getConnection();
		PreparedStatement createStatement;
		try {
			createStatement = connection.prepareStatement(SQL_INSERT);
			createStatement.setLong(1, expense.getId());
			createStatement.setDate(2, new java.sql.Date(expense.getDay().getTimeInMillis()), expense.getDay());
			createStatement.setDouble(3, expense.getAmount());
			createStatement.setString(4, expense.getReason());
			createStatement.setLong(5, categoryId);
			createStatement.setLong(6, accountId);
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

		Connection connection = getConnection();
		PreparedStatement readStatement;
		try {
			readStatement = connection.prepareStatement(SQL_READ_BY_ID);
			readStatement.setLong(1, id);
			ResultSet result = readStatement.executeQuery();
			if (result.next()) {
				expense = new Expense();
				expense.setId(id);
				expense.setDay(result.getDate("day"));
				expense.setAmount(result.getDouble("amount"));
				expense.setReason(result.getString("reason"));
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

		Connection connection = getConnection();
		PreparedStatement updateStatement;
		try {
			updateStatement = connection.prepareStatement(SQL_UPDATE_BY_ID);
			updateStatement.setDate(1, new java.sql.Date(expense.getDay().getTimeInMillis()), expense.getDay());
			updateStatement.setDouble(2, expense.getAmount());
			updateStatement.setString(3, expense.getReason());
			updateStatement.setLong(4, expense.getCategoryId());
			String categoryName = categoryDAO.read(expense.getCategoryId()).getName();
			expense.setCategoryName(categoryName);
			updateStatement.setLong(5, expense.getId());
			updated = 1 == updateStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return updated;
	}

	public List<Expense> readByAccountId(final long accountId) {
		List<Expense> expenses = new ArrayList<Expense>();

		Connection connection = getConnection();
		PreparedStatement readStatement;
		try {
			readStatement = connection.prepareStatement(SQL_READ_BY_ACCOUNT_ID);
			readStatement.setLong(1, accountId);
			ResultSet result = readStatement.executeQuery();
			while (result.next()) {
				Expense expense = new Expense();
				expense.setId(result.getLong("id"));
				expense.setDay(result.getDate("day"));
				expense.setAmount(result.getDouble("amount"));
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

	public boolean deleteById(final long expenseId) {
		boolean deleted = false;

		Connection connection = getConnection();
		PreparedStatement deleteStatement;
		try {
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
