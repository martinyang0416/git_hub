package entity.event;

import entity.IEvent;
import entity.Identifiable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * An Event is a subclass of {@code IEvent} meaning it occupies a time and location
 * for an {@link usecase.schedule.Schedule}, specifically, an Event can only be added
 * to {@link usecase.schedule.Schedule} of the conference.
 *
 * This way when the organizer plans the event, there won't be double booking of the time
 * or location, this is enforced in {@link usecase.schedule.Schedule}.
 */
public abstract class Event extends Identifiable implements IEvent{
	
	private       String        title;
	// Organizer and Speaker
	private final Set<UUID>     signedUp;
	private       UUID          organizer;
	private       UUID          speaker;
	// signup control
	private       int           capacity;
	// IEvent
	private       String        location;
	private       LocalDateTime startTime;
	private       LocalDateTime endTime;
	
	/**
	 * Construct an Event. Notice that:
	 * <ul>
	 * 	   <li>the title has a default value {@code ""} (emtpy string)</li>
	 * 	   <li>the capacity has a default value {@code 2}</li>
	 * 	   <li>the organizer has a default value {@code null}</li>
	 * 	   <li>the speaker has a default value {@code null}</li>
	 * </ul>
	 *
	 * @param location the location of this Event.
	 * @param startTime the startTime of this Event.
	 * @param endTime the endTime of this Event.
	 */
	public Event(String title, String location, LocalDateTime startTime, LocalDateTime endTime){
		this.title = title;
		this.organizer = null;
		this.speaker = null;
		this.signedUp = new HashSet<>();
		// for phase 1, the capacity is 2
		this.capacity = 2;
		this.location = location;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/**
	 * Visitor Design Patten.
	 *
	 * @param visitor a {@code EventVisitor}.
	 * @see EventVisitor
	 */
	public abstract void accept(EventVisitor visitor);
	
	@Override
	public String getLocation(){
		return location;
	}
	
	@Override
	public LocalDateTime getStartTime(){
		return startTime;
	}
	
	@Override
	public LocalDateTime getEndTime(){
		return endTime;
	}
	
	/**
	 * Set the location of this {@code Event}.
	 */
	public void setLocation(String location){
		this.location = location;
	}
	
	/**
	 * Set the start time of this {@code Event}.
	 */
	public void setStartTime(LocalDateTime startTime){
		this.startTime = startTime;
	}
	
	/**
	 * Set the end time of this {@code Event}.
	 */
	public void setEndTime(LocalDateTime endTime){
		this.endTime = endTime;
	}
	
	/**
	 * Get the title of this Event
	 *
	 * @return the title
	 */
	public String getTitle(){
		return title;
	}
	
	/**
	 * Set tht title of this {@code Event}
	 */
	public void setTitle(String title){
		this.title = title;
	}
	
	/**
	 * Get the capacity of this Event.
	 *
	 * @return the capacity of this Event.
	 */
	public int getCapacity(){
		return capacity;
	}
	
	/**
	 * Set the capacity of this Event.
	 *
	 * @throws IllegalArgumentException if the capacity is less than 0.
	 */
	public void setCapacity(int capacity){
		if(capacity < 0)
			throw new IllegalArgumentException("Capacity can't be negative!");
		this.capacity = capacity;
	}
	
	/**
	 * Sign up a people, by adding it to signed up list.
	 *
	 * @param person a people who wants to sign up.
	 * @throws IllegalArgumentException if there isn't enough capacity for the person
	 * or the person is already in the list of signed up.
	 */
	public void signUp(UUID person){
		// core business logic, the signup can't exceed the capacity
		if(signedUp.size() > capacity - 1)
			throw new IllegalArgumentException("Signup count can't exceed Capacity!");
		if(signedUp.contains(person))
			throw new IllegalArgumentException("Same People can't signup twice!");
		this.signedUp.add(person);
	}
	
	/**
	 * Remove the people from signed up list.
	 *
	 * @param person the Person.
	 * @throws IllegalArgumentException if the person hasn't signedUp yet.
	 */
	public void removeSignUp(UUID person){
		if(! signedUp.contains(person))
			throw new IllegalArgumentException("The person hasn't signed up yet!");
		this.signedUp.remove(person);
	}
	
	/**
	 * Get the set of id of the people who is signed up for the event.
	 *
	 * @return a set of id.
	 */
	public Set<UUID> getSignedUp(){
		return signedUp;
	}
	
	/**
	 * Check is the people signed up.
	 *
	 * @param people a people's ID
	 * @return true if the people is signed up already, otherwise false.
	 */
	public boolean isSignedUp(UUID people){
		return signedUp.contains(people);
	}
	
	/**
	 * Get the number of people that is already signed up.
	 *
	 * @return the size of the signup list.
	 */
	public int getSignedUpCount(){
		return signedUp.size();
	}
	
	/**
	 * Get the ID of the organizer of this Event.
	 *
	 * @return the organizer
	 */
	public UUID getOrganizer(){
		return organizer;
	}
	
	/**
	 * Set the Organizer of the Event.
	 */
	public void setOrganizer(UUID organizer){
		this.organizer = organizer;
	}
	
	/**
	 * Get the ID of the speaker of this Event.
	 *
	 * @return the speaker.
	 */
	public UUID getSpeaker(){
		return speaker;
	}
	
	/**
	 * Set the Speaker of the Event.
	 */
	public void setSpeaker(UUID speaker){
		this.speaker = speaker;
	}
}
