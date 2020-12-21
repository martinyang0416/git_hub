package controller;

import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import usecase.friends.FriendsManage;
import usecase.people.PeopleManage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LoginController implements LoginControl{
	
	private final Session session;
	
	public LoginController(Session session){
		this.session = session;
	}
	
	@Override
	public ActionsPresenter getActions(){
		List<String> actions = new ArrayList<>();
		actions.add("Login");
		actions.add("Create Attendee Account");
		actions.add("Exit Program");
		return new ActionsPresenter(actions);
	}
	
	@Override
	public Respond login(String username, char[] password){
		PeopleManage pm = session.getPM();
		UUID loggedIn = pm.login(username, new String(password));
		if(loggedIn != null){
			// the login was successful
			session.setLoggedIn(loggedIn);
			return new Respond(true, "Login Successful!");
		}else{
			// the login was unSuccessful
			return new Respond(false, "Login Unsuccessful! Please try again!");
		}
	}
	
	@Override
	public Respond createAttendee(String username, char[] password){
		PeopleManage pm = session.getPM();
		FriendsManage fm = session.getFM();
		if(pm.hasSameUsername(username)){
			return new Respond(false, "Username already in use!");
		}
		UUID attendee = pm.create("Attendee", username, new String(password));
		// connect attendee to all organizer
		fm.updateWorkRelation(
				attendee,
				pm.getAllPeople().stream().filter(pm::canOrganize).collect(Collectors.toList()),
				uuid -> + 1
		);
		return new Respond("Attendee Created!");
	}
	
	@Override
	public MenuControl getMenuControl(){
		return new MenuController(session);
	}
}
