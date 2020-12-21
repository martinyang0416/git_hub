package UI.screens.event;

import UI.textUI.Screen;
import UI.textUI.ScreenExecutor;
import UI.textUI.Selection;
import UI.textUI.TextIO;
import controller.event.EventControl;
import entity.event.Event;
import exception.IllegalInputException;
import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.seletable.EventsPresenter;

/**
 * The Event Screen.
 */
public class EventScreen implements Screen{
	
	private final EventControl eventControl;
	private       boolean      hasNext = true;
	
	public EventScreen(EventControl eventControl){
		this.eventControl = eventControl;
	}
	
	@Override
	public void render(TextIO io){
		// greeting message
		io.printLine("Event Center");
		io.printDivider1();
		// print the event listing
		Selection<Event> eventSelect = printEventListing(io);
		io.printDivider1();
		// the event controls
		ActionsPresenter actions = eventControl.getActions();
		Selection<String> actionSelect = new Selection<>("Possible Actions : ", actions);
		// print selections
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
					case "event create/change":
					case "event manage":
						new ScreenExecutor(new EventManageScreen(eventControl.getEventCreateControl())).run(io);
						break;
					case "signup":
						if(! signup(io, eventSelect)) selected = null;
						break;
					case "cancel signup":
						if(! cancelSignup(io, eventSelect)) selected = null;
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
	
	private boolean signup(TextIO io, Selection<Event> eventSelect){
		Respond respond;
		do{
			io.printDivider2();
			Event event = selectEvent(io, eventSelect);
			if(event == null){
				io.printLine("Aborting!");
				return false;
			}
			respond = eventControl.signup(event);
			io.printLines(respond);
		}while(! respond.isSuccess());
		return respond.isSuccess();
	}
	
	private boolean cancelSignup(TextIO io, Selection<Event> eventSelect){
		Respond respond;
		do{
			io.printDivider2();
			Event event = selectEvent(io, eventSelect);
			if(event == null){
				io.printLine("Aborting!");
				return false;
			}
			respond = eventControl.cancelSignup(event);
			io.printLines(respond);
		}while(! respond.isSuccess());
		return respond.isSuccess();
	}
	
	private Event selectEvent(TextIO io, Selection<Event> eventSelect){
		if(! eventSelect.hasSelection()){
			io.printLine("There ain't any Event yet!");
			return null;
		}
		Event event;
		do{
			String input = io.readLineWithPrompt("Select an event from the listing above : ");
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
	
	private Selection<Event> printEventListing(TextIO io){
		io.printLine("Event Listing : ");
		io.printDivider2();
		EventsPresenter events = eventControl.getEventsListing();
		Selection<Event> selection = new Selection<>();
		selection.addUnit("Talks : ", events);
		io.printLines(selection);
		return selection;
	}
	
	@Override
	public boolean hasNextScreen(){
		return hasNext;
	}
}
