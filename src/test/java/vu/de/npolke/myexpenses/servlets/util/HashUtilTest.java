package vu.de.npolke.myexpenses.servlets.util;

import static org.junit.Assert.assertEquals;

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
public class HashUtilTest {

	@Test
	public void testMD5_password() {
		assertEquals("5f4dcc3b5aa765d61d8327deb882cf99", HashUtil.toMD5("password"));
	}

	@Test
	public void testMD5_password1() {
		assertEquals("7c6a180b36896a0a8c02787eeafb0e4c", HashUtil.toMD5("password1"));
	}

	@Test
	public void testMD5_admin() {
		assertEquals("21232f297a57a5a743894a0e4a801fc3", HashUtil.toMD5("admin"));
	}
}
