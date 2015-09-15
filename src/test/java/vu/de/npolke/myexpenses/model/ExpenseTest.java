package vu.de.npolke.myexpenses.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ExpenseTest {

	private Expense expense;

	@Before
	public void setup() {
		expense = new Expense();
	}

	@Test
	public void toString_NormalValues() {
		expense.setId(4);
		expense.setAmount(Double.valueOf("18.35"));
		expense.setReason("food");
		assertEquals("Expense: #4 - 18,35 € for <food>", expense.toString());
	}

	@Test
	public void toString_NullValues() {
		assertEquals("Expense: # - 0,00 € for <>", expense.toString());
	}

	@Test
	public void toString_AmountWithoutDecimalFraction() {
		expense.setAmount(Double.valueOf("18"));
		assertEquals("Expense: # - 18,00 € for <>", expense.toString());
	}
}
