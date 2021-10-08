package vu.de.npolke.myexpenses.filter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static vu.de.npolke.myexpenses.filter.CachingFilter.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

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
public class CachingFilterTest {

	private CachingFilter filter = new CachingFilter();

	private HttpServletRequest request;
	private FilterChain filterChain;
	private HttpServletResponse response;

	@Before
	public void init() {
		request = mock(HttpServletRequest.class);
		filterChain = mock(FilterChain.class);
		response = mock(HttpServletResponse.class);
	}

	@Test
	public void changeExistingHeader() throws IOException, ServletException {
		when(response.containsHeader(HEADER_CACHE)).thenReturn(true);
		when(request.getRequestURI()).thenReturn("");

		filter.doFilter(request, response, filterChain);

		verify(response).setHeader(HEADER_CACHE, HEADER_CACHE_VALUE);
		verify(response, never()).addHeader(HEADER_CACHE, HEADER_CACHE_VALUE);
	}

	@Test
	public void addNonExistingHeader() throws IOException, ServletException {
		when(response.containsHeader(HEADER_CACHE)).thenReturn(false);
		when(request.getRequestURI()).thenReturn("");

		filter.doFilter(request, response, filterChain);

		verify(response, never()).setHeader(HEADER_CACHE, HEADER_CACHE_VALUE);
		verify(response).addHeader(HEADER_CACHE, HEADER_CACHE_VALUE);
	}

	@Test
	public void contentTypeCSS() throws IOException, ServletException {
		when(request.getRequestURI()).thenReturn("abcd/abc.css");

		filter.doFilter(request, response, filterChain);

		verify(response).setContentType(CONTENTTYPE_CSS);
	}

	@Test
	public void contentTypeNonCSS() throws IOException, ServletException {
		when(request.getRequestURI()).thenReturn("abcd/abc.png");

		filter.doFilter(request, response, filterChain);

		verify(response).setContentType(CONTENTTYPE_DEFAULT);
	}
}
