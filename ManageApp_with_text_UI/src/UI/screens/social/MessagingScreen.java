package UI.screens.social;

import UI.textUI.Screen;
import UI.textUI.TextIO;
import controller.social.MessagingControl;
import presenter.MessagePresenter;
import presenter.Respond;
import presenter.status.ChatStatusPresenter;

/**
 * The Messaging Screen.
 */
public class MessagingScreen implements Screen{
	
	private final MessagingControl messagingControl;
	private       boolean          hasNext  = true;
	private       boolean          firstRun = true;
	
	public MessagingScreen(MessagingControl messagingControl){
		this.messagingControl = messagingControl;
	}
	
	@Override
	public void render(TextIO io){
		ChatStatusPresenter chatStatus = messagingControl.getChatStatus();
		// print the chat name
		io.printLine(chatStatus.getChatName());
		io.printDivider1();
		// we print the messages
		printChatMessages(io);
		// the messaging hints
		printChatHints(io);
		Respond respond;
		do{
			io.printDivider2();
			// process the input
			String input = io.readLineWithPrompt(
					"%s (%s) : ",
					chatStatus.getAddingAs(),
					chatStatus.getAccess()
			);
			if(input.equals("") || input.equals("!exit")){
				io.printLine("Aborting!");
				hasNext = false;
				return;
			}
			// add message
			respond = messagingControl.sendMessage(input);
			io.printLines(respond);
		}while(! respond.isSuccess());
	}
	
	private void printChatMessages(TextIO io){
		MessagePresenter messages = messagingControl.getMessages();
		io.printLines(messages);
	}
	
	private void printChatHints(TextIO io){
		if(firstRun){
			io.printLine("Press enter to send message, enter \"!exit\" to exit the Chat!");
			firstRun = false;
		}
	}
	
	@Override
	public boolean hasNextScreen(){
		return hasNext;
	}
}
