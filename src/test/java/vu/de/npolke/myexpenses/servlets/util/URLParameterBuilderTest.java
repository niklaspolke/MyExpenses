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
public class URLParameterBuilderTest {

	private URLParameterBuilder parameterBuilder = new URLParameterBuilder();

	@Test
	public void build_noParameter() {
		assertEquals("", parameterBuilder.buildURL());
	}

	@Test
	public void build_oneStringParameter() {
		final String KEY1 = "key1";
		final String VALUE1 = "value1";
		parameterBuilder.add(KEY1, VALUE1);

		assertEquals("?" + KEY1 + "=" + VALUE1, parameterBuilder.buildURL());
	}

	@Test
	public void build_oneStringParameter_withSpecialCharacter() {
		final String KEY1 = "key1";
		final String VALUE1 = "value = 1";
		parameterBuilder.add(KEY1, VALUE1);

		assertEquals("?" + KEY1 + "=value+%3D+1", parameterBuilder.buildURL());
	}

	@Test
	public void build_oneIntegerParameter() {
		final String KEY1 = "key1";
		final int VALUE1 = 4;
		parameterBuilder.add(KEY1, VALUE1);

		assertEquals("?" + KEY1 + "=" + VALUE1, parameterBuilder.buildURL());
	}

	@Test
	public void build_twoDifferentParameters() {
		final String KEY1 = "key1";
		final int VALUE1 = 4;
		final String KEY2 = "key2";
		final String VALUE2 = "value 2";
		parameterBuilder.add(KEY1, VALUE1);
		parameterBuilder.add(KEY2, VALUE2);

		assertEquals("?" + KEY1 + "=" + VALUE1 + "&" + KEY2 + "=value+2", parameterBuilder.buildURL());
	}
}
