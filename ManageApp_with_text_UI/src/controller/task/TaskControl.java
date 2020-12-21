package controller.task;

import presenter.seletable.ActionsPresenter;
import presenter.seletable.TaskPresenter;

/**
 * Control for TaskScreen
 *
 * @see UI.screens.task.TaskScreen
 */
public interface TaskControl{
	
	/**
	 * Get the possible actions, specified by the type of user of the logged in.
	 */
	ActionsPresenter getActions();
	
	/**
	 * Get all task of the LoggedIn.
	 */
	TaskPresenter getTasks();
}
