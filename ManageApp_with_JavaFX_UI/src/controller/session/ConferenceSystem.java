package controller.session;

import gateway.SessionIO;
import javafx.application.Application;
import javafx.stage.Stage;
import usecase.people.PersonType;

import java.io.IOException;

public class ConferenceSystem extends Application{
	
	public static void main(String[] args){
		launch(args);
	}
	
	// If you want to load the demo program in the demo video, change SESSION_PATH = "./session/Session_demo.ser"
	private static final String SESSION_PATH = "./session/Session.ser";
	
	private Session session;
	
	@Override
	public void start(Stage primaryStage){
		StageHandler stageHandler = new StageHandler(session);
		stageHandler.attachMenu("CSC207 Conference Project", primaryStage);
		stageHandler.show();
	}
	
	@Override
	public void init(){
		this.session = restoreSession();
	}
	
	@Override
	public void stop(){
		saveSession();
	}
	
	private Session restoreSession(){
		SessionIO io = new SessionIO();
		try{
			Session session = io.restoreSession(SESSION_PATH);
			System.out.println("Session load successes!");
			return session;
		}catch(IOException | ClassNotFoundException e){
			System.out.println("Session load failed, the detail stacktrace is as follow, starting new Session!");
			System.out.println("\t" + e.getLocalizedMessage());
			return createNewSession();
		}
	}
	
	private void saveSession(){
		SessionIO io = new SessionIO();
		try{
			io.saveSession(SESSION_PATH, session);
			System.out.println("Session save successes!");
		}catch(IOException e){
			System.out.println("Session save failed, the detail stacktrace is as follow!");
			System.out.println("\t" + e.getLocalizedMessage());
		}
	}
	
	private Session createNewSession(){
		Session session = new Session();
		loadDemo(session);
		return session;
	}
	
	private void loadDemo(Session session){
		session.getPM().debugCreate(PersonType.ORGANIZER, "org1", new char[]{'1'});
		session.getPM().debugCreate(PersonType.ORGANIZER, "org2", new char[]{'2'});
		session.getPM().debugCreate(PersonType.SPEAKER, "spk1", new char[]{'1'});
		session.getPM().debugCreate(PersonType.SPEAKER, "spk2", new char[]{'2'});
		session.getPM().debugCreate(PersonType.SPEAKER, "spk3", new char[]{'3'});
		session.getPM().debugCreate(PersonType.ATTENDEE, "att1", new char[]{'1'});
		session.getPM().debugCreate(PersonType.ATTENDEE, "att2", new char[]{'2'});
		session.getPM().debugCreate(PersonType.ATTENDEE, "att3", new char[]{'3'});
		session.getPM().debugCreate(PersonType.ATTENDEE, "att4", new char[]{'4'});
	}
}
