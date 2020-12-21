package usecase.commu.chat.manager.facade;

import entity.comm.MessageVisitor;
import entity.comm.StringMessage;

import java.io.Serializable;

/**
 *
 * Visit the StringMessages.
 *
 * Check the validity of the message.
 *
 * @see ChatRecordConvert
 *
 */

public class MessageValidator implements MessageVisitor, Serializable{
	
	private boolean lastResult = false;
	
	@Override
	public void visitStringMessage(StringMessage stringMessage){
		lastResult = ! stringMessage.getContent().equals("");
	}
	
	public boolean getLastResult(){
		return lastResult;
	}
}
