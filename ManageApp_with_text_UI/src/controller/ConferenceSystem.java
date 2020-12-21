package controller;

import UI.screens.LoginScreen;
import UI.textUI.ScreenExecutor;
import UI.textUI.TextIO;
import controller.event.EventManageControl;
import controller.event.EventManageController;
import controller.people.PeopleControl;
import controller.people.PeopleController;
import gateway.SessionIO;

import java.io.IOException;

/**
 * Conference System is the main class.
 */
public class ConferenceSystem{
	
	/**
	 * Run the Conference System,
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException{
		// the path of where the session is stored!
		String sessionPath = "./session/Session.ser";
		// if you wish to start with an empty session, set it to false.
		boolean loadExample = true;
		// initialize the system
		ConferenceSystem system = new ConferenceSystem(sessionPath, loadExample);
		// we start the program
		system.run();
		// save the session
		system.save(sessionPath);
	}
	
	private final Session session;
	private       TextIO  io;
	
	/**
	 * Construct a Conference System, and try to load a session from the file specified by sessionPath,
	 *
	 * If load failed and loadExample is true, we load an example session specified by
	 * {@link ConferenceSystem#loadExampleSession(Session)}. Otherwise we create load an empty
	 * session specified by {@link ConferenceSystem#loadEmptySession(Session)}.
	 *
	 * @see ConferenceSystem#loadExampleSession(Session)
	 * @see ConferenceSystem#loadEmptySession(Session)
	 */
	public ConferenceSystem(String sessionPath, boolean loadExample) throws ClassNotFoundException{
		SessionIO sessionIO = new SessionIO();
		Session session = sessionIO.restoreSession(sessionPath);
		if(session == null){
			if(loadExample){
				System.out.println("Loading Example Session...");
				this.session = loadExampleSession(new Session());
			}else{
				System.out.println("Loading Empty Session...");
				this.session = loadEmptySession(new Session());
			}
		}else{
			System.out.println("Session Restore Successful!");
			this.session = session;
		}
	}
	
	/**
	 * Run the conference, execute the LoginScreen.
	 */
	public void run(){
		// set up UI
		ScreenExecutor executor = new ScreenExecutor();
		executor.pushScreen(new LoginScreen(new LoginController(session)));
		// execute the Screen
		this.io = new TextIO();
		executor.run(io);
	}
	
	/**
	 * Save the conference (saving the session), and close the TextIO.
	 */
	public void save(String sessionPath) throws IOException{
		SessionIO sessionIO = new SessionIO();
		if(! sessionIO.saveSession(sessionPath, session)){
			// handle not saved!
			System.out.println("Session Save UnSuccessful!");
		}else{
			System.out.println("Session Save Successful!");
		}
		io.close();
	}
	
	/**
	 * Load an empty session.
	 *
	 * We need at least one organizer account!
	 *
	 * <pre>
	 *     The Empty Session contains the following:
	 *     - 1 Organizer
	 *       - Username : org1, Password : 1
	 * </pre>
	 */
	public Session loadEmptySession(Session session){
		// we need at least one organizer!
		PeopleControl peopleControl = new PeopleController(session);
		peopleControl.createOrganizer("org1", new char[]{'1'});
		return session;
	}
	
	/**
	 * Load the an example session.
	 *
	 * <pre>
	 * 		 The Example session contains the following:
	 * 		 - 2 Organizer
	 * 		    - Username : org1, Password : 1
	 * 		    - Username : org2, Password : 2
	 * 		 - 3 Speaker
	 * 		    - Username : spk1, Password : 1
	 * 		    - Username : spk2, Password : 2
	 * 		    - Username : spk3, Password : 3
	 * 		 - 4 Attendee
	 * 		    - Username : att1, Password : 1
	 * 		    - Username : att2, Password : 2
	 * 		    - Username : att3, Password : 3
	 * 		    - Username : att4, Password : 4
	 * 		 - Some Events,
	 * 		    - Talk 1
	 * 		      - Organizer : org1
	 * 		      - Speaker : not set,
	 * 		      - Location : Convocation Hall
	 * 		      - Date : 2020/11/22
	 * 		      - Time : 10:00 ~ 11:00
	 * 		    - Talk 2
	 * 		      - Organizer : org1
	 * 		      - Speaker : not set,
	 * 		      - Location : Bahen Center
	 * 		      - Date : 2020/11/22
	 * 		      - Time : 13:00 ~ 14:00
	 * 		    - Talk 3
	 * 		      - Organizer : org1
	 * 		      - Speaker : not set,
	 * 		      - Location : Convocation Hall
	 * 		      - Date : 2020/11/22
	 * 		      - Time : 13:00 ~ 14:00
	 * 		    Notice that the Speaker of Talk 1 and Talk 2 can't
	 * 		    be the same, because they have the same time and different location.
	 * </pre>
	 */
	public Session loadExampleSession(Session session){
		// create some controller
		PeopleControl peopleControl = new PeopleController(session);
		LoginControl loginControl = new LoginController(session);
		MenuControl menuControl = new MenuController(session);
		EventManageControl eventManageControl = new EventManageController(session);
		// we add 2 organizer
		peopleControl.createOrganizer("org1", new char[]{'1'});
		peopleControl.createOrganizer("org2", new char[]{'2'});
		// we add 3 speaker
		peopleControl.createSpeaker("spk1", new char[]{'1'});
		peopleControl.createSpeaker("spk2", new char[]{'2'});
		peopleControl.createSpeaker("spk3", new char[]{'3'});
		// we add 4 attendee
		loginControl.createAttendee("att1", new char[]{'1'});
		loginControl.createAttendee("att2", new char[]{'2'});
		loginControl.createAttendee("att3", new char[]{'3'});
		loginControl.createAttendee("att4", new char[]{'4'});
		// we fake a login
		loginControl.login("org1", new char[]{'1'});
		// we load some default events
		eventManageControl.createTalk("Talk 1", "Convocation Hall", "2020/11/22", "10:00");
		eventManageControl.createTalk("Talk 2", "Bahen Center", "2020/11/22", "13:00");
		eventManageControl.createTalk("Talk 3", "Convocation Hall", "2020/11/22", "13:00");
		// logout
		menuControl.logout();
		return session;
	}
}
