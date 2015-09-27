package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.backend.DatabaseConnection;
import vu.de.npolke.myexpenses.model.Account;
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
@WebServlet("/deletecategory")
public class DeleteCategoryServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	private final DatabaseConnection DB_CONNECT = new DatabaseConnection();

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		String id = request.getParameter("id");
		String confirmed = request.getParameter("confirmed");

		if ("yes".equalsIgnoreCase(confirmed)) {
			EntityManager dbConnection = DB_CONNECT.connect();

			Category category = dbConnection.find(Category.class, Long.parseLong(id));
			account = dbConnection.find(Account.class, account.getId());
			if (category.getExpenses().size() == 0) {
				account.remove(category);
				category.setAccount(account);
				dbConnection.remove(category);
			}

			DB_CONNECT.commit();
			DB_CONNECT.close();
		}

		session.setAttribute("account", account);

		request.getRequestDispatcher("listcategories").forward(request, response);
		;
	}
}
