package usecase.commu;

import entity.event.Event;
import exception.social.SocialException;
import usecase.commu.chat.Announcement;
import usecase.commu.chat.Chat;
import usecase.commu.chat.DirectChat;
import usecase.commu.chat.access.AccessLevel;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatManager implements ChatManage{
	
	private final List<Announcement> announcements;
	private final List<DirectChat>   directChats;
	
	public ChatManager(){
		announcements = new LinkedList<>();
		directChats = new LinkedList<>();
	}
	
	@Override
	public List<Chat> getAllAnnouncement(UUID person){
		return getAllANCFor(person).collect(Collectors.toList());
	}
	
	@Override
	public List<Chat> getAllDirectChat(UUID person){
		return getAllDCFor(person).collect(Collectors.toList());
	}
	
	@Override
	public Chat getDirectChat(UUID p1, UUID p2, Function<UUID, String> nameMap){
		checkSamePerson(p1, p2);
		Optional<DirectChat> chat = getDC(p1, p2);
		if(chat.isPresent()) return chat.get();
		DirectChat dc = new DirectChat();
		String chatName = String.format("Direct Chat for %s, %s", nameMap.apply(p1), nameMap.apply(p2));
		dc.initialize(p1, p2, chatName);
		directChats.add(dc);
		return dc;
	}
	
	@Override
	public void createAnnouncement(Event event, Function<Event, String> nameMap){
		if(getANC(event.getID()).isPresent())
			throw new SocialException("Announcement already been created for " + event.getTitle() + "!");
		String chatName = String.format("Announcement Group for %s", nameMap.apply(event));
		Announcement anc = new Announcement(event.getID(), chatName);
		announcements.add(anc);
	}
	
	@Override
	public void enrollAnnouncement(Event event, UUID person, AccessLevel level){
		getANC(event.getID())
				.orElseThrow(() -> new SocialException("Announcement is not created for " + event.getTitle() + "!"))
				.enrollFor(person, person, level);
	}
	
	@Override
	public void deleteAnnouncement(Event event, UUID person){
		getANC(event.getID())
				.orElseThrow(() -> new SocialException("Announcement is not created for " + event.getTitle() + "!"))
				.deleteFor(person, person);
	}
	
	private void checkSamePerson(UUID p1, UUID p2) throws SocialException{
		if(p1.equals(p2))
			throw new SocialException("They are the same people!");
	}
	
	private Optional<Announcement> getANC(UUID event){
		return announcements.stream()
		                    .filter(anc -> anc.isAnnouncementFor(event))
		                    .findAny();
	}
	
	private Optional<DirectChat> getDC(UUID p1, UUID p2){
		return directChats.stream()
		                  .filter(dc -> dc.isDirectChatFor(p1, p2))
		                  .findAny();
	}
	
	private Stream<Announcement> getAllANCFor(UUID person){
		return announcements.stream().filter(anc -> anc.hasRecordFor(person));
	}
	
	private Stream<DirectChat> getAllDCFor(UUID person){
		return directChats.stream().filter(dc -> dc.hasRecordFor(person));
	}
}
