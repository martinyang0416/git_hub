package usecase.friends.relationship.relation;

import java.io.Serializable;

/**
 * A Relationship is a mutable integer.
 */
public interface Relationship extends Serializable{
	
	/**
	 * Get the layer of connection to this relationship.
	 *
	 * @return the layer of connection.
	 */
	int getLayer();
	
	/**
	 * Apply an offset of 1.
	 */
	void increase();
	
	/**
	 * Apply an offset of -1.
	 */
	void decrease();
	
	/**
	 * Apply an offset.
	 *
	 * @param offset the offset.
	 */
	void applyOffset(int offset);
}
