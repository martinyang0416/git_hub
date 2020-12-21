package UI.screens.people;

import UI.textUI.Screen;
import UI.textUI.Selection;
import UI.textUI.TextIO;
import controller.people.PeopleControl;
import exception.IllegalInputException;
import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.seletable.PeoplePresenter;

import java.util.UUID;

/**
 * The People Screen.
 */
public class PeopleScreen implements Screen{
	
	private final PeopleControl peopleControl;
	private       boolean       hasNext = true;
	
	public PeopleScreen(PeopleControl peopleControl){
		this.peopleControl = peopleControl;
	}
	
	@Override
	public void render(TextIO io){
		io.printLine("People Center");
		io.printDivider1();
		// print all the people
		printPeoples(io);
		io.printDivider1();
		// print the controls
		ActionsPresenter actions = peopleControl.getActions();
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
					case "create organizer account":
						if(! createOrganizer(io)) selected = null;
						break;
					case "create speaker account":
						if(! createSpeaker(io)) selected = null;
						break;
					case "message all speaker (direct chat)":
						messageAllSpeaker(io);
						break;
					case "message all attendee (direct chat)":
						messageAllAttendee(io);
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
	
	private void messageAllAttendee(TextIO io){
		do{
			io.printDivider2();
			// now we ask for a message
			String message = io.readLineWithPrompt("Enter the Message : ");
			if(message.equals("")){
				break;
			}
			// we add a message
			Respond respond = peopleControl.messageAllAttendee(message);
			io.printLines(respond);
		}while(true);
	}
	
	private void messageAllSpeaker(TextIO io){
		do{
			io.printDivider2();
			// now we ask for a message
			String message = io.readLineWithPrompt("Enter the Message : ");
			if(message.equals("")){
				break;
			}
			// we add a message
			Respond respond = peopleControl.messageAllSpeaker(message);
			io.printLines(respond);
		}while(true);
	}
	
	private boolean createSpeaker(TextIO io){
		Respond respond;
		do{
			io.printDivider2();
			io.printLine("Enter the detail of the Speaker : ");
			String name = io.readLineWithPrompt("Name/Username : ");
			char[] password = io.readPasswordWithPrompt("Password : ");
			if(name.equals("") && password.length == 0){
				io.printLine("Aborting!");
				return false;
			}
			respond = peopleControl.createSpeaker(name, password);
			io.printLines(respond);
		}while(! respond.isSuccess());
		return respond.isSuccess();
	}
	
	private boolean createOrganizer(TextIO io){
		Respond respond;
		do{
			io.printDivider2();
			io.printLine("Enter the detail of the Organizer : ");
			String name = io.readLineWithPrompt("Name/Username : ");
			char[] password = io.readPasswordWithPrompt("Password : ");
			if(name.equals("") && password.length == 0){
				io.printLine("Aborting!");
				return false;
			}
			respond = peopleControl.createOrganizer(name, password);
			io.printLines(respond);
		}while(! respond.isSuccess());
		return respond.isSuccess();
	}
	
	private Selection<UUID> printPeoples(TextIO io){
		io.printLine("Here is all registered person in the system : ");
		io.printDivider2();
		PeoplePresenter organizers = peopleControl.getOrganizers();
		PeoplePresenter speakers = peopleControl.getSpeakers();
		PeoplePresenter attendees = peopleControl.getAttendees();
		Selection<UUID> selection = new Selection<>();
		selection.addUnit("Organizer : ", organizers);
		selection.addUnit("Speaker : ", speakers);
		selection.addUnit("Attendee : ", attendees);
		io.printLines(selection);
		return selection;
	}
	
	@Override
	public boolean hasNextScreen(){
		return hasNext;
	}
}
