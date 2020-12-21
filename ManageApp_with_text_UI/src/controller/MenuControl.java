package controller;

import controller.event.EventControl;
import controller.people.PeopleControl;
import controller.social.ChatsControl;
import controller.social.FriendsControl;
import controller.task.TaskControl;
import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.status.PersonStatusPresenter;

/**
 * Control for MenuScreen.
 *
 * @see UI.screens.MenuScreen
 */
public interface MenuControl{
	
	/**
	 * Get the possible actions, specified by the type of user of the logged in.
	 */
	ActionsPresenter getActions();
	
	/**
	 * Get the status of the LoggedIn.
	 */
	PersonStatusPresenter getStatus();
	
	/**
	 * Logout the loggedIn.
	 */
	Respond logout();
	
	/**
	 * Create a EventControl.
	 */
	EventControl getEventControl();
	
	/**
	 * Create a PeopleControl.
	 */
	PeopleControl getPeopleControl();
	
	/**
	 * Create a TaskControl.
	 */
	TaskControl getTaskControl();
	
	/**
	 * Create a ChatsControl.
	 */
	ChatsControl getChatControl();
	
	/**
	 * Create a FriendsControl.
	 */
	FriendsControl getFriendsControl();
}
