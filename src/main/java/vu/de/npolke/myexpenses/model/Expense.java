package vu.de.npolke.myexpenses.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

@Entity
@NamedQueries(
	value={
	@NamedQuery(
		name="Expense.findAll",
		query="SELECT e FROM Expense e")
	}
)
public class Expense implements Serializable {

	private static final long serialVersionUID = 1L;

	@Transient
	private final NumberFormat FORMATTER = NumberFormat.getCurrencyInstance(Locale.GERMANY);

	@Id
	@GeneratedValue(generator = "ID_SEQ", strategy = GenerationType.TABLE)
	@TableGenerator(name="ID_SEQ",
		table="sequences",
		pkColumnName="SEQ_NAME", // Specify the name of the column of the primary key
		valueColumnName="SEQ_NUMBER", // Specify the name of the column that stores the last value generated
		pkColumnValue="ID_GENERATOR", // Specify the primary key column value that would be considered as a primary key generator
		allocationSize=1)
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
