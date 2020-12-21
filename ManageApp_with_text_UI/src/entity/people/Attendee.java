package entity.people;

/**
 * Attendee represents the visitors at a conference.
 */
public class Attendee extends Person{
	
	/**
	 * Construct a Attendee
	 *
	 * @param name the display name of this Person
	 */
	public Attendee(String name){
		super(name);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return always true.
	 */
	@Override
	public boolean canAttend(){
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return always false.
	 */
	@Override
	public boolean canSpeak(){
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return always false.
	 */
	@Override
	public boolean canOrganize(){
		return false;
	}
}
