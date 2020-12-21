package entity.event.factory;

import entity.event.Event;
import entity.event.Talk;
import exception.NotSupportedException;

import java.time.LocalDateTime;

public class EventFactory{
	
	public Event create(String type, String title,
	                    String location, LocalDateTime startTime, LocalDateTime endTime){
		switch(type.toLowerCase()){
			case "talk":
				return new Talk(title, location, startTime, endTime);
			default:
				throw new NotSupportedException("Event of type " + type + " is not supported!");
		}
	}
	
}
