package entity.event;

import entity.OccupancyLevel;

import java.time.LocalDateTime;

/**
 * Talk represents a Talk happens at a Conference, with a {@code OCCUPANCY_SPECIFIC}
 * level of Occupancy to the Schedule.
 */
public class Talk extends Event{
	
	public Talk(String title, String location, LocalDateTime startTime, LocalDateTime endTime){
		super(title, location, startTime, endTime);
	}
	
	@Override
	public void accept(EventVisitor visitor){
		visitor.visitTalk(this);
	}
	
	/**
	 * Get the occupancy level of this {@code Talk},
	 * which is {@code OccupancyLevel.OCCUPANCY_SPECIFIC}
	 *
	 * @return {@code OccupancyLevel.OCCUPANCY_SPECIFIC}
	 * @see OccupancyLevel
	 */
	@Override
	public OccupancyLevel getOccupancyLevel(){
		return OccupancyLevel.OCCUPANCY_SPECIFIC;
	}
}
