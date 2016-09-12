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
import vu.de.npolke.myexpenses.services.StatisticsDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;

public class ListExpensesServletTest {

	private static final long ACCOUNT_ID = 123;
	private static final String MONTH = "2016.01";
	private static final long CATEGORY_ID = 13;
	private static final String CATEGORY = "Food";

	private Account account;
	private ListExpensesServlet servlet;

	@Before
	public void init() {
		account = new Account();
		account.setId(ACCOUNT_ID);
		servlet = new ListExpensesServlet();
		servlet.expenseDAO = mock(ExpenseDAO.class);
		servlet.statisticsDAO = mock(StatisticsDAO.class);
	}

	@Test
	public void prepareListExpenses_NoExpenses_NoPage() {
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		when(servlet.expenseDAO.readAmountOfExpenses(ACCOUNT_ID)).thenReturn(0L);
		when(servlet.expenseDAO.readByAccountId(eq(ACCOUNT_ID), anyLong(), anyLong())).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, null, null, null);

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

		final ServletReaction reaction = servlet.prepareListExpenses(account, "1", null, null);

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

		final ServletReaction reaction = servlet.prepareListExpenses(account, "2", null, null);

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

		final ServletReaction reaction = servlet.prepareListExpenses(account, "4", null, null);

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
	public void prepareListExpenses_MonthAndCategory() {
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		Expense expense = new Expense();
		expense.setCategoryId(CATEGORY_ID);
		expense.setCategoryName(CATEGORY);
		expenses.add(expense);
		when(servlet.statisticsDAO.readTopTenByMonthAndCategory(ACCOUNT_ID, MONTH, CATEGORY_ID)).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, null, MONTH, "" + CATEGORY_ID);

		assertNotNull(reaction);
		// correct month in request
		assertEquals(MONTH, reaction.getRequestAttributes().get("month"));
		// correct categoryId in request
		assertEquals(CATEGORY, reaction.getRequestAttributes().get("category"));
		// correct expenses in session
		assertSame(expenses, reaction.getSessionAttributes().get("expenses"));
		// correct navigation
		assertEquals("listexpenses.jsp", reaction.getForward());
	}

	@Test
	public void prepareListExpenses_MonthAndCategory_UnparseableCategory() {
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		when(servlet.statisticsDAO.readTopTenByMonthAndCategory(ACCOUNT_ID, MONTH, 0)).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, null, MONTH, "error" + CATEGORY_ID);

		assertNotNull(reaction);
		// correct month in request
		assertEquals(MONTH, reaction.getRequestAttributes().get("month"));
		// correct categoryId in request
		assertEquals("error" + CATEGORY_ID, reaction.getRequestAttributes().get("category"));
		// correct expenses in session
		assertSame(expenses, reaction.getSessionAttributes().get("expenses"));
		// correct navigation
		assertEquals("listexpenses.jsp", reaction.getForward());
	}

	@Test
	public void prepareListExpenses_MonthAndCategory_NoMonth() {
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		Expense expense = new Expense();
		expense.setCategoryId(CATEGORY_ID);
		expense.setCategoryName(CATEGORY);
		expenses.add(expense);
		when(servlet.statisticsDAO.readTopTenByMonthAndCategory(ACCOUNT_ID, null, CATEGORY_ID)).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, null, null, "" + CATEGORY_ID);

		assertNotNull(reaction);
		// correct month in request
		assertEquals(null, reaction.getRequestAttributes().get("month"));
		// correct categoryId in request
		assertEquals(CATEGORY, reaction.getRequestAttributes().get("category"));
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
