package controller;

import presenter.Respond;
import presenter.seletable.ActionsPresenter;

/**
 * Control for LoginScreen
 *
 * @see UI.screens.LoginScreen
 */
public interface LoginControl{
	
	/**
	 * Get all Actions
	 */
	ActionsPresenter getActions();
	
	/**
	 * Login people through PeopleManage
	 */
	Respond login(String username, char[] password);
	
	/**
	 * Create attendee through people manage
	 *
	 * Update Work Relation:
	 * All organizer to new attendee +1
	 */
	Respond createAttendee(String username, char[] password);
	
	/**
	 * Get the Menu control.
	 */
	MenuControl getMenuControl();
}
