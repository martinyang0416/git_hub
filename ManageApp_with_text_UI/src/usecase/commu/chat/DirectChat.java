package usecase.commu.chat;

import usecase.commu.chat.access.AccessLevel;

import java.util.UUID;

/**
 * Direct Chat, for messaging between two people.
 */
public class DirectChat extends Chat{
	
	public DirectChat(){
		super();
	}
	
	public void initialize(UUID p1, UUID p2, String name){
		this.createFor(p1, name, AccessLevel.OWN);
		this.createFor(p2, name, AccessLevel.OWN);
	}
	
	public boolean isDirectChatFor(UUID p1, UUID p2){
		return hasAccess(p1, AccessLevel.OWN) &&
		       hasAccess(p2, AccessLevel.OWN);
	}
}
