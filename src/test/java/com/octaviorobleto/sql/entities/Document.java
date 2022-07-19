package com.octaviorobleto.sql.entities;

import java.util.Objects;

import com.octaviorobleto.sql.annotations.Column;

public class Document {
	@Column(name = "documentType")
	private String type;
	@Column(name = "documentNumber")
	private Integer number;

	public Document() {
		super();
	}

	public Document(String type, Integer number) {
		super();
		this.type = type;
		this.number = number;
	}

	@Override
	public String toString() {
		return "Document [type=" + type + ", number=" + number + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(number, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		return Objects.equals(number, other.number) && Objects.equals(type, other.type);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

}
