package vu.de.npolke.myexpenses.servlets.util;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
public class JsonObjectTest {

	private JsonObject json;

	@Before
	public void setup() {
		json = new JsonObject();
	}

	@Test
	public void testJson4ChartistJs() {
		String[] names = { "Januar", "Februar" };
		Double[] values = { 10d, 20.55 };

		json.addParameter("labels", names);
		json.addParameter("series", values);

		assertEquals("{\"labels\":[\"Januar\",\"Februar\"],\"series\":[10.0,20.55]}", json.toString());
	}

	@Test
	public void testJson4ChartistJsWithLists() {
		List<String> names = new LinkedList<String>();
		names.add("Januar");
		names.add("Februar");
		List<Double> values = new LinkedList<Double>();
		values.add(10d);
		values.add(20.55);

		json.addParameter("labels", names);
		json.addParameter("series", values);

		assertEquals("{\"labels\":[\"Januar\",\"Februar\"],\"series\":[10.0,20.55]}", json.toString());
	}

	@Test
	public void testJson4ChartJs() {
		Object[] series = new Object[2];

		Map<String,Object> series1 = new TreeMap<String,Object>();
		series1.put("label", "Januar");
		series1.put("value", 10d);
		series[0] = series1;

		Map<String,Object> series2 = new TreeMap<String,Object>();
		series2.put("label", "Februar");
		series2.put("value", 20.55);
		series[1] = series2;

		json.addParameter("series", series);

		assertEquals("{\"series\":[{\"label\":\"Januar\",\"value\":10.0},{\"label\":\"Februar\",\"value\":20.55}]}", json.toString());
	}
}
