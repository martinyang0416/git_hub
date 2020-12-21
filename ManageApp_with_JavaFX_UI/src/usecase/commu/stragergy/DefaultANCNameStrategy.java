package usecase.commu.stragergy;

import entity.event.Event;
import usecase.event.EventManage;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class DefaultANCNameStrategy implements AnnouncementNameStrategy{
	
	private final EventManage em;
	
	public DefaultANCNameStrategy(EventManage em){
		this.em = em;
	}
	
	@Override
	public String generateName(UUID event){
		Event e = em.get(event);
		return String.format(
				"Announcement Group for %s at %s", e.getTitle(),
				e.getStartTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
		);
	}
}
