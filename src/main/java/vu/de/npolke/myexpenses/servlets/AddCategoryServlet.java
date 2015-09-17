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

@WebServlet("/addcategory")
public class AddCategoryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final DatabaseConnection DB_CONNECT = new DatabaseConnection();

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		final String name = request.getParameter("name");

		Category category = new Category();
		category.setName(name);

		EntityManager dbConnection = DB_CONNECT.connect();
		dbConnection.persist(category);

		DB_CONNECT.commit();
		DB_CONNECT.close();

		response.sendRedirect("listcategories");
	}
}
