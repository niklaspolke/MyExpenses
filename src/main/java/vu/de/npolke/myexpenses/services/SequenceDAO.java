package vu.de.npolke.myexpenses.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class SequenceDAO extends AbstractConnectionDAO {

	private static final String SEQ_PRIMARY_KEYS = "ID_GENERATOR";

	private static final String SQL_READ = "SELECT seq_number FROM Sequence WHERE seq_name = ?";

	private static final String SQL_UPDATE = "UPDATE Sequence SET seq_number = ? WHERE seq_name = ? AND seq_number = ?";

	private long primaryKeyCache = -1;

	public synchronized long getNextPrimaryKey() {
		long nextPrimaryKey = -1;

		Connection connection = getConnection();
		PreparedStatement updateStatement;
		try {
			updateStatement = connection.prepareStatement(SQL_UPDATE);
			updateStatement.setLong(1, primaryKeyCache + 1);
			updateStatement.setString(2, SEQ_PRIMARY_KEYS);
			updateStatement.setLong(3, primaryKeyCache);
			boolean updated = 1 == updateStatement.executeUpdate();
			if (updated) {
				primaryKeyCache += 1;
				nextPrimaryKey = primaryKeyCache;
			} else {
				primaryKeyCache = readPrimaryKey();
				updateStatement = connection.prepareStatement(SQL_UPDATE);
				updateStatement.setLong(1, primaryKeyCache + 1);
				updateStatement.setString(2, SEQ_PRIMARY_KEYS);
				updateStatement.setLong(3, primaryKeyCache);
				updated = 1 == updateStatement.executeUpdate();
				if (updated) {
					primaryKeyCache += 1;
					nextPrimaryKey = primaryKeyCache;
				}
			}
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return nextPrimaryKey;
	}

	private long readPrimaryKey() {
		long primaryKey = -1;

		Connection connection = getConnection();
		PreparedStatement readStatement;
		try {
			readStatement = connection.prepareStatement(SQL_READ);
			readStatement.setString(1, SEQ_PRIMARY_KEYS);
			ResultSet result = readStatement.executeQuery();
			if (result.next()) {
				primaryKey = result.getLong("seq_number");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return primaryKey;
	}
}
