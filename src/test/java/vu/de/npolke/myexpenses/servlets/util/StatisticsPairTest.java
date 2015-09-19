package vu.de.npolke.myexpenses.servlets.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StatisticsPairTest {

	@Test
	public void constructorToGetterName() {
		StatisticsPair pair = new StatisticsPair("category", 3.3);

		assertEquals("category", pair.getName());
	}

	@Test
	public void constructorToGetterValue() {
		StatisticsPair pair = new StatisticsPair("category", 3.3);

		assertEquals(3.3, pair.getValue(), 0.01);
	}
}
