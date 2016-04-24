package vu.de.npolke.myexpenses.servlets;

import java.io.IOException;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.model.Account;
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

	void handleServletTask(final ServletReaction reaction, final HttpServletRequest request,
			final HttpServletResponse response, final HttpSession session) throws ServletException, IOException {
		if (reaction != null) {
			for (Entry<String, Object> entry : reaction.getSessionAttributes().entrySet()) {
				if (entry.getValue() != null) {
					session.setAttribute(entry.getKey(), entry.getValue());
				} else {
					session.removeAttribute(entry.getKey());
				}
			}
			for (Entry<String, Object> entry : reaction.getRequestAttributes().entrySet()) {
				if (entry.getValue() != null) {
					request.setAttribute(entry.getKey(), entry.getValue());
				} else {
					request.removeAttribute(entry.getKey());
				}
			}
			if (reaction.getDoRedirect()) {
				response.sendRedirect(reaction.getRedirect());
			} else {
				request.getRequestDispatcher(reaction.getForward()).forward(request, response);
			}
		}
	}

	@Override
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		setEncoding(request, response);

		HttpSession session = request.getSession();

		ServletReaction task = doGet(request, response, session, getAccount(session));
		handleServletTask(task, request, response, session);
	}

	protected ServletReaction doGet(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {
		super.doGet(request, response);
		return null;
	}

	@Override
	protected final void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		setEncoding(request, response);

		HttpSession session = request.getSession();

		ServletReaction task = doPost(request, response, session, getAccount(session));
		handleServletTask(task, request, response, session);
	}

	protected ServletReaction doPost(final HttpServletRequest request, final HttpServletResponse response,
			final HttpSession session, Account account) throws ServletException, IOException {
		super.doPost(request, response);
		return null;
	}
}
