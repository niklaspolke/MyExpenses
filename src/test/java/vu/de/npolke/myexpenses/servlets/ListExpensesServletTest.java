package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.ExpenseDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;

public class ListExpensesServletTest {

	private static final long ACCOUNT_ID = 123;

	private Account account;
	private ListExpensesServlet servlet;

	@Before
	public void init() {
		account = new Account();
		account.setId(ACCOUNT_ID);
		servlet = new ListExpensesServlet();
		servlet.expenseDAO = mock(ExpenseDAO.class);
	}

	@Test
	public void prepareListExpenses_NoExpenses_NoPage() {
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		when(servlet.expenseDAO.readAmountOfExpenses(ACCOUNT_ID)).thenReturn(0L);
		when(servlet.expenseDAO.readByAccountId(eq(ACCOUNT_ID), anyLong(), anyLong())).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, null);

		assertNotNull(reaction);
		// correct page in request
		assertEquals(1, reaction.getRequestAttributes().get("page"));
		// correct pageMax in request
		assertEquals(1, reaction.getRequestAttributes().get("pageMax"));
		// correct expenses in session
		assertSame(expenses, reaction.getSessionAttributes().get("expenses"));
		// correct navigation
		assertEquals("listexpenses.jsp", reaction.getForward());
	}

	@Test
	public void prepareListExpenses_Expenses_WithPage() {
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		expenses.add(new Expense());
		expenses.add(new Expense());
		expenses.add(new Expense());
		when(servlet.expenseDAO.readAmountOfExpenses(ACCOUNT_ID)).thenReturn(3L);
		when(servlet.expenseDAO.readByAccountId(eq(ACCOUNT_ID), anyLong(), anyLong())).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, "1");

		assertNotNull(reaction);
		// correct page in request
		assertEquals(1, reaction.getRequestAttributes().get("page"));
		// correct pageMax in request
		assertEquals(1, reaction.getRequestAttributes().get("pageMax"));
		// correct expenses in session
		assertSame(expenses, reaction.getSessionAttributes().get("expenses"));
		// correct navigation
		assertEquals("listexpenses.jsp", reaction.getForward());
	}

	@Test
	public void prepareListExpenses_Expenses_WithPage_TwoPages() {
		final int AMOUNT = 12;
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		addExpenses(expenses, AMOUNT);
		when(servlet.expenseDAO.readAmountOfExpenses(ACCOUNT_ID)).thenReturn(12L);
		when(servlet.expenseDAO.readByAccountId(eq(ACCOUNT_ID), anyLong(), anyLong())).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, "2");

		assertNotNull(reaction);
		// correct page in request
		assertEquals(2, reaction.getRequestAttributes().get("page"));
		// correct pageMax in request
		assertEquals(2, reaction.getRequestAttributes().get("pageMax"));
		// correct expenses in session
		assertSame(expenses, reaction.getSessionAttributes().get("expenses"));
		// correct navigation
		assertEquals("listexpenses.jsp", reaction.getForward());
	}

	@Test
	public void prepareListExpenses_Expenses_WithWrongPage_TwoPages() {
		final int AMOUNT = 12;
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		addExpenses(expenses, AMOUNT);
		when(servlet.expenseDAO.readAmountOfExpenses(ACCOUNT_ID)).thenReturn(12L);
		when(servlet.expenseDAO.readByAccountId(eq(ACCOUNT_ID), anyLong(), anyLong())).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, "4");

		assertNotNull(reaction);
		// correct page in request
		assertEquals(2, reaction.getRequestAttributes().get("page"));
		// correct pageMax in request
		assertEquals(2, reaction.getRequestAttributes().get("pageMax"));
		// correct expenses in session
		assertSame(expenses, reaction.getSessionAttributes().get("expenses"));
		// correct navigation
		assertEquals("listexpenses.jsp", reaction.getForward());
	}

	private void addExpenses(final List<Expense> expenses, final int amount) {
		for (int counter = 0; counter < amount; counter++) {
			expenses.add(new Expense());
		}
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
