package vu.de.npolke.myexpenses.servlets.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import vu.de.npolke.myexpenses.model.Category;
import vu.de.npolke.myexpenses.model.Expense;

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
public class ExpenseComparatorTest {

	private static Expense createExpense(final String readableDate, final String categoryName) {
		Category c = new Category();
		c.setName(categoryName);
		Expense e = new Expense();
		e.setAmount(0.0);
		e.setReadableDayAsString(readableDate);
		e.setCategory(c);
		return e;
	}

	@Test
	public void sortByDate() {
		Expense e1 = createExpense("01.10.2015", "food");
		Expense e2 = createExpense("10.01.2015", "food");
		Expense e3 = createExpense("07.06.2015", "food");
		Expense e4 = createExpense("31.12.2015", "food");

		List<Expense> expenses = new ArrayList<Expense>();
		expenses.add(e1);
		expenses.add(e2);
		expenses.add(e3);
		expenses.add(e4);

		Collections.sort(expenses, new ExpenseComparator<Expense>());

		assertEquals(e4, expenses.get(0));
		assertEquals(e1, expenses.get(1));
		assertEquals(e3, expenses.get(2));
		assertEquals(e2, expenses.get(3));
	}

	@Test
	public void sortByCategory() {
		Expense e1 = createExpense("01.10.2015", "food");
		Expense e2 = createExpense("01.10.2015", "sports");
		Expense e3 = createExpense("01.10.2015", "health");
		Expense e4 = createExpense("01.10.2015", "car");

		List<Expense> expenses = new ArrayList<Expense>();
		expenses.add(e1);
		expenses.add(e2);
		expenses.add(e3);
		expenses.add(e4);

		Collections.sort(expenses, new ExpenseComparator<Expense>());

		assertEquals(e4, expenses.get(0));
		assertEquals(e1, expenses.get(1));
		assertEquals(e3, expenses.get(2));
		assertEquals(e2, expenses.get(3));
	}

	@Test
	public void sortByDateAndCategory() {
		Expense e1 = createExpense("01.10.2015", "food");
		Expense e2 = createExpense("01.10.2014", "sports");
		Expense e3 = createExpense("01.10.2015", "health");
		Expense e4 = createExpense("01.10.2014", "car");

		List<Expense> expenses = new ArrayList<Expense>();
		expenses.add(e1);
		expenses.add(e2);
		expenses.add(e3);
		expenses.add(e4);

		Collections.sort(expenses, new ExpenseComparator<Expense>());

		assertEquals(e1, expenses.get(0));
		assertEquals(e3, expenses.get(1));
		assertEquals(e4, expenses.get(2));
		assertEquals(e2, expenses.get(3));
	}
}
