package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.backend.DatabaseConnection;
import vu.de.npolke.myexpenses.servlets.util.JsonObject;

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
@WebServlet("/showstatistics")
public class ShowStatisticsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final DatabaseConnection DB_CONNECT = new DatabaseConnection();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		EntityManager dbConnection = DB_CONNECT.connect();
		dbConnection.getTransaction().setRollbackOnly();

		Query sumsQuery = dbConnection.createNativeQuery("SELECT c.name, sum(e.amount) FROM expenses e JOIN categories c ON e.category_id = c.id GROUP BY e.category_id ORDER BY sum(e.amount) DESC;");
		@SuppressWarnings("unchecked")
		List<Object[]> results = sumsQuery.getResultList();

		DB_CONNECT.rollback();
		DB_CONNECT.close();

		String[] names = new String[results.size()];
		Double[] values = new Double[results.size()];

		int index = 0;
		for (Object[] result : results) {
			names[index] = (String) result[0];
			values[index] = (Double) result[1];
			index++;
		}

		JsonObject json = new JsonObject();
		json.addParameter("labels", names);
		json.addParameter("series", values);

		HttpSession session = request.getSession();
		session.setAttribute("chart", json.toString());

		response.sendRedirect("showstatistics.jsp");
	}
}
