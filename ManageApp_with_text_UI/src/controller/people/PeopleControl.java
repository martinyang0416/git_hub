package controller.people;

import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.seletable.PeoplePresenter;

/**
 * Control for PeopleScreen.
 *
 * @see UI.screens.people.PeopleScreen
 */
public interface PeopleControl{
	
	/**
	 * Get the possible actions, specified by the type of user of the logged in.
	 */
	ActionsPresenter getActions();
	
	/**
	 * Create speaker through people manage
	 *
	 * Update Work Relation:
	 * All organizer to new speaker +1
	 */
	Respond createSpeaker(String username, char[] password);
	
	/**
	 * Create organizer through people manage
	 *
	 * Update Work Relation:
	 * All speaker to new organizer +1
	 */
	Respond createOrganizer(String username, char[] password);
	
	/**
	 * Get all organizers.
	 */
	PeoplePresenter getOrganizers();
	
	/**
	 * Get all speakers.
	 */
	PeoplePresenter getSpeakers();
	
	/**
	 * Get all attendees.
	 */
	PeoplePresenter getAttendees();
	
	/**
	 * Message all Attendees.
	 */
	Respond messageAllAttendee(String message);
	
	/**
	 * Message all Speakers.
	 */
	Respond messageAllSpeaker(String message);
}
