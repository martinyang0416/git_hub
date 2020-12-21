package controller.event;

import entity.event.Event;
import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.seletable.EventsPresenter;

/**
 * The Controls for EventScreen.
 *
 * @see UI.screens.event.EventScreen
 */
public interface EventControl{
	
	/**
	 * Get the possible actions, specified by the type of user of the logged in.
	 */
	ActionsPresenter getActions();
	
	/**
	 * Get all events.
	 */
	EventsPresenter getEventsListing();
	
	/**
	 * Create a EventCreateControl.
	 */
	EventManageControl getEventCreateControl();
	
	/**
	 * Sign up someone.
	 *
	 * Check capacity through EventManage.
	 *
	 * Add an Appointment to the event through TaskManage, check if there are conflicting task.
	 *
	 * Enroll for the Announcement Group for that event through ChatManage, with View access (so can't add message).
	 *
	 * Update Work relation through FriendsManage:
	 * - To Speaker +1 (if no speaker, do nothing)
	 *
	 * Update signup through EventManage.
	 */
	Respond signup(Event event);
	
	/**
	 * Cancel Sign up.
	 *
	 * Remove the Appointment to the event through TaskManage.
	 *
	 * Delete for the Announcement Group for that event through ChatManage.
	 *
	 * Update Work relation through FriendsManage:
	 * - To Speaker -1 (if no speaker, do nothing)
	 *
	 * Update signup through EventManage.
	 */
	Respond cancelSignup(Event event);
}
