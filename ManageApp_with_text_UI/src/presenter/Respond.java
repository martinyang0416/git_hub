package presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A General Respond from a Controller, contains a list of message,
 * and a status (success) to represent the operation is success or not.
 */
public class Respond implements Iterable<String>{
	
	private final List<String> messages;
	private final boolean      success;
	
	/**
	 * Construct a Respond with messages and success.
	 */
	public Respond(boolean success, List<String> messages){
		this.messages = messages;
		this.success = success;
	}
	
	/**
	 * Construct a Respond with messages and success.
	 */
	public Respond(boolean success, String... messages){
		this.messages = Arrays.asList(messages);
		this.success = success;
	}
	
	/**
	 * Construct a Respond with messages and success as true.
	 */
	public Respond(List<String> messages){
		this(true, messages);
	}
	
	/**
	 * Construct a Respond with messages and success as true.
	 */
	public Respond(String... messages){
		this(true, messages);
	}
	
	public List<String> getMessages(){
		return messages;
	}
	
	public boolean isSuccess(){
		return success;
	}

	@Override
	public Iterator<String> iterator(){
		return getMessages().iterator();
	}
}
