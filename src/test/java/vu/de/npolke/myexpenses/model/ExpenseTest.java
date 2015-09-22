package vu.de.npolke.myexpenses.model;

import static org.junit.Assert.assertEquals;

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
public class ExpenseTest {

	private static final int ID = 4;
	private static final Double AMOUNT = 18.35;
	private static final String REASON = "chicken";
	private static final String DATABASE_DATE = "15.09.15";
	private static final Account ACCOUNT = new Account();

	private Expense expense;

	@Before
	public void setup() {
		expense = new Expense();
	}

	@Test
	public void toString_NormalValues() {
		expense.setId(ID);
		expense.setReadableDayAsString("?#*");
		expense.setReadableDayAsString(DATABASE_DATE);
		expense.setAmount(AMOUNT);
		expense.setReason(REASON);
		expense.setAccount(ACCOUNT);
		Category cat = new Category();
		cat.setName("food");
		expense.setCategory(cat);

		assertEquals(ID, expense.getId());
		assertEquals(AMOUNT, expense.getAmount(), 0.01);
		assertEquals(REASON, expense.getReason());
		assertEquals(ACCOUNT, expense.getAccount());
		assertEquals(DATABASE_DATE, expense.getReadableDayAsString());

		assertEquals("Expense: (15.09.15) - 18,35 € - food for chicken", expense.toString());
	}

	@Test
	public void toString_AmountWithoutDecimalFraction() {
		expense.setAmount(Double.valueOf("18"));
		expense.setReadableDayAsString(DATABASE_DATE);

		assertEquals("Expense: (15.09.15) - 18,00 € - null for ", expense.toString());
	}
}
