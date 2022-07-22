package com.octaviorobleto.sql.jdbc.entities;

import static com.octaviorobleto.sql.jdbc.utils.DTOUtils.getElements;
import static com.octaviorobleto.sql.jdbc.utils.DTOUtils.setParametersPreparedStatement;
import static com.octaviorobleto.sql.jdbc.utils.DTOUtils.setValueParameter;
import static com.octaviorobleto.sql.jdbc.utils.FieldUtils.getFieldsWrapper;
import static com.octaviorobleto.sql.jdbc.utils.FieldUtils.getTableName;
import static com.octaviorobleto.sql.jdbc.utils.FieldUtils.getValuesField;
import static com.octaviorobleto.sql.jdbc.utils.QueryUtils.getQueryDelete;
import static com.octaviorobleto.sql.jdbc.utils.QueryUtils.getQueryFindAll;
import static com.octaviorobleto.sql.jdbc.utils.QueryUtils.getQueryFindById;
import static com.octaviorobleto.sql.jdbc.utils.QueryUtils.getQueryInsert;
import static com.octaviorobleto.sql.jdbc.utils.QueryUtils.getQueryUpdate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.octaviorobleto.commons.utilities.text.StringUtils;
import com.octaviorobleto.sql.jdbc.exceptions.ExceptionField;
import com.octaviorobleto.sql.jdbc.interfaces.DAO;
import com.octaviorobleto.sql.jdbc.utils.DTOUtils.CRUD;
import com.octaviorobleto.sql.jdbc.utils.FieldUtils;

/**
 * 
 * Clase abstracta que provee los métodos necesarios para acceder, modificar,
 * eliminar e insertar nuestros objetos
 * 
 * @author <a href="https://octaviorobleto.com" target="_blank">Octavio
 *         Robleto</a>
 * @version 1.0
 * @date 2022-07-14
 * @class GenericImplementation
 * 
 * @param <E> Objeto de Negocio
 * @param <K> Objeto que representa la clave primaria (Compuesta o Simple) de
 *            <E> en la base de datos
 */
public abstract class GenericImplementation<E, K> implements DAO<E, K> {
	private static Logger logger = LogManager.getLogger();
	private final Class<E> clazz;
	private final String table;
	private List<FieldWrapper> fieldsWrapper;
	private boolean keyExists;
	private Connection connection;
	private PreparedStatement preparedStatementFindByID;
	private PreparedStatement preparedStatementInsert;
	private PreparedStatement preparedStatementDelete;
	private PreparedStatement preparedStatementUpdate;
	private PreparedStatement preparedStatementFindAll;

	/**
	 * 
	 * @param clazz      Clase para obtener los atributos y mapearlos en las
	 *                   consultas preparadas
	 * @param connection Objeto de Conexion SQL
	 */
	public GenericImplementation(Class<E> clazz, Connection connection) {
		this.clazz = clazz;
		this.connection = connection;
		table = getTableName(clazz);
		fieldsWrapper = getFieldsWrapper(clazz);
		keyExists = fieldsWrapper.stream().filter(e -> e.isKey()).count() != 0;
	}

	public E findById(K k) {
		validateNull(k);
		if (!keyExists) {
			throw new ExceptionField("No se puede buscar por clave al no existir primary key, puede usar el findAll");
		}
		try {
			if (preparedStatementFindByID == null) {
				String queryFindByID = getQueryFindById(fieldsWrapper, table);
				preparedStatementFindByID = connection.prepareStatement(queryFindByID);
			}

			// verifico si es un objeto propio
			if (k.getClass().getName().startsWith("java.")) {
				FieldWrapper fieldWrapper = new FieldWrapper();
				fieldWrapper.setClazz(k.getClass());
				fieldWrapper.setValue(k);
				setValueParameter(preparedStatementFindByID, fieldWrapper, 1);
			} else {
				List<FieldWrapper> fieldsWrapper = FieldUtils.getFieldsWrapper(k.getClass());
				fieldsWrapper.forEach(fieldWrapper -> fieldWrapper.setKey(true));
				getValuesField(k, fieldsWrapper);
				setParametersPreparedStatement(preparedStatementFindByID, fieldsWrapper, CRUD.FIND);
			}
			logger.debug(preparedStatementFindByID);

			List<E> elements = getElements(preparedStatementFindByID.executeQuery(), this.fieldsWrapper, clazz);
			return elements != null && elements.size() > 0 ? elements.get(0) : null;
		} catch (SQLException error) {
			logger.error(error);
		}
		return null;
	}

	public boolean save(E e) {
		validateNull(e);
		FieldUtils.getValuesField(e, fieldsWrapper);
		logger.debug(keyExists);
		if (keyExists) {
			@SuppressWarnings("unchecked")
			K k = (K) fieldsWrapper.stream()
					.filter(fieldWrapper -> fieldWrapper.isKey() && StringUtils.isEmpty(fieldWrapper.getParentField()))
					.map(c -> c.getValue()).reduce((a, b) -> a).get();
			E element = findById(k);

			if (element == null) {
				return insert(e);
			}
			return update(e);
		}
		return insert(e);
	}

	private boolean insert(E e) {
		validateNull(e);
		try {
			if (preparedStatementInsert == null) {
				String queryInsert = getQueryInsert(fieldsWrapper, table);
				preparedStatementInsert = connection.prepareStatement(queryInsert);
			}
			FieldUtils.getValuesField(e, fieldsWrapper);
			// armamos los parametros del preparedStatement
			setParametersPreparedStatement(preparedStatementInsert, fieldsWrapper, CRUD.INSERT);
			logger.debug(preparedStatementInsert);

			return preparedStatementInsert.executeUpdate() == 1;
		} catch (SQLIntegrityConstraintViolationException error) {
			if (!keyExists) {
				throw new ExceptionField("La tabla '" + table + "' posee clave primaria [" + error.getMessage()
						+ "] y la entidad no posee definida ninguna, utilice la anotacion @Id en el campo correspondiente");
			}
		} catch (SQLException error) {
			logger.error(error);
		}
		return false;
	}

	private boolean update(E e) {
		validateNull(e);
		if (!keyExists) {
			throw new ExceptionField(
					"Al no poseer clave primaria, se recomienda reescribir el metodo save o crear un metodo update en la implementacion");
		}
		try {
			if (preparedStatementUpdate == null) {
				String queryUpdate = getQueryUpdate(fieldsWrapper, table);
				preparedStatementUpdate = connection.prepareStatement(queryUpdate);
			}

			getValuesField(e, fieldsWrapper);
			// armamos los parametros del preparedStatement
			setParametersPreparedStatement(preparedStatementUpdate, fieldsWrapper, CRUD.UPDATE);
			logger.debug(preparedStatementUpdate);

			return preparedStatementUpdate.executeUpdate() == 1;
		} catch (SQLException error) {
			logger.error(error);
		}
		return false;
	}

	public boolean delete(E e) {
		validateNull(e);
		if (!keyExists) {
			throw new ExceptionField(
					"Al no poseer clave primaria, se recomienda reescribir el metodo delete en la implementacion");
		}
		try {
			if (preparedStatementDelete == null) {
				String queryDelete = getQueryDelete(fieldsWrapper, table);
				preparedStatementDelete = connection.prepareStatement(queryDelete);
			}
			// obtenemos los valores del objeto
			getValuesField(e, fieldsWrapper);
			// armamos los parametros del preparedStatement
			setParametersPreparedStatement(preparedStatementDelete, fieldsWrapper, CRUD.DELETE);
			logger.debug(preparedStatementDelete);

			return preparedStatementDelete.executeUpdate() == 1;
		} catch (SQLException error) {
			logger.error(error);
		}
		return false;
	}

	public List<E> findAll() {
		try {
			if (preparedStatementFindAll == null) {
				String queryFindall = getQueryFindAll(fieldsWrapper, table);
				preparedStatementFindAll = connection.prepareStatement(queryFindall);
			}
			logger.debug(preparedStatementFindAll);
			return getElements(preparedStatementFindAll.executeQuery(), fieldsWrapper, clazz);
		} catch (SQLException error) {
			logger.error(error);
		}
		return null;
	}

	private void validateNull(Object obj) {
		if (obj == null) {
			throw new ExceptionField("No puede enviar null");
		}
	}
}
