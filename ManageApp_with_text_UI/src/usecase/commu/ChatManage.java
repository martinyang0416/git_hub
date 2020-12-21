package usecase.commu;

import entity.event.Event;
import usecase.commu.chat.Chat;
import usecase.commu.chat.access.AccessLevel;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * ChatManage manages all chats, including
 * DirectChat (two people directly talk to each other),
 * Announcement (a group of people can view, and only some can add message).
 */
public interface ChatManage extends Serializable{
	
	/**
	 * Get all Announcements for person.
	 *
	 * @param person the person.
	 * @return a list of announcements.
	 */
	List<Chat> getAllAnnouncement(UUID person);
	
	/**
	 * Get all Direct Chats for person.
	 *
	 * @param person the person.
	 * @return a list of direct chats.
	 */
	List<Chat> getAllDirectChat(UUID person);
	
	/**
	 * Create a direct chat for p1 and p2.
	 * If the chat already exist, return it.
	 *
	 * @param p1 a person.
	 * @param p2 a person.
	 * @param nameMap a function that returns a person's name.
	 * @return a direct chat.
	 */
	Chat getDirectChat(UUID p1, UUID p2, Function<UUID, String> nameMap);
	
	/**
	 * Create a announcement for that event.
	 * If the announcement already exist, throws an {@link exception.social.SocialException}.
	 *
	 * @param event the event.
	 * @param nameMap a function that returns the name of the event.
	 * @throws exception.social.SocialException if the announcement already exist.
	 */
	void createAnnouncement(Event event, Function<Event, String> nameMap);
	
	/**
	 * enroll a person to an announcement.
	 *
	 * @param event the event.
	 * @param person a person.
	 * @param level the initial level.
	 */
	void enrollAnnouncement(Event event, UUID person, AccessLevel level);
	
	/**
	 * remove the person from an announcement.
	 *
	 * @param event the event.
	 * @param person the person.
	 */
	void deleteAnnouncement(Event event, UUID person);
}
