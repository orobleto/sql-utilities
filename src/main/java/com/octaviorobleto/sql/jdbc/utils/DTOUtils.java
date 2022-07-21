package com.octaviorobleto.sql.jdbc.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.octaviorobleto.commons.utilities.time.DateUtils;
import com.octaviorobleto.sql.jdbc.entities.FieldWrapper;

/**
 * 
 * Clase que provee metodos para instanciar los objetos utilizados en las
 * implementaciones y los parametros de las consultas preparadas
 * 
 * @author <a href="https://octaviorobleto.com" target="_blank">Octavio
 *         Robleto</a>
 * @version 1.0
 * @date 2022-07-19
 * @class DTOUtils
 */
public class DTOUtils {
	private static Logger logger = LogManager.getLogger();

	/**
	 * No permitir crear una instancia de {@code DTOUtils}
	 */
	private DTOUtils() {
	}

	public static enum CRUD {
		FIND, INSERT, DELETE, UPDATE;
	}

	public static void setParametersPreparedStatement(PreparedStatement preparedStatement,
			List<FieldWrapper> fieldsWrapper, CRUD crud) {

		List<FieldWrapper> fieldWrapperList = fieldsWrapper.stream().filter(e -> !e.isOwnObject())
				.collect(Collectors.toList());

		int indexParam = 1;
		switch (crud) {
		case INSERT:
			for (FieldWrapper fieldWrapper : fieldWrapperList)
				indexParam = setValueParameter(preparedStatement, fieldWrapper, indexParam);
			break;
		case UPDATE:
			for (FieldWrapper fieldWrapper : fieldWrapperList)
				if (!fieldWrapper.isKey())
					indexParam = setValueParameter(preparedStatement, fieldWrapper, indexParam);
		case DELETE:
		case FIND:
			for (FieldWrapper fieldWrapper : fieldWrapperList)
				if (fieldWrapper.isKey())
					indexParam = setValueParameter(preparedStatement, fieldWrapper, indexParam);
		default:
			break;
		}
	}

	public static int setValueParameter(PreparedStatement preparedStatement, FieldWrapper fieldsWrapper,
			int indexParam) {
		try {
			String clazz = fieldsWrapper.getClazz().getSimpleName();

			switch (clazz) {
			case "Byte":
				preparedStatement.setByte(indexParam, (Byte) fieldsWrapper.getValue());
				break;
			case "Short":
				preparedStatement.setShort(indexParam, (Short) fieldsWrapper.getValue());
				break;
			case "Integer":
				preparedStatement.setInt(indexParam, (Integer) fieldsWrapper.getValue());
				break;
			case "Long":
				preparedStatement.setLong(indexParam, (Long) fieldsWrapper.getValue());
				break;
			case "Float":
				preparedStatement.setFloat(indexParam, (Float) fieldsWrapper.getValue());
				break;
			case "Double":
				preparedStatement.setDouble(indexParam, (Double) fieldsWrapper.getValue());
				break;
			case "Boolean":
				preparedStatement.setBoolean(indexParam, (Boolean) fieldsWrapper.getValue());
				break;
			case "LocalDate":
				preparedStatement.setString(indexParam,
						DateUtils.getString((LocalDate) fieldsWrapper.getValue(), DateUtils.FORMAT_YYYY_MM_DD));
				break;
			case "LocalDateTime":
				preparedStatement.setString(indexParam, DateUtils.getString((LocalDateTime) fieldsWrapper.getValue(),
						DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_24H));
				break;
			default:
				preparedStatement.setString(indexParam, (String) fieldsWrapper.getValue());
				break;
			}
		} catch (SQLException error) {
			logger.error(error);
		}
		return (indexParam + 1);
	}

	public static <E> List<E> getElements(ResultSet resultSet, List<FieldWrapper> fieldsWrapper, Class<E> clazzE) {

		if (resultSet == null) {
			return null;
		}
		List<E> listElements = new ArrayList<>();
		try {
			while (resultSet.next()) {
				for (FieldWrapper fieldWrapper : fieldsWrapper) {
					if (fieldWrapper.isOwnObject()) {
						continue;
					}
					String clazz = fieldWrapper.getClazz().getSimpleName();

					switch (clazz) {
					case "Byte":
						fieldWrapper.setValue((Byte) resultSet.getByte(fieldWrapper.getDestinationName()));
						break;
					case "Short":
						fieldWrapper.setValue((Short) resultSet.getShort(fieldWrapper.getDestinationName()));
						break;
					case "Integer":
						fieldWrapper.setValue((Integer) resultSet.getInt(fieldWrapper.getDestinationName()));
						break;
					case "Long":
						fieldWrapper.setValue((Long) resultSet.getLong(fieldWrapper.getDestinationName()));
						break;
					case "Float":
						fieldWrapper.setValue((Float) resultSet.getFloat(fieldWrapper.getDestinationName()));
						break;
					case "Double":
						fieldWrapper.setValue((Double) resultSet.getDouble(fieldWrapper.getDestinationName()));
						break;
					case "Boolean":
						fieldWrapper.setValue((Boolean) resultSet.getBoolean(fieldWrapper.getDestinationName()));
						break;
					case "LocalDate":
						fieldWrapper.setValue(DateUtils
								.getLocalDate(resultSet.getString(fieldWrapper.getDestinationName()), "yyyy-MM-dd"));
						break;
					case "LocalDateTime":
						fieldWrapper.setValue(DateUtils.getLocalDateTime(
								resultSet.getString(fieldWrapper.getDestinationName()), "yyyy-MM-dd HH:mm:ss"));
						break;
					default:
						fieldWrapper.setValue((String) resultSet.getString(fieldWrapper.getDestinationName()));
						break;
					}

				}

				listElements.add(FieldUtils.setValuesField(clazzE, fieldsWrapper));
			}
		} catch (SQLException error) {
			logger.error(error);
		}
		return listElements;
	}

}
