package utils;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javafx.scene.layout.VBox;

public class TimeStamp {
	
	private static int minutes;
	private static int secondes;
	private static Duration duration;
	
	public static void plage(String debut, String duree){

		System.out.println("durée : " + duree);
		
		debut = debut.split("UTC")[1].trim();
		LocalDateTime ldt = LocalDateTime.parse(debut, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Instant debut_instant = ldt.toInstant(ZoneOffset.UTC);
        
		if(duree.split("mn").length > 1){
			minutes = Integer.parseInt(duree.split("mn")[0].trim());
			if(duree.split("s").length > 0){
				secondes = Integer.parseInt(duree.split("mn")[1].trim().split("s")[0].trim());
			}
			else {
				secondes = 0;
			}
		}
		else {
			if(duree.split("s").length > 0){
				minutes = 0;
				secondes = Integer.parseInt(duree.split("s")[0].trim());	
		    }
			else {
				secondes = 0;
			}
			    
		}
		
		System.out.println("minutes : " + minutes + " ,  secondes : " + secondes);
		
		duration = duration.ZERO;
		duration = Duration.ofMinutes(minutes).plusSeconds(secondes);
		
		Instant fin_instant = debut_instant.plus(duration);
		
		
		System.out.println("debut : " + debut_instant);
		System.out.println("durée : " + duration);
		System.out.println("fin : " + fin_instant);
		
		//Instant fromIso8601 = Instant.parse("2010-01-01T12:00:00Z");
		//Instant firstInstant= Instant.ofEpochSecond( 1294881180 ); // 2011-01-13 01:13
		//Duration between = Duration.between(firstInstant, secondInstant);
	}
	
	public static void plage(Path path){
		
		MediaInfo.getInfos(path, new VBox());
		plage(Messages.getTimestamp(), Messages.getDuration());
	}
	
	

}
