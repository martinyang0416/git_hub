package entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * The Objects who implements this interface Occupy a time period and
 * location inside a {@code Schedule}, and indicates their OccupancyLevel.
 *
 * @see usecase.schedule.Schedule
 * @see OccupancyLevel
 */
public interface IEvent extends Serializable{
	
	/**
	 * Get the location it occupy on {@code Schedule}.
	 *
	 * @return a String represents the location
	 */
	String getLocation();
	
	/**
	 * Get the start time of it occupy on {@code Schedule}.
	 *
	 * @return a {@code LocalDateTime} represents the start time.
	 */
	LocalDateTime getStartTime();
	
	/**
	 * Get the end time of it occupy on {@code Schedule}.
	 *
	 * @return a {@code LocalDateTime} represents the end time.
	 */
	LocalDateTime getEndTime();
	
	/**
	 * Get the occupancy level of it for {@code Schedule}.
	 *
	 * @return a {@code OccupancyLevel}
	 * @see OccupancyLevel
	 */
	OccupancyLevel getOccupancyLevel();
	
	/**
	 * Get the duration of this.
	 *
	 * @return a {@code Duration} between the start and end time.
	 */
	default Duration getDuration(){
		return Duration.between(getStartTime(), getEndTime());
	}
}
