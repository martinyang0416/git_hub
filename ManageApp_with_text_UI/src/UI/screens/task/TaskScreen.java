package UI.screens.task;

import UI.textUI.Screen;
import UI.textUI.Selection;
import UI.textUI.TextIO;
import controller.task.TaskControl;
import entity.task.Task;
import exception.IllegalInputException;
import presenter.seletable.ActionsPresenter;
import presenter.seletable.TaskPresenter;

/**
 * The Task Screen.
 */
public class TaskScreen implements Screen{
	
	private final TaskControl taskControl;
	private       boolean     hasNext = true;
	
	public TaskScreen(TaskControl taskControl){
		this.taskControl = taskControl;
	}
	
	@Override
	public void render(TextIO io){
		// greeting message
		io.printLine("Task Center");
		io.printDivider1();
		// print tasks
		printTasks(io);
		io.printDivider1();
		// print the tools
		ActionsPresenter actions = taskControl.getActions();
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
	
	private Selection<Task> printTasks(TextIO io){
		io.printLine("You currently have the following Task : ");
		io.printDivider2();
		TaskPresenter tasks = taskControl.getTasks();
		Selection<Task> selection = new Selection<>("Tasks : ", tasks);
		io.printLines(selection);
		return selection;
	}
	
	@Override
	public boolean hasNextScreen(){
		return hasNext;
	}
}
