package controller.social;

import controller.Session;
import presenter.MessagePresenter;
import presenter.Respond;
import presenter.status.ChatStatusPresenter;
import usecase.commu.chat.Chat;
import usecase.commu.chat.access.AccessLevel;
import usecase.commu.chat.record.ChatRecord;
import usecase.friends.FriendsManage;
import usecase.people.PeopleManage;

import java.util.UUID;

public class MessagingController implements MessagingControl{
	
	private final Session session;
	private final Chat    chat;
	
	public MessagingController(Session session, Chat chat){
		this.session = session;
		this.chat = chat;
	}
	
	@Override
	public ChatStatusPresenter getChatStatus(){
		UUID loggedIn = session.getLoggedIn();
		PeopleManage pm = session.getPM();
		return new ChatStatusPresenter(
				chat.getRecordFor(loggedIn).getName(),
				chat.getAccess(loggedIn).toString(),
				pm.getName(loggedIn)
		);
	}
	
	@Override
	public MessagePresenter getMessages(){
		UUID loggedIn = session.getLoggedIn();
		PeopleManage pm = session.getPM();
		ChatRecord record = chat.getRecordFor(loggedIn);
		return new MessagePresenter(record.getMessages(), pm::getName);
	}
	
	@Override
	public Respond sendMessage(Object message){
		UUID loggedIn = session.getLoggedIn();
		FriendsManage fm = session.getFM();
		if(message.equals("")){
			return new Respond(false, "Message can't be empty!");
		}
		if(! chat.hasAccess(loggedIn, AccessLevel.EDIT)){
			return new Respond(false, "You don't have Edit Access!");
		}
		// check relation!
//		if(fm.hasRelationTo(loggedIn, ))
//			return null;
		chat.sendMessage(loggedIn, message);
		return new Respond("Message Sent!");
	}
}
