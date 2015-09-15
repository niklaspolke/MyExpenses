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
import vu.de.npolke.myexpenses.model.Expense;

@WebServlet("/listexpenses")
public class ListExpensesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final DatabaseConnection DB_CONNECT = new DatabaseConnection();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		EntityManager dbConnection = DB_CONNECT.connect();
		dbConnection.getTransaction().setRollbackOnly();

		TypedQuery<Expense> findAllExpensesQuery = dbConnection.createNamedQuery("Expense.findAll", Expense.class);
		List<Expense> allExpenses = findAllExpensesQuery.getResultList();

		HttpSession session = request.getSession();
		session.setAttribute("expenses", allExpenses);

		DB_CONNECT.rollback();
		DB_CONNECT.close();

		response.sendRedirect("listexpenses.jsp");
	}
}
