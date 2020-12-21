package usecase.task;

import entity.IEvent;
import entity.event.Event;
import entity.task.Task;
import exception.schedule.ScheduleException;
import usecase.schedule.Schedule;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public interface TaskManage extends Serializable{
	
	/**
	 * Add a task for the person.
	 *
	 * @param person the person.
	 * @param task the new task for that person.
	 * @throws ScheduleException if the event can't be added.
	 * @see Schedule#add(IEvent)
	 */
	void addTask(UUID person, Task task);
	
	/**
	 * Check does the person has a task for the event.
	 * (using {@code Task.isTaskFor(event)})
	 *
	 * @param person the person.
	 * @param event the id of the event.
	 * @return true if there is a task for that event, otherwise false
	 */
	boolean hasTaskFor(UUID person, Event event);
	
	/**
	 * Remove the task for the event of the person.
	 * (using {@code Task.isTaskFor(event)})
	 * If no such task, do nothing.
	 *
	 * @param person the person.
	 * @param event the id of the event.
	 */
	void removeTaskFor(UUID person, Event event);
	
	/**
	 * Return a list of task for that person.
	 *
	 * @param person the person.
	 * @return a list of task.
	 */
	List<Task> getTasks(UUID person);
	
	/**
	 * Return a list of conflicting task to that task for the person.
	 *
	 * @param person the person.
	 * @param task a task to check.
	 * @return a list of task.
	 * @see Schedule#getConflicting(IEvent)
	 */
	List<Task> getConflicting(UUID person, Task task);
	
	/**
	 * Create a Task based on type.
	 * This method should use the TaskFactory.
	 *
	 * @param type the type
	 * @param event the event.
	 * @return a Task.
	 * @see entity.task.factory.TaskFactory
	 */
	Task createTask(String type, Event event);
}
