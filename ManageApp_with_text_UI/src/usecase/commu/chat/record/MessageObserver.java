package usecase.commu.chat.record;

import entity.social.Message;
import exception.social.MessageTimeFlowError;

/**
 * Observer Design Patter, MessageObserver updates upon receiving a message.
 *
 * @see ChatRecord
 */
public interface MessageObserver{
	
	/**
	 * Add a Message to the record. If successful then sync to all other chat records.
	 *
	 * @param message the message to add.
	 * @throws MessageTimeFlowError if the new message has a time before the last message.
	 */
	void update(Message message);
}
