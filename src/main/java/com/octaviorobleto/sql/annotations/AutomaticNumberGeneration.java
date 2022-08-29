package com.octaviorobleto.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Indica un campo con incremento automatico y en la base de datos con cada
 * insercion (Solo se permite uno por objeto)
 * 
 * @author <a href="https://octaviorobleto.com" target="_blank">Octavio
 *         Robleto</a>
 * @version 1.0
 * @date 2022-08-29
 * @class AutomaticNumberGeneration
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface AutomaticNumberGeneration {

}
