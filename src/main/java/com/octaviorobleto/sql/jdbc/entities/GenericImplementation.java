package com.octaviorobleto.sql.jdbc.entities;

import java.util.List;

import com.octaviorobleto.sql.jdbc.interfaces.DAO;

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

	public E findById(K k) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean save(E e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delete(E e) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<E> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
