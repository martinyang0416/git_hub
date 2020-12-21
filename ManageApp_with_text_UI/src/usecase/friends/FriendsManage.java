package usecase.friends;

import usecase.friends.relationship.RelationNetwork;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * FriendsManage manages the relationship between two people.
 *
 * <p>
 * There are two different relationship between two people: friend ship and work relation.
 * Friendship is established when a person add the other person as a friend. And Work relation
 * is established when you signup an event (i.e. you and the speaker is connected through work relation).
 * </p>
 *
 * <p>
 * Note: there are a few important difference to point out between friendship and work relation.
 * Friendship can only have one connection, you could be a friend or not a friend to someone.
 * But work relation can have multiple layers, that is you can be connected to someone else by work
 * relation with multiple connections. For example, if someone is a speaker for two talks, and you
 * attend both talks, then you have two layers of connection to that speaker in work relation. It's
 * important to track the layers, because otherwise if the speaker changes, we would have to check for every
 * person are there other connection to determine to keep the relation or not.
 * </p>
 */
public interface FriendsManage extends Serializable{
	
	/**
	 * Get a list of people who is related by friendship to person.
	 *
	 * @param person the person.
	 * @return a list of people.
	 */
	List<UUID> getFriends(UUID person);
	
	/**
	 * Get a list of people who is related by work relation to person.
	 *
	 * @param person the person.
	 * @return a list of people.
	 */
	List<UUID> getCoWorker(UUID person);
	
	/**
	 * Check p1 and p2 is connected by either friend ship or work relation.
	 *
	 * @param p1 a person.
	 * @param p2 a person.
	 * @return true iff p1 is connected through friendship or work relation to p2, false otherwise.
	 */
	boolean hasRelationTo(UUID p1, UUID p2);
	
	/**
	 * Check p1 and p2 is connected by friend
	 *
	 * @param p1 a person.
	 * @param p2 a person.
	 * @return true iff the two people is connected through friendship.
	 */
	boolean isFriend(UUID p1, UUID p2);
	
	/**
	 * Establish a friend relationship between p1 and p2.
	 * If p1 and p2 is already connected by friendship, do nothing.
	 *
	 * @param p1 a person.
	 * @param p2 a person.
	 */
	void addFriend(UUID p1, UUID p2);
	
	/**
	 * Remove the friend relationship between p1 and p2.
	 * If p1 and p2 hasn't been connected by friendship, do nothing.
	 *
	 * @param p1 a person.
	 * @param p2 a person.
	 */
	void removeFriend(UUID p1, UUID p2);
	
	/**
	 * Establish a work relationship between p1 and p2.
	 *
	 * @param p1 a person.
	 * @param p2 a person.
	 */
	void addWorkRelation(UUID p1, UUID p2);
	
	/**
	 * Remove the work relationship between p1 and p2.
	 *
	 * @param p1 a person.
	 * @param p2 a person.
	 */
	void removeWorkRelation(UUID p1, UUID p2);
	
	/**
	 * Update the work relation graph, by applying the offset specified by the function.
	 *
	 * @see RelationNetwork#update(UUID, Collection, Function)
	 */
	void updateWorkRelation(UUID p, Collection<UUID> ps, Function<UUID, Integer> function);
}
