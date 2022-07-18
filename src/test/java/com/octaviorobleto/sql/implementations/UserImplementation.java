package com.octaviorobleto.sql.implementations;

import com.octaviorobleto.sql.entities.Document;
import com.octaviorobleto.sql.entities.MariaDBConnectionManager;
import com.octaviorobleto.sql.entities.User;
import com.octaviorobleto.sql.jdbc.entities.GenericImplementation;

public class UserImplementation extends GenericImplementation<User, Document> {

	public UserImplementation() {
		super(User.class, new MariaDBConnectionManager().getConnection());
	}
}
