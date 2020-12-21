package UI.screens.event;

import UI.textUI.Screen;
import UI.textUI.Selection;
import UI.textUI.TextIO;
import controller.event.EventManageControl;
import entity.event.Event;
import exception.IllegalInputException;
import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.seletable.EventsPresenter;
import presenter.seletable.PeoplePresenter;
import presenter.status.EventStatusPresenter;

import java.util.UUID;

/**
 * The EventManage Screen.
 */
public class EventManageScreen implements Screen{
	
	private final EventManageControl manageControl;
	private       boolean            hasNext = true;
	
	public EventManageScreen(EventManageControl manageControl){
		this.manageControl = manageControl;
	}
	
	@Override
	public void render(TextIO io){
		// greeting message
		io.printLine("Event Management Tool");
		io.printDivider1();
		// print the managing events
		Selection<Event> eventSelect = printManagingEvent(io);
		io.printDivider1();
		// print the tools
		ActionsPresenter actions = manageControl.getActions();
		Selection<String> actionSelect = new Selection<>("Possible Actions : ", actions);
		io.printLines(actionSelect);
		String selected;
		do{
			io.printDivider1();
			String input = io.readLineWithPrompt("Select an Action : ");
			try{
				selected = actionSelect.processInput(input);
				if(selected == null)
					selected = "go back";
				switch(selected.toLowerCase()){
					case "create talk":
						if(! createTalk(io)) selected = null;
						break;
					case "assign speaker":
						if(! assignSpeaker(io, eventSelect)) selected = null;
						break;
					case "message all attendees (direct chat)":
						if(! messageAllAttendee(io, eventSelect)) selected = null;
						break;
					case "go back":
						hasNext = false;
						break;
					default:
						io.printLine("Invalid Selection! Please try again!");
						selected = null;
				}
			}catch(IllegalInputException e){
				io.printLine(e.getLocalizedMessage());
				selected = null;
			}
		}while(selected == null);
	}
	
	private boolean createTalk(TextIO io){
		Respond respond;
		do{
			io.printDivider2();
			io.printLine("Enter the detail of the Talk : ");
			io.printLine("Currently, we only support Event that start time between 9:00 to 18:00.");
			String title = io.readLineWithPrompt("Title : ");
			String location = io.readLineWithPrompt("Location : ");
			String date = io.readLineWithPrompt("Date (yyyy/MM/dd or \"today\") : ");
			String startTime = io.readLineWithPrompt("Start time (H:mm) : ");
			if(title.equals("") && location.equals("") && date.equals("") && startTime.equals("")){
				io.printLine("Aborting!");
				return false;
			}
			respond = manageControl.createTalk(title, location, date, startTime);
			io.printDivider3();
			io.printLines(respond);
		}while(! respond.isSuccess());
		return respond.isSuccess();
	}
	
	private boolean assignSpeaker(TextIO io, Selection<Event> eventSelect){
		Event event = selectEvent(io, eventSelect);
		if(event == null){
			io.printLine("Aborting!");
			return false;
		}
		// once we have an event, we print it's status
		printEventStatus(io, event);
		io.printDivider3();
		// then we choose a speaker
		Selection<UUID> speakerSelect = printSpeakers(io);
		// we keep trying until a speaker works.
		Respond respond;
		do{
			UUID speaker = selectSpeaker(io, speakerSelect);
			if(speaker == null){
				io.printLine("Aborting!");
				return false;
			}
			// we try to assign it.
			respond = manageControl.assignSpeaker(event, speaker);
			io.printDivider3();
			io.printLines(respond);
		}while(! respond.isSuccess());
		return respond.isSuccess();
	}
	
	private boolean messageAllAttendee(TextIO io, Selection<Event> eventSelect){
		Event event = selectEvent(io, eventSelect);
		if(event == null){
			io.printLine("Aborting!");
			return false;
		}
		do{
			io.printDivider3();
			// now we ask for a message
			String message = io.readLineWithPrompt("Enter the Message : ");
			if(message.equals("")){
				break;
			}
			// we add a message
			Respond respond = manageControl.messageAllAttendee(event, message);
			io.printLines(respond);
		}while(true);
		return true;
	}
	
	private void printEventStatus(TextIO io, Event event){
		EventStatusPresenter eventStatus = manageControl.getEventStatus(event);
		io.printLine("\"%s\" currently %s",
		             eventStatus.getTitle(),
		             eventStatus.hasSpeaker() ?
		             "has " + eventStatus.getSpeaker() + " as the speaker." :
		             "does not have a speaker.");
	}
	
	private UUID selectSpeaker(TextIO io, Selection<UUID> speakerSelect){
		if(! speakerSelect.hasSelection()){
			io.printLine("There isn't any Speaker to Select!");
			return null;
		}
		UUID speaker;
		do{
			io.printDivider2();
			String input = io.readLineWithPrompt("Select a Speaker : ");
			try{
				speaker = speakerSelect.processInput(input);
				// if the user wants to abort
				if(speaker == null){
					return null;
				}
			}catch(IllegalInputException e){
				io.printLine(e.getLocalizedMessage());
				speaker = null;
			}
		}while(speaker == null);
		return speaker;
	}
	
	private Event selectEvent(TextIO io, Selection<Event> eventSelect){
		if(! eventSelect.hasSelection()){
			io.printLine("You haven't manage any event yet!");
			return null;
		}
		Event event;
		do{
			io.printDivider2();
			String input = io.readLineWithPrompt("Select a event from above : ");
			try{
				event = eventSelect.processInput(input);
				// if the user wants to abort
				if(event == null){
					return null;
				}
			}catch(IllegalInputException e){
				io.printLine(e.getLocalizedMessage());
				event = null;
			}
		}while(event == null);
		return event;
	}
	
	private Selection<UUID> printSpeakers(TextIO io){
		PeoplePresenter speakers = manageControl.getSpeakers();
		Selection<UUID> selection = new Selection<>("Speakers : ", speakers);
		io.printLines(selection);
		return selection;
	}
	
	private Selection<Event> printManagingEvent(TextIO io){
		io.printLine("You are managing the following events : ");
		io.printDivider2();
		EventsPresenter events = manageControl.getManagingEvents();
		Selection<Event> selection = new Selection<>();
		selection.addUnit("Events : ", events);
		io.printLines(selection);
		return selection;
	}
	
	@Override
	public boolean hasNextScreen(){
		return hasNext;
	}
}
