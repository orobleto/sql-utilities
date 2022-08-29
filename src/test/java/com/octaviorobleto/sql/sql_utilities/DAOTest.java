package com.octaviorobleto.sql.sql_utilities;

import static org.junit.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.octaviorobleto.commons.utilities.time.DateUtils;
import com.octaviorobleto.sql.entities.Document;
import com.octaviorobleto.sql.entities.User;
import com.octaviorobleto.sql.implementations.UserImplementation;

public class DAOTest {
	private UserImplementation userImplementation = new UserImplementation();
	private static Logger logger = LogManager.getLogger();

	@Test
	public void save() {
		User user = new User("user62@gmail.com", 1234, DateUtils.getLocalDate("1983-03-16", DateUtils.FORMAT_YYYY_MM_DD),
				DateUtils.getLocalDateTime("2022-03-05 18:36:36", DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_24H), true,
				new Document("DNI", 14));
		boolean actual = false;
		try {
			actual = userImplementation.save(user);
			logger.info(user.hashCode());
			logger.info(user);
			user = userImplementation.findById(new Document("DNI", 10));
			logger.info(user);
		} catch (Exception e) {
			logger.error(e);
		}

		boolean expected = true;
		assertEquals(expected, actual);
	}
	/*
	 * @Test public void delete() { User user = new User("user1@gmail.com", 1234,
	 * DateUtils.getLocalDate("1983-03-16", DateUtils.FORMAT_YYYY_MM_DD),
	 * DateUtils.getLocalDateTime("2022-03-05 18:36:36",
	 * DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_24H), true, new Document("DNI", 10));
	 * 
	 * boolean actual = userImplementation.delete(user); boolean expected = true;
	 * assertEquals(expected, actual); }
	 * 
	 * @Test public void find() { User userActual = new User("user1@gmail.com",
	 * 1234, DateUtils.getLocalDate("1983-03-16", DateUtils.FORMAT_YYYY_MM_DD),
	 * DateUtils.getLocalDateTime("2022-03-05 18:36:36",
	 * DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_24H), true, new Document("DNI", 6));
	 * 
	 * User userExpected = userImplementation.findById(new Document("DNI", 6));
	 * 
	 * assertEquals(userExpected, userActual); }
	 * 
	 * @Test public void findAll() {
	 * 
	 * int actual = userImplementation.findAll().size(); int expected = 3;
	 * 
	 * assertEquals(expected, actual); }
	 */
}
