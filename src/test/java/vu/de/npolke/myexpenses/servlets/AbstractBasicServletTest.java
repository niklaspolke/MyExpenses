package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

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
public class AbstractBasicServletTest {

	private AbstractBasicServlet servlet;

	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private RequestDispatcher dispatcher;

	@Before
	public void init() {
		servlet = new AbstractBasicServlet();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		dispatcher = mock(RequestDispatcher.class);
	}

	@Test
	public void handleServletTask_redirectNoAttributes() throws ServletException, IOException {
		final String URL = "LOCATION";
		ServletReaction reaction = new ServletReaction();
		reaction.setRedirect(URL);

		try {
			servlet.handleServletReaction(reaction, request, response, session);
		} catch (Exception e) {
			fail();
		}

		// correct navigation
		verify(response).sendRedirect(URL);
		// no session attribute changes
		verify(session, never()).setAttribute(anyString(), anyObject());
		verify(session, never()).removeAttribute(anyString());
		// no request attribute changes
		verify(request, never()).setAttribute(anyString(), anyObject());
		verify(request, never()).removeAttribute(anyString());
	}

	@Test
	public void handleServletTask_requestWithSessionAttributes() throws ServletException, IOException {
		final String URL = "LOCATION";
		ServletReaction reaction = new ServletReaction();
		reaction.setRedirect(URL);

		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("1", new Integer(1));
		attributes.put("2", null);
		attributes.put("3", new Integer(2));
		for (Entry<String, Object> entry : attributes.entrySet()) {
			reaction.setSessionAttribute(entry.getKey(), entry.getValue());
		}

		try {
			servlet.handleServletReaction(reaction, request, response, session);
		} catch (Exception e) {
			fail();
		}

		// correct navigation
		verify(response).sendRedirect(URL);
		// correct session attribute changes
		for (Entry<String, Object> entry : attributes.entrySet()) {
			if (entry.getValue() == null) {
				verify(session).removeAttribute(entry.getKey());
			} else {
				verify(session).setAttribute(entry.getKey(), entry.getValue());
			}
			verify(session, times(2)).setAttribute(anyString(), anyObject());
			verify(session, times(1)).removeAttribute(anyString());
		}
		// no request attribute changes
		verify(request, never()).setAttribute(anyString(), anyObject());
		verify(request, never()).removeAttribute(anyString());
	}

	@Test
	public void handleServletTask_forwardWithRequestAttributes() throws ServletException, IOException {
		final String URL = "LOCATION";
		ServletReaction reaction = new ServletReaction();
		reaction.setForward(URL);

		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("1", new Integer(1));
		attributes.put("2", null);
		attributes.put("3", new Integer(2));
		for (Entry<String, Object> entry : attributes.entrySet()) {
			reaction.setRequestAttribute(entry.getKey(), entry.getValue());
		}

		when(request.getRequestDispatcher(URL)).thenReturn(dispatcher);

		try {
			servlet.handleServletReaction(reaction, request, response, session);
		} catch (Exception e) {
			fail();
		}

		// correct navigation
		verify(dispatcher).forward(request, response);
		// correct session attribute changes
		verify(session, never()).setAttribute(anyString(), anyObject());
		verify(session, never()).removeAttribute(anyString());
		// no request attribute changes
		for (Entry<String, Object> entry : attributes.entrySet()) {
			if (entry.getValue() == null) {
				verify(request).removeAttribute(entry.getKey());
			} else {
				verify(request).setAttribute(entry.getKey(), entry.getValue());
			}
			verify(request, times(2)).setAttribute(anyString(), anyObject());
			verify(request, times(1)).removeAttribute(anyString());
		}
	}
}
