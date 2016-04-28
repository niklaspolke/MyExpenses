package vu.de.npolke.myexpenses.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vu.de.npolke.myexpenses.model.Account;

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
@WebFilter("/*")
public class LoginFilter implements Filter {

	//@formatter:off
	public static final String LOGIN_PAGE           = "index.jsp";
	public static final String LOGIN_URL            = "login";
	public static final String LOGIN_METHOD         = "POST";
	public static final String[] POSTFIX_RESSOURCES = { ".css", ".js", ".png", ".ico" };
	public static final String REGISTER_PAGE        = "register.jsp";
	public static final String REGISTER_URL         = "register";
	public static final String REGISTER_METHOD      = "POST";
	//@formatter:on

	final Logger logger = Logger.getLogger(LoginFilter.class.getName());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		//@formatter:off
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final String requestURI              = httpRequest.getRequestURI();
		final String contextPath             = httpRequest.getContextPath();
		final String method                  = httpRequest.getMethod();
		final HttpSession session            = httpRequest.getSession();
		final Account account                = (Account) session.getAttribute("account");
		//@formatter:on

		if (redirectToLoginPage(requestURI, contextPath, method, account)) {
			logger.info("redirect to login page (original target: " + requestURI + ")");
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendRedirect(LOGIN_PAGE);
		} else {
			filterChain.doFilter(request, response);
		}
	}

	public boolean redirectToLoginPage(final String requestURI, final String contextPath, final String method,
			final Account account) {
		boolean isLoggedIn = account != null;
		boolean loginPage = requestURI.startsWith(contextPath + "/" + LOGIN_PAGE);
		boolean loginRequest = requestURI.startsWith(contextPath + "/" + LOGIN_URL)
				&& LOGIN_METHOD.equalsIgnoreCase(method);
		boolean resourceRequest = false;
		if (requestURI.startsWith(contextPath)) {
			for (int index = 0; index < POSTFIX_RESSOURCES.length; index++) {
				if (requestURI.endsWith(POSTFIX_RESSOURCES[index])) {
					resourceRequest = true;
					break;
				}
			}
		}
		boolean registerPage = requestURI.startsWith(contextPath + "/" + REGISTER_PAGE);
		boolean registerRequest = requestURI.startsWith(contextPath + "/" + REGISTER_URL)
				&& REGISTER_METHOD.equalsIgnoreCase(method);

		return !(isLoggedIn || loginPage || loginRequest || resourceRequest || registerPage || registerRequest);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
