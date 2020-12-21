package entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * The objects extends this class is uniquely identified by a identifier.
 *
 * <p>
 * This class implements hashcode and equals, which compares it's unique identifier.
 * </p>
 */
public abstract class Identifiable implements Serializable{
	
	private final UUID id;
	
	public Identifiable(){
		this(UUID.randomUUID());
	}
	
	public Identifiable(UUID id){
		this.id = id;
	}
	
	/**
	 * Get the Unique Identifier for this Object.
	 *
	 * @return a {@code UUID} that uniquely identifies this Object.
	 */
	public UUID getID(){
		return id;
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Identifiable that = (Identifiable) o;
		return Objects.equals(id, that.id);
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(id);
	}
}
