package vu.de.npolke.myexpenses.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Entity
@NamedQueries(
	value={
	@NamedQuery(
		name="Expense.findAll",
		query="SELECT e FROM Expense e ORDER BY e.databaseDate DESC")
	}
)
public class Expense implements Serializable {

	private static final long serialVersionUID = 1L;

	@Transient
	private final NumberFormat NUMBER_FORMATTER = NumberFormat.getCurrencyInstance(Locale.GERMANY);
	@Transient
	private final DateTimeFormatter READABLE_DATE_FORMATTER = DateTimeFormat.forPattern("dd.MM.yyyy");
	@Transient
	private final DateTimeFormatter DATABASE_FORMATTER = DateTimeFormat.forPattern("yyyy.MM.dd");

	@Id
	@GeneratedValue(generator = "ID_SEQ", strategy = GenerationType.TABLE)
	@TableGenerator(name="ID_SEQ",
		table="sequences",
		pkColumnName="SEQ_NAME", // Specify the name of the column of the primary key
		valueColumnName="SEQ_NUMBER", // Specify the name of the column that stores the last value generated
		pkColumnValue="ID_GENERATOR", // Specify the primary key column value that would be considered as a primary key generator
		allocationSize=1)
	private long id;

	@Transient
	private LocalDate date;

	@Column(name="date")
	private String databaseDate;

	private Double amount;

	private String reason;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDatabaseDate() {
		return databaseDate;
	}

	public void setDatabaseDate(String databaseDate) {
		this.databaseDate = databaseDate;
		this.date = DATABASE_FORMATTER.parseLocalDate(databaseDate);
	}

	public void setReadableDateAsString(final String readableDate) {
		this.date = READABLE_DATE_FORMATTER.parseLocalDate(readableDate);
		this.databaseDate = this.date.toString(DATABASE_FORMATTER);
	}

	public String getReadableDateAsString() {
		return getDate() != null ? getDate().toString(READABLE_DATE_FORMATTER) : null;
	}

	public LocalDate getDate() {
		if (date == null && databaseDate != null) {
			date = DATABASE_FORMATTER.parseLocalDate(databaseDate);
		}
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
		this.databaseDate = this.date.toString(DATABASE_FORMATTER);
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
				.append(getId() != 0 ? getId() : "").append(" ")
				.append("(").append(getReadableDateAsString() != null ? getReadableDateAsString() : "??.??.????").append(") - ")
				.append(getAmount() != null ? NUMBER_FORMATTER.format(getAmount().doubleValue()) : NUMBER_FORMATTER.format(0)).append(" for ")
				.append("<").append(getReason() != null ? getReason() : "").append(">");
		return text.toString();
		//@formatter:on
	}
}
