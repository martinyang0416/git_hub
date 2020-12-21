package usecase.friends.relationship.relation;

import exception.NotSupportedException;

public class RelationshipFactory{
	
	/**
	 * Create a relationship with initial value
	 *
	 * @param initialValue the initial value.
	 * @return a relationship.
	 */
	public Relationship create(String type, int initialValue){
		switch(type.toLowerCase()){
			case "non-negative":
				return new NonNegativeRelationship(initialValue);
			default:
				throw new NotSupportedException("Relation of type " + type + " is not Supported!");
		}
	}
}
