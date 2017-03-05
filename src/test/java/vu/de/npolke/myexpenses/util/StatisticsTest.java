package vu.de.npolke.myexpenses.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

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
public class StatisticsTest {

	private static final double DELTA = 0.001;

	private static final double AMOUNT = 1.1;
	private static final Month M1 = Month.create(2015, 5);
	private static final Month M2 = M1.next();
	private static final String C1 = "A category1";
	private static final String C2 = "B category2";
	private static final String C3 = "C category3";
	private static final String C4 = "D category4";
	private static final String C5 = "E category5";

	private static final StatisticsElement M1_C2_F_F = StatisticsElement.create(M1, C2, AMOUNT, false, false);
	private static final StatisticsElement M1_C2_T_F = StatisticsElement.create(M1, C2, AMOUNT, true, false);
	private static final StatisticsElement M1_C2_T_T = StatisticsElement.create(M1, C2, AMOUNT, true, true);
	private static final StatisticsElement M1_C2_F_T = StatisticsElement.create(M1, C2, AMOUNT, false, true);

	private static final StatisticsElement M2_C1_F_F = StatisticsElement.create(M2, C1, AMOUNT, false, false);
	private static final StatisticsElement M2_C3_T_F = StatisticsElement.create(M2, C3, AMOUNT, true, false);
	private static final StatisticsElement M2_C4_T_T = StatisticsElement.create(M2, C4, AMOUNT, true, true);
	private static final StatisticsElement M2_C5_F_T = StatisticsElement.create(M2, C5, AMOUNT, false, true);

	private static Statistics statistics = new Statistics();

	static {
		List<StatisticsElement> elements = new ArrayList<StatisticsElement>();
		elements.add(M1_C2_F_F);
		elements.add(M1_C2_T_F);
		elements.add(M1_C2_T_T);
		elements.add(M1_C2_F_T);
		elements.add(M2_C1_F_F);
		elements.add(M2_C3_T_F);
		elements.add(M2_C4_T_T);
		elements.add(M2_C5_F_T);
		statistics.add(elements);
	}

	@Test
	public void assertMonths() {
		assertEquals(2, statistics.getMonths().size());

		assertEquals(M1, statistics.getMonths().get(0));
		assertEquals(M2, statistics.getMonths().get(1));
	}

	@Test
	public void filterNonExistingMonth_noEmptyCategories() {
		StatisticsOfMonth stats = statistics.filter(M2.next());

		assertEquals(0.0, stats.getSumExpenses(), DELTA);
		assertEquals(0.0, stats.getSumMonthlyExpenses(), DELTA);
		assertEquals(0.0, stats.getSumIncome(), DELTA);

		assertEquals(0, stats.getExpenses().size());
		assertEquals(0, stats.getMonthlyExpenses().size());
		assertEquals(0, stats.getIncome().size());

		assertEquals(M2.next().toString(), stats.getMonth().toString());
	}

	@Test
	public void filterNonExistingMonth_withEmptyCategories() {
		StatisticsOfMonth stats = statistics.filter(M2.next(), true);

		assertEquals(0.0, stats.getSumExpenses(), DELTA);
		assertEquals(0.0, stats.getSumMonthlyExpenses(), DELTA);
		assertEquals(0.0, stats.getSumIncome(), DELTA);

		assertEquals(2, stats.getExpenses().size());
		assertEquals(C1, stats.getExpenses().get(0).getCategory());
		assertEquals(C2, stats.getExpenses().get(1).getCategory());

		assertEquals(2, stats.getMonthlyExpenses().size());
		assertEquals(C2, stats.getMonthlyExpenses().get(0).getCategory());
		assertEquals(C3, stats.getMonthlyExpenses().get(1).getCategory());

		assertEquals(3, stats.getIncome().size());
		assertEquals(C2, stats.getIncome().get(0).getCategory());
		assertEquals(C4, stats.getIncome().get(1).getCategory());
		assertEquals(C5, stats.getIncome().get(2).getCategory());

		assertEquals(M2.next().toString(), stats.getMonth().toString());
	}

	@Test
	public void filterMonth_noEmptyCategories() {
		StatisticsOfMonth stats = statistics.filter(M1);

		assertEquals(AMOUNT, stats.getSumExpenses(), DELTA);
		assertEquals(AMOUNT, stats.getSumMonthlyExpenses(), DELTA);
		assertEquals(2 * AMOUNT, stats.getSumIncome(), DELTA);

		assertEquals(1, stats.getExpenses().size());
		assertEquals(C2, stats.getExpenses().get(0).getCategory());

		assertEquals(1, stats.getMonthlyExpenses().size());
		assertEquals(C2, stats.getMonthlyExpenses().get(0).getCategory());

		assertEquals(1, stats.getIncome().size());
		assertEquals(C2, stats.getIncome().get(0).getCategory());

		assertEquals(M1.toString(), stats.getMonth().toString());
	}

	@Test
	public void filterMonth_withEmptyCategories() {
		StatisticsOfMonth stats = statistics.filter(M1, true);

		assertEquals(AMOUNT, stats.getSumExpenses(), DELTA);
		assertEquals(AMOUNT, stats.getSumMonthlyExpenses(), DELTA);
		assertEquals(2 * AMOUNT, stats.getSumIncome(), DELTA);

		assertEquals(2, stats.getExpenses().size());
		assertEquals(C1, stats.getExpenses().get(0).getCategory());
		assertEquals(0.0, stats.getExpenses().get(0).getAmount(), DELTA);
		assertEquals(C2, stats.getExpenses().get(1).getCategory());
		assertEquals(AMOUNT, stats.getExpenses().get(1).getAmount(), DELTA);

		assertEquals(2, stats.getMonthlyExpenses().size());
		assertEquals(C2, stats.getMonthlyExpenses().get(0).getCategory());
		assertEquals(AMOUNT, stats.getMonthlyExpenses().get(0).getAmount(), DELTA);
		assertEquals(C3, stats.getMonthlyExpenses().get(1).getCategory());
		assertEquals(0.0, stats.getMonthlyExpenses().get(1).getAmount(), DELTA);

		assertEquals(3, stats.getIncome().size());
		assertEquals(C2, stats.getIncome().get(0).getCategory());
		assertEquals(2 * AMOUNT, stats.getIncome().get(0).getAmount(), DELTA);
		assertEquals(C4, stats.getIncome().get(1).getCategory());
		assertEquals(0.0, stats.getIncome().get(1).getAmount(), DELTA);
		assertEquals(C5, stats.getIncome().get(2).getCategory());
		assertEquals(0.0, stats.getIncome().get(2).getAmount(), DELTA);

		assertEquals(M1.toString(), stats.getMonth().toString());
	}
}
