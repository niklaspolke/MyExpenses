package vu.de.npolke.myexpenses.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vu.de.npolke.myexpenses.model.Category;

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
public class CategoryDAO extends AbstractConnectionDAO {

	private static final String SQL_INSERT = "INSERT INTO Category (id, name, account_id) VALUES (?, ?, ?)";

	private static final String SQL_READ_BY_ID = "SELECT name FROM Category WHERE id = ?";

	private static final String SQL_READ_BY_ACCOUNT_ID = "SELECT id, name FROM Category WHERE account_id = ? ORDER BY name ASC";

	private static final String SQL_UPDATE_BY_ID = "UPDATE Category SET name = ? WHERE id = ?";

	private static final String SQL_DELETE_BY_ID = "DELETE FROM Category WHERE id = ?";

	private SequenceDAO sequenceDAO;

	public CategoryDAO(final SequenceDAO sequenceDAO) {
		this.sequenceDAO = sequenceDAO;
	}

	public Category create(final String name, final long accountId) {
		Category category = null;
		long newId = sequenceDAO.getNextPrimaryKey();

		Connection connection = getConnection();
		PreparedStatement createStatement;
		try {
			createStatement = connection.prepareStatement(SQL_INSERT);
			createStatement.setLong(1, newId);
			createStatement.setString(2, name);
			createStatement.setLong(3, accountId);
			boolean created = 1 == createStatement.executeUpdate();
			if (created) {
				category = new Category();
				category.setId(newId);
				category.setName(name);
			}
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return category;
	}

	public Category read(final long id) {
		Category category = null;

		Connection connection = getConnection();
		PreparedStatement readStatement;
		try {
			readStatement = connection.prepareStatement(SQL_READ_BY_ID);
			readStatement.setLong(1, id);
			ResultSet result = readStatement.executeQuery();
			if (result.next()) {
				category = new Category();
				category.setId(id);
				category.setName(result.getString("name"));
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return category;
	}

	public boolean update(final Category category) {
		boolean updated = false;

		Connection connection = getConnection();
		PreparedStatement updateStatement;
		try {
			updateStatement = connection.prepareStatement(SQL_UPDATE_BY_ID);
			updateStatement.setString(1, category.getName());
			updateStatement.setLong(2, category.getId());
			updated = 1 == updateStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return updated;
	}

	public List<Category> readByAccountId(final long accountId) {
		List<Category> categories = new ArrayList<Category>();

		Connection connection = getConnection();
		PreparedStatement readStatement;
		try {
			readStatement = connection.prepareStatement(SQL_READ_BY_ACCOUNT_ID);
			readStatement.setLong(1, accountId);
			ResultSet result = readStatement.executeQuery();
			while (result.next()) {
				Category category = new Category();
				category.setId(result.getLong("id"));
				category.setName(result.getString("name"));
				categories.add(category);
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return categories;
	}

	public boolean deleteById(final long categoryId) {
		boolean deleted = false;

		Connection connection = getConnection();
		PreparedStatement deleteStatement;
		try {
			deleteStatement = connection.prepareStatement(SQL_DELETE_BY_ID);
			deleteStatement.setLong(1, categoryId);
			deleted = 1 == deleteStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return deleted;
	}
}
