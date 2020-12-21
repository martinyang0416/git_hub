package usecase.event;

import entity.IEvent;
import entity.event.Event;
import exception.schedule.ScheduleException;
import usecase.schedule.Schedule;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * EventManage contains all {@link Event} for a conference.
 * EventManage uses {@link Schedule} to ensure no double booking of time and location.
 *
 * @see Schedule
 */
public interface EventManage extends Serializable{
	
	/**
	 * Add a Event.
	 * If it can not be added, throws the schedule exception.
	 *
	 * @param e an {@code Event} to add.
	 * @throws ScheduleException if the event can't be added.
	 * @see Schedule#add(IEvent)
	 */
	void addEvent(Event e);
	
	/**
	 * Remove the Event.
	 *
	 * @param e an {@code Event} to remove.
	 * @see Schedule#remove(IEvent)
	 */
	void removeEvent(Event e);
	
	/**
	 * Check the {@code Event} belongs here.
	 *
	 * @param e the {@code Event} to check.
	 * @return true iff the event belongs here, false otherwise.
	 * @see Schedule#has(IEvent)
	 */
	boolean hasEvent(Event e);
	
	/**
	 * Get all the {@code Event}.
	 *
	 * @return all the {@code Event} as a List.
	 * @see Schedule#getAll()
	 */
	List<Event> getAllEvents();
	
	/**
	 * Return a list of conflicting event to that event.
	 *
	 * @param e the {@code Event} to check.
	 * @return a list of {@code Event} that is conflicting with the event.
	 * @see Schedule#getConflicting(IEvent)
	 */
	List<Event> getConflicting(Event e);
	
	/**
	 * Get the capacity of the Event
	 *
	 * @param e the Event.
	 * @return the capacity.
	 */
	int getCapacity(Event e);
	
	/**
	 * Set the capacity of the Event
	 *
	 * @param e the Event.
	 * @param capacity the new Capacity
	 */
	void setCapacity(Event e, int capacity);
	
	/**
	 * Add people to the sign up list.
	 *
	 * @param e the Event.
	 * @param people a new signup People.
	 */
	void signUp(Event e, UUID people);
	
	/**
	 * Remove the people from signed up list.
	 *
	 * @param e the Event.
	 * @param people an existing signup People.
	 */
	void removeSignUp(Event e, UUID people);
	
	/**
	 * Get the list of signedUp people for an event.
	 *
	 * @param e the Event.
	 * @return a set of signedUp people.
	 */
	Set<UUID> getSignedUp(Event e);
	
	/**
	 * Check if the people is already signedUp.
	 *
	 * @param e the Event.
	 * @param people a People.
	 * @return true if the people is already signed up, otherwise false.
	 */
	boolean isSignedUp(Event e, UUID people);
	
	/**
	 * Get the capacity of the Event.
	 *
	 * @param e the Event.
	 * @return the capacity.
	 */
	int getSignedUpCount(Event e);
	
	/**
	 * Check can i more people sign up to the event.
	 *
	 * @param e the event
	 * @param i the number of new signed up
	 * @return true iff there are enough space for i more signup, otherwise false
	 */
	boolean haveEnoughSpaceFor(Event e, int i);
	
	/**
	 * Get the Organizer of the Event.
	 *
	 * @param e the Event.
	 * @return the organizer.
	 */
	UUID getOrganizer(Event e);
	
	/**
	 * Set the Organizer of the Event.
	 *
	 * @param e the Event.
	 * @param organizer the new Organizer.
	 */
	void setOrganizer(Event e, UUID organizer);
	
	/**
	 * Get the Speaker of the Event.
	 *
	 * @param e the Event.
	 * @return the speaker.
	 */
	UUID getSpeaker(Event e);
	
	/**
	 * Set the Speaker of the Event.
	 *
	 * @param e the Event.
	 * @param speaker the new Speaker.
	 */
	void setSpeaker(Event e, UUID speaker);
	
	/**
	 * Get the Title of the Event.
	 *
	 * @param e the Event.
	 * @return the title.
	 */
	String getTitle(Event e);
	
	/**
	 * Get the Title of the Event with the ID.
	 * If no such event exist, return null.
	 *
	 * @param eventID the ID.
	 * @return the title.
	 */
	String getTitle(UUID eventID);
	
	/**
	 * Set the Title of the Event.
	 *
	 * @param e the Event.
	 * @param title the new title.
	 */
	void setTitle(Event e, String title);
	
	/**
	 * Get the location of the Event.
	 *
	 * @param e the Event.
	 */
	String getLocation(Event e);
	
	/**
	 * Get the start time of the Event.
	 *
	 * @param e the Event.
	 */
	LocalDateTime getStartTime(Event e);
	
	/**
	 * Get the end time of the Event.
	 *
	 * @param e the Event.
	 */
	LocalDateTime getEndTime(Event e);
	
	/**
	 * Create an Event (not adding an event), this method should use EventFactory.
	 *
	 * @see entity.event.factory.EventFactory
	 */
	Event createEvent(String type, String title, String location, LocalDateTime start, LocalDateTime end);
}
