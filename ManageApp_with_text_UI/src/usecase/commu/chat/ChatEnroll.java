package usecase.commu.chat;

import usecase.commu.chat.access.AccessLevel;

import java.util.UUID;

/**
 * This interface defines the enrolling method for a chat.
 * The chats that can hold an arbitrary number (not fixed) of people should implement this.
 *
 * <p>
 * Note : not every chats supports enroll, for example {@link DirectChat} doesn't support enroll.
 * Because it's only defined for two people.
 * </p>
 */
public interface ChatEnroll{
	
	/**
	 * Create a record for person.
	 * If there is not enough capacity, throws a CapacityException.
	 * If the record for that person already exist do nothing.
	 *
	 * <p>
	 * There are two kinds of enrollment, self enrollment, manager enrollment.
	 * Self enrollment means some one is enrolling for themself, some chat
	 * permits this kinds of enrollment. Manager enrollment requires the caster
	 * to have {@link AccessLevel#MANAGE} access, otherwise it will throws a
	 * {@link exception.social.AccessException}.
	 * </p>
	 *
	 * @param caster the caster of this operation.
	 * @param person the owner of the new create chat.
	 * @param initialAccess the initial access level.
	 * @throws exception.social.AccessException if the caster doesn't have {@link AccessLevel#MANAGE} access.
	 */
	void enrollFor(UUID caster, UUID person, AccessLevel initialAccess);
	
	/**
	 * Remove the record for person.
	 * Set the access of that person to {@link AccessLevel#UNDEFINED}.
	 *
	 * <p>
	 * This method deletes the chat record for the person, so only the person itself
	 * can perform this action.
	 * </p>
	 *
	 * @param caster the caster of this operation.
	 * @param person the person that is effected by this operation.
	 * @throws exception.social.AccessException if the caster and person is not the same.
	 */
	void deleteFor(UUID caster, UUID person);
}
