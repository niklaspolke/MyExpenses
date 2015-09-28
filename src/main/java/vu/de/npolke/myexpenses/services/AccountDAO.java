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

	private static final String SQL_READ_BY_LOGIN = "SELECT id, login FROM Account WHERE login = ? AND password = ?";

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
			}
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return account;
	}
}
