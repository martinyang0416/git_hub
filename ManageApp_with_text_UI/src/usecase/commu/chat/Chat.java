package usecase.commu.chat;

import entity.social.Message;
import entity.social.factory.MessageFactory;
import exception.social.AccessException;
import exception.social.SocialException;
import usecase.commu.chat.access.AccessLevel;
import usecase.commu.chat.access.AccessManage;
import usecase.commu.chat.access.AccessManager;
import usecase.commu.chat.record.ChatRecord;
import usecase.commu.chat.record.ChatRecorder;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The Base class for Chat.
 * Any chat should supports Manage and Messaging.
 */
public abstract class Chat implements MessageObservable, Serializable{
	
	private final Map<UUID, ChatRecord> syncList;
	private final AccessManage          access;
	
	public Chat(){
		// LinkedHashMap provide half the proportion time for iterating over it.
		this.syncList = new LinkedHashMap<>();
		this.access = new AccessManager();
	}
	
	@Override
	public void notifyAll(Message message){
		// dispatch the change
		syncList.entrySet().stream()
		        .filter(entry -> access.hasAccess(entry.getKey(), AccessLevel.SYNC))
		        .forEach(entry -> entry.getValue().update(message));
	}
	
	@Override
	public void notify(UUID person, Message newMessage){
		syncList.get(person).update(newMessage);
	}
	
	/**
	 * Send a message.
	 * If the sender don't have {@link AccessLevel#EDIT}, throws {@link exception.social.AccessException}.
	 *
	 * <p>
	 * Before using this method, check the sender's access, and the chat's active status.
	 * </p>
	 *
	 * @param content the content
	 * @throws exception.social.MessageTimeFlowError if the time of the new message is prior to the previous message.
	 * @throws exception.social.AccessException if the person don't have {@link AccessLevel#EDIT}.
	 */
	public void sendMessage(UUID sender, Object content){
		checkAccess(sender, AccessLevel.EDIT);
		Message message = getFactory().create(sender, content);
		notifyAll(message);
	}
	
	/**
	 * Set the access level for the person, only when the caster has {@link AccessLevel#MANAGE}.
	 * If the person doesn't have a record yet, do nothing.
	 *
	 * @param caster the caster of this operation.
	 * @param person the person that is effected.
	 * @param level the level of access that it has been given.
	 * @throws exception.social.AccessException if the caster don't has {@link AccessLevel#MANAGE}.
	 * @see AccessManage#setAccess(UUID, AccessLevel)
	 */
	public void setAccess(UUID caster, UUID person, AccessLevel level){
		checkAccess(caster, AccessLevel.MANAGE);
		if(hasRecordFor(person))
			access.setAccess(person, level);
	}
	
	/**
	 * Check the person has at least the access level specified.
	 *
	 * @param person the person.
	 * @param level the level required.
	 * @return true if people has at least the level of access.
	 * @see AccessManage#hasAccess(UUID, AccessLevel)
	 */
	public boolean hasAccess(UUID person, AccessLevel level){
		return access.hasAccess(person, level);
	}
	
	/**
	 * Get the access level for the person.
	 *
	 * @param person the person.
	 * @return the access level for the person.
	 * @see AccessManage#getAccess(UUID)
	 */
	public AccessLevel getAccess(UUID person){
		return access.getAccess(person);
	}
	
	/**
	 * Return all people that has some access (not {@link AccessLevel#UNDEFINED}).
	 *
	 * @return a list of people that has some access.
	 * @see AccessManage#getAllParticipant()
	 */
	public List<UUID> getAllParticipant(){
		return access.getAllParticipant();
	}
	
	/**
	 * Get the record for this person.
	 *
	 * @param person the person.
	 * @return the chat record.
	 */
	public ChatRecord getRecordFor(UUID person){
		return syncList.get(person);
	}
	
	/**
	 * Check does this contains person has a record.
	 *
	 * @param person the person.
	 * @return true if this person has a record.
	 */
	public boolean hasRecordFor(UUID person){
		return syncList.containsKey(person);
	}
	
	/**
	 * Create a new record of chat for the person.
	 *
	 * @param person the person.
	 * @param initialAccess the initial access.
	 */
	protected void createFor(UUID person, String name, AccessLevel initialAccess){
		if(syncList.containsKey(person))
			throw new SocialException(person + " already has a record of the chat!");
		ChatRecord record = new ChatRecorder(name);
		syncList.put(person, record);
		access.setAccess(person, initialAccess);
	}
	
	/**
	 * Remove the record of person, and set the access to {@link AccessLevel#UNDEFINED}
	 *
	 * @param person the person.
	 */
	protected void deleteFor(UUID person){
		if(! syncList.containsKey(person))
			throw new SocialException(person + " doesn't have a record of the chat!");
		syncList.remove(person);
		access.setAccess(person, AccessLevel.UNDEFINED);
	}
	
	/**
	 * Check does the caster has the level of access required, if not
	 * throws a {@link AccessException}.
	 *
	 * @param caster the caster.
	 * @param level the level required.
	 */
	protected boolean checkAccess(UUID caster, AccessLevel level){
		if(! hasAccess(caster, level))
			throw new AccessException(caster + " doesn't have " + level + " Access!");
		return true;
	}
	
	private MessageFactory getFactory(){
		return new MessageFactory();
	}
}
