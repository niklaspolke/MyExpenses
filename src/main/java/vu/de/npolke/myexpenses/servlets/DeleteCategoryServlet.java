package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vu.de.npolke.myexpenses.backend.DatabaseConnection;
import vu.de.npolke.myexpenses.model.Category;

@WebServlet("/deletecategory")
public class DeleteCategoryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final DatabaseConnection DB_CONNECT = new DatabaseConnection();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		final String id = request.getParameter("id");

		EntityManager dbConnection = DB_CONNECT.connect();

		Category category = dbConnection.find(Category.class, Long.parseLong(id));
		if (category.getExpenses().size() == 0) {
			dbConnection.remove(category);
		}

		DB_CONNECT.commit();
		DB_CONNECT.close();

		request.getRequestDispatcher("listcategories").forward(request, response);;
	}
}
