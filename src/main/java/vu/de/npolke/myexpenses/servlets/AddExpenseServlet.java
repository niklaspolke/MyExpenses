package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vu.de.npolke.myexpenses.backend.DatabaseConnection;
import vu.de.npolke.myexpenses.model.Expense;

@WebServlet("/addexpense")
public class AddExpenseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final DatabaseConnection DB_CONNECT = new DatabaseConnection();

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		final Double amount = Double.valueOf(request.getParameter("amount").replaceAll(",", "."));
		final String reason = request.getParameter("reason");
		final String day = request.getParameter("day");
		final String month = request.getParameter("month");
		final String year = request.getParameter("year");

		Expense expense = new Expense();
		expense.setAmount(amount);
		expense.setReason(reason);
		expense.setReadableDateAsString(day + "." + month + "." + year);

		EntityManager dbConnection = DB_CONNECT.connect();
		dbConnection.persist(expense);

		DB_CONNECT.commit();
		DB_CONNECT.close();

		response.sendRedirect("listexpenses");
	}
}
