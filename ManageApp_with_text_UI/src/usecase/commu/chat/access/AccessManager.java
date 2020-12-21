package usecase.commu.chat.access;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class AccessManager implements AccessManage{
	
	private final Map<UUID, AccessLevel> accessMap;
	
	public AccessManager(){
		accessMap = new HashMap<>();
	}
	
	@Override
	public AccessLevel getAccess(UUID person){
		return accessMap.getOrDefault(person, AccessLevel.UNDEFINED);
	}
	
	@Override
	public boolean hasAccess(UUID person, AccessLevel accessLevel){
		return getAccess(person).getLevel() >= accessLevel.getLevel();
	}
	
	@Override
	public void setAccess(UUID person, AccessLevel accessLevel){
		accessMap.put(person, accessLevel);
	}
	
	@Override
	public List<UUID> getAllParticipant(){
		return accessMap.keySet().stream()
		                .filter(person -> ! getAccess(person).equals(AccessLevel.UNDEFINED))
		                .collect(Collectors.toList());
	}
}
