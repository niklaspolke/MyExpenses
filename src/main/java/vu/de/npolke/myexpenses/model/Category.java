package vu.de.npolke.myexpenses.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="categories")
@NamedQueries(
	value={
	@NamedQuery(
		name="Category.findAll",
		query="SELECT c FROM Category c ORDER BY c.name ASC"),
	@NamedQuery(
		name="Category.findById",
		query="SELECT c FROM Category c WHERE c.id = :id")
	}
)
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "ID_SEQ", strategy = GenerationType.TABLE)
	private long id;

	private String name;

	public Category() {
	}

	public Category(final long id) {
		setId(id);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		//@formatter:off
		StringBuilder text = new StringBuilder().append("Category: #")
				.append(getId() != 0 ? getId() : "").append(" - ")
				.append(getName());
		return text.toString();
		//@formatter:on
	}
}
