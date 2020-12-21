package controller.description_visitor;

import controller.Session;
import entity.task.Appointment;
import entity.task.SpeakerDuty;
import entity.task.TaskVisitor;

/**
 * Visitor Design Pattern.
 * Generate a Description of Task.
 */
public class TaskDescriptionVisitor implements TaskVisitor{
	
	private final Session session;
	private       String  lastResult = "";
	
	public TaskDescriptionVisitor(Session session){
		this.session = session;
	}
	
	@Override
	public void visitAppointment(Appointment appointment){
		lastResult = String.format("Appointment for %s",
		                           session.getEM().getTitle(appointment.getTaskFor()));
	}
	
	@Override
	public void visitSpeakerDuty(SpeakerDuty speakerDuty){
		lastResult = String.format("Speaker Duty for %s",
		                           session.getEM().getTitle(speakerDuty.getTaskFor()));
	}
	
	public String getLastResult(){
		return lastResult;
	}
	
}
