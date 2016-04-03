package vu.de.npolke.myexpenses.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.util.HashUtil;

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
public class AccountDAO extends AbstractConnectionDAO {

	private static final String SQL_INSERT = "INSERT INTO Account (id, login, password) VALUES (?, ?, ?)";

	private static final String SQL_UPDATE_BY_ID = "UPDATE Account SET login = ?, password = ? WHERE id = ?";

	private static final String SQL_READ_BY_LOGIN = "SELECT id, login FROM Account WHERE login = ? AND password = ?";

	private SequenceDAO sequenceDAO;

	public AccountDAO(final SequenceDAO sequenceDAO) {
		this.sequenceDAO = sequenceDAO;
	}

	public Account create(final String login, final String password) {
		Account account = null;
		String passwordHash = HashUtil.toMD5(password);

		long newId = sequenceDAO.getNextPrimaryKey();

		try (Connection connection = getConnection()) {
			PreparedStatement createStatement;
			createStatement = connection.prepareStatement(SQL_INSERT);
			createStatement.setLong(1, newId);
			createStatement.setString(2, login);
			createStatement.setString(3, passwordHash);
			boolean created = 1 == createStatement.executeUpdate();
			if (created) {
				account = new Account();
				account.setId(newId);
				account.setLogin(login);
				account.setPassword(passwordHash);
			}
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return account;
	}

	public boolean update(final Account account) {
		boolean updated = false;

		try (Connection connection = getConnection()) {
			PreparedStatement updateStatement;
			updateStatement = connection.prepareStatement(SQL_UPDATE_BY_ID);
			updateStatement.setString(1, account.getLogin());
			updateStatement.setString(2, account.getPassword());
			updateStatement.setLong(3, account.getId());
			updated = 1 == updateStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return updated;
	}

	public Account readByLogin(final String login, final String password) {
		Account account = null;

		String passwordHash = HashUtil.toMD5(password);

		try (Connection connection = getConnection()) {
			PreparedStatement validateLoginStatement;
			validateLoginStatement = connection.prepareStatement(SQL_READ_BY_LOGIN);
			validateLoginStatement.setString(1, login);
			validateLoginStatement.setString(2, passwordHash);
			ResultSet result = validateLoginStatement.executeQuery();
			if (result.next()) {
				account = new Account();
				account.setId(result.getLong("id"));
				account.setLogin(result.getString("login"));
				account.setPassword(passwordHash);
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return account;
	}
}
