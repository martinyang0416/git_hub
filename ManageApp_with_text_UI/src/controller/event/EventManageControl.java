package controller.event;

import entity.event.Event;
import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.seletable.EventsPresenter;
import presenter.seletable.PeoplePresenter;
import presenter.status.EventStatusPresenter;

import java.util.UUID;

/**
 * The Controls for the EventManageScreen
 *
 * @see UI.screens.event.EventManageScreen
 */
public interface EventManageControl{
	
	/**
	 * Get the possible actions, specified by the type of user of the logged in.
	 */
	ActionsPresenter getActions();
	
	/**
	 * Get all events that the loggedIn is managing (i.e. the organizer or speaker equals to the loggedIn)
	 */
	EventsPresenter getManagingEvents();
	
	/**
	 * Get all speakers (i.e. everyone that can speak).
	 */
	PeoplePresenter getSpeakers();
	
	/**
	 * Get the status of the Event.
	 */
	EventStatusPresenter getEventStatus(Event event);
	
	/**
	 * Create the Talk through EventManager.
	 *
	 * Try to add the Talk.
	 *
	 * Create the Announcement Group for that event.
	 *
	 * Set the organizer to the loggedIn.
	 */
	Respond createTalk(String title, String location, String date, String time);
	
	/**
	 * Create a Speaker Duty through TaskManage.
	 *
	 * Try to add the speaker duty to the new speaker.
	 *
	 * If there is an old speaker,
	 * - remove speaker duty.
	 * - delete announcement access.
	 * - remove work relation to Attendees.
	 *
	 * Add the new speaker's access to announcement.
	 *
	 * Update Work Relation:
	 * - New Speaker to Attendees +1
	 *
	 * Set the speaker through EventManage.
	 */
	Respond assignSpeaker(Event event, UUID speaker);
	
	/**
	 * Message all attendees of the event.
	 */
	Respond messageAllAttendee(Event event, String message);
}
