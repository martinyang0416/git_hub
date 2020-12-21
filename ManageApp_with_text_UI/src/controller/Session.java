package controller;

import usecase.commu.ChatManage;
import usecase.commu.ChatManager;
import usecase.event.EventManage;
import usecase.event.EventManager;
import usecase.friends.FriendsManage;
import usecase.friends.FriendsManager;
import usecase.people.PeopleManage;
import usecase.people.PeopleManager;
import usecase.task.TaskManage;
import usecase.task.TaskManager;

import java.io.Serializable;
import java.util.UUID;

/**
 * Session contains all Managers, and also being Serializable.
 */
public class Session implements Serializable{
	
	// loggedIn is transient because we don't need to store it
	// when the session get restored, it needs to login again.
	transient private UUID loggedIn;
	
	// managers
	private final EventManage   eventManage;
	private final PeopleManage  peopleManage;
	private final FriendsManage friendsManage;
	private final ChatManage    chatManage;
	private final TaskManage    taskManage;
	
	public Session(){
		this.eventManage = new EventManager();
		this.peopleManage = new PeopleManager();
		this.friendsManage = new FriendsManager();
		this.chatManage = new ChatManager();
		this.taskManage = new TaskManager();
	}
	
	/**
	 * Get the loggedIn person's ID.
	 */
	public UUID getLoggedIn(){
		return loggedIn;
	}
	
	/**
	 * Set the loggedIn.
	 */
	public void setLoggedIn(UUID loggedIn){
		this.loggedIn = loggedIn;
	}
	
	/**
	 * Check the LoggedIn Can Attend.
	 */
	public boolean canAttend(){
		return peopleManage.canAttend(getLoggedIn());
	}
	
	/**
	 * Check the LoggedIn can Speak.
	 */
	public boolean canSpeak(){
		return peopleManage.canSpeak(getLoggedIn());
	}
	
	/**
	 * Check the LoggedIn can Organize.
	 */
	public boolean canOrganize(){
		return peopleManage.canOrganize(getLoggedIn());
	}
	
	/**
	 * Return the EventManager
	 */
	public EventManage getEM(){
		return eventManage;
	}
	
	/**
	 * Return the ChatManage
	 */
	public ChatManage getCM(){
		return chatManage;
	}
	
	/**
	 * Return the PeopleManage
	 */
	public PeopleManage getPM(){
		return peopleManage;
	}
	
	/**
	 * Return the FriendsManage
	 */
	public FriendsManage getFM(){
		return friendsManage;
	}
	
	/**
	 * Return the TaskManage
	 */
	public TaskManage getTM(){
		return taskManage;
	}
}
