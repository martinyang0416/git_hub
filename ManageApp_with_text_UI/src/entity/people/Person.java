package entity.people;

import entity.Identifiable;

import java.io.Serializable;

/**
 * Person represents any human beings at this conference.
 */
public abstract class Person extends Identifiable implements Serializable{
	
	private String name;
	
	/**
	 * Construct a AbstractPerson.
	 *
	 * @param name the display name of this Person.
	 */
	public Person(String name){
		this.name = name;
	}
	
	/**
	 * Get the display name for this Person.
	 *
	 * Display name is the name displayed on the profile
	 *
	 * @return the display name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Set a new display name of this Person
	 *
	 * @param displayName the new display name of this Person.
	 */
	public void setName(String displayName){
		this.name = displayName;
	}
	
	/**
	 * Check can this person attend some event, in another words, check does this person
	 * have Attendee Privilege
	 *
	 * @return the true if this person have Attendee privilege.
	 */
	public abstract boolean canAttend();
	
	/**
	 * Check can this person speak, in another words, check does this person
	 * have Speaker Privilege
	 *
	 * @return the true if this person have Speaker privilege.
	 */
	public abstract boolean canSpeak();
	
	/**
	 * Check can this person organise, in another words, check does this person
	 * have Organizer Privilege
	 *
	 * @return the true if this person have Organizer privilege.
	 */
	public abstract boolean canOrganize();
}
