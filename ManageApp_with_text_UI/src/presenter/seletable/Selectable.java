package presenter.seletable;

import java.util.List;

/**
 * Selectable provides a list of E to select.
 *
 * @param <E> any type.
 */
public interface Selectable<E>{
	
	/**
	 * Get all selection.
	 */
	List<E> getSelection();
	
	/**
	 * Get the description of a Selection.
	 */
	String getDescription(E e);
}
