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
import vu.de.npolke.myexpenses.model.Expense;

@WebServlet("/addexpense")
public class AddExpenseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final DatabaseConnection DB_CONNECT = new DatabaseConnection();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		EntityManager dbConnection = DB_CONNECT.connect();

		TypedQuery<Category> findAllCategoriesQuery = dbConnection.createNamedQuery("Category.findAll", Category.class);
		List<Category> categories = findAllCategoriesQuery.getResultList();

		DB_CONNECT.rollback();
		DB_CONNECT.close();

		HttpSession session = request.getSession();
		session.setAttribute("categories", categories);

		response.sendRedirect("addexpense.jsp");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		expense.setReadableDateAsString(day + "." + month + "." + year);
		expense.setCategory(new Category(Long.parseLong(categoryId)));

		EntityManager dbConnection = DB_CONNECT.connect();

		dbConnection.persist(expense);

		DB_CONNECT.commit();
		DB_CONNECT.close();

		response.sendRedirect("listexpenses");
	}
}
