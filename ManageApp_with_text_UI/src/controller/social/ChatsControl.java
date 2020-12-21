package controller.social;

import presenter.seletable.ChatsPresenter;
import usecase.commu.chat.Chat;

/**
 * Control for ChatsScreen.
 *
 * @see UI.screens.social.ChatsScreen
 */
public interface ChatsControl{
	
	/**
	 * Get all Announcements from ChatManage
	 */
	ChatsPresenter getAnnouncements();
	
	/**
	 * Get all DirectChats from ChatManage
	 */
	ChatsPresenter getDirectChats();
	
	/**
	 * Create a MessagingControl for the Chat.
	 */
	MessagingControl getMessagingControl(Chat chat);
}
