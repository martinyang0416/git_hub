package presenter.status;

/**
 * Contains the Info about the status of a chat.
 */
public class ChatStatusPresenter{
	
	private final String chatName;
	private final String access;
	private final String addingAs;
	
	public ChatStatusPresenter(String chatName, String access, String addingAs){
		this.chatName = chatName;
		this.access = access;
		this.addingAs = addingAs;
	}
	
	public String getChatName(){
		return chatName;
	}
	
	public String getAddingAs(){
		return addingAs;
	}
	
	public String getAccess(){
		return access;
	}
}
