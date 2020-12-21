package controller;

import controller.event.EventControl;
import controller.event.EventController;
import controller.people.PeopleControl;
import controller.people.PeopleController;
import controller.social.ChatsControl;
import controller.social.ChatsController;
import controller.social.FriendsControl;
import controller.social.FriendsController;
import controller.task.TaskControl;
import controller.task.TaskController;
import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.status.PersonStatusPresenter;
import usecase.people.PeopleManage;

import java.util.ArrayList;
import java.util.UUID;

public class MenuController implements MenuControl{
	
	private final Session session;
	
	public MenuController(Session session){
		this.session = session;
	}
	
	@Override
	public ActionsPresenter getActions(){
		ArrayList<String> actions = new ArrayList<>();
		if(session.canOrganize()){
			actions.add("Event Center");
			actions.add("People Center");
		}
		if(session.canSpeak()){
			actions.add("Event Center");
			actions.add("View Speaker Duties");
		}
		if(session.canAttend()){
			actions.add("Event Listing");
			actions.add("View Appointments");
		}
		actions.add("Friends & Co-Workers");
		actions.add("Messaging");
		actions.add("Logout");
		return new ActionsPresenter(actions);
	}
	
	@Override
	public PersonStatusPresenter getStatus(){
		UUID loggedIn = session.getLoggedIn();
		PeopleManage pm = session.getPM();
		return new PersonStatusPresenter(
				pm.getName(loggedIn),
				getPersonTitle()
		);
	}
	
	private String getPersonTitle(){
		if(session.canOrganize())
			return "Organizer";
		else if(session.canSpeak())
			return "Speaker";
		else
			return "Attendee";
	}
	
	@Override
	public Respond logout(){
		session.setLoggedIn(null);
		return new Respond("Logged Out!");
	}
	
	@Override
	public EventControl getEventControl(){
		return new EventController(session);
	}
	
	@Override
	public PeopleControl getPeopleControl(){
		return new PeopleController(session);
	}
	
	@Override
	public TaskControl getTaskControl(){
		return new TaskController(session);
	}
	
	@Override
	public ChatsControl getChatControl(){
		return new ChatsController(session);
	}
	
	@Override
	public FriendsControl getFriendsControl(){
		return new FriendsController(session);
	}
}
