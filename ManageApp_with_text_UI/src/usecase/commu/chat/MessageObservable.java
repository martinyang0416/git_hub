package usecase.commu.chat;

import entity.social.Message;

import java.util.UUID;

/**
 * Observer Design Pattern. Update all the ChatRecords when a new message comes.
 *
 * @see usecase.commu.chat.record.MessageObserver
 */
public interface MessageObservable{
	
	/**
	 * Update all other chat with the updater.
	 *
	 * @param newMessage the new messages.
	 */
	void notifyAll(Message newMessage);
	
	/**
	 * Update all other chat with the updater.
	 *
	 * @param person the person's chat record to notify.
	 * @param newMessage the new messages.
	 */
	void notify(UUID person, Message newMessage);
}
