package controller.session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PeriodConvert{
	
	private final DateTimeFormatter dateFormatter;
	private final DateTimeFormatter timeFormatter;
	
	public PeriodConvert(){
		dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		timeFormatter = DateTimeFormatter.ofPattern("H:mm");
	}
	
	public String convertTime(LocalDateTime start, LocalDateTime end){
		String result;
		if(start.toLocalDate().equals(end.toLocalDate())){
			result = String.format(
					"%s %s ~ %s",
					start.format(dateFormatter),
					start.format(timeFormatter), end.format(timeFormatter)
			);
		}else{
			result = String.format(
					"%s %s ~ %s %s",
					start.format(dateFormatter), start.format(timeFormatter),
					end.format(dateFormatter), end.format(timeFormatter)
			);
		}
		return result;
	}
	
	public String convertDuration(LocalDateTime start, LocalDateTime end){
		Duration duration = Duration.between(start, end);
		long minutes = duration.toMinutes();
		return String.format(
				"%d hour %d minutes", minutes / 60, minutes % 60
		);
	}
}
