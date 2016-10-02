package vu.de.npolke.myexpenses.servlets.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
public class ServletReactionTest {

	private static final String DEFAULT_URL = "/myurl/index.jsp";

	private ServletReaction reaction = new ServletReaction();

	@Test
	public void redirect() {
		reaction.setRedirect(DEFAULT_URL);

		assertEquals(DEFAULT_URL, reaction.getRedirect());
		assertNull(reaction.getForward());
	}

	@Test
	public void forward() {
		reaction.setForward(DEFAULT_URL);

		assertNull(reaction.getRedirect());
		assertEquals(DEFAULT_URL, reaction.getForward());
	}

	@Test
	public void setSessionAttribute() {
		Double attr1 = new Double(3);
		Integer attr2 = new Integer(2);
		Object attr3 = null;

		reaction.setSessionAttribute("attribute1", attr1);
		reaction.setSessionAttribute("attribute2", attr2);
		reaction.setSessionAttribute("attribute3", attr3);

		assertEquals(3, reaction.getSessionAttributes().size());
		assertEquals(attr1, reaction.getSessionAttributes().get("attribute1"));
		assertEquals(attr2, reaction.getSessionAttributes().get("attribute2"));
		assertNull(reaction.getSessionAttributes().get("attribute3"));
	}

	@Test
	public void removeSessionAttribute() {
		Double attr1 = new Double(3);
		Integer attr2 = new Integer(2);
		Object attr3 = null;
		reaction.setSessionAttribute("attribute1", attr1);
		reaction.setSessionAttribute("attribute2", attr2);
		reaction.setSessionAttribute("attribute3", attr3);

		reaction.removeSessionAttribute("attribute2");
		reaction.removeSessionAttribute("attribute3");

		assertEquals(1, reaction.getSessionAttributes().size());
		assertEquals(attr1, reaction.getSessionAttributes().get("attribute1"));
		assertNull(reaction.getSessionAttributes().get("attribute2"));
		assertNull(reaction.getSessionAttributes().get("attribute3"));
	}

	@Test
	public void setRequestAttribute() {
		Double attr1 = new Double(3);
		Integer attr2 = new Integer(2);
		Object attr3 = null;

		reaction.setRequestAttribute("attribute1", attr1);
		reaction.setRequestAttribute("attribute2", attr2);
		reaction.setRequestAttribute("attribute3", attr3);

		assertEquals(3, reaction.getRequestAttributes().size());
		assertEquals(attr1, reaction.getRequestAttributes().get("attribute1"));
		assertEquals(attr2, reaction.getRequestAttributes().get("attribute2"));
		assertNull(reaction.getRequestAttributes().get("attribute3"));
	}

	@Test
	public void removeRequestAttribute() {
		Double attr1 = new Double(3);
		Integer attr2 = new Integer(2);
		Object attr3 = null;
		reaction.setRequestAttribute("attribute1", attr1);
		reaction.setRequestAttribute("attribute2", attr2);
		reaction.setRequestAttribute("attribute3", attr3);

		reaction.removeRequestAttribute("attribute2");
		reaction.removeRequestAttribute("attribute3");

		assertEquals(1, reaction.getRequestAttributes().size());
		assertEquals(attr1, reaction.getRequestAttributes().get("attribute1"));
		assertNull(reaction.getRequestAttributes().get("attribute2"));
		assertNull(reaction.getRequestAttributes().get("attribute3"));
	}

	@Test
	public void addRequestParameter() {
		final String KEY = "mykey";
		final String VALUE = "value";

		URLParameterBuilder parameter = reaction.setRedirect(DEFAULT_URL);
		parameter.add(KEY, VALUE);

		assertEquals(DEFAULT_URL + "?" + KEY + "=" + VALUE, reaction.getRedirect());
	}

	@Test
	public void addTwoRequestParameterWithBuilder() {
		final String KEY1 = "mykey";
		final String VALUE1 = "value";
		final String KEY2 = "mykey2";
		final String VALUE2 = "value2";

		reaction.setRedirect(DEFAULT_URL).add(KEY1, VALUE1).add(KEY2, VALUE2);

		// expect: /myurl/index.jsp?mykey=value&mykey2=value2
		assertEquals(DEFAULT_URL + "?" + KEY1 + "=" + VALUE1 + "&" + KEY2 + "=" + VALUE2, reaction.getRedirect());
	}

	@Test
	public void changeRequestParameterWithNewRedirect() {
		final String KEY1 = "mykey";
		final String VALUE1 = "value";
		final String KEY2 = "mykey2";
		final String VALUE2 = "value2";
		final String OTHER_URL = "otherURL";

		reaction.setRedirect(DEFAULT_URL).add(KEY1, VALUE1);
		reaction.setRedirect(OTHER_URL).add(KEY2, VALUE2);

		// expect: otherURL?mykey=value
		assertEquals(OTHER_URL + "?" + KEY2 + "=" + VALUE2, reaction.getRedirect());
	}
}
