package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Category;
import vu.de.npolke.myexpenses.services.CategoryDAO;
import vu.de.npolke.myexpenses.services.DAOFactory;

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
@WebServlet("/editcategory")
public class EditCategoryServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	private CategoryDAO categoryDAO = (CategoryDAO) DAOFactory.getDAO(Category.class);

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		long id = Long.parseLong(request.getParameter("id"));

		Category category = categoryDAO.read(id);

		boolean errorOccured = false;

		if (category == null || category.getAccountId() != account.getId()) {
			errorOccured = true;
		}

		if (errorOccured) {
			request.setAttribute("errorMessage",
					"You tried to edit a non existing category or a category that isn't yours!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
		} else {
			session.setAttribute("category", category);
			response.sendRedirect("editcategory.jsp");
		}
	}

	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response, final HttpSession session,
			Account account) throws ServletException, IOException {

		final String name = request.getParameter("name");

		Category category = (Category) session.getAttribute("category");

		category.setName(name);
		categoryDAO.update(category);

		response.sendRedirect("listcategories");
	}
}
