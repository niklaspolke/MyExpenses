package vu.de.npolke.myexpenses.servlets.util;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import vu.de.npolke.myexpenses.services.DAOFactory;
import vu.de.npolke.myexpenses.services.SystemDAO;

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
@WebListener
public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		SystemDAO systemDAO = (SystemDAO) DAOFactory.getDAO(null);
		systemDAO.checkpoint();
	}
}
