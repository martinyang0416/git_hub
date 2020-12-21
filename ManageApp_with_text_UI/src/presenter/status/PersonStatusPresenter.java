package presenter.status;

/**
 * Contains the Info about the status of a People (usually the loggedIn).
 */
public class PersonStatusPresenter{
	
	private final String name;
	private final String title;
	
	public PersonStatusPresenter(String name, String title){
		this.name = name;
		this.title = title;
	}
	
	public String getName(){
		return name;
	}
	
	public String getTitle(){
		return title;
	}
}
