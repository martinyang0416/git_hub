package presenter.seletable;

import entity.event.Event;

import java.util.List;
import java.util.function.Function;

/**
 * A Selection of Events.
 */
public class EventsPresenter implements Selectable<Event>{
	
	private final List<Event>             events;
	private final Function<Event, String> eventMap;
	
	public EventsPresenter(List<Event> events, Function<Event, String> eventMap){
		this.events = events;
		this.eventMap = eventMap;
	}
	
	@Override
	public List<Event> getSelection(){
		return events;
	}
	
	@Override
	public String getDescription(Event event){
		return eventMap.apply(event);
	}
}
