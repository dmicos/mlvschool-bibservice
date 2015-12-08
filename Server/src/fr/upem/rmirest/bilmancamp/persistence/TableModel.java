package fr.upem.rmirest.bilmancamp.persistence;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * 
 * @author ybilissor
 * 
 *         This interfaces defines a model of relational table. It allows us to
 *         perform <b>CRUD</b> operations and advanced research.
 * @param <T>
 *            The Entity model
 */

public interface TableModel<T> {

	/**
	 * Add the given {@link T} into the database
	 * 
	 * @param obj
	 *            The item to add
	 * @return
	 */
	public boolean insert(T obj) throws SQLException,RemoteException;

	/**
	 * Delete the row(s) matching given keys
	 * 
	 * @param pk
	 *            The table primary keys
	 * @return <code>true</code> if operation succeeds otherwise
	 *         <code>false</code>
	 */
	public boolean delete(Object... pk) throws SQLException,RemoteException;

	/**
	 * Get all table's rows contained into current table
	 * 
	 * @return the list of {@link T}
	 */
	public List<T> select() throws SQLException,RemoteException;

	/**
	 * Get rows data from table
	 * 
	 * @param index
	 *            the cursor position
	 * @param limit
	 *            the number of rows to retrieve
	 * @return a list of {@link T}
	 */
	public List<T> select(int index, int limit) throws SQLException,RemoteException;

	/**
	 * find all rows that match given key(s)
	 * 
	 * @param pk
	 *            the table's primary key(s)
	 * @return an optional object of type {@link T}
	 */
	public Optional<T> find(Object... pk) throws SQLException,RemoteException;

	/**
	 * Search into table if there is any row matching given keywords
	 * 
	 * @param keyWords
	 *            the {@link T} tags
	 * @return a list of {@link T}
	 */
	public List<T> search(String... tags) throws SQLException,RemoteException;

	/**
	 * Update given old{@link T} by the new {@link T}
	 * 
	 * @param oldVal
	 * @param newVal
	 * @return <code>true</code> if operation in successful otherwise
	 *         <code>false</code>
	 */
	public boolean update(T oldVal, T newVal) throws SQLException,RemoteException;

}
