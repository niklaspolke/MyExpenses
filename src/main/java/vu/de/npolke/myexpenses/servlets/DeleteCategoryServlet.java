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
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;

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

	CategoryDAO categoryDAO = (CategoryDAO) DAOFactory.getDAO(Category.class);

	@Override
	protected ServletReaction doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		String categoryId = request.getParameter("id");
		String isDeleteConfirmed = request.getParameter("confirmed");

		return deleteCategory(account, categoryId, isDeleteConfirmed);
	}

	public ServletReaction deleteCategory(final Account account, final String idAsString, final String confirmed) {
		long categoryId = Long.parseLong(idAsString);

		ServletReaction reaction = new ServletReaction();
		reaction.setDoForward();
		reaction.setNextLocation("listcategories");

		if ("yes".equalsIgnoreCase(confirmed)) {
			Category category = categoryDAO.read(categoryId);
			if (category != null && category.getAccountId() == account.getId()) {
				categoryDAO.deleteById(categoryId);
			} else {
				reaction.setRequestAttribute("errorMessage",
						"You tried to delete a non existing category or a category that isn't yours!");
				reaction.setNextLocation("error.jsp");
			}
		}

		return reaction;
	}
}
