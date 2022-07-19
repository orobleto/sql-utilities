package com.octaviorobleto.sql.jdbc.exceptions;

/**
 * 
 * Clase para el manejo de excepciones de campos
 * 
 * @author <a href="https://octaviorobleto.com" target="_blank">Octavio
 *         Robleto</a>
 * @version 1.0
 * @date 2022-07-18
 * @class ExceptionField
 */
public class ExceptionField extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExceptionField(String message) {
		super(message);
	}

}
