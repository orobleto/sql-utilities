package com.octaviorobleto.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Indica el nombre de la columna y si es primary key en los campos utilizados
 * 
 * @author <a href="https://octaviorobleto.com" target="_blank">Octavio
 *         Robleto</a>
 * @version 1.0
 * @date 2022-07-13
 * @class Column
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.FIELD })
public @interface Column {
	String name() default "";

	boolean isKey() default false;
}
