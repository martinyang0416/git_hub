package presenter.seletable;

import java.util.List;

/**
 * A Selection of Actions.
 */
public class ActionsPresenter implements Selectable<String>{
	
	private final List<String> actions;
	
	public ActionsPresenter(List<String> actions){
		this.actions = actions;
	}
	
	@Override
	public List<String> getSelection(){
		return actions;
	}
	
	@Override
	public String getDescription(String s){
		return s;
	}
}
