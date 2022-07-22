package com.octaviorobleto.sql.jdbc.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.octaviorobleto.sql.jdbc.entities.FieldWrapper;

/**
 * 
 * Clase que provee metodos para facilitar la construccion de las consultas
 * 
 * @author <a href="https://octaviorobleto.com" target="_blank">Octavio
 *         Robleto</a>
 * @version 1.0
 * @date 2022-07-15
 * @class QueryUtils
 */
public final class QueryUtils {
	/**
	 * No permitir crear una instancia de {@code QueryUtils}
	 */
	private QueryUtils() {

	}

	/**
	 * Retorna un String con el query del select por clave
	 * 
	 * @param fieldsWrapper lista de {@link FieldWrapper}
	 * @param table         nombre de la tabla {@link String}
	 * @return {@link String}
	 */

	public static String getQueryFindById(List<FieldWrapper> fieldsWrapper, String table) {
		return "select " + getSelect(fieldsWrapper) + " from " + table + getWhere(fieldsWrapper);
	}

	/**
	 * Retorna un String con el query del delete por clave
	 * 
	 * @param fieldsWrapper lista de {@link FieldWrapper}
	 * @param table         nombre de la tabla {@link String}
	 * @return {@link String}
	 */
	public static String getQueryDelete(List<FieldWrapper> fieldsWrapper, String table) {
		return "delete from " + table + getWhere(fieldsWrapper);
	}

	/**
	 * Retorna un String con el query del insert con todos los valores para esta
	 * version no se tiene en cuenta el autoincrement
	 * 
	 * @param fieldsWrapper lista de {@link FieldWrapper}
	 * @param table         nombre de la tabla {@link String}
	 * @return {@link String}
	 */
	public static String getQueryInsert(List<FieldWrapper> fieldsWrapper, String table) {
		return "insert into  " + table + " (" + getSelect(fieldsWrapper) + ") values (" + getValues(fieldsWrapper)
				+ ")";
	}

	/**
	 * Retorna un String con el query del update por clave
	 * 
	 * @param fieldsWrapper lista de {@link FieldWrapper}
	 * @param table         nombre de la tabla {@link String}
	 * @return {@link String}
	 */
	public static String getQueryUpdate(List<FieldWrapper> fieldsWrapper, String table) {
		return "update  " + table + " set " + getSet(fieldsWrapper) + getWhere(fieldsWrapper);
	}

	/**
	 * Retorna un String con el query del select sin condicion
	 * 
	 * @param fieldsWrapper lista de {@link FieldWrapper}
	 * @param table         nombre de la tabla {@link String}
	 * @return {@link String}
	 */
	public static String getQueryFindAll(List<FieldWrapper> fieldsWrapper, String table) {
		return "select " + getSelect(fieldsWrapper) + " from " + table;
	}

	/**
	 * Retorna un String con el cuerpo del select
	 * 
	 * @param fieldsWrapper lista de {@link FieldWrapper}
	 * @return {@link String}
	 */
	private static String getSelect(List<FieldWrapper> fieldsWrapper) {
		return fieldsWrapper.stream().filter(e -> !e.isOwnObject()).map(e -> e.getDestinationName())
				.reduce((a, b) -> a.concat(",").concat(b)).get();
	}

	/**
	 * Retorna un String con el cuerpo del set
	 * 
	 * @param fieldsWrapper lista de {@link FieldWrapper}
	 * @return {@link String}
	 */
	private static String getSet(List<FieldWrapper> fieldsWrapper) {
		String and = " = ?, ";
		String set = fieldsWrapper.stream().filter(e -> (!e.isKey() && !e.isOwnObject()))
				.map(e -> e.getDestinationName()).reduce((a, b) -> a.concat(and).concat(b)).get().concat(" = ?");
		return set;
	}

	/**
	 * Retorna un String con los parametros a reemplazar en el PreparedStatement
	 * 
	 * @param fieldsWrapper lista de {@link FieldWrapper}
	 * @return {@link String}
	 */
	private static String getValues(List<FieldWrapper> fieldsWrapper) {
		return Arrays.asList(getSelect(fieldsWrapper).split(",")).stream().map(e -> "?")
				.reduce((a, b) -> a.concat(",").concat(b)).get();
	}

	/**
	 * Retorna un String el cuerpo del where
	 * 
	 * @param fieldsWrapper lista de {@link FieldWrapper}
	 * @return {@link String}
	 */
	private static String getWhere(List<FieldWrapper> fieldsWrapper) {
		// obtengo las claves
		List<FieldWrapper> fieldsWrapperKeys = fieldsWrapper.stream().filter(e -> (e.isKey() && !e.isOwnObject()))
				.collect(Collectors.toList());
		if (fieldsWrapperKeys.size() == 0) {
			return "";
		}

		String and = " = ? and ";
		String where = fieldsWrapperKeys.stream().map(e -> e.getDestinationName())
				.reduce((a, b) -> a.concat(and).concat(b)).get().concat(" = ?");
		return " where " + where;
	}
}
