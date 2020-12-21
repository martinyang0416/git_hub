package controller.dialogs;

import controller.session.Controller;
import controller.session.Session;
import controller.session.PeriodConvert;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import presenter.event.EventSignupPresent;
import presenter.event.EventViewPresent;
import usecase.dto.EventDTO;
import usecase.dto.PersonDTO;
import usecase.dto.TaskDTO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class EventViewController extends Controller{
	
	@FXML private Button viewOrg;
	@FXML private Button viewSpk;
	@FXML private Button rate;
	
	@FXML private Label type;
	@FXML private Label title;
	@FXML private Label organizer;
	@FXML private Label speaker;
	@FXML private Label location;
	@FXML private Label time;
	@FXML private Label duration;
	@FXML private Label availability;
	@FXML private Label reward;
	@FXML private Label requirement;
	@FXML private Label rating;
	@FXML private Label respond;
	
	@FXML private Button signUp;
	@FXML private Button cancelSignUp;
	
	private EventSignupPresent signupPresenter;
	private EventViewPresent   viewPresenter;
	private PeriodConvert      periodConvert;
	private UUID               event;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		signupPresenter = new EventSignupPresenter();
		viewPresenter = new EventViewPresenter();
		periodConvert = new PeriodConvert();
	}
	
	public void initEvent(UUID event){
		this.event = event;
		fetchView();
	}
	
	private void fetchView(){
		boolean canAttend = session.canAttend();
		boolean isSignedUp = session.getEM().isSignedUp(session.getLoggedIn(), event);
		boolean isFinished = session.getEM().isFinished(event);
		rate.setDisable((! isFinished) || (! isSignedUp));
		signUp.setDisable((! canAttend) || isSignedUp);
		cancelSignUp.setDisable((! canAttend) || (! isSignedUp));
		session.getEM().updateView(event, viewPresenter);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		// hook some listener
		signUp.setOnAction(actionEvent -> signUp());
		cancelSignUp.setOnAction(actionEvent -> cancelSignUp());
		viewOrg.setOnAction(actionEvent -> viewOrganizer());
		viewSpk.setOnAction(actionEvent -> viewSpeaker());
		rate.setOnAction(actionEvent -> rate());
	}
	
	private void signUp(){
		if(session.getEM().signUp(event, session.getLoggedIn(), signupPresenter)){
			stageHandler.close();
		}else{
			fetchView();
		}
	}
	
	private void cancelSignUp(){
		if(session.getEM().cancelSignUp(event, session.getLoggedIn(), signupPresenter)){
			stageHandler.close();
		}else{
			fetchView();
		}
	}
	
	private void rate(){
		stageHandler.attachEventRating(event);
		stageHandler.showAndWait();
		fetchView();
	}
	
	private void viewOrganizer(){
		PersonDTO organizer = session.getEM().getOrganizer(event);
		if(organizer == null){
			setRespond(Color.RED, "No Organizer to View!");
		}else{
			stageHandler.attachPeopleView(organizer.getID());
			stageHandler.showAndWait();
		}
		fetchView();
	}
	
	private void viewSpeaker(){
		List<PersonDTO> speakers = session.getEM().getSpeakers(event);
		PersonDTO selectedSpeaker = selectSpeaker(speakers);
		if(selectedSpeaker == null){
			setRespond(Color.RED, "No Speaker to View!");
		}else{
			stageHandler.attachPeopleView(selectedSpeaker.getID());
			stageHandler.showAndWait();
		}
		fetchView();
	}
	
	private PersonDTO selectSpeaker(List<PersonDTO> speakers){
		if(speakers.size() == 0){
			return null;
		}else if(speakers.size() == 1){
			return speakers.get(0);
		}else{
			return stageHandler.showChoiceDialog(
					"Select a Speaker", "", speakers, speakers.get(0)
			).orElse(null);
		}
	}
	
	private void setRespond(Paint paint, String format, Object... objs){
		respond.setText(String.format(format, objs));
		respond.setTextFill(paint);
	}
	
	private void respondInfo(String format, Object... objs){
		setRespond(Color.BLACK, format, objs);
	}
	
	private void respondError(String format, Object... objs){
		setRespond(Color.RED, format, objs);
	}
	
	private class EventViewPresenter implements EventViewPresent{
		
		@Override
		public void updateView(EventDTO eventDTO){
			type.setText(eventDTO.getType());
			title.setText(eventDTO.getTitle());
			organizer.setText(eventDTO.getOrganizer());
			speaker.setText(eventDTO.getSpeaker());
			location.setText(eventDTO.getLocation());
			time.setText(periodConvert.convertTime(eventDTO.getStartTime(), eventDTO.getEndTime()));
			duration.setText(periodConvert.convertDuration(eventDTO.getStartTime(), eventDTO.getEndTime()));
			updateAvailability(eventDTO);
			reward.setText(String.valueOf(eventDTO.getReward()));
			requirement.setText(String.valueOf(eventDTO.getRequirement()));
			rating.setText(String.valueOf(eventDTO.getScore()));
		}
		
		private void updateAvailability(EventDTO eventDTO){
			int capacity = eventDTO.getCapacity();
			int signedUpCount = eventDTO.getSignedUp().size();
			availability.setText(String.format(
					"%d/%d",
					signedUpCount, capacity
			));
		}
	}
	
	private class EventSignupPresenter implements EventSignupPresent{
		
		@Override
		public void respondEventFull(){
			respondError("Event is Full!");
		}
		
		@Override
		public void respondAlreadySignedUp(){
			respondInfo("You are already signed up!");
		}
		
		@Override
		public void respondNotYetSignedUp(){
			respondInfo("You haven't signed up yet!");
		}
		
		@Override
		public void respondRequirementNotMeet(){
			respondError("You doesn't meet the requirement of this event!");
		}
		
		@Override
		public void respondEventStarted(){
			respondError("Event has Started!");
		}
		
		@Override
		public void notifyPromotionToVIP(){
			stageHandler.showInfoDialog(
					"Promotion",
					"Congratulations!",
					"You have been promoted to VIP!"
			);
		}
		
		@Override
		public void notifyDemotionToRegular(){
			stageHandler.showInfoDialog(
					"Demotion",
					null,
					"You are no longer VIP!"
			);
		}
		
		@Override
		public void respondConflictingTasks(List<TaskDTO> conflictingTasks){
			String conflicting = conflictingTasks
					.stream()
					.map(taskDTO -> String.format("%s for %s", taskDTO.getType(), taskDTO.getTitle()))
					.collect(Collectors.joining(", "));
			respondError("You have other %d events in conflict! %s", conflictingTasks.size(), conflicting);
		}
		
		@Override
		public void respondAlreadyOwnTask(){
			respondAlreadySignedUp();
		}
	}
}
