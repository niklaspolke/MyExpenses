package vu.de.npolke.myexpenses.servlets.util;

import java.util.Comparator;

import vu.de.npolke.myexpenses.model.Category;

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
public class CategoryComparator<T> implements Comparator<T> {

	@Override
	public int compare(T o1, T o2) {
		Category category1 = (Category) o1;
		Category category2 = (Category) o2;

		return category1.getName().compareTo(category2.getName());
	}
}
