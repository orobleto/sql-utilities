package com.octaviorobleto.sql.jdbc.interfaces;

import java.util.List;

/**
 * 
 * interfaz que contiene la abstracción de los métodos necesarios para acceder,
 * modificar, eliminar e insertar nuestros objetos
 * 
 * @author <a href="https://octaviorobleto.com" target="_blank">Octavio
 *         Robleto</a>
 * @version 1.0
 * @date 2022-07-14
 * @class DAO
 * 
 * @param <E> Objeto de Negocio
 * @param <K> Objeto que representa la clave primaria (Compuesta o Simple) de
 *            <E> en la base de datos
 */
public interface DAO<E, K> {
	E findById(K k);

	boolean save(E e);

	boolean delete(E e);

	List<E> findAll();
}
