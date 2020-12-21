package controller.social;

import controller.Session;
import presenter.seletable.ChatsPresenter;
import usecase.commu.ChatManage;
import usecase.commu.chat.Chat;

import java.util.List;
import java.util.UUID;

public class ChatsController implements ChatsControl{
	
	private final Session session;
	
	public ChatsController(Session session){
		this.session = session;
	}
	
	@Override
	public ChatsPresenter getAnnouncements(){
		UUID loggedIn = session.getLoggedIn();
		ChatManage cm = session.getCM();
		List<Chat> chats = cm.getAllAnnouncement(session.getLoggedIn());
		return new ChatsPresenter(chats, loggedIn);
	}
	
	@Override
	public ChatsPresenter getDirectChats(){
		UUID loggedIn = session.getLoggedIn();
		ChatManage cm = session.getCM();
		List<Chat> chats = cm.getAllDirectChat(session.getLoggedIn());
		return new ChatsPresenter(chats, loggedIn);
	}
	
	@Override
	public MessagingControl getMessagingControl(Chat chat){
		return new MessagingController(session, chat);
	}
}
