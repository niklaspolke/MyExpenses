package vu.de.npolke.myexpenses.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
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
	public static final String LOGIN_PAGE           = "login.jsp";
	public static final String STATISTICS_PAGE      = "applicationstatistics.jsp";
	public static final String[] POSTFIX_RESSOURCES = { ".css", ".js", ".png", ".ico" };
	public static final String REGISTER_PAGE        = "register.jsp";
	public static final String REGISTER_URL         = "register";
	public static final String REGISTER_METHOD      = "POST";
	public static final String URI_DELIMITER        = "/";
	public static final String URI_ORIGINAL_URL     = "origurl";
	//@formatter:on

	final Logger logger = Logger.getLogger(LoginFilter.class.getName());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		/*
		 * IMPORTANT!!! If characterEncoding isn't set to UTF-8 and it is
		 * operated on the request parameter, any other setting of the encoding
		 * (AbstractBasicServlet) is for nothing / without any effect and
		 * special characters arent't parsed correctly from forms!
		 */
		request.setCharacterEncoding("UTF-8");
		//@formatter:off
		final HttpServletRequest httpRequest   = (HttpServletRequest) request;
		final String requestURI                = httpRequest.getRequestURI();
		final String contextPath               = httpRequest.getContextPath();
		final String method                    = httpRequest.getMethod();
		final HttpSession session              = httpRequest.getSession();
		final Map<String, String[]> parameter  = httpRequest.getParameterMap();
		final Account account                  = (Account) session.getAttribute("account");
		//@formatter:on

		if (redirectToLoginPage(requestURI, contextPath, method, account)) {
			logger.info("redirect to login page (original target: " + requestURI + ")");
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			session.setAttribute("redirectAfterLogin", getRedirectURL(requestURI, parameter));
			httpResponse.sendRedirect(LOGIN_PAGE);
		} else {
			filterChain.doFilter(request, response);
		}
	}

	public boolean redirectToLoginPage(final String requestURI, final String contextPath, final String method,
			final Account account) {
		boolean isLoggedIn = account != null;
		boolean indirectLoginPage = requestURI.equals(contextPath + URI_DELIMITER) || requestURI.equals(contextPath);
		boolean loginPage = requestURI.startsWith(contextPath + URI_DELIMITER + LOGIN_PAGE);
		boolean statisticsPage = requestURI.startsWith(contextPath + URI_DELIMITER + STATISTICS_PAGE);
		boolean resourceRequest = false;
		if (requestURI.startsWith(contextPath)) {
			for (int index = 0; index < POSTFIX_RESSOURCES.length; index++) {
				if (requestURI.endsWith(POSTFIX_RESSOURCES[index])) {
					resourceRequest = true;
					break;
				}
			}
		}
		boolean registerPage = requestURI.startsWith(contextPath + URI_DELIMITER + REGISTER_PAGE);
		boolean registerRequest = requestURI.startsWith(contextPath + URI_DELIMITER + REGISTER_URL)
				&& REGISTER_METHOD.equalsIgnoreCase(method);

		return !(isLoggedIn || indirectLoginPage || loginPage || statisticsPage || resourceRequest || registerPage
				|| registerRequest);
	}

	protected String extractOrignalRequest(final String requestURI) {
		String originalRequest = requestURI;
		int index;

		if (!originalRequest.endsWith(URI_DELIMITER)) {
			while ((index = originalRequest.indexOf(URI_DELIMITER)) != -1) {
				if (index < originalRequest.length() - 1) {
					originalRequest = originalRequest.substring(index + 1);
				}
			}
		}

		return originalRequest;
	}

	protected String getRedirectURL(final String requestURI, final Map<String, String[]> parameter) {
		String redirectURL = extractOrignalRequest(requestURI);
		String param = null;
		if (parameter.size() > 0) {
			try {
				for (String paramKey : parameter.keySet()) {
					param = param == null ? "?" : param + "&";
					param += paramKey + "=" + URLEncoder.encode(parameter.get(paramKey)[0], "UTF-8");
				}
				redirectURL += param;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return redirectURL;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
