package entity.person;

public interface PersonVisitor{
	
	void visitOrganizer(Organizer organizer);
	
	void visitSpeaker(Speaker speaker);
	
	void visitAttendee(Attendee attendee);
	
	void visitVIPAttendee(VIPAttendee vipAttendee);
}
