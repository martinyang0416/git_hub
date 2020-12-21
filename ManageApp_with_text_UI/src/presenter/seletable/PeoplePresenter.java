package presenter.seletable;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * A Selection of People (UUID).
 */
public class PeoplePresenter implements Selectable<UUID>{
	
	private final List<UUID>             ids;
	private final Function<UUID, String> nameMap;
	
	public PeoplePresenter(List<UUID> ids, Function<UUID, String> nameMap){
		this.ids = ids;
		this.nameMap = nameMap;
	}
	
	@Override
	public List<UUID> getSelection(){
		return ids;
	}
	
	@Override
	public String getDescription(UUID uuid){
		return nameMap.apply(uuid);
	}
}
