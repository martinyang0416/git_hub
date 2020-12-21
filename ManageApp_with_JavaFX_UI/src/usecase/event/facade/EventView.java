package usecase.event.facade;

import entity.event.*;
import presenter.center.EventCenterPresent;
import presenter.event.EventViewPresent;
import usecase.event.EventManage;
import usecase.event.EventSearchProperty;
import usecase.people.PeopleManage;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EventView implements Serializable{
	
	private final EventManage  em;
	private final PeopleManage pm;
	
	private final EventConvert eventConvert;
	private final Pattern      timePatten;
	
	public EventView(EventManage em, PeopleManage pm, EventConvert eventConvert){
		this.em = em;
		this.pm = pm;
		this.eventConvert = eventConvert;
		// matches 12:34~12~45
		this.timePatten = Pattern
				.compile("^\\s*(2[0-4]|1[0-9]|[0-9]):([0-5]?[0-9])\\s*~\\s*(2[0-4]|1[0-9]|[0-9]):([0-5]?[0-9])\\s*$");
	}
	
	public void updateView(EventSearchProperty searchProperty, String keyword, EventCenterPresent presenter){
		List<UUID> events = em.getAll().stream()
		                      .filter(filterPredicate(searchProperty, keyword.toLowerCase()))
		                      .collect(Collectors.toList());
		presenter.updateView(eventConvert.convert(events));
	}
	
	public void updateView(UUID event, EventViewPresent presenter){
		presenter.updateView(eventConvert.convert(event));
	}
	
	public boolean isManagedBy(UUID event, UUID person){
		Event e = em.get(event);
		return e.getOrganizer().equals(person) || new SpeakerMatch(id -> id.equals(person), e).isMatched();
	}
	
	private Predicate<UUID> filterPredicate(EventSearchProperty searchProperty, String keyword){
		return id -> {
			if(keyword == null || keyword.equals(""))
				return true;
			boolean isSpecific = ! searchProperty.equals(EventSearchProperty.ALL);
			boolean matched = false;
			Event e = em.get(id);
			switch(searchProperty){
				default:
				case ALL:
				case TYPE:
					matched = matchType(e, keyword);
					if(isSpecific) break;
				case TITLE:
					matched |= matchTitle(e, keyword);
					if(isSpecific) break;
				case SPEAKER:
					matched |= matchSpeaker(e, keyword);
					if(isSpecific) break;
				case LOCATION:
					matched |= matchLocation(e, keyword);
					if(isSpecific) break;
				case ORGANIZER:
					matched |= matchOrganizer(e, keyword);
					if(isSpecific) break;
				case TIME:
					matched |= matchTime(e, keyword);
					if(isSpecific) break;
			}
			return matched;
		};
	}
	
	private boolean matchType(Event e, String keyword){
		return new TypeMatch(keyword, e).isMatched();
	}
	
	private boolean matchTitle(Event e, String keyword){
		return e.getTitle().toLowerCase().contains(keyword);
	}
	
	private boolean matchSpeaker(Event e, String keyword){
		return new SpeakerMatch(id -> pm.getName(id).toLowerCase().contains(keyword), e).isMatched();
	}
	
	private boolean matchLocation(Event e, String keyword){
		return e.getLocation().toLowerCase().contains(keyword);
	}
	
	private boolean matchOrganizer(Event e, String keyword){
		return pm.getName(e.getOrganizer()).toLowerCase().contains(keyword);
	}
	
	private boolean matchTime(Event e, String keyword){
		Matcher matcher = timePatten.matcher(keyword);
		if(matcher.matches()){
			LocalTime start = LocalTime.of(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
			LocalTime end = LocalTime.of(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
			boolean startBefore = start.equals(e.getStartTime().toLocalTime()) ||
			                      start.isBefore(e.getStartTime().toLocalTime());
			boolean endAfter = end.equals(e.getEndTime().toLocalTime()) ||
			                   end.isAfter(e.getEndTime().toLocalTime());
			return startBefore && endAfter;
		}
		return false;
	}
	
	private static class SpeakerMatch implements EventVisitor{
		
		private final Predicate<UUID> predicate;
		private       boolean         isMatch;
		
		public SpeakerMatch(Predicate<UUID> predicate, Event e){
			this.predicate = predicate;
			e.accept(this);
		}
		
		public boolean isMatched(){
			return isMatch;
		}
		
		@Override
		public void visitTalk(Talk talk){
			if(talk.getSpeaker() != null)
				isMatch = predicate.test(talk.getSpeaker());
			else
				isMatch = false;
		}
		
		@Override
		public void visitNonSpeakerEvent(NonSpeakerEvent nse){
			isMatch = false;
		}
		
		@Override
		public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
			isMatch = mse.getSpeakers().stream().anyMatch(predicate);
		}
	}
	
	private static class TypeMatch implements EventVisitor{
		
		private final String  keyword;
		private       boolean isMatch;
		
		public TypeMatch(String keyword, Event e){
			this.keyword = keyword;
			e.accept(this);
		}
		
		public boolean isMatched(){
			return isMatch;
		}
		
		@Override
		public void visitTalk(Talk talk){
			isMatch = "talk".contains(keyword);
		}
		
		@Override
		public void visitNonSpeakerEvent(NonSpeakerEvent nse){
			isMatch = "nonspeakerevent".contains(keyword);
		}
		
		@Override
		public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
			isMatch = "multispeakerevent".contains(keyword);
		}
	}
}
