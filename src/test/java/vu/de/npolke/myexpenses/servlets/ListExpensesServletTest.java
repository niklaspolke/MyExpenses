package vu.de.npolke.myexpenses.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;
import vu.de.npolke.myexpenses.model.Expense;
import vu.de.npolke.myexpenses.services.ExpenseDAO;
import vu.de.npolke.myexpenses.services.StatisticsDAO;
import vu.de.npolke.myexpenses.servlets.util.ServletReaction;
import vu.de.npolke.myexpenses.util.Month;

public class ListExpensesServletTest {

	private static final String LISTEXPENSES_JSP = "WEB-INF/listexpenses.jsp";
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
	public void getCurrentMonth() {
		final Calendar now = Calendar.getInstance(Locale.GERMANY);
		now.setTimeInMillis(System.currentTimeMillis());

		final Month RESULT = servlet.getCurrentMonth();

		assertEquals(now.get(Calendar.YEAR), RESULT.getYear());
		assertEquals(now.get(Calendar.MONTH) + 1, RESULT.getMonth());
	}

	@Test
	public void calcMaxMonth_NoMonths() {
		Month month = Month.create(2015, 5);

		Month maxMonth = servlet.calcMaxMonth(month, new ArrayList<Month>());

		assertEquals(month, maxMonth);
	}

	@Test
	public void calcMaxMonth_CurrentIsBigger() {
		Month month = Month.create(2015, 5);
		List<Month> months = new ArrayList<Month>();
		months.add(Month.create(2015, 4));
		months.add(Month.create(2015, 3));

		Month maxMonth = servlet.calcMaxMonth(month, months);

		assertEquals(month, maxMonth);
	}

	@Test
	public void calcMaxMonth_MaxFromList() {
		Month month = Month.create(2015, 5);
		List<Month> months = new ArrayList<Month>();
		months.add(Month.create(2015, 6));
		months.add(Month.create(2015, 3));

		Month maxMonth = servlet.calcMaxMonth(month, months);

		assertEquals("2015.06", maxMonth.toString());
	}

	@Test
	public void calcMinMonth_NoMonths() {
		Month month = Month.create(2015, 5);

		Month minMonth = servlet.calcMinMonth(month, new ArrayList<Month>());

		assertEquals(month, minMonth);
	}

	@Test
	public void calcMinMonth_CurrentIsSmaller() {
		Month month = Month.create(2015, 5);
		List<Month> months = new ArrayList<Month>();
		months.add(Month.create(2015, 7));
		months.add(Month.create(2015, 6));

		Month minMonth = servlet.calcMinMonth(month, months);

		assertEquals(month, minMonth);
	}

	@Test
	public void calcMinMonth_MinFromList() {
		Month month = Month.create(2015, 5);
		List<Month> months = new ArrayList<Month>();
		months.add(Month.create(2015, 6));
		months.add(Month.create(2015, 3));

		Month minMonth = servlet.calcMinMonth(month, months);

		assertEquals("2015.03", minMonth.toString());
	}

	@Test
	public void prepareListExpenses_NoExpenses_NoPage_NoMessage() {
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		when(servlet.expenseDAO.readAmountOfExpensesInFuture(ACCOUNT_ID)).thenReturn(0L);
		when(servlet.expenseDAO.readAmountOfExpensesUpToNow(ACCOUNT_ID)).thenReturn(0L);
		when(servlet.expenseDAO.readByAccountId(ACCOUNT_ID, 1, 10, false)).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, null, null, null, null, null);

		assertNotNull(reaction);
		// correct mode in request
		assertEquals(ListExpensesServlet.MODE_EXPENSES, reaction.getRequestAttributes().get("mode"));
		// correct page in request
		assertEquals(0, reaction.getRequestAttributes().get("page"));
		// correct pageMin in request
		assertEquals(0, reaction.getRequestAttributes().get("pageMin"));
		// correct pageMax in request
		assertEquals(0, reaction.getRequestAttributes().get("pageMax"));
		// correct message / no message
		assertNull(reaction.getRequestAttributes().get("message"));
		// correct expenses in session
		assertSame(expenses, reaction.getRequestAttributes().get("expenses"));
		// correct navigation
		assertEquals(LISTEXPENSES_JSP, reaction.getForward());
	}

	@Test
	public void prepareListExpenses_WithMessage() {
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		when(servlet.expenseDAO.readAmountOfExpensesInFuture(ACCOUNT_ID)).thenReturn(0L);
		when(servlet.expenseDAO.readAmountOfExpensesUpToNow(ACCOUNT_ID)).thenReturn(0L);
		when(servlet.expenseDAO.readByAccountId(ACCOUNT_ID, 1, 10, false)).thenReturn(expenses);
		final String MESSAGE = "My Message";

		final ServletReaction reaction = servlet.prepareListExpenses(account, null, null, null, null, MESSAGE);

		assertNotNull(reaction);
		assertEquals(MESSAGE, reaction.getRequestAttributes().get("message"));
		assertTrue(reaction.getSessionAttributes().containsKey("message"));
		assertNull(reaction.getSessionAttributes().get("message"));
	}

	@Test
	public void prepareListExpenses_Expenses_WithPage() {
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		expenses.add(new Expense());
		expenses.add(new Expense());
		expenses.add(new Expense());
		when(servlet.expenseDAO.readAmountOfExpensesInFuture(ACCOUNT_ID)).thenReturn(0L);
		when(servlet.expenseDAO.readAmountOfExpensesUpToNow(ACCOUNT_ID)).thenReturn(3L);
		when(servlet.expenseDAO.readByAccountId(ACCOUNT_ID, 1, 10, false)).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, "0", null, null, null, null);

		assertNotNull(reaction);
		// correct mode in request
		assertEquals(ListExpensesServlet.MODE_EXPENSES, reaction.getRequestAttributes().get("mode"));
		// correct page in request
		assertEquals(0, reaction.getRequestAttributes().get("page"));
		// correct pageMin in request
		assertEquals(0, reaction.getRequestAttributes().get("pageMin"));
		// correct pageMax in request
		assertEquals(0, reaction.getRequestAttributes().get("pageMax"));
		// correct expenses in session
		assertSame(expenses, reaction.getRequestAttributes().get("expenses"));
		// correct navigation
		assertEquals(LISTEXPENSES_JSP, reaction.getForward());
	}

	@Test
	public void prepareListExpenses_Expenses_WithPage_Future() {
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		expenses.add(new Expense());
		expenses.add(new Expense());
		expenses.add(new Expense());
		when(servlet.expenseDAO.readAmountOfExpensesInFuture(ACCOUNT_ID)).thenReturn(3L);
		when(servlet.expenseDAO.readAmountOfExpensesUpToNow(ACCOUNT_ID)).thenReturn(3L);
		when(servlet.expenseDAO.readByAccountId(ACCOUNT_ID, 1, 10, true)).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, "1", null, null, null, null);

		assertNotNull(reaction);
		// correct mode in request
		assertEquals(ListExpensesServlet.MODE_EXPENSES, reaction.getRequestAttributes().get("mode"));
		// correct page in request
		assertEquals(1, reaction.getRequestAttributes().get("page"));
		// correct pageMin in request
		assertEquals(0, reaction.getRequestAttributes().get("pageMin"));
		// correct pageMax in request
		assertEquals(1, reaction.getRequestAttributes().get("pageMax"));
		// correct expenses in session
		assertSame(expenses, reaction.getRequestAttributes().get("expenses"));
		// correct navigation
		assertEquals(LISTEXPENSES_JSP, reaction.getForward());
	}

	@Test
	public void prepareListExpenses_Expenses_WithPage_TwoPages() {
		final int AMOUNT = 12;
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		addExpenses(expenses, AMOUNT);
		when(servlet.expenseDAO.readAmountOfExpensesInFuture(ACCOUNT_ID)).thenReturn(0L);
		when(servlet.expenseDAO.readAmountOfExpensesUpToNow(ACCOUNT_ID)).thenReturn(12L);
		when(servlet.expenseDAO.readByAccountId(ACCOUNT_ID, 11, 20, false)).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, "-1", null, null, null, null);

		assertNotNull(reaction);
		// correct mode in request
		assertEquals(ListExpensesServlet.MODE_EXPENSES, reaction.getRequestAttributes().get("mode"));
		// correct page in request
		assertEquals(-1, reaction.getRequestAttributes().get("page"));
		// correct pageMin in request
		assertEquals(-1, reaction.getRequestAttributes().get("pageMin"));
		// correct pageMax in request
		assertEquals(0, reaction.getRequestAttributes().get("pageMax"));
		// correct expenses in session
		assertSame(expenses, reaction.getRequestAttributes().get("expenses"));
		// correct navigation
		assertEquals(LISTEXPENSES_JSP, reaction.getForward());
	}

	@Test
	public void prepareListExpenses_Expenses_WithPage_TwoPages_Future() {
		final int AMOUNT = 12;
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		addExpenses(expenses, AMOUNT);
		when(servlet.expenseDAO.readAmountOfExpensesInFuture(ACCOUNT_ID)).thenReturn(12L);
		when(servlet.expenseDAO.readAmountOfExpensesUpToNow(ACCOUNT_ID)).thenReturn(5L);
		when(servlet.expenseDAO.readByAccountId(ACCOUNT_ID, 11, 20, true)).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, "2", null, null, null, null);

		assertNotNull(reaction);
		// correct mode in request
		assertEquals(ListExpensesServlet.MODE_EXPENSES, reaction.getRequestAttributes().get("mode"));
		// correct page in request
		assertEquals(2, reaction.getRequestAttributes().get("page"));
		// correct pageMin in request
		assertEquals(0, reaction.getRequestAttributes().get("pageMin"));
		// correct pageMax in request
		assertEquals(2, reaction.getRequestAttributes().get("pageMax"));
		// correct expenses in session
		assertSame(expenses, reaction.getRequestAttributes().get("expenses"));
		// correct navigation
		assertEquals(LISTEXPENSES_JSP, reaction.getForward());
	}

	@Test
	public void prepareListExpenses_Expenses_WithWrongPage_TwoPages() {
		final int AMOUNT = 12;
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		addExpenses(expenses, AMOUNT);
		when(servlet.expenseDAO.readAmountOfExpensesInFuture(ACCOUNT_ID)).thenReturn(0L);
		when(servlet.expenseDAO.readAmountOfExpensesUpToNow(ACCOUNT_ID)).thenReturn(12L);
		when(servlet.expenseDAO.readByAccountId(ACCOUNT_ID, 11, 20, false)).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, "-4", null, null, null, null);

		assertNotNull(reaction);
		// correct mode in request
		assertEquals(ListExpensesServlet.MODE_EXPENSES, reaction.getRequestAttributes().get("mode"));
		// correct page in request
		assertEquals(-1, reaction.getRequestAttributes().get("page"));
		// correct pageMin in request
		assertEquals(-1, reaction.getRequestAttributes().get("pageMin"));
		// correct pageMax in request
		assertEquals(0, reaction.getRequestAttributes().get("pageMax"));
		// correct expenses in session
		assertSame(expenses, reaction.getRequestAttributes().get("expenses"));
		// correct navigation
		assertEquals(LISTEXPENSES_JSP, reaction.getForward());
	}

	@Test
	public void prepareListExpenses_Expenses_WithWrongPage_TwoPages_Future() {
		final int AMOUNT = 12;
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		addExpenses(expenses, AMOUNT);
		when(servlet.expenseDAO.readAmountOfExpensesInFuture(ACCOUNT_ID)).thenReturn(12L);
		when(servlet.expenseDAO.readAmountOfExpensesUpToNow(ACCOUNT_ID)).thenReturn(5L);
		when(servlet.expenseDAO.readByAccountId(ACCOUNT_ID, 11, 20, true)).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, "5", null, null, null, null);

		assertNotNull(reaction);
		// correct mode in request
		assertEquals(ListExpensesServlet.MODE_EXPENSES, reaction.getRequestAttributes().get("mode"));
		// correct page in request
		assertEquals(2, reaction.getRequestAttributes().get("page"));
		// correct pageMin in request
		assertEquals(0, reaction.getRequestAttributes().get("pageMin"));
		// correct pageMax in request
		assertEquals(2, reaction.getRequestAttributes().get("pageMax"));
		// correct expenses in session
		assertSame(expenses, reaction.getRequestAttributes().get("expenses"));
		// correct navigation
		assertEquals(LISTEXPENSES_JSP, reaction.getForward());
	}

	@Test
	public void prepareListExpenses_MonthAndCategory() {
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		Expense expense = new Expense();
		expense.setCategoryId(CATEGORY_ID);
		expense.setCategoryName(CATEGORY);
		expenses.add(expense);
		when(servlet.statisticsDAO.readTopTenByMonthAndCategory(ACCOUNT_ID, MONTH, CATEGORY_ID)).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, null, MONTH, "" + CATEGORY_ID, null,
				null);

		assertNotNull(reaction);
		// correct mode in request
		assertEquals(ListExpensesServlet.MODE_TOP_10, reaction.getRequestAttributes().get("mode"));
		// correct month in request
		assertEquals(MONTH, reaction.getRequestAttributes().get("month"));
		// correct categoryId in request
		assertEquals(CATEGORY, reaction.getRequestAttributes().get("category"));
		// correct expenses in session
		assertSame(expenses, reaction.getRequestAttributes().get("expenses"));
		// correct navigation
		assertEquals(LISTEXPENSES_JSP, reaction.getForward());
	}

	@Test
	public void prepareListExpenses_MonthAndCategory_UnparseableCategory() {
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		when(servlet.statisticsDAO.readTopTenByMonthAndCategory(ACCOUNT_ID, MONTH, 0)).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, null, MONTH, "error" + CATEGORY_ID, null,
				null);

		assertNotNull(reaction);
		// correct mode in request
		assertEquals(ListExpensesServlet.MODE_TOP_10, reaction.getRequestAttributes().get("mode"));
		// correct month in request
		assertEquals(MONTH, reaction.getRequestAttributes().get("month"));
		// correct categoryId in request
		assertEquals("error" + CATEGORY_ID, reaction.getRequestAttributes().get("category"));
		// correct expenses in session
		assertSame(expenses, reaction.getRequestAttributes().get("expenses"));
		// correct navigation
		assertEquals(LISTEXPENSES_JSP, reaction.getForward());
	}

	@Test
	public void prepareListExpenses_MonthAndCategory_NoMonth() {
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		Expense expense = new Expense();
		expense.setCategoryId(CATEGORY_ID);
		expense.setCategoryName(CATEGORY);
		expenses.add(expense);
		when(servlet.statisticsDAO.readTopTenByMonthAndCategory(ACCOUNT_ID, null, CATEGORY_ID)).thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, null, null, "" + CATEGORY_ID, null, null);

		assertNotNull(reaction);
		// correct mode in request
		assertEquals(ListExpensesServlet.MODE_TOP_10, reaction.getRequestAttributes().get("mode"));
		// correct month in request
		assertEquals(null, reaction.getRequestAttributes().get("month"));
		// correct categoryId in request
		assertEquals(CATEGORY, reaction.getRequestAttributes().get("category"));
		// correct expenses in session
		assertSame(expenses, reaction.getRequestAttributes().get("expenses"));
		// correct navigation
		assertEquals(LISTEXPENSES_JSP, reaction.getForward());
	}

	@Test
	public void prepareListExpenses_Monthly() {
		servlet = spy(servlet);
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		Expense expense = new Expense();
		expense.setCategoryId(CATEGORY_ID);
		expense.setCategoryName(CATEGORY);
		expenses.add(expense);
		List<Month> months = new ArrayList<Month>();
		months.add(Month.create("2016.10"));
		months.add(Month.create("2016.08"));
		when(servlet.getCurrentMonth()).thenReturn(Month.create("2016.09"));
		when(servlet.statisticsDAO.readDistinctMonthsByAccountId(ACCOUNT_ID)).thenReturn(months);
		when(servlet.expenseDAO.readMonthlyByAccountAndMonth(eq(ACCOUNT_ID), eq(Month.create("2016.09"))))
				.thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, null, null, null, "true", null);

		assertNotNull(reaction);
		// correct mode in request
		assertEquals(ListExpensesServlet.MODE_MONTHLY, reaction.getRequestAttributes().get("mode"));
		assertEquals("2016.09", reaction.getRequestAttributes().get("monthCurrent").toString());
		assertEquals("2016.10", reaction.getRequestAttributes().get("monthMax").toString());
		assertEquals("2016.08", reaction.getRequestAttributes().get("monthMin").toString());
		// correct expenses in session
		assertSame(expenses, reaction.getRequestAttributes().get("expenses"));
		// correct navigation
		assertEquals(LISTEXPENSES_JSP, reaction.getForward());
	}

	@Test
	public void prepareListExpenses_Monthly_ChoosenMonth() {
		servlet = spy(servlet);
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		Expense expense = new Expense();
		expense.setCategoryId(CATEGORY_ID);
		expense.setCategoryName(CATEGORY);
		expenses.add(expense);
		List<Month> months = new ArrayList<Month>();
		months.add(Month.create("2016.10"));
		months.add(Month.create("2016.08"));
		when(servlet.getCurrentMonth()).thenReturn(Month.create("2016.12"));
		when(servlet.statisticsDAO.readDistinctMonthsByAccountId(ACCOUNT_ID)).thenReturn(months);
		when(servlet.expenseDAO.readMonthlyByAccountAndMonth(eq(ACCOUNT_ID), eq(Month.create("2016.11"))))
				.thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, null, "2016.11", null, "true", null);

		assertNotNull(reaction);
		// correct mode in request
		assertEquals(ListExpensesServlet.MODE_MONTHLY, reaction.getRequestAttributes().get("mode"));
		assertEquals("2016.11", reaction.getRequestAttributes().get("monthCurrent").toString());
		assertEquals("2016.12", reaction.getRequestAttributes().get("monthMax").toString());
		assertEquals("2016.08", reaction.getRequestAttributes().get("monthMin").toString());
		// correct expenses in session
		assertSame(expenses, reaction.getRequestAttributes().get("expenses"));
		// correct navigation
		assertEquals(LISTEXPENSES_JSP, reaction.getForward());
	}

	@Test
	public void prepareListExpenses_Monthly_ChoosenWrongMonth() {
		servlet = spy(servlet);
		final ArrayList<Expense> expenses = new ArrayList<Expense>();
		Expense expense = new Expense();
		expense.setCategoryId(CATEGORY_ID);
		expense.setCategoryName(CATEGORY);
		expenses.add(expense);
		List<Month> months = new ArrayList<Month>();
		months.add(Month.create("2016.10"));
		months.add(Month.create("2016.08"));
		when(servlet.getCurrentMonth()).thenReturn(Month.create("2016.09"));
		when(servlet.statisticsDAO.readDistinctMonthsByAccountId(ACCOUNT_ID)).thenReturn(months);
		when(servlet.expenseDAO.readMonthlyByAccountAndMonth(eq(ACCOUNT_ID), eq(Month.create("2016.09"))))
				.thenReturn(expenses);

		final ServletReaction reaction = servlet.prepareListExpenses(account, null, "2016.11", null, "true", null);

		assertNotNull(reaction);
		// correct mode in request
		assertEquals(ListExpensesServlet.MODE_MONTHLY, reaction.getRequestAttributes().get("mode"));
		assertEquals("2016.09", reaction.getRequestAttributes().get("monthCurrent").toString());
		assertEquals("2016.10", reaction.getRequestAttributes().get("monthMax").toString());
		assertEquals("2016.08", reaction.getRequestAttributes().get("monthMin").toString());
		// correct expenses in session
		assertSame(expenses, reaction.getRequestAttributes().get("expenses"));
		// correct navigation
		assertEquals(LISTEXPENSES_JSP, reaction.getForward());
	}

	private void addExpenses(final List<Expense> expenses, final int amount) {
		for (int counter = 0; counter < amount; counter++) {
			expenses.add(new Expense());
		}
	}

	@Test
	public void parseRequestedPage_NormalWithPage() {
		assertEquals(2, servlet.parseRequestedPage("2", 0, 3));
	}

	@Test
	public void parseRequestedPage_NormalWithoutPage() {
		assertEquals(0, servlet.parseRequestedPage(null, 0, 3));
	}

	@Test
	public void parseRequestedPage_UnparseablePage() {
		assertEquals(0, servlet.parseRequestedPage("abc", -1, 3));
	}

	@Test
	public void parseRequestedPage_TooSmallPage() {
		assertEquals(-1, servlet.parseRequestedPage("-2", -1, 3));
	}

	@Test
	public void parseRequestedPage_TooBigPage() {
		assertEquals(3, servlet.parseRequestedPage("4", -1, 3));
	}

	@Test
	public void calcAmountOfPages_NoEntries() {
		assertEquals(0, servlet.calcAmountOfPages(0, 10));
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
