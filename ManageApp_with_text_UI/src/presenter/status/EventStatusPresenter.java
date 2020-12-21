package presenter.status;

/**
 * Contains the info about the status of a event.
 */
public class EventStatusPresenter{
	
	private final String title;
	private final String speaker;
	
	public EventStatusPresenter(String title, String speaker){
		this.title = title;
		this.speaker = speaker;
	}
	
	public boolean hasSpeaker(){
		return speaker != null;
	}
	
	public String getSpeaker(){
		return speaker;
	}
	
	public String getTitle(){
		return title;
	}
}
