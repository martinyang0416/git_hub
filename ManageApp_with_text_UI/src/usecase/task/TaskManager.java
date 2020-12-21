package usecase.task;

import entity.IEvent;
import entity.event.Event;
import entity.task.Task;
import entity.task.factory.TaskFactory;
import exception.schedule.ScheduleException;
import usecase.schedule.Schedule;
import usecase.schedule.Scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TaskManager implements TaskManage{
	
	private final HashMap<UUID, Schedule<Task>> peopleScheduleMap;
	
	//constructor
	public TaskManager(){
		this.peopleScheduleMap = new HashMap<>();
	}
	
	/**
	 * Add a task for the person.
	 *
	 * @param person the person.
	 * @param task the new task for that person.
	 * @throws ScheduleException if the event can't be added.
	 * @see Schedule#add(IEvent)
	 */
	@Override
	public void addTask(UUID person, Task task){
		//person does not exist
//		if(! peopleScheduleMap.containsKey(person)){
//			Schedule<Task> schedule = new Scheduler<>();
//			peopleScheduleMap.put(person, schedule);
//		}
		// we need to ensure the schedule exist
		ensureExist(person);
		peopleScheduleMap.get(person).add(task);
	}
	
	/**
	 * Check does the person has a task for the event.
	 * (using {@code Task.isTaskFor(event)})
	 *
	 * @param person the person.
	 * @param event the id of the event.
	 * @return true if there is a task for that event, otherwise false
	 */
	@Override
	public boolean hasTaskFor(UUID person, Event event){
		//person does not exist
		if(! peopleScheduleMap.containsKey(person)){
			return false;
		}
		
		for(Task task : peopleScheduleMap.get(person).getAll()){
			if(task.getTaskFor().equals(event.getID())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Remove the task for the event of the person.
	 * (using {@code Task.isTaskFor(event)})
	 * If no such task, do nothing.
	 *
	 * @param person the person.
	 * @param event the id of the event.
	 */
	@Override
	public void removeTaskFor(UUID person, Event event){
		//person does not exist
		if(! peopleScheduleMap.containsKey(person)){
			throw new IllegalArgumentException("Cannot remove this event to the schedule since person does not exist ");
		}
		// concurrent modification!!!
//		for(Task task : peopleScheduleMap.get(person).getAll()){
//			if(task.getTaskFor().equals(event.getID())){
//				peopleScheduleMap.get(person).remove(task);
//			}
//		}
		peopleScheduleMap.get(person)
		                 .removeIf(task -> task.getTaskFor().equals(event.getID()));
	}
	
	/**
	 * Return a list of task for that person.
	 *
	 * @param person the person.
	 * @return a list of task.
	 */
	@Override
	public List<Task> getTasks(UUID person){
		//person does not exist, return empty list
//		if(! peopleScheduleMap.containsKey(person)){
//			// if the people doesn't exist, we create one.
//			Schedule<Task> schedule = new Scheduler<>();
//			peopleScheduleMap.put(person, schedule);
////            throw new IllegalArgumentException("Cannot get this event to the schedule since person does not exist ");
//		}
		// we need to ensure the schedule exist
		ensureExist(person);
		return peopleScheduleMap.get(person).getAll();
	}
	
	/**
	 * Return a list of conflicting task to that task for the person.
	 *
	 * @param person the person.
	 * @param task a task to check.
	 * @return a list of task.
	 * @see Schedule#getConflicting(IEvent)
	 */
	@Override
	public List<Task> getConflicting(UUID person, Task task){
		// we need to ensure the schedule exist
		ensureExist(person);
		return peopleScheduleMap.get(person).getConflicting(task);
	}
	
	/**
	 * Create a Task based on type.
	 * This method should use the TaskFactory.
	 *
	 * @param type the type
	 * @param event the event.
	 * @return a Task.
	 * @see TaskFactory
	 */
	@Override
	public Task createTask(String type, Event event){
		TaskFactory factory = new TaskFactory();
		return factory.create(type, event);
	}
	
	private void ensureExist(UUID person){
		if(! peopleScheduleMap.containsKey(person))
			peopleScheduleMap.put(person, new Scheduler<>());
	}
}

