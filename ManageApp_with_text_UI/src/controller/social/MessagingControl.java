package controller.social;

import presenter.MessagePresenter;
import presenter.Respond;
import presenter.status.ChatStatusPresenter;

/**
 * Control for MessagingScreen.
 *
 * @see UI.screens.social.MessagingScreen
 */
public interface MessagingControl{
	
	/**
	 * Get the chat status.
	 */
	ChatStatusPresenter getChatStatus();
	
	/**
	 * Get all message inside the ChatRecord of the LoggedIn
	 */
	MessagePresenter getMessages();
	
	/**
	 * Send a Message as LoggedIn.
	 */
	Respond sendMessage(Object message);
}

