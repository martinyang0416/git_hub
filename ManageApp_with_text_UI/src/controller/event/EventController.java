package controller.event;

import controller.Session;
import controller.description_visitor.EventDescriptionVisitor;
import controller.description_visitor.TaskDescriptionVisitor;
import entity.event.Event;
import entity.task.Task;
import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.seletable.EventsPresenter;
import usecase.commu.chat.access.AccessLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventController implements EventControl{
	
	private final Session session;
	
	public EventController(Session session){
		this.session = session;
	}
	
	@Override
	public ActionsPresenter getActions(){
		List<String> actions = new ArrayList<>();
		if(session.canOrganize()){
			actions.add("Event Create/Change");
		}
		if(session.canSpeak()){
			actions.add("Event Manage");
		}
		if(session.canAttend()){
			actions.add("Signup");
			actions.add("Cancel Signup");
		}
		actions.add("Go Back");
		return new ActionsPresenter(actions);
	}
	
	@Override
	public EventsPresenter getEventsListing(){
		final EventDescriptionVisitor visitor = new EventDescriptionVisitor(session);
		return new EventsPresenter(
				session.getEM().getAllEvents(),
				event -> {
					event.accept(visitor);
					return visitor.getLastResult();
				}
		);
	}
	
	@Override
	public EventManageControl getEventCreateControl(){
		return new EventManageController(session);
	}
	
	@Override
	public Respond signup(Event event){
		UUID person = session.getLoggedIn();
		// check capacity
		if(! session.getEM().haveEnoughSpaceFor(event, 1))
			return new Respond(false, "The Event is full!");
		// check if the people already have signed up for the event
		if(session.getEM().isSignedUp(event, person))
			return new Respond(false, "You are already signed up!");
		// create the appointment, and try
		Task appointment = session.getTM().createTask("Appointment", event);
		List<Task> conflicting = session.getTM().getConflicting(person, appointment);
		if(conflicting.size() != 0){
			// if there are conflicting tasks
			List<String> report = new ArrayList<>();
			report.add("Signup failed, because you have the following conflicting task : ");
			TaskDescriptionVisitor visitor = new TaskDescriptionVisitor(session);
			for(Task t : conflicting){
				t.accept(visitor);
				report.add(visitor.getLastResult());
			}
			return new Respond(false, report);
		}
		session.getTM().addTask(person, appointment);
		// enroll to the announcement, with sync
		session.getCM().enrollAnnouncement(event, person, AccessLevel.SYNC);
		// update the work relation
		UUID speaker = session.getEM().getSpeaker(event);
		if(speaker != null){
			session.getFM().addWorkRelation(person, speaker);
		}
		// update signup in event
		session.getEM().signUp(event, person);
		return new Respond("Signup Successful!");
	}
	
	@Override
	public Respond cancelSignup(Event event){
		UUID person = session.getLoggedIn();
		// if the people don't have appointment for the event.
		if(! session.getEM().isSignedUp(event, person))
			return new Respond(false, "You haven't signed up yet!");
		// remove the appointment
		session.getTM().removeTaskFor(person, event);
		// remove the announcement
		session.getCM().deleteAnnouncement(event, person);
		// update the work relation
		UUID speaker = session.getEM().getSpeaker(event);
		if(speaker != null){
			session.getFM().removeWorkRelation(person, speaker);
		}
		// subtract one to signed up count.
		session.getEM().removeSignUp(event, person);
		return new Respond("Cancel Successful!");
	}
}
