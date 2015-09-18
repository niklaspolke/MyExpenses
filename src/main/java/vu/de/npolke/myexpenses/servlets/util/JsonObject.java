package vu.de.npolke.myexpenses.servlets.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//@formatter:off
public class JsonObject {

	private static final String OBJ_START = "{";
	private static final String OBJ_END = "}";
	private static final String KEY_VALUE_DELIMITER = ":";
	private static final String PARAMETER_DELIMITER = ",";
	private static final String STRING_START = "\"";
	private static final String STRING_END = STRING_START;
	private static final String ARRAY_START = "[";
	private static final String ARRAY_END = "]";

	private Map<String, Object> parameter = new HashMap<String, Object>();

	public JsonObject addParameter(final String key, final Object value) {
		parameter.put(key, value);
		return this;
	}

	private static void format(final String string, StringBuilder jsonRepresentation) {
		jsonRepresentation
			.append(STRING_START)
			.append(string)
			.append(STRING_END);
	}

	private static void format(final List<Object> list, StringBuilder jsonRepresentation) {
		jsonRepresentation.append(ARRAY_START);

		boolean additionalParam = false;
		for (Object objectInList : list) {
			jsonRepresentation.append(additionalParam ? PARAMETER_DELIMITER : "");
			format(objectInList, jsonRepresentation);
			additionalParam = true;
		}

		jsonRepresentation.append(ARRAY_END);
	}

	private static void format(final Map<String, Object> map, StringBuilder jsonRepresentation) {
		jsonRepresentation.append(OBJ_START);

		boolean additionalParam = false;
		for (String key : map.keySet()) {
			jsonRepresentation
						.append(additionalParam ? PARAMETER_DELIMITER : "")
						.append(STRING_START)
						.append(key)
						.append(STRING_END)
						.append(KEY_VALUE_DELIMITER);
			format(map.get(key), jsonRepresentation);
			additionalParam = true;
		}

		jsonRepresentation.append(OBJ_END);
	}

	private static void format(final Object[] array, StringBuilder jsonRepresentation) {
		jsonRepresentation.append(ARRAY_START);

		boolean additionalParam = false;
		for (Object objectInList : array) {
			jsonRepresentation.append(additionalParam ? PARAMETER_DELIMITER : "");
			format(objectInList, jsonRepresentation);
			additionalParam = true;
		}

		jsonRepresentation.append(ARRAY_END);
	}

	private static void format(final Object object, StringBuilder jsonRepresentation) {
		if (object instanceof String) {
			format((String) object, jsonRepresentation);
		} else if (object instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) object;
			format(list, jsonRepresentation);
		} else if (object instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) object;
			format(map, jsonRepresentation);
		} else if(object instanceof Object[]) {
			Object[] array = (Object[]) object;
			format(array, jsonRepresentation);
		} else {
			jsonRepresentation.append(object);
		}
	}

	@Override
	public String toString() {
		StringBuilder jsonRepresentation = new StringBuilder().append(OBJ_START);

		boolean additionalParam = false;
		for (String key : parameter.keySet()) {
			jsonRepresentation
						.append(additionalParam ? PARAMETER_DELIMITER : "")
						.append(STRING_START)
						.append(key)
						.append(STRING_END)
						.append(KEY_VALUE_DELIMITER);
			format(parameter.get(key), jsonRepresentation);
			additionalParam = true;
		}

		return jsonRepresentation.append(OBJ_END).toString();
	}
}
