package controller.social;

import controller.Session;
import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.seletable.PeoplePresenter;
import usecase.commu.ChatManage;
import usecase.friends.FriendsManage;
import usecase.people.PeopleManage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FriendsController implements FriendsControl{
	
	private final Session session;
	
	public FriendsController(Session session){
		this.session = session;
	}
	
	@Override
	public ActionsPresenter getActions(){
		List<String> actions = new ArrayList<>();
		actions.add("Message To");
		if(session.canAttend()){
			actions.add("Add Friend");
			actions.add("Remove Friend");
		}
		actions.add("Go Back");
		return new ActionsPresenter(actions);
	}
	
	@Override
	public PeoplePresenter getFriends(){
		UUID loggedIn = session.getLoggedIn();
		PeopleManage pm = session.getPM();
		FriendsManage fm = session.getFM();
		return new PeoplePresenter(
				fm.getFriends(loggedIn),
				pm::getName
		);
	}
	
	@Override
	public PeoplePresenter getCoWorkers(){
		UUID loggedIn = session.getLoggedIn();
		PeopleManage pm = session.getPM();
		FriendsManage fm = session.getFM();
		return new PeoplePresenter(
				fm.getCoWorker(loggedIn),
				pm::getName
		);
	}
	
	@Override
	public Respond addFriend(UUID person){
		UUID loggedIn = session.getLoggedIn();
		FriendsManage fm = session.getFM();
		if(fm.isFriend(loggedIn, person)){
			return new Respond(false, "You two are already friends!");
		}
		fm.addFriend(loggedIn, person);
		return new Respond("Friend Added!");
	}
	
	@Override
	public Respond removeFriend(UUID person){
		UUID loggedIn = session.getLoggedIn();
		FriendsManage fm = session.getFM();
		if(! fm.isFriend(loggedIn, person)){
			return new Respond(false, "You two are not yet friends!");
		}
		fm.removeFriend(loggedIn, person);
		return new Respond("Friend Removed!");
	}
	
	@Override
	public MessagingControl getMessagingControl(UUID friend){
		UUID loggedIn = session.getLoggedIn();
		ChatManage cm = session.getCM();
		PeopleManage pm = session.getPM();
		return new MessagingController(session, cm.getDirectChat(loggedIn, friend, pm::getName));
	}
	
	@Override
	public PeoplePresenter getAllNonFriendsAttendee(){
		UUID loggedIn = session.getLoggedIn();
		PeopleManage pm = session.getPM();
		FriendsManage fm = session.getFM();
		List<UUID> nonFriendsAttendee = pm.getAllPeople().stream()
		                                  .filter(pm::canAttend)
		                                  .filter(uuid -> ! uuid.equals(loggedIn) && ! fm.isFriend(loggedIn, uuid))
		                                  .collect(Collectors.toList());
		return new PeoplePresenter(nonFriendsAttendee, pm::getName);
	}
}
