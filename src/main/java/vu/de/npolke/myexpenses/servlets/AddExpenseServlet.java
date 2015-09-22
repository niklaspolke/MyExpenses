package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.backend.DatabaseConnection;
import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Category;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.servlets.util.CategoryComparator;

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
@WebServlet("/addexpense")
public class AddExpenseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final DatabaseConnection DB_CONNECT = new DatabaseConnection();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("account");

		EntityManager dbConnection = DB_CONNECT.connect();

		account = dbConnection.find(Account.class, account.getId());
		account.getCategories().sort(new CategoryComparator<Category>());

		DB_CONNECT.rollback();
		DB_CONNECT.close();

		session.setAttribute("account", account);
		session.setAttribute("categories", new ArrayList<Category>(account.getCategories()));
		Calendar now = Calendar.getInstance(Locale.GERMANY);
		now.setTimeInMillis(System.currentTimeMillis());
		session.setAttribute("defaultDate", now);

		response.sendRedirect("addexpense.jsp");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		final Double amount = Double.valueOf(request.getParameter("amount").replaceAll(",", "."));
		final String reason = request.getParameter("reason");
		final String day = request.getParameter("day");
		final String month = request.getParameter("month");
		final String year = request.getParameter("year");
		final String categoryId = request.getParameter("category");

		Expense expense = new Expense();
		expense.setAmount(amount);
		expense.setReason(reason);
		expense.setReadableDayAsString(day + "." + month + "." + year);

		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("account");

		EntityManager dbConnection = DB_CONNECT.connect();

		account = dbConnection.find(Account.class, account.getId());
		Category category = dbConnection.find(Category.class, Long.parseLong(categoryId));

		account.add(expense);
		category.add(expense);

		dbConnection.persist(expense);

		DB_CONNECT.commit();
		DB_CONNECT.close();

		session.setAttribute("account", account);

		response.sendRedirect("listexpenses");
	}
}
