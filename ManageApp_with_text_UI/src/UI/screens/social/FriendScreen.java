package UI.screens.social;

import UI.textUI.Screen;
import UI.textUI.ScreenExecutor;
import UI.textUI.Selection;
import UI.textUI.TextIO;
import controller.social.FriendsControl;
import controller.social.MessagingControl;
import exception.IllegalInputException;
import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.seletable.PeoplePresenter;

import java.util.UUID;

/**
 * The Friends Screen.
 */
public class FriendScreen implements Screen{
	
	private final FriendsControl friendsControl;
	private       boolean        hasNext = true;
	
	public FriendScreen(FriendsControl friendsControl){
		this.friendsControl = friendsControl;
	}
	
	@Override
	public void render(TextIO io){
		// greeting messages
		io.printLine("Relationship Center");
		io.printDivider1();
		// print the friends
		Selection<UUID> friendsSelect = printFriends(io);
		io.printDivider1();
		// the friends controls
		ActionsPresenter actions = friendsControl.getActions();
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
					case "message to":
						if(! messageTo(io, friendsSelect)) selected = null;
						break;
					case "add friend":
						if(! addFriend(io)) selected = null;
						break;
					case "remove friend":
						if(! removeFriend(io, friendsSelect)) selected = null;
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
	
	private boolean addFriend(TextIO io){
		io.printDivider2();
		PeoplePresenter allAttendee = friendsControl.getAllNonFriendsAttendee();
		Selection<UUID> attendeeSelection = new Selection<>("Attendees : ", allAttendee);
		io.printLines(attendeeSelection);
		Respond respond;
		do{
			io.printDivider2();
			UUID attendee = selectFriends(io, "Attendee", attendeeSelection);
			if(attendee == null){
				io.printLine("Aborting!");
				return false;
			}
			respond = friendsControl.addFriend(attendee);
			io.printLines(respond);
		}while(! respond.isSuccess());
		return respond.isSuccess();
	}
	
	private boolean removeFriend(TextIO io, Selection<UUID> friendsSelect){
		Respond respond;
		do{
			io.printDivider2();
			UUID friend = selectFriends(io, "a friend from above to remove", friendsSelect);
			if(friend == null){
				io.printLine("Aborting!");
				return false;
			}
			respond = friendsControl.removeFriend(friend);
			io.printLines(respond);
		}while(! respond.isSuccess());
		return respond.isSuccess();
	}
	
	private boolean messageTo(TextIO io, Selection<UUID> friendsSelect){
		io.printDivider2();
		UUID friend = selectFriends(io, "a friend from above to message", friendsSelect);
		if(friend == null){
			io.printLine("Aborting!");
			return false;
		}
		// we execute the messaging screen
		MessagingControl messagingControl = friendsControl.getMessagingControl(friend);
		new ScreenExecutor(new MessagingScreen(messagingControl)).run(io);
		return true;
	}
	
	private UUID selectFriends(TextIO io, String selecting, Selection<UUID> friendSelect){
		if(! friendSelect.hasSelection()){
			io.printLine("Nothing to select!");
			return null;
		}
		UUID friend;
		do{
			String input = io.readLineWithPrompt("Select %s : ", selecting);
			try{
				friend = friendSelect.processInput(input);
				if(friend == null){
					return null;
				}
				io.printDivider2();
			}catch(IllegalInputException e){
				io.printLine(e.getLocalizedMessage());
				io.printDivider2();
				friend = null;
			}
		}while(friend == null);
		return friend;
	}
	
	private Selection<UUID> printFriends(TextIO io){
		io.printLine("You are related to the following people : ");
		io.printDivider2();
		PeoplePresenter friends = friendsControl.getFriends();
		PeoplePresenter coWorkers = friendsControl.getCoWorkers();
		Selection<UUID> selection = new Selection<>();
		selection.addUnit("Friends : ", friends);
		selection.addUnit("CoWorkers : ", coWorkers);
		io.printLines(selection);
		return selection;
	}
	
	@Override
	public boolean hasNextScreen(){
		return hasNext;
	}
}
