package com.octaviorobleto.sql.jdbc.entities;

import static com.octaviorobleto.sql.jdbc.utils.DTOUtils.*;
import static com.octaviorobleto.sql.jdbc.utils.FieldUtils.*;
import static com.octaviorobleto.sql.jdbc.utils.QueryUtils.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.octaviorobleto.commons.utilities.text.StringUtils;
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
	private Connection connection;
	private PreparedStatement preparedStatementFindByID;
	private PreparedStatement preparedStatementInsert;
	private PreparedStatement preparedStatementDelete;
	private PreparedStatement preparedStatementUpdate;
	private PreparedStatement preparedStatementFindAll;

	public GenericImplementation(Class<E> clazz, Connection connection) {
		this.clazz = clazz;
		this.connection = connection;
		table = getTableName(clazz);
		fieldsWrapper = getFieldsWrapper(clazz);
	}

	public E findById(K k) {
		if (k == null) {
			return null;
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

		FieldUtils.getValuesField(e, fieldsWrapper);
		@SuppressWarnings("unchecked")
		K k = (K) fieldsWrapper.stream()
				.filter(fieldWrapper -> fieldWrapper.isKey() && StringUtils.isEmpty(fieldWrapper.getParentField()))
				.map(c -> c.getValue()).reduce((a, b) -> a).get();

		E element = findById(k);
		logger.debug(k);
		logger.debug(element);
		
		if (element == null) {
			return insert(e);
		}
		return update(e);
	}

	private boolean insert(E e) {
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
		} catch (SQLException error) {
			logger.error(error);
		}
		return false;
	}

	private boolean update(E e) {
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

}
