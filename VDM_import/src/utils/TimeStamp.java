package utils;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.layout.VBox;
import models.Rush;

public class TimeStamp {
	
	private static int heures;
	private static int minutes;
	private static int secondes;
	private static Duration duration;
	
	public static void plage(String debut, String duree){

		System.out.println("durée : " + duree);
		
		if(debut.startsWith("UTC")){
			debut = debut.split("UTC")[1].trim();
		}
		
		
		LocalDateTime ldt = null;
		
		try {
			ldt = LocalDateTime.parse(debut, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
		}
		catch (DateTimeParseException dtpe) {
			ldt = LocalDateTime.parse(debut, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
		
		Instant debut_instant = ldt.toInstant(ZoneOffset.UTC);
        
        String[] bouts_duree = duree.split(" ");
        Pattern p_heures = Pattern.compile("\\dh");
        Pattern p_minutes = Pattern.compile("\\dmn");
        Pattern p_secondes = Pattern.compile("\\ds");
        
		heures = 0;
		minutes = 0;
		secondes = 0;
		
		for (String s0 : bouts_duree){
			
			Matcher h = p_heures.matcher(s0);
			Matcher m = p_minutes.matcher(s0);
			Matcher s = p_secondes.matcher(s0);
			
			if (h.find()){
				heures = Integer.parseInt(s0.split("h")[0]);
			}
			else if (m.find()){
				minutes = Integer.parseInt(s0.split("mn")[0]);
			}
			else if (s.find()){
				secondes = Integer.parseInt(s0.split("s")[0]);
			}
		}
		
		duration = duration.ZERO;
		duration = Duration.ofHours(heures).plusMinutes(minutes).plusSeconds(secondes);
		
		Instant fin_instant = debut_instant.plus(duration);
		
		
		System.out.println("debut : " + debut_instant);
		System.out.println("durée : " + duration);
		System.out.println("fin : " + fin_instant);
	}
	
	public static void plage(Rush rush){
		
		MediaInfo.getInfos(rush.toPath(), new VBox());
		plage(Messages.getTimestamp(), Messages.getDuration());
	}
	
	

}
