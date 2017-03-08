package vu.de.npolke.myexpenses.util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Copyright 2015 Niklas Polke
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * @author Niklas Polke
 */
public class StatisticsGroup {

	private TreeSet<String> allCategories = new TreeSet<String>();
	private TreeSet<StatisticsElement> groupElements = new TreeSet<StatisticsElement>();

	public void add(final StatisticsElement pair) {
		allCategories.add(pair.getCategoryName());
		StatisticsElement existingPair = removeExisting(pair.getMonth(), pair.getCategoryName());
		if (existingPair != null) {
			groupElements.add(StatisticsElement.create(pair.getMonth(), pair.getCategoryId(), pair.getCategoryName(),
					pair.getAmount() + existingPair.getAmount(), pair.isMonthly(), pair.isIncome()));
		} else {
			groupElements.add(pair);
		}
	}

	private StatisticsElement removeExisting(final Month month, final String category) {
		StatisticsElement existingElement = findExisting(month, category);
		if (existingElement != null) {
			groupElements.remove(existingElement);
		}
		return existingElement;
	}

	private StatisticsElement findExisting(final Month month, final String category) {
		StatisticsElement existingElement = null;
		for (StatisticsElement element : groupElements) {
			if (element.getMonth().equals(month) && element.getCategoryName().equals(category)) {
				existingElement = element;
				break;
			}
		}
		return existingElement;
	}

	public List<StatisticsElement> filter(final Month month, final boolean withEmptyCategories) {
		ArrayList<StatisticsElement> extractedMonth = new ArrayList<StatisticsElement>();
		final boolean NONSENSE_DEFAULT = false;
		for (String category : allCategories) {
			StatisticsElement element = findExisting(month, category);
			if (element != null) {
				extractedMonth.add(element);
			} else if (withEmptyCategories) {
				extractedMonth
						.add(StatisticsElement.create(month, 0, category, 0.0, NONSENSE_DEFAULT, NONSENSE_DEFAULT));
			}
		}
		return extractedMonth;
	}
}
