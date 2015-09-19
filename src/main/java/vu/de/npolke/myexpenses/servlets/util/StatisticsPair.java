package vu.de.npolke.myexpenses.servlets.util;

public class StatisticsPair {

	private String name;
	private Double value;

	public StatisticsPair(final String name, final Double value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Double getValue() {
		return value;
	}
}
