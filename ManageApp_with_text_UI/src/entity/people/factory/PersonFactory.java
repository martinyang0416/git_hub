package entity.people.factory;

import entity.people.Attendee;
import entity.people.Organizer;
import entity.people.Person;
import entity.people.Speaker;
import exception.NotSupportedException;

public class PersonFactory{
	
	public Person create(String type, String name){
		switch(type.toLowerCase()){
			case "organizer":
				return new Organizer(name);
			case "speaker":
				return new Speaker(name);
			case "attendee":
				return new Attendee(name);
			default:
				throw new NotSupportedException("Person of type " + type + " is not supported!");
		}
	}
}
