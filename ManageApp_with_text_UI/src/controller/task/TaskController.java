package controller.task;

import controller.Session;
import controller.description_visitor.TaskDescriptionVisitor;
import entity.task.Task;
import presenter.seletable.ActionsPresenter;
import presenter.seletable.TaskPresenter;

import java.util.ArrayList;
import java.util.List;

public class TaskController implements TaskControl{
	
	private final Session session;
	
	public TaskController(Session session){
		this.session = session;
	}
	
	@Override
	public ActionsPresenter getActions(){
		List<String> actions = new ArrayList<>();
		actions.add("Go Back");
		return new ActionsPresenter(actions);
	}
	
	@Override
	public TaskPresenter getTasks(){
		List<Task> tasks = session.getTM().getTasks(session.getLoggedIn());
		TaskDescriptionVisitor visitor = new TaskDescriptionVisitor(session);
		return new TaskPresenter(
				tasks,
				task -> {
					task.accept(visitor);
					return visitor.getLastResult();
				}
		);
	}
}
