package controller.social;

import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.seletable.PeoplePresenter;

import java.util.UUID;

/**
 * Control for FriendsScreen.
 *
 * @see UI.screens.social.FriendScreen
 */
public interface FriendsControl{
	
	/**
	 * Get the possible actions, specified by the type of user of the logged in.
	 */
	ActionsPresenter getActions();
	
	/**
	 * Get Friends through FriendsManage
	 */
	PeoplePresenter getFriends();
	
	/**
	 * Get Work related through FriendsManage
	 */
	PeoplePresenter getCoWorkers();
	
	/**
	 * Add Friend through FriendsManage
	 */
	Respond addFriend(UUID person);
	
	/**
	 * Remove Friend through FriendsManage
	 */
	Respond removeFriend(UUID person);
	
	/**
	 * Get all Non friends Attendees (also not including itself).
	 */
	PeoplePresenter getAllNonFriendsAttendee();
	
	/**
	 * Creat the Messaging Control for a Friends, that is the
	 * MessagingControl for the DirectChat of LoggedIn and the friend.
	 */
	MessagingControl getMessagingControl(UUID friend);
}
