package presenter.seletable;

import entity.task.Task;

import java.util.List;
import java.util.function.Function;

/**
 * A Selection of Task.
 */
public class TaskPresenter implements Selectable<Task>{
	
	private final List<Task>             tasks;
	private final Function<Task, String> taskMap;
	
	public TaskPresenter(List<Task> tasks, Function<Task, String> taskMap){
		this.tasks = tasks;
		this.taskMap = taskMap;
	}
	
	@Override
	public List<Task> getSelection(){
		return tasks;
	}
	
	@Override
	public String getDescription(Task task){
		return taskMap.apply(task);
	}
}
