package vu.de.npolke.myexpenses.services;

import java.sql.Connection;

import vu.de.npolke.myexpenses.services.connections.ConnectionStrategy;

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
public abstract class AbstractConnectionDAO {

	private ConnectionStrategy connectionStrategy;

	public void setConnectionStrategy(final ConnectionStrategy connectionStrategy) {
		this.connectionStrategy = connectionStrategy;
	}

	protected Connection getConnection() {
		return connectionStrategy.getConnection();
	}
}
