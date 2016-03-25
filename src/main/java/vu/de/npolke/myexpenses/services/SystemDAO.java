package vu.de.npolke.myexpenses.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
public class SystemDAO extends AbstractConnectionDAO {

	private static final String SQL_CHECKPOINT = "CHECKPOINT DEFRAG";

	public SystemDAO() {
	}

	public void checkpoint() {
		try (Connection connection = getConnection()) {
			PreparedStatement createStatement;
			createStatement = connection.prepareStatement(SQL_CHECKPOINT);
			createStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
