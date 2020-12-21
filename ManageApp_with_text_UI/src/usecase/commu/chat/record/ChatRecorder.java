package usecase.commu.chat.record;

import entity.social.Message;
import exception.social.MessageTimeFlowError;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * {@inheritDoc}
 * </br>
 * An implementation of ChatManage as a flow of message using ArrayList to represent flow of messages.
 *
 * <p>
 * Note: Notice that we are adding always adding message to the end of the list,
 * and for MessageIterator, we are getting the message based on it's index,
 * so we use a array list, which provides O(1) on addMessage and RandomAccess.
 * </p>
 */
public class ChatRecorder implements ChatRecord, MessageObserver{
	
	private final List<Message> messageList;
	private       String        name;
	private       int           modCount;
	
	public ChatRecorder(String name){
		super();
		this.messageList = new ArrayList<>();
		this.modCount = 0;
		this.name = name;
	}
	
	@Override
	public void update(Message message){
		addMessage(message);
	}
	
	private void addMessage(Message message){
		if(messageList.size() > 0 && message.compareTo(getLast()) < 0)
			throw new MessageTimeFlowError("The new Message Added has time " +
			                               message.getTime() +
			                               ", but the latest message recorded had time " +
			                               messageList.get(0).getTime());
		messageList.add(messageList.size(), message);
		modCount++;
	}
	
	private Message getLast(){
		return messageList.get(messageList.size() - 1);
	}
	
	@Override
	public Iterator<Message> getMessages(){
		return messageList.listIterator();
	}

//	@Override
//	public Iterator<Message> getMessages(int start, int end){
//		return new MessageIterator(start, end, modCount);
//	}
	
	@Override
	public Message getMostRecent(){
		return messageList.size() == 0 ? null : getLast();
	}
	
	@Override
	public String getName(){
		return name;
	}
	
	@Override
	public void setName(String name){
		this.name = name;
	}
	
	@Override
	public String toString(){
		return name;
	}

//	private class MessageIterator implements Iterator<Message>{
//
//		private final int expectedModCount;
//		private final int end;
//		private       int current;
//
//		public MessageIterator(int start, int end, int currentModCount){
//			this.expectedModCount = currentModCount;
//			this.current = start;
//			this.end = end;
//		}
//
//		@Override
//		public boolean hasNext(){
//			return (current + 1) < end && getRealIndex(current + 1) >= 0;
//		}
//
//		@Override
//		public Message next(){
//			checkConcurrentModification();
//			if(! hasNext())
//				throw new NoSuchElementException();
//			current += 1;
//			return messageList.get(getRealIndex(current));
//		}
//
//		private int getRealIndex(int index){
//			return messageList.size() - index;
//		}
//
//		private void checkConcurrentModification(){
//			if(expectedModCount != modCount)
//				throw new ConcurrentModificationException();
//		}
//	}
}
