package vu.de.npolke.myexpenses.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

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
@WebFilter(urlPatterns = { "*.css", "*.js", "*.png" })
public class CachingFilter implements Filter {

	public static final String HEADER_CACHE = "Cache-Control";
	public static final String HEADER_CACHE_VALUE = "private,max-age=2592000";

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
		response.setContentType("text/html;charset=UTF-8");

		filterChain.doFilter(request, response);

		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (httpResponse.containsHeader(HEADER_CACHE)) {
			httpResponse.setHeader(HEADER_CACHE, HEADER_CACHE_VALUE);
		} else {
			httpResponse.addHeader(HEADER_CACHE, HEADER_CACHE_VALUE);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
