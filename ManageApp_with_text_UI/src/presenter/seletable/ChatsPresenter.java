package presenter.seletable;

import usecase.commu.chat.Chat;

import java.util.List;
import java.util.UUID;

/**
 * A Selection of Chats.
 */
public class ChatsPresenter implements Selectable<Chat>{
	
	private final List<Chat> chats;
	private final UUID       owner;
	
	public ChatsPresenter(List<Chat> chats, UUID owner){
		this.chats = chats;
		this.owner = owner;
	}
	
	@Override
	public List<Chat> getSelection(){
		return chats;
	}
	
	@Override
	public String getDescription(Chat chat){
		return chat.getRecordFor(owner).getName();
	}
}
