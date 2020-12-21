package UI.screens.social;

import UI.textUI.Screen;
import UI.textUI.ScreenExecutor;
import UI.textUI.Selection;
import UI.textUI.TextIO;
import controller.social.ChatsControl;
import controller.social.MessagingControl;
import exception.IllegalInputException;
import presenter.seletable.ChatsPresenter;
import usecase.commu.chat.Chat;
/**
 * The Chat Screen.
 */
public class ChatsScreen implements Screen{
	
	private final ChatsControl chatsControl;
	private       boolean      hasNext = true;
	
	public ChatsScreen(ChatsControl chatsControl){
		this.chatsControl = chatsControl;
	}
	
	@Override
	public void render(TextIO io){
		io.printLine("Messaging Center");
		io.printDivider1();
		// print all chats
		Selection<Chat> chatSelect = printChats(io);
		// select a chat
		if(! chatSelect.hasSelection()){
			io.printDivider1();
			io.printLine("You don't have any Chat yet!");
			io.readLineWithPrompt("Press enter to Go Back : ");
			hasNext = false;
		}else{
			Chat chat = selectChat(io, chatSelect);
			if(chat == null){
				io.printLine("Aborting!");
				hasNext = false;
				return;
			}
			// we execute the messaging screen
			MessagingControl messagingControl = chatsControl.getMessagingControl(chat);
			new ScreenExecutor(new MessagingScreen(messagingControl)).run(io);
		}
	}
	
	private Chat selectChat(TextIO io, Selection<Chat> chatSelect){
		Chat chat;
		do{
			io.printDivider1();
			String input = io.readLineWithPrompt("Select a Chat : ");
			try{
				chat = chatSelect.processInput(input);
				if(chat == null)
					return null;
			}catch(IllegalInputException e){
				io.printLine(e.getLocalizedMessage());
				chat = null;
			}
		}while(chat == null);
		return chat;
	}
	
	private Selection<Chat> printChats(TextIO io){
		io.printLine("You have the following Chats : ");
		io.printDivider2();
		ChatsPresenter announcements = chatsControl.getAnnouncements();
		ChatsPresenter directChats = chatsControl.getDirectChats();
		Selection<Chat> selection = new Selection<>();
		selection.addUnit("Announcements : ", announcements);
		selection.addUnit("Direct Chats : ", directChats);
		io.printLines(selection);
		return selection;
	}
	
	@Override
	public boolean hasNextScreen(){
		return hasNext;
	}
}
