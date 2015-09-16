package vu.de.npolke.myexpenses.model;

import static org.junit.Assert.*;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

public class ExpenseTest {

	private final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("dd.MM.yyyy");

	private Expense expense;

	@Before
	public void setup() {
		expense = new Expense();
	}

	@Test
	public void toString_NormalValues() {
		expense.setId(4);
		expense.setDatabaseDate("2015.09.15");
		expense.setAmount(Double.valueOf("18.35"));
		expense.setReason("chicken");
		Category cat = new Category();
		cat.setName("food");
		expense.setCategory(cat);
		assertEquals("Expense: #4 (15.09.2015) - 18,35 € - food --> <chicken>", expense.toString());
	}

	@Test
	public void toString_NullValues() {
		assertEquals("Expense: # (??.??.????) - 0,00 € - null --> <>", expense.toString());
	}

	@Test
	public void toString_AmountWithoutDecimalFraction() {
		expense.setAmount(Double.valueOf("18"));
		assertEquals("Expense: # (??.??.????) - 18,00 € - null --> <>", expense.toString());
	}

	@Test
	public void databaseSetDateAsString() {
		expense.setDatabaseDate("2015.09.15");

		assertEquals("15.09.2015", expense.getDate().toString(DateTimeFormat.forPattern("dd.MM.yyyy")));
	}

	@Test
	public void applicationSetDate() {
		expense.setDate(DATE_FORMATTER.parseLocalDate("15.09.2015"));

		assertEquals("2015.09.15", expense.getDatabaseDate());
	}
}
