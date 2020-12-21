package usecase.commu.chat;

import exception.social.AccessException;
import usecase.commu.chat.access.AccessLevel;

import java.util.UUID;

/**
 * Announcement group, for messaging all attendee at an announcement.
 */
public class Announcement extends Chat implements ChatEnroll{
	
	private final UUID   event;
	private final String name;
	
	public Announcement(UUID event, String name){
		super();
		this.event = event;
		this.name = name;
	}
	
	@Override
	public void enrollFor(UUID caster, UUID person, AccessLevel initialAccess){
		// if person already has, do nothing
		if(hasRecordFor(person))
			return;
		// Check if we are self enrolling, Announcement allows self enrolling, otherwise,
		// when the caster is enrolling for someone else, the caster must have manage access
		if(caster.equals(person) || checkAccess(caster, AccessLevel.MANAGE))
			createFor(person, name, initialAccess);
	}
	
	@Override
	public void deleteFor(UUID caster, UUID person){
		if(! hasRecordFor(person))
			return;
		if(! caster.equals(person))
			throw new AccessException("Only the person itself can delete the chat.");
		deleteFor(person);
	}
	
	public boolean isAnnouncementFor(UUID event){
		return this.event.equals(event);
	}
}
