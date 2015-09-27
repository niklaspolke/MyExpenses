package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.model.Account;

public class AbstractBasicServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void setEncoding(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
	}

	protected Account getAccount(final HttpSession session) {
		return (Account) session.getAttribute("account");
	}

	@Override
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		setEncoding(request, response);

		HttpSession session = request.getSession();

		doGet(request, response, session, getAccount(session));
	}

	protected void doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {
		super.doGet(request, response);
	}

	@Override
	protected final void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		setEncoding(request, response);

		HttpSession session = request.getSession();

		doPost(request, response, session, getAccount(session));
	}

	protected void doPost(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {
		super.doPost(request, response);
	}
}
