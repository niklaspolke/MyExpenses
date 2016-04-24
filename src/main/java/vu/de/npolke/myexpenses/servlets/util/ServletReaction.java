package vu.de.npolke.myexpenses.servlets.util;

import java.util.HashMap;
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
public class ServletReaction {

	private Map<String, Object> sessionAttributes = new HashMap<String, Object>();
	private Map<String, Object> requestAttributes = new HashMap<String, Object>();

	private String location = null;

	private boolean doRedirect = true;

	public boolean getDoRedirect() {
		return doRedirect;
	}

	public void setRedirect(final String location) {
		doRedirect = true;
		this.location = location;
	}

	public void setForward(final String location) {
		doRedirect = false;
		this.location = location;
	}

	public String getRedirect() {
		return doRedirect ? this.location : null;
	}

	public String getForward() {
		return !doRedirect ? this.location : null;
	}

	public Map<String, Object> getSessionAttributes() {
		return sessionAttributes;
	}

	public void setSessionAttribute(final String key, final Object value) {
		if (key != null) {
			sessionAttributes.put(key, value);
		}
	}

	public void removeSessionAttribute(final String key) {
		if (key != null) {
			sessionAttributes.remove(key);
		}
	}

	public Map<String, Object> getRequestAttributes() {
		return requestAttributes;
	}

	public void setRequestAttribute(final String key, final Object value) {
		if (key != null) {
			requestAttributes.put(key, value);
		}
	}

	public void removeRequestAttribute(final String key) {
		if (key != null) {
			requestAttributes.remove(key);
		}
	}
}
