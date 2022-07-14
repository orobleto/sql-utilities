package com.octaviorobleto.sql.jdbc.entities;

import java.util.Objects;

/**
 * 
 * Clase que representa el atributo del objeto al campo en la tabla de la base
 * de datos
 * 
 * @author <a href="https://octaviorobleto.com" target="_blank">Octavio
 *         Robleto</a>
 * @version 1.0
 * @date 2022-07-14
 * @class FieldWrapper
 */
public final class FieldWrapper {
	private Class<?> clazz;
	private String sourceName;
	private String destinationName;
	private boolean key;
	private String parentField;
	private boolean ownObject;
	private Object value;

	public FieldWrapper() {
		super();
	}

	public FieldWrapper(Class<?> clazz, String sourceName, String destinationName, boolean key, String parentField,
			boolean ownObject, Object value) {
		super();
		this.clazz = clazz;
		this.sourceName = sourceName;
		this.destinationName = destinationName;
		this.key = key;
		this.parentField = parentField;
		this.ownObject = ownObject;
		this.value = value;
	}

	public String toString() {
		return "FieldWrapper [clazz=" + clazz + ", sourceName=" + sourceName + ", destinationName=" + destinationName
				+ ", key=" + key + ", parentField=" + parentField + ", ownObject=" + ownObject + ", value=" + value
				+ "]";
	}

	public int hashCode() {
		return Objects.hash(clazz, destinationName, key, ownObject, parentField, sourceName, value);
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FieldWrapper other = (FieldWrapper) obj;
		return Objects.equals(clazz, other.clazz) && Objects.equals(destinationName, other.destinationName)
				&& key == other.key && ownObject == other.ownObject && Objects.equals(parentField, other.parentField)
				&& Objects.equals(sourceName, other.sourceName) && Objects.equals(value, other.value);
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public String getParentField() {
		return parentField;
	}

	public void setParentField(String parentField) {
		this.parentField = parentField;
	}

	public boolean isOwnObject() {
		return ownObject;
	}

	public void setOwnObject(boolean ownObject) {
		this.ownObject = ownObject;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
