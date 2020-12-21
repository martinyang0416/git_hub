package UI.screens;

import UI.textUI.Screen;
import UI.textUI.ScreenExecutor;
import UI.textUI.Selection;
import UI.textUI.TextIO;
import controller.LoginControl;
import exception.IllegalInputException;
import presenter.Respond;
import presenter.seletable.ActionsPresenter;

/**
 * The Login Screen.
 */
public class LoginScreen implements Screen{
	
	private final LoginControl loginControl;
	private       boolean      hasNext = true;
	
	public LoginScreen(LoginControl loginControl){
		this.loginControl = loginControl;
	}
	
	@Override
	public void render(TextIO io){
		// greeting message
		io.printLine("Welcome to Conference Control System!");
		// print the tools
		io.printDivider1();
		ActionsPresenter actions = loginControl.getActions();
		Selection<String> actionSelect = new Selection<>("Login Options : ", actions);
		io.printLines(actionSelect);
		String selected;
		do{
			io.printDivider1();
			String input = io.readLineWithPrompt("Select an Action : ");
			try{
				selected = actionSelect.processInput(input);
				switch(selected.toLowerCase()){
					case "login":
						if(! login(io)) selected = null;
						else new ScreenExecutor(new MenuScreen(loginControl.getMenuControl())).run(io);
						break;
					case "create attendee account":
						if(! createAttendee(io)) selected = null;
						break;
					case "exit program":
						hasNext = false;
						io.printLine("Exiting! Have a good day!");
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
	
	private boolean login(TextIO io){
		Respond respond;
		do{
			io.printDivider2();
			io.printLine("Enter your Credential : ");
			String username = io.readLineWithPrompt("Username : ");
			char[] password = io.readPasswordWithPrompt("Password : ");
			if(username.equals("") && password.length == 0){
				io.printLine("Aborting!");
				return false;
			}
			respond = loginControl.login(username, password);
			io.printDivider3();
			io.printLines(respond);
		}while(! respond.isSuccess());
		return respond.isSuccess();
	}
	
	private boolean createAttendee(TextIO io){
		Respond respond;
		do{
			io.printDivider2();
			io.printLine("Enter the detail of the Attendee : ");
			String name = io.readLineWithPrompt("Name/Usernames : ");
			char[] password = io.readPasswordWithPrompt("Password : ");
			if(name.equals("") && password.length == 0){
				io.printLine("Aborting!");
				return false;
			}
			respond = loginControl.createAttendee(name, password);
			io.printDivider3();
			io.printLines(respond);
		}while(! respond.isSuccess());
		return respond.isSuccess();
	}
	
	@Override
	public boolean hasNextScreen(){
		return hasNext;
	}
}
