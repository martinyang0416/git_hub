package usecase.people;

import exception.people.ShouldCreatePersonException;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * PeopleManage manages a all {@code Person}.
 */
public interface PeopleManage extends Serializable{
	
	/**
	 * Get a Person based on it's username and password, this method is used when logging in.
	 *
	 * @param username the username provided
	 * @param password the password provided
	 * @return a Person if the username and password pair matches some Person, otherwise return null.
	 */
	UUID login(String username, String password);
	
	/**
	 * Set the password of this Person with the new password
	 *
	 * @param person the person
	 * @param newPassword the new password
	 */
	void setPassword(UUID person, String newPassword) throws ShouldCreatePersonException;
	
	/**
	 * Get the name of the person
	 *
	 * @param person the person
	 * @return a person
	 */
	String getName(UUID person);
	
	
	/**
	 * Set the name of the person.
	 *
	 * @param person the person.
	 * @param newName the name.
	 */
	void setName(UUID person, String newName) throws ShouldCreatePersonException;
	
	/**
	 * Check the person can attend or not.
	 *
	 * @param person the person.
	 * @return true iff the person can attend.
	 */
	boolean canAttend(UUID person);
	
	/**
	 * Check the person can speak or not.
	 *
	 * @param person the person.
	 * @return true iff the person can speak.
	 */
	boolean canSpeak(UUID person);
	
	/**
	 * Check the person can organize or not.
	 *
	 * @param person the person.
	 * @return true iff the person can organize.
	 */
	boolean canOrganize(UUID person);
	
	/**
	 * Get all people's ID as a list.
	 *
	 * @return all people's ID.
	 */
	List<UUID> getAllPeople();
	
	/**
	 * Create and store a new Person with type, and name, also with password,
	 *
	 * <p>
	 * Notice that the Person Entity only needs name. The password is stored inside people manage.
	 * And this method uses PersonFactory to create.
	 * </p>
	 *
	 * @param type the type
	 * @param name the name
	 * @param password the password
	 * @return a person.
	 * @see entity.people.factory.PersonFactory
	 */
	UUID create(String type, String name, String password);
	
	/**
	 * Check the name is already have or not, if the name in, return true, else
	 * return false.
	 *
	 * @param username the username
	 * @return bool
	 */
	boolean hasSameUsername(String username);
}
