package controller.people;

import controller.Session;
import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.seletable.PeoplePresenter;
import usecase.commu.ChatManage;
import usecase.commu.chat.Chat;
import usecase.friends.FriendsManage;
import usecase.people.PeopleManage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PeopleController implements PeopleControl{
	
	private final Session session;
	
	public PeopleController(Session session){
		this.session = session;
	}
	
	@Override
	public ActionsPresenter getActions(){
		List<String> actions = new ArrayList<>();
		if(session.canOrganize()){
			actions.add("Create Organizer Account");
			actions.add("Create Speaker Account");
			actions.add("Message all Speaker (Direct Chat)");
			actions.add("Message all Attendee (Direct Chat)");
		}
		actions.add("Go Back");
		return new ActionsPresenter(actions);
	}
	
	@Override
	public Respond createSpeaker(String username, char[] password){
		PeopleManage pm = session.getPM();
		FriendsManage fm = session.getFM();
		if(pm.hasSameUsername(username)){
			return new Respond(false, "Username already in use!");
		}
		UUID speaker = pm.create("Speaker", username, new String(password));
		// connect speaker to all organizer
		fm.updateWorkRelation(
				speaker,
				pm.getAllPeople().stream().filter(pm::canOrganize).collect(Collectors.toList()),
				uuid -> + 1
		);
		return new Respond("Speaker Created!");
	}
	
	@Override
	public Respond createOrganizer(String username, char[] password){
		PeopleManage pm = session.getPM();
		FriendsManage fm = session.getFM();
		if(pm.hasSameUsername(username)){
			return new Respond(false, "Username already in use!");
		}
		UUID organizer = pm.create("Organizer", username, new String(password));
		// connect organizer to all speaker
		fm.updateWorkRelation(
				organizer,
				pm.getAllPeople().stream().filter(pm::canSpeak).collect(Collectors.toList()),
				uuid -> + 1
		);
		return new Respond("Organizer Created!");
	}
	
	@Override
	public PeoplePresenter getOrganizers(){
		PeopleManage pm = session.getPM();
		List<UUID> organizers = pm.getAllPeople().stream()
		                          .filter(pm::canOrganize).collect(Collectors.toList());
		return new PeoplePresenter(organizers, pm::getName);
	}
	
	@Override
	public PeoplePresenter getSpeakers(){
		PeopleManage pm = session.getPM();
		List<UUID> speakers = pm.getAllPeople().stream()
		                        .filter(pm::canSpeak).collect(Collectors.toList());
		return new PeoplePresenter(speakers, pm::getName);
	}
	
	@Override
	public PeoplePresenter getAttendees(){
		PeopleManage pm = session.getPM();
		List<UUID> attendees = pm.getAllPeople().stream()
		                         .filter(pm::canAttend).collect(Collectors.toList());
		return new PeoplePresenter(attendees, pm::getName);
	}
	
	@Override
	public Respond messageAllAttendee(String message){
		PeopleManage pm = session.getPM();
		if(message.equals(""))
			return new Respond(false, "Empty Message Not allowed!");
		messageAll(message, pm::canAttend);
		return new Respond("Message Sent to all attendees!");
	}
	
	@Override
	public Respond messageAllSpeaker(String message){
		PeopleManage pm = session.getPM();
		if(message.equals(""))
			return new Respond(false, "Empty Message Not allowed!");
		messageAll(message, pm::canSpeak);
		return new Respond("Message Sent to all speakers!");
	}
	
	private void messageAll(String message, Predicate<UUID> filter){
		UUID loggedIn = session.getLoggedIn();
		FriendsManage fm = session.getFM();
		PeopleManage pm = session.getPM();
		ChatManage cm = session.getCM();
		fm.getCoWorker(loggedIn).stream()
		  .filter(filter)
		  .forEach(uuid -> {
			  Chat c = cm.getDirectChat(loggedIn, uuid, pm::getName);
			  // loggedIn have access because direct chat all has OWN
			  // loggedIn has relation because from coWorker,
			  // so we can directly add message.
			  c.sendMessage(loggedIn, message);
		  });
	}
}
