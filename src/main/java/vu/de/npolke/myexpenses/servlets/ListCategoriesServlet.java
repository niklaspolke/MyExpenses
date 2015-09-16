package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.backend.DatabaseConnection;
import vu.de.npolke.myexpenses.model.Category;

@WebServlet("/listcategories")
public class ListCategoriesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final DatabaseConnection DB_CONNECT = new DatabaseConnection();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		EntityManager dbConnection = DB_CONNECT.connect();
		dbConnection.getTransaction().setRollbackOnly();

		TypedQuery<Category> findAllCategoriesQuery = dbConnection.createNamedQuery("Category.findAll", Category.class);
		List<Category> allCategories = findAllCategoriesQuery.getResultList();

		HttpSession session = request.getSession();
		session.setAttribute("categories", allCategories);

		DB_CONNECT.rollback();
		DB_CONNECT.close();

		response.sendRedirect("listcategories.jsp");
	}
}
