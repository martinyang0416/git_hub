package usecase.event.facade;

import entity.event.*;
import usecase.dto.PersonDTO;
import usecase.event.EventManage;
import usecase.people.PeopleManage;
import usecase.people.facade.PeopleConvert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventExtract implements Serializable{
	
	private final EventManage   em;
	private final PeopleConvert peopleConvert;
	
	public EventExtract(EventManage em, PeopleManage pm){
		this.em = em;
		this.peopleConvert = new PeopleConvert(pm);
	}
	
	public List<PersonDTO> getSpeakers(UUID event){
		Event e = em.get(event);
		ExtractSpeaker extractSpeaker = new ExtractSpeaker();
		e.accept(extractSpeaker);
		return peopleConvert.convert(extractSpeaker.getSpeakers());
	}
	
	public PersonDTO getOrganizer(UUID event){
		Event e = em.get(event);
		return peopleConvert.convert(e.getOrganizer());
	}
	
	private static class ExtractSpeaker implements EventVisitor{
		
		private final List<UUID> speakers = new ArrayList<>();
		
		public List<UUID> getSpeakers(){
			return speakers;
		}
		
		@Override
		public void visitTalk(Talk talk){
			if(talk.getSpeaker() != null)
				speakers.add(talk.getSpeaker());
		}
		
		@Override
		public void visitNonSpeakerEvent(NonSpeakerEvent nse){
			// do nothing
		}
		
		@Override
		public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
			speakers.addAll(mse.getSpeakers());
		}
	}
}
