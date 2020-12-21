package usecase.event;

import entity.event.Event;
import entity.event.factory.EventFactory;
import usecase.schedule.Schedule;
import usecase.schedule.Scheduler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EventManager implements EventManage{
	
	private final Schedule<Event> events;
	
	public EventManager(){
		this.events = new Scheduler<>();
	}
	
	@Override
	public void addEvent(Event e){
		events.add(e);
	}
	
	@Override
	public void removeEvent(Event e){
		events.remove(e);
	}
	
	@Override
	public boolean hasEvent(Event e){
		return events.has(e);
	}
	
	@Override
	public List<Event> getAllEvents(){
		return events.getAll();
	}
	
	@Override
	public List<Event> getConflicting(Event e){
		return events.getConflicting(e);
	}
	
	@Override
	public void setCapacity(Event e, int newCapacity){
		if(hasEvent(e)){
			e.setCapacity(newCapacity);
		}
	}
	
	@Override
	public void signUp(Event e, UUID people){
		if(hasEvent(e)){
			e.signUp(people);
		}
	}
	
	@Override
	public void removeSignUp(Event e, UUID people){
		if(hasEvent(e)){
			e.removeSignUp(people);
		}
	}
	
	@Override
	public int getCapacity(Event e){
		if(hasEvent(e)){
			return e.getCapacity();
		}
		return 0;
	}
	
	@Override
	public Set<UUID> getSignedUp(Event e){
		if(hasEvent(e)){
			return e.getSignedUp();
		}
		return null;
	}
	
	@Override
	public boolean isSignedUp(Event e, UUID people){
		if(hasEvent(e)){
			return e.isSignedUp(people);
		}
		return false;
	}
	
	@Override
	public int getSignedUpCount(Event e){
		if(hasEvent(e)){
			return e.getSignedUpCount();
		}
		return 0;
	}
	
	@Override
	public boolean haveEnoughSpaceFor(Event e, int i){
		if(hasEvent(e)){
			return e.getSignedUpCount() <= e.getCapacity() - i;
		}
		return false;
	}
	
	@Override
	public UUID getOrganizer(Event e){
		if(hasEvent(e)){
			return e.getOrganizer();
		}
		return null;
	}
	
	@Override
	public void setOrganizer(Event e, UUID organizer){
		if(hasEvent(e)){
			e.setOrganizer(organizer);
		}
	}
	
	@Override
	public UUID getSpeaker(Event e){
		if(hasEvent(e)){
			return e.getSpeaker();
		}
		return null;
	}
	
	@Override
	public void setSpeaker(Event e, UUID speaker){
		if(hasEvent(e)){
			e.setSpeaker(speaker);
		}
	}
	
	@Override
	public void setTitle(Event e, String title){
		if(hasEvent(e)){
			e.setTitle(title);
		}
	}
	
	@Override
	public String getLocation(Event e){
		if(hasEvent(e)){
			return e.getLocation();
		}
		return null;
	}
	
	@Override
	public LocalDateTime getStartTime(Event e){
		if(hasEvent(e)){
			return e.getStartTime();
		}
		return null;
	}
	
	@Override
	public LocalDateTime getEndTime(Event e){
		if(hasEvent(e)){
			return e.getEndTime();
		}
		return null;
	}
	
	@Override
	public Event createEvent(String type, String title, String location, LocalDateTime start,
	                         LocalDateTime end){
		// added using event factory!
		return new EventFactory().create(type, title, location, start, end);
	}
	
	@Override
	public String getTitle(Event e){
		if(hasEvent(e)){
			return e.getTitle();
		}
		return null;
	}
	
	@Override
	public String getTitle(UUID eventID){
		for(Event e : getAllEvents()){
			if(e.getID() == eventID){
				return e.getTitle();
			}
		}
		return null;
	}
}
