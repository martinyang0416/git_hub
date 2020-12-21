package UI.screens;

import UI.screens.event.EventScreen;
import UI.screens.people.PeopleScreen;
import UI.screens.social.ChatsScreen;
import UI.screens.social.FriendScreen;
import UI.screens.task.TaskScreen;
import UI.textUI.Screen;
import UI.textUI.ScreenExecutor;
import UI.textUI.Selection;
import UI.textUI.TextIO;
import controller.MenuControl;
import exception.IllegalInputException;
import presenter.Respond;
import presenter.seletable.ActionsPresenter;
import presenter.status.PersonStatusPresenter;

/**
 * The Menu Screen.
 */
public class MenuScreen implements Screen{
	
	private final MenuControl menuControl;
	private       boolean     hasNext = true;
	
	public MenuScreen(MenuControl menuControl){
		this.menuControl = menuControl;
	}
	
	@Override
	public void render(TextIO io){
		// welcome message
		// print basic status of the person
		PersonStatusPresenter status = menuControl.getStatus();
		io.printLine("Hello %s, you are logged in as %s!", status.getName(), status.getTitle());
		io.printDivider1();
		// print a list of actions that is available for the person logged in
		ActionsPresenter actions = menuControl.getActions();
		Selection<String> selection = new Selection<>("Possible Actions : ", actions);
		io.printLines(selection);
		String selected;
		do{
			io.printDivider1();
			String input = io.readLineWithPrompt("Select an Action : ");
			try{
				selected = selection.processInput(input);
				// the selection wants to abort
				if(selected == null){
					break;
				}
				switch(selected.toLowerCase()){
					case "event center":
					case "event listing":
						new ScreenExecutor(new EventScreen(menuControl.getEventControl())).run(io);
						break;
					case "people center":
						new ScreenExecutor(new PeopleScreen(menuControl.getPeopleControl())).run(io);
						break;
					case "view appointments":
					case "view speaker duties":
						new ScreenExecutor(new TaskScreen(menuControl.getTaskControl())).run(io);
						break;
					case "friends & co-workers":
						new ScreenExecutor(new FriendScreen(menuControl.getFriendsControl())).run(io);
						break;
					case "messaging":
						new ScreenExecutor(new ChatsScreen(menuControl.getChatControl())).run(io);
						break;
					case "logout":
						Respond respond = menuControl.logout();
						io.printLines(respond);
						io.printLine("See you again!");
						hasNext = ! respond.isSuccess();
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
	
	@Override
	public boolean hasNextScreen(){
		return hasNext;
	}
}
