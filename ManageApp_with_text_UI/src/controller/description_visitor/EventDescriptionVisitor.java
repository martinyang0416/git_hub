package controller.description_visitor;

import controller.Session;
import entity.event.EventVisitor;
import entity.event.Talk;
import usecase.event.EventManage;
import usecase.people.PeopleManage;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Visitor Design Pattern.
 * Generate a Description of an Event.
 */
public class EventDescriptionVisitor implements EventVisitor{
	
	private final Session session;
	private       String  lastResult = "";
	
	public EventDescriptionVisitor(Session session){
		this.session = session;
	}
	
	@Override
	public void visitTalk(Talk talk){
		UUID speaker = session.getEM().getSpeaker(talk);
		EventManage em = session.getEM();
		PeopleManage pm = session.getPM();
		lastResult = String.format(
				"Talk \"%s\"%s at %s <%s~%s> (%d/%d)",
				em.getTitle(talk),
				speaker != null ? " : '" + pm.getName(speaker) + "'" : "",
				em.getLocation(talk),
				em.getStartTime(talk).format(DateTimeFormatter.ofPattern("MM/dd H:mm")),
				em.getEndTime(talk).format(DateTimeFormatter.ofPattern("H:mm")),
				em.getSignedUpCount(talk),
				em.getCapacity(talk)
		);
	}
	
	public String getLastResult(){
		return lastResult;
	}
}
