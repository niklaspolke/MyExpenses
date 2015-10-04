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

	public static final String		LOGIN_PAGE			= "index.jsp";
	public static final String		LOGIN_URL			= "login";
	public static final String		LOGIN_METHOD		= "POST";
	public static final String[]	POSTFIX_RESSOURCES	= { ".css", ".js", ".png" };
	public static final String		REGISTER_PAGE		= "register.jsp";
	public static final String		REGISTER_URL		= "register";
	public static final String		REGISTER_METHOD		= "POST";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		Logger logger = Logger.getLogger(LoginFilter.class.getName());

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		final String requestURI = httpRequest.getRequestURI();
		final String contextPath = httpRequest.getContextPath();

		HttpSession session = httpRequest.getSession();

		boolean isLoggedIn = session.getAttribute("account") != null;
		boolean loginPage = requestURI.startsWith(contextPath + "/" + LOGIN_PAGE);
		boolean loginRequest = requestURI.startsWith(contextPath + "/" + LOGIN_URL)
				&& LOGIN_METHOD.equalsIgnoreCase(httpRequest.getMethod());
		boolean resourceRequest = requestURI.startsWith(contextPath) && (requestURI.endsWith(POSTFIX_RESSOURCES[0])
				|| requestURI.endsWith(POSTFIX_RESSOURCES[1]) || requestURI.endsWith(POSTFIX_RESSOURCES[2]));
		boolean registerPage = requestURI.startsWith(contextPath + "/" + REGISTER_PAGE);
		boolean registerRequest = requestURI.startsWith(contextPath + "/" + REGISTER_URL)
				&& REGISTER_METHOD.equalsIgnoreCase(httpRequest.getMethod());

		if (isLoggedIn || loginPage || loginRequest || resourceRequest || registerPage || registerRequest) {
			filterChain.doFilter(request, response);
		} else {
			logger.info("redirect to login page (original target: " + requestURI + ")");
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendRedirect(LOGIN_PAGE);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
