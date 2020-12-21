package controller.event;

import controller.Session;
import controller.description_visitor.EventDescriptionVisitor;
import controller.description_visitor.TaskDescriptionVisitor;
import entity.event.Event;
import entity.task.Task;
import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.seletable.EventsPresenter;
import presenter.seletable.PeoplePresenter;
import presenter.status.EventStatusPresenter;
import usecase.commu.ChatManage;
import usecase.commu.chat.Chat;
import usecase.commu.chat.access.AccessLevel;
import usecase.event.EventManage;
import usecase.friends.FriendsManage;
import usecase.people.PeopleManage;
import usecase.task.TaskManage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventManageController implements EventManageControl{
	
	private final Session session;
	
	public EventManageController(Session session){
		this.session = session;
	}
	
	@Override
	public ActionsPresenter getActions(){
		List<String> actions = new ArrayList<>();
		if(session.canOrganize()){
			actions.add("Create Talk");
			actions.add("Assign Speaker");
		}
		if(session.canSpeak()){
			actions.add("Message All Attendees (Direct Chat)");
		}
		actions.add("Go Back");
		return new ActionsPresenter(actions);
	}
	
	@Override
	public EventsPresenter getManagingEvents(){
		UUID loggedIn = session.getLoggedIn();
		EventManage em = session.getEM();
		List<Event> managing = em.getAllEvents().stream()
		                         .filter(event -> {
			                         UUID org = em.getOrganizer(event);
			                         UUID spk = em.getSpeaker(event);
			                         return loggedIn.equals(org) || loggedIn.equals(spk);
		                         })
		                         .collect(Collectors.toList());
		final EventDescriptionVisitor visitor = new EventDescriptionVisitor(session);
		return new EventsPresenter(
				managing,
				event -> {
					event.accept(visitor);
					return visitor.getLastResult();
				}
		);
	}
	
	@Override
	public PeoplePresenter getSpeakers(){
		PeopleManage pm = session.getPM();
		List<UUID> people = pm.getAllPeople().stream()
		                      .filter(pm::canSpeak)
		                      .collect(Collectors.toList());
		return new PeoplePresenter(people, pm::getName);
	}
	
	@Override
	public EventStatusPresenter getEventStatus(Event event){
		EventManage em = session.getEM();
		PeopleManage pm = session.getPM();
		UUID speaker = em.getSpeaker(event);
		return new EventStatusPresenter(
				em.getTitle(event),
				speaker == null ? null : pm.getName(speaker)
		);
	}
	
	@Override
	public Respond createTalk(String title, String location, String date, String time){
		UUID loggedIn = session.getLoggedIn();
		EventManage em = session.getEM();
		ChatManage cm = session.getCM();
		// create the event first
		LocalDateTime start;
		try{
			start = phaseDateTime(date, time);
		}catch(DateTimeParseException parseException){
			return new Respond(false,
			                   "Can't parse DateTime!",
			                   parseException.getLocalizedMessage());
		}
		// we validate the time
		List<String> dtReport = validateDateTime(start);
		if(dtReport.size() != 0){
			dtReport.add(0, "Date Time Not valid, because : ");
			return new Respond(false, dtReport);
		}
		LocalDateTime end = start.plusHours(1);
		// we create an event.
		Event event = em.createEvent("Talk", title, location, start, end);
		// now we try the talk
		List<Event> conflicting = em.getConflicting(event);
		if(conflicting.size() != 0){
			// if there are conflicting events
			List<String> report = new ArrayList<>();
			report.add("Create Talk Failed, due to the following conflicting events : ");
			EventDescriptionVisitor visitor = new EventDescriptionVisitor(session);
			for(Event e : conflicting){
				e.accept(visitor);
				report.add(visitor.getLastResult());
			}
			return new Respond(false, report);
		}
		em.addEvent(event);
		// we create the announcement group
		cm.createAnnouncement(event, em::getTitle);
		// set the organizer
		em.setOrganizer(event, loggedIn);
		return new Respond("Create Talk Successful!");
	}
	
	private LocalDateTime phaseDateTime(String dateString, String timeString) throws DateTimeParseException{
		LocalDate date;
		if(dateString.equals("today")){
			date = LocalDate.now();
		}else{
			date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		}
		LocalTime time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("H:mm"));
		return LocalDateTime.of(date, time);
	}
	
	private List<String> validateDateTime(LocalDateTime dateTime){
		// for phase 1, we only allows from 9am - 7pm, that is from 9:00 ~ 19:00,
		// and duration of 1 hour, so time must be between 9:00 ~ 18:00
		List<String> report = new ArrayList<>();
		if(dateTime.getHour() < 9 || dateTime.getHour() > 18){
			report.add("Hour out of bound! Currently we only support hour from 9-18.");
		}
		if(dateTime.getHour() == 18 && dateTime.getMinute() != 0){
			report.add("Start time is too late! The event must end before 19:00.");
		}
		return report;
	}
	
	@Override
	public Respond assignSpeaker(Event event, UUID newSpeaker){
		EventManage em = session.getEM();
		TaskManage tm = session.getTM();
		ChatManage cm = session.getCM();
		FriendsManage fm = session.getFM();
		// first get the old speaker, check if the old and new speaker are the same.
		UUID oldSpeaker = em.getSpeaker(event);
		if(oldSpeaker != null && oldSpeaker.equals(newSpeaker))
			return new Respond(false, "The Speaker is already the speaker for the event!");
		// create a speaker duty, and try
		Task speakerDuty = tm.createTask("SpeakerDuty", event);
		List<Task> conflicting = tm.getConflicting(newSpeaker, speakerDuty);
		if(conflicting.size() != 0){
			List<String> report = new ArrayList<>();
			report.add("Assign Speaker Failed, because the speaker have the following conflicting task : ");
			TaskDescriptionVisitor visitor = new TaskDescriptionVisitor(session);
			for(Task t : conflicting){
				t.accept(visitor);
				report.add(visitor.getLastResult());
			}
			return new Respond(false, report);
		}
		tm.addTask(newSpeaker, speakerDuty);
		// now we remove the connections of the old speakers.
		if(oldSpeaker != null){
			// remove speaker duty
			tm.removeTaskFor(oldSpeaker, event);
			// delete announcement access
			cm.deleteAnnouncement(event, oldSpeaker);
			// remove relation to attendees
			fm.updateWorkRelation(
					oldSpeaker,
					em.getSignedUp(event),
					uuid -> - 1
			);
		}
		// add announcement access
		cm.enrollAnnouncement(event, newSpeaker, AccessLevel.MANAGE);
		// update relation to attendees
		fm.updateWorkRelation(
				newSpeaker,
				em.getSignedUp(event),
				uuid -> + 1
		);
		// set speaker in event
		em.setSpeaker(event, newSpeaker);
		return new Respond("Assign Speaker Successful!");
	}
	
	@Override
	public Respond messageAllAttendee(Event event, String message){
		UUID loggedIn = session.getLoggedIn();
		EventManage em = session.getEM();
		ChatManage cm = session.getCM();
		PeopleManage pm = session.getPM();
		FriendsManage fm = session.getFM();
		if(message.equals(""))
			return new Respond(false, "Empty Message Not allowed!");
		Stream<UUID> attendee = fm.getCoWorker(loggedIn).stream()
		                          .filter(uuid -> em.isSignedUp(event, uuid));
		attendee.forEach(uuid -> {
			Chat c = cm.getDirectChat(loggedIn, uuid, pm::getName);
			c.sendMessage(loggedIn, message);
		});
		return new Respond("Message Sent to All Attendee!");
	}
}
