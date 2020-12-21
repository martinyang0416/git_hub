package usecase.commu.chat.record;

import entity.social.Message;
import exception.social.AccessException;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Iterator Design Pattern, A ChatManage is a "flow" of {@code Message}, meaning the message
 * can only be added to one end, and can't be removed. No message can be added to prior
 * the time of the latest record message.
 *
 * <p>
 * This class also utilize the Observer Design Pattern, We are keeping a copy of the ChatManage
 * for every person who owns it, so we have many "aliases". But notice that these are not real aliases,
 * they still are separate objects, but they obtain the same information by a sync logic,
 * if a message is send in one end we need to sync the message to the other ends. So whenever
 * someone added a message to this class, it will also sync all the aliases. The reason for making this
 * design is to make sure everyone has a copy of the chat, so if someone leaves the chat they would still
 * have a copy of the previous messages.
 * </p>
 *
 * <p>
 * Class Invariant:
 * <ul>
 *     <li>The Messages that the iterator outputs is always be sorted by time (i.e. the first message comes
 *     out is the least recent message, and the last message would be the most recent message).</li>
 * </ul>
 * </p>
 */
public interface ChatRecord extends MessageObserver, Serializable{
	
//	/**
//	 * Returns a list of Message, from index start to index end.
//	 * If there is not enough message in the range [start, end) we would return all we got.
//	 *
//	 * <p>
//	 * For example, if wants to query the latest 10 message, you would call {@code getChatRecords(0, 10);}
//	 * </p>
//	 *
//	 * @param start the start index (inclusive).
//	 * @param end the end index (exclusive).
//	 * @return a list of Message with length (end - start) if there are enough message else returns all we got.
//	 */
//	Iterator<Message> getMessages(int start, int end);
	
	/**
	 * Returns all the messages in the ChatRecord.
	 *
	 * @return all the message of this ChatRecord.
	 */
	Iterator<Message> getMessages();
	
	/**
	 * Get the most recent message.
	 * If there is no Message returns null.
	 *
	 * @return the most recent Message, if there are no message yet or
	 * the owner can't view the message, returns null.
	 */
	Message getMostRecent() throws AccessException;
	
	/**
	 * Get the name of this ChatManage
	 *
	 * @return the name
	 */
	String getName();
	
	/**
	 * Set the name of this ChatManage
	 *
	 * @param name the new name
	 */
	void setName(String name);
}
