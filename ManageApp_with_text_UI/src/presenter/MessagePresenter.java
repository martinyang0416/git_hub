package presenter;

import entity.social.Message;
import entity.social.MessageVisitor;
import entity.social.StringMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;

/**
 * Contains a Iterable of Messages from a Chat Record.
 */
public class MessagePresenter implements Iterable<String>{
	
	private final Iterator<Message>      messageList;
	private final Function<UUID, String> nameMap;
	
	public MessagePresenter(Iterator<Message> messageList, Function<UUID, String> nameMap){
		this.messageList = messageList;
		this.nameMap = nameMap;
	}
	
	@Override
	public Iterator<String> iterator(){
		return new MessageIterator();
	}
	
	private class MessageIterator implements Iterator<String>{
		
		final MessageExtractVisitor visitor;
		
		private MessageIterator(){
			visitor = new MessageExtractVisitor();
		}
		
		@Override
		public boolean hasNext(){
			return messageList.hasNext();
		}
		
		@Override
		public String next(){
			if(! hasNext())
				throw new NoSuchElementException();
			messageList.next().accept(visitor);
			return String.format(
					"%11s %8s : %s",
					visitor.time.format(DateTimeFormatter.ofPattern("MM/dd H:mm")),
					nameMap.apply(visitor.sender),
					visitor.currentMessage
			);
		}
	}
	
	private static class MessageExtractVisitor implements MessageVisitor{
		
		String        currentMessage;
		UUID          sender;
		LocalDateTime time;
		
		@Override
		public void visitStringMessage(StringMessage s){
			currentMessage = s.getContent();
			sender = s.getSender();
			time = s.getTime();
		}
	}
}
