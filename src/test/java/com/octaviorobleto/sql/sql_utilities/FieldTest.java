package com.octaviorobleto.sql.sql_utilities;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.octaviorobleto.sql.entities.Document;
import com.octaviorobleto.sql.entities.User;
import com.octaviorobleto.sql.jdbc.entities.FieldWrapper;
import com.octaviorobleto.sql.jdbc.utils.FieldUtils;

/**
 * Unit test for simple App.
 */
public class FieldTest {

	@Test
	public void getFieldsWrapper() {
		List<FieldWrapper> fieldsWrapperActual = getFieldWrapperActual();
		List<FieldWrapper> fieldsWrapperExpected = FieldUtils.getFieldsWrapper(User.class);
		assertEquals(fieldsWrapperExpected, fieldsWrapperActual);
	}

	@Test
	public void getTableName() {
		String actual = FieldUtils.getTableName(User.class);
		String expected = "users";
		assertEquals(expected, actual);
	}

	private List<FieldWrapper> getFieldWrapperActual() {
		List<FieldWrapper> fieldsWrapper = new ArrayList<>();
		FieldWrapper email_user = new FieldWrapper();
		FieldWrapper key = new FieldWrapper();
		FieldWrapper creationDate = new FieldWrapper();
		FieldWrapper lastAccessDate = new FieldWrapper();
		FieldWrapper active = new FieldWrapper();
		FieldWrapper document = new FieldWrapper();
		FieldWrapper type = new FieldWrapper();
		FieldWrapper number = new FieldWrapper();

		email_user.setClazz(String.class);
		email_user.setSourceName("email_user");
		email_user.setDestinationName("email_user");
		email_user.setKey(false);
		email_user.setOwnObject(false);
		email_user.setParentField(null);
		email_user.setValue(null);

		key.setClazz(Integer.class);
		key.setSourceName("key");
		key.setDestinationName("key_user");
		key.setKey(false);
		key.setOwnObject(false);
		key.setParentField(null);
		key.setValue(null);

		creationDate.setClazz(LocalDate.class);
		creationDate.setSourceName("creationDate");
		creationDate.setDestinationName("creationDate");
		creationDate.setKey(false);
		creationDate.setOwnObject(false);
		creationDate.setParentField(null);
		creationDate.setValue(null);

		lastAccessDate.setClazz(LocalDateTime.class);
		lastAccessDate.setSourceName("lastAccessDate");
		lastAccessDate.setDestinationName("lastAccessDate");
		lastAccessDate.setKey(false);
		lastAccessDate.setOwnObject(false);
		lastAccessDate.setParentField(null);
		lastAccessDate.setValue(null);

		active.setClazz(Boolean.class);
		active.setSourceName("active");
		active.setDestinationName("active");
		active.setKey(false);
		active.setOwnObject(false);
		active.setParentField(null);
		active.setValue(null);

		document.setClazz(Document.class);
		document.setSourceName("document");
		document.setDestinationName("document");
		document.setKey(true);
		document.setOwnObject(true);
		document.setParentField(null);
		document.setValue(null);

		type.setClazz(String.class);
		type.setSourceName("type");
		type.setDestinationName("documentType");
		type.setKey(true);
		type.setOwnObject(false);
		type.setParentField("document");
		type.setValue(null);

		number.setClazz(Integer.class);
		number.setSourceName("number");
		number.setDestinationName("documentNumber");
		number.setKey(true);
		number.setOwnObject(false);
		number.setParentField("document");
		number.setValue(null);

		fieldsWrapper.add(email_user);
		fieldsWrapper.add(key);
		fieldsWrapper.add(creationDate);
		fieldsWrapper.add(lastAccessDate);
		fieldsWrapper.add(active);
		fieldsWrapper.add(document);
		fieldsWrapper.add(type);
		fieldsWrapper.add(number);

		return fieldsWrapper;
	}

}
