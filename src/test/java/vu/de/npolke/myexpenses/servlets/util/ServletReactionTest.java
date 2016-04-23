package vu.de.npolke.myexpenses.servlets.util;

import org.junit.Test;
import static org.junit.Assert.*;

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

	private ServletReaction reaction = new ServletReaction();

	@Test
	public void redirect() {
		reaction.setDoRedirect();
		reaction.setNextLocation("addexpense");

		assertTrue(reaction.getDoRedirect());
		assertEquals("addexpense", reaction.getNextLocation());
	}

	@Test
	public void forward() {
		reaction.setDoForward();
		reaction.setNextLocation("listexpenses");

		assertFalse(reaction.getDoRedirect());
		assertEquals("listexpenses", reaction.getNextLocation());
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
}
