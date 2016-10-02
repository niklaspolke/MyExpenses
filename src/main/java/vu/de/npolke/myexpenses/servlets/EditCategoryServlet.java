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
@WebServlet("/editcategory.jsp")
public class EditCategoryServlet extends AbstractBasicServlet {

	private static final long serialVersionUID = 1L;

	CategoryDAO categoryDAO = (CategoryDAO) DAOFactory.getDAO(Category.class);

	@Override
	protected ServletReaction doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		String id = request.getParameter("id");

		return prepareEditCategory(account, id);
	}

	public ServletReaction prepareEditCategory(final Account account, final String idAsString) {
		ServletReaction reaction = new ServletReaction();

		long id = Long.parseLong(idAsString);
		Category category = categoryDAO.read(account.getId(), id);

		if (category == null || category.getAccountId() != account.getId()) {
			reaction.setRequestAttribute("errorMessage",
					"You tried to edit a non existing category or a category that isn't yours!");
			reaction.setForward("WEB-INF/error.jsp");
		} else {
			reaction.setRequestAttribute("category", category);
			reaction.setForward("WEB-INF/editcategory.jsp");
		}

		return reaction;
	}

	@Override
	public ServletReaction doPost(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {

		final String id = request.getParameter("id");
		final String name = request.getParameter("name");

		return editCategory(account, id, name);
	}

	public ServletReaction editCategory(final Account account, final String idAsString, final String name) {
		long id = Long.parseLong(idAsString);
		Category category = categoryDAO.read(account.getId(), id);
		category.setName(name);
		categoryDAO.update(category);

		ServletReaction reaction = new ServletReaction();
		reaction.setRedirect("listcategories.jsp");
		return reaction;
	}
}
