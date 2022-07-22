package com.octaviorobleto.sql.jdbc.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.octaviorobleto.commons.utilities.text.StringUtils;
import com.octaviorobleto.sql.annotations.Column;
import com.octaviorobleto.sql.annotations.Id;
import com.octaviorobleto.sql.annotations.Table;
import com.octaviorobleto.sql.jdbc.entities.FieldWrapper;
import com.octaviorobleto.sql.jdbc.exceptions.ExceptionField;

/**
 * 
 * Clase que provee metodos para instanciar objetos {@code FieldWrapper} con sus
 * caracteristicas
 * 
 * @author <a href="https://octaviorobleto.com" target="_blank">Octavio
 *         Robleto</a>
 * @version 1.0
 * @date 2022-07-14
 * @class FieldUtils
 */
public final class FieldUtils {

	private static Logger logger = LogManager.getLogger();

	/**
	 * No permitir crear una instancia de {@code FieldUtils}
	 */
	private FieldUtils() {
	}

	/**
	 * Retorna una lista de {@link FieldWrapper} con la informacion de los campos
	 * 
	 * @param clazz {@link Class} clase para obtener los atributos
	 * @return List {@link FieldWrapper}
	 */
	public static List<FieldWrapper> getFieldsWrapper(Class<?> clazz) {
		List<FieldWrapper> fieldsWrapper = new ArrayList<>();
		List<FieldWrapper> ownFieldsWrapper = new ArrayList<>();
		// obtengo los atributos de la clase (Miembros de Instancia)
		Field[] fields = clazz.getDeclaredFields();

		// recorremos todos los atributos e instanciamos un FieldWrapper
		for (Field field : fields) {
			FieldWrapper fieldWrapper = new FieldWrapper();
			fieldWrapper.setSourceName(field.getName());

			String clazzFieldWrapper = getPrimitiveClass(field.getGenericType().getTypeName());
			// asumimos el nombre del campo como el nombre del atributo
			String destinationName = field.getName();
			try {
				fieldWrapper.setClazz(Class.forName(clazzFieldWrapper));
			} catch (ClassNotFoundException error) {
				logger.error(error);
			}

			// evaluamos si es una clase propia o de java
			if (!clazzFieldWrapper.startsWith("java.")) {
				fieldWrapper.setOwnObject(true);
				// obtenemos los atributos que tiene nuestro objeto
				ownFieldsWrapper = getFieldsWrapper(fieldWrapper.getClazz());
			}

			// verificamos el nombre destino, sino existe entonces colocamos el mismo nombre
			// del campo esto atraves de las anotaciones Columna
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				if (!StringUtils.isEmpty(column.name())) {
					destinationName = column.name();
				}
			}

			// verificamos si el objeto es clave primaria
			if (field.isAnnotationPresent(Id.class)) {
				fieldWrapper.setKey(true);
			}

			fieldWrapper.setDestinationName(destinationName);

			// agregamos el objeto a la lista
			fieldsWrapper.add(fieldWrapper);

			// agregamos todos los campos del objeto propio y setemoa nombre padre y si es
			// clave
			if (ownFieldsWrapper.size() > 0) {
				fieldsWrapper.addAll(ownFieldsWrapper.stream().map(e -> {
					e.setParentField(fieldWrapper.getSourceName());
					e.setKey(fieldWrapper.isKey());
					return e;
				}).collect(Collectors.toList()));
			}

		}
		verifyId(fieldsWrapper);

		return fieldsWrapper;
	}

	/**
	 * Retorna un string {@link String} con el nombre de la tabla
	 * 
	 * @param clazz {@link Class} clase para obtener el nombre
	 * @return {@link String} si existe la anotacion {@link Table} con el name no
	 *         vacio retorna el name de lo contrario retorna el nombre de la clase
	 */
	public static String getTableName(Class<?> clazz) {
		// buscamos el nombre de la tabla, sino existe entonces colocamos el mismo
		// nombre del objeto
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = clazz.getAnnotation(Table.class);
			return !StringUtils.isEmpty(table.name()) ? table.name() : clazz.getSimpleName();
		}
		return clazz.getSimpleName();
	}

	/**
	 * Retorna una lista de {@link FieldWrapper} seteando en cada campo el atributo
	 * value si lo posee el objeto
	 * 
	 * @param <E>
	 * @param e             del tipo <E> objeto para tomar los valores
	 * @param fieldsWrapper lista de {@link FieldWrapper}
	 * @return List {@link FieldWrapper}
	 */
	public static <E> List<FieldWrapper> getValuesField(E e, List<FieldWrapper> fieldsWrapper) {
		if (e == null || fieldsWrapper == null) {
			return fieldsWrapper;
		}
		Class<?> clazz = e.getClass();

		// limpio los valores
		fieldsWrapper.forEach(fieldWrapper -> fieldWrapper.setValue(null));

		// recorro la lista con cada campo y obtengo sus valores
		fieldsWrapper.forEach(parentField -> {

			Field field;

			try {
				field = clazz.getDeclaredField(parentField.getSourceName());

				field.setAccessible(true);
				Object value = field.get(e);
				parentField.setValue(value);
				// si el atributo es un objeto propio (padre) busco los valores de sus atributos
				// (hijos)
				if (value != null && parentField.isOwnObject()) {
					getValuesField(value, fieldsWrapper.stream()
							.filter(childField -> childField.getParentField() != null
									&& childField.getParentField().equalsIgnoreCase(parentField.getSourceName()))
							.collect(Collectors.toList()));
				}
			} catch (NoSuchFieldException error) {
				logger.error(error + " -> " + parentField);
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException error) {
				logger.error(error);
			} catch (Exception error) {
				logger.error(error);
			}

		});

		return fieldsWrapper;
	}

	/**
	 * Retorna un objeto con los valores que posean los campos de la lista de
	 * {@link FieldWrapper}
	 * 
	 * @param <E>
	 * @param clazz         {@link Class} clase a instanciar
	 * @param fieldsWrapper lista de {@link FieldWrapper}
	 * @return un objeto instanciado del tipo <E>
	 */
	public static <E> E setValuesField(Class<E> clazz, List<FieldWrapper> fieldsWrapper) {
		E e;
		try {
			e = clazz.getDeclaredConstructor().newInstance();
			fieldsWrapper.forEach(parentField -> {
				try {
					Field field;
					// obtengo el nombre del campo
					field = clazz.getDeclaredField(parentField.getSourceName());
					// lo coloco accesible para poder darle valor
					field.setAccessible(true);

					// si el objeto es propio lo instancio y seteo sus atributos
					if (parentField.isOwnObject()) {
						field.set(e, setValuesField(parentField.getClazz(), fieldsWrapper.stream()
								.filter(childField -> childField.getParentField() != null
										&& childField.getParentField().equalsIgnoreCase(parentField.getSourceName()))
								.collect(Collectors.toList())));
					} else {
						field.set(e, parentField.getValue());
					}

				} catch (NoSuchFieldException error) {
					logger.error(error + " -> " + parentField);
				} catch (IllegalArgumentException error) {
					logger.error(error);
				} catch (IllegalAccessException error) {
					logger.error(error);
				}

			});

			return e;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException error) {
			logger.error(error);
		}

		return null;
	}

	/**
	 * Retorna una clase Envoltorio si el atributo evaluado es un primitivo
	 * 
	 * @param clazz
	 * @return {@link String} clase a instanciar
	 */
	private static String getPrimitiveClass(String clazz) {
		// evaluo al principio si es una clase o un primitivo, al ser una clase returno
		// el String
		if (!StringUtils.isEmpty(clazz) && !(clazz.indexOf(".") == -1)) {
			return clazz;
		}

		switch (clazz) {
		case "byte":
			return "java.lang.Byte";
		case "short":
			return "java.lang.Short";
		case "int":
			return "java.lang.Integer";
		case "long":
			return "java.lang.Long";
		case "float":
			return "java.lang.Float";
		case "double":
			return "java.lang.Double";
		case "boolean":
			return "java.lang.Boolean";
		case "char":
			return "java.lang.Character";
		default:
			return clazz;
		}
	}

	/**
	 * Evalua si la entidad posee mas de una clave primaria Si la clave primaria es
	 * compuesta por 2 o mas elementos debe crear un objeto propio
	 * 
	 * @param fieldsWrapper lista de {@link FieldWrapper}
	 */

	private static void verifyId(List<FieldWrapper> fieldsWrapper) {

		int ownObjectKeys = 0;
		int javaObjectKeys = 0;
		List<String> keys = new ArrayList<>();

		// solo recorro las claves
		for (FieldWrapper fieldWrapper : fieldsWrapper.stream().filter(fieldWrapper -> fieldWrapper.isKey())
				.collect(Collectors.toList())) {
			keys.add(fieldWrapper.getSourceName());
			if (fieldWrapper.isOwnObject()) {
				ownObjectKeys++;
			} else if (!fieldWrapper.isOwnObject() && (StringUtils.isEmpty(fieldWrapper.getParentField()))) {
				javaObjectKeys++;
			}

			if ((ownObjectKeys > 0 && javaObjectKeys > 0) || ownObjectKeys > 1 || javaObjectKeys > 1) {
				throw new ExceptionField(
						"Solo esta permitido un Objeto Propio o Primitivo como clave en la entidad.\nClaves Primarias Actuales: "
								+ keys);
			}
		}
	}

}
