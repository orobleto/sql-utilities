package com.octaviorobleto.sql.entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class MariaDBConnectionManager {
	private Connection connection;

	public Connection getConnection() {
		try {
			String URL = "jdbc:mariadb://localhost:3306/sql_utilities";
			String USER = "root";
			String KEY = "";

			Class.forName("org.mariadb.jdbc.Driver");
			connection = DriverManager.getConnection(URL, USER, KEY);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {

		}
		return connection;
	}
}
