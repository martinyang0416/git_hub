package usecase.commu.chat.manager.facade;

import entity.comm.Message;
import entity.comm.MessageVisitor;
import entity.comm.StringMessage;
import usecase.commu.chat.record.ChatRecord;
import usecase.dto.MessageDTO;
import usecase.people.PeopleManage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Convert the messages in the chat recording section to acceptable types.
 *
 *
 *
 * @see MessageVisitor, Serializable
 */

public class ChatRecordConvert implements MessageVisitor, Serializable{
	
	transient private List<MessageDTO> messageDTOS;
	
	private final PeopleManage pm;
	
	public ChatRecordConvert(PeopleManage pm){
		this.pm = pm;
	}
	
	public List<MessageDTO> convert(ChatRecord chatRecord){
		messageDTOS = new ArrayList<>();
		for(Message message : chatRecord){
			message.accept(this);
		}
		return messageDTOS;
	}

	/**
	 *
	 * Visit the StringMessages
	 *
	 * @param  stringMessage  the messages in the chat
	 *
	 */

	@Override
	public void visitStringMessage(StringMessage stringMessage){
		MessageDTO dto = new MessageDTO();
		dto.setContent(stringMessage.getContent());
		dto.setSender(pm.getName(stringMessage.getSender()));
		dto.setSenderID(stringMessage.getSender());
		dto.setTime(stringMessage.getTime());
		messageDTOS.add(dto);
	}
}
