package vu.de.npolke.myexpenses.servlets.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
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
public class URLParameterBuilder {

	private Map<String, String> parameter = new LinkedHashMap<String, String>();

	public URLParameterBuilder add(final String key, final Object value) {
		try {
			parameter.put(key, URLEncoder.encode("" + value, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return this;
	}

	public String buildURL() {
		String paramsAsURL = "";
		for (String key : parameter.keySet()) {
			if (paramsAsURL.equals("")) {
				paramsAsURL = "?";
			} else {
				paramsAsURL += "&";
			}
			paramsAsURL += key + "=" + parameter.get(key);
		}
		return paramsAsURL;
	}
}
