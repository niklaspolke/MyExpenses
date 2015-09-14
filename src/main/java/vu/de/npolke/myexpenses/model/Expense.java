package vu.de.npolke.myexpenses.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Expense implements Serializable {

	private static final long serialVersionUID = 1L;

	private final NumberFormat FORMATTER = NumberFormat.getCurrencyInstance(Locale.GERMANY);

	@Id
	@GeneratedValue(generator = "ID_SEQ", strategy = GenerationType.TABLE)
	private long id;

	private Double amount;

	private String reason;

	public long getId() {
		return id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		//@formatter:off
		StringBuilder text = new StringBuilder().append("Expense: #")
				.append(getId() != 0 ? getId() : "").append(" - ")
				.append(getAmount() != null ? FORMATTER.format(getAmount().doubleValue()) : FORMATTER.format(0)).append(" for ")
				.append("<").append(getReason() != null ? getReason() : "").append(">");
		return text.toString();
		//@formatter:on
	}
}
