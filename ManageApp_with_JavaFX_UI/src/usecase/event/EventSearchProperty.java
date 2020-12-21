package usecase.event;

public enum EventSearchProperty{
	ALL("All", "Search Everything"),
	TYPE("Type", "Search Event Type (e.g. \"talk\")"),
	TITLE("Title", "Search Title"),
	SPEAKER("Speaker", "Search Speaker's Name"),
	ORGANIZER("Organizer", "Search Organizer's Name"),
	LOCATION("Location", "Search Location"),
	TIME("Time", "Search Time Range (e.g. 12:00~13:00)");
	
	private final String rep;
	private final String hint;
	
	EventSearchProperty(String rep, String hint){
		this.rep = rep;
		this.hint = hint;
	}
	
	public String getHint(){
		return hint;
	}
	
	@Override
	public String toString(){
		return rep;
	}
}
