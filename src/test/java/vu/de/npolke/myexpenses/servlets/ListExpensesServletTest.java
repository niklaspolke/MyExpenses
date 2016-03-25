package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ListExpensesServletTest {

	private ListExpensesServlet servlet;

	public ListExpensesServletTest() {
		servlet = new ListExpensesServlet();
	}

	@Test
	public void parseRequestedPage_NormalWithPage() {
		assertEquals(2, servlet.parseRequestedPage("2", 3));
	}

	@Test
	public void parseRequestedPage_NormalWithoutPage() {
		assertEquals(1, servlet.parseRequestedPage(null, 3));
	}

	@Test
	public void parseRequestedPage_UnparseablePage() {
		assertEquals(1, servlet.parseRequestedPage("abc", 3));
	}

	@Test
	public void parseRequestedPage_TooSmallPage() {
		assertEquals(1, servlet.parseRequestedPage("0", 3));
	}

	@Test
	public void parseRequestedPage_TooBigPage() {
		assertEquals(3, servlet.parseRequestedPage("4", 3));
	}

	@Test
	public void calcAmountOfPages_NoEntries() {
		assertEquals(1, servlet.calcAmountOfPages(0, 10));
	}

	@Test
	public void calcAmountOfPages_LessEntriesThanPageLimit() {
		assertEquals(1, servlet.calcAmountOfPages(9, 10));
	}

	@Test
	public void calcAmountOfPages_EqualEntriesAsPageLimit() {
		assertEquals(1, servlet.calcAmountOfPages(10, 10));
	}

	@Test
	public void calcAmountOfPages_MoreEntriesThanLimit() {
		assertEquals(2, servlet.calcAmountOfPages(11, 10));
	}
}
