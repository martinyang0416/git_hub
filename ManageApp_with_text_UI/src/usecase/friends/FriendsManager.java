package usecase.friends;

import exception.social.SocialException;
import usecase.friends.relationship.BiDiRelationNetwork;
import usecase.friends.relationship.RelationNetwork;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class FriendsManager implements FriendsManage{
	
	private final RelationNetwork friendship;
	private final RelationNetwork workRelation;
	
	public FriendsManager(){
		friendship = new BiDiRelationNetwork();
		workRelation = new BiDiRelationNetwork();
	}
	
	@Override
	public List<UUID> getFriends(UUID person){
		return friendship.getConnectedTo(person);
	}
	
	@Override
	public List<UUID> getCoWorker(UUID person){
		return workRelation.getConnectedTo(person);
	}
	
	@Override
	public boolean hasRelationTo(UUID p1, UUID p2){
		return friendship.isConnected(p1, p2) || workRelation.isConnected(p1, p2);
	}
	
	@Override
	public boolean isFriend(UUID p1, UUID p2){
		return friendship.isConnected(p1, p2);
	}
	
	@Override
	public void addFriend(UUID p1, UUID p2){
		checkSamePerson(p1, p2);
		if(! friendship.isConnected(p1, p2))
			friendship.connect(p1, p2);
	}
	
	@Override
	public void removeFriend(UUID p1, UUID p2){
		checkSamePerson(p1, p2);
		if(friendship.isConnected(p1, p2))
			friendship.disconnect(p1, p2);
	}
	
	@Override
	public void addWorkRelation(UUID p1, UUID p2){
		workRelation.connect(p1, p2);
	}
	
	@Override
	public void removeWorkRelation(UUID p1, UUID p2){
		workRelation.disconnect(p1, p2);
	}
	
	@Override
	public void updateWorkRelation(UUID p, Collection<UUID> ps, Function<UUID, Integer> function){
		workRelation.update(p, ps, function);
	}
	
	private void checkSamePerson(UUID p1, UUID p2) throws SocialException{
		if(p1.equals(p2))
			throw new SocialException("They are the same people!");
	}
}
