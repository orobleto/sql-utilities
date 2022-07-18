package com.octaviorobleto.sql.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.octaviorobleto.sql.annotations.Column;
import com.octaviorobleto.sql.annotations.Id;
import com.octaviorobleto.sql.annotations.Table;

@Table(name = "usuarios")
public class User {
	@Column(name = "email")
	private String email_user;
	private Integer key;
	private LocalDate creationDate;
	private LocalDateTime lastAccessDate;
	private boolean active;
	@Id
	private Document document;

	public User() {
		super();
	}

	public User(String email_user, Integer key, LocalDate creationDate, LocalDateTime lastAccessDate, boolean active,
			Document document) {
		super();
		this.email_user = email_user;
		this.key = key;
		this.creationDate = creationDate;
		this.lastAccessDate = lastAccessDate;
		this.active = active;
		this.document = document;
	}

	@Override
	public String toString() {
		return "User [email_user=" + email_user + ", key=" + key + ", CreationDate=" + creationDate
				+ ", lastAccessDate=" + lastAccessDate + ", active=" + active + ", document=" + document + "]";
	}

	public String getEmail_user() {
		return email_user;
	}

	public void setEmail_user(String email_user) {
		this.email_user = email_user;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getLastAccessDate() {
		return lastAccessDate;
	}

	public void setLastAccessDate(LocalDateTime lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

}
