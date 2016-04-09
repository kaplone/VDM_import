package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Rush;

public class MediaInfo {
	
	static HBox hbox;
	static int piste; 
	static int meta; 
	static String timestamp;
	static String duree;
	private static int heures;
	private static int minutes;
	private static int secondes;
	private static Duration duration;
	private static boolean boucle;
	
	public static Rush getTimeStamp(Path fichier) {
		
		boucle = true;
		Rush r = new Rush(fichier.toString());
		
		System.out.println("getTimeStamp(fichier) : " + fichier) ;

		String[] mediaInfo = new String[] {"mediainfo", 
                fichier.toString()};
		
		Process p;
		try {
			p = Runtime.getRuntime().exec(mediaInfo);
			p.waitFor();
			
			BufferedReader reader = 
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";	

			while (boucle && (line = reader.readLine())!= null) {
                String tag = line.split(":")[0].trim();
				
				switch(tag){
				case "Video"            : boucle = false;
				                          break;
				
				case "Encoded date"     :  timestamp = String.format("%s:%s:%s", line.split(":")[1].trim(), 
                                                                                 line.split(":")[2].trim(),
                                                                                 line.split(":")[3].trim());
                                           break;
                                           
				case "Duration"         : duree = line.split(":")[1].trim();
                                          Messages.setDuration(line.split(":")[1].trim());
                                          break;                           
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if(timestamp.startsWith("UTC")){
			timestamp = timestamp.split("UTC")[1].trim();
		}
		
		long secondsFromEpoch = 0;
		
		try {
			secondsFromEpoch = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")).toEpochSecond(ZoneOffset.UTC);
		}
		catch (DateTimeParseException dtpe) {
			secondsFromEpoch = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toEpochSecond(ZoneOffset.UTC);
		}
		
		r.setDebut(secondsFromEpoch);
		r.setDuree(getDuree(duree));
		
		return r;
		
	}
	
	public static Duration getDuree(String duree){
		
		heures = 0;
		minutes = 0;
		secondes = 0;
		
		String[] bouts_duree = duree.split(" ");
        Pattern p_heures = Pattern.compile("\\dh");
        Pattern p_minutes = Pattern.compile("\\dmn");
        Pattern p_secondes = Pattern.compile("\\ds");
		
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
		
		return duration;
		
	}
	
	public static VBox getInfos(Path fichier, VBox vbox){
		
		boolean general = false;
		boolean video = false;
		boolean audio = false;
		boolean audio1 = false;
		boolean audio2 = false;
		boolean audio3 = false;
		boolean audio4 = false;
		boolean autre = false;
		boolean autre1 = false;
		boolean autre2 = false;
		boolean autre3 = false;
		boolean autre4 = false;
		
		piste = 0;
		meta = 0;
		
		String color = "";
		
		vbox.getChildren().clear();
		
		
		
		
		String[] mediaInfo = new String[] {"mediainfo", 
                                            fichier.toString()};
		
		Process p;
		try {
			p = Runtime.getRuntime().exec(mediaInfo);
			p.waitFor();
			
			BufferedReader reader = 
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";			
			while ((line = reader.readLine())!= null) {
				
				String tag = line.split(":")[0].trim();
				
				switch(tag){
				case "General" : general = true;
	                           break;
				case "Video" : addRow(vbox);
	                           video = true;
				               general = false;
				               break;
				case "Audio" : addRow(vbox);
				               addRow(vbox, "Audio", String.format("Piste %d", ++piste));
					           audio = true;
	                           video = false;
	                           break;
				case "Audio #1" : addRow(vbox);
	                              addRow(vbox, "Audio", String.format("Piste %d", ++piste));
					              audio1 = true;
				                  video = false;
                                  break;
				case "Audio #2" : addRow(vbox);
	                              addRow(vbox, "Audio", String.format("Piste %d", ++piste));
					              audio2 = true;
                                  audio1 = false;
                                  break;
				case "Audio #3" : addRow(vbox);
				                  addRow(vbox, "Audio", String.format("Piste %d", ++piste));
					              audio3 = true;
				                  audio2 = false;
				                  break;
				case "Audio #4" : addRow(vbox);
				                  addRow(vbox, "Audio", String.format("Piste %d", ++piste));
					              audio4 = true;
				                  audio3 = false;
				                  break;
				case "Other"    : addRow(vbox);
				                  addRow(vbox, "Métadata", String.format("Méta %d", ++meta));
					              autre = true;
					              audio = false;
					              audio1 = false;
					              audio2 = false;
					              audio3 = false;
				                  audio4 = false;
				                  break;
				case "Other #1" : addRow(vbox);
	                              addRow(vbox, "Métadata", String.format("Méta %d", ++meta));
					              autre1 = true;
					              audio = false;
					              audio1 = false;
					              audio2 = false;
					              audio3 = false;
				                  audio4 = false;
                                  break;
				case "Other #2" : addRow(vbox);
	                              addRow(vbox, "Métadata", String.format("Méta %d", ++meta));
	                              autre2 = true;
                                  autre1 = false;
                                  break;
				case "Other #3" : addRow(vbox);
	                              addRow(vbox, "Métadata", String.format("Méta %d", ++meta));
	                              autre3 = true;
                                  autre2 = false;
                                  break;
				case "Other #4" : addRow(vbox);
	                              addRow(vbox, "Métadata", String.format("Méta %d", ++meta));
	                              autre4 = true;
                                  autre3 = false;
                                  break;
				}
                
				if (general){	
					switch(tag){
					
					case "Complete name"    : addRow(vbox, "Nom complet", line.split(":")[1].trim());
                                              break;
					case "Encoded date"     : addRow(vbox, "Timestamp", String.format("%s:%s:%s", line.split(":")[1].trim(), 
                                                                                                  line.split(":")[2].trim(),
                                                                                                  line.split(":")[3].trim()));
					                          Messages.setTimestamp(String.format("%s:%s:%s", line.split(":")[1].trim(), 
                                                                                              line.split(":")[2].trim(),
                                                                                              line.split(":")[3].trim()));
                                              break;
					}
				}
				else if (video){
					switch(tag){
					
					case "Commercial name"  : addRow(vbox, "Format", line.split(":")[1].trim());
					                          break;
					case "Format/Info"      : addRow(vbox, "Codec", line.split(":")[1].trim());
                                              break;
					case "Duration"         : addRow(vbox, "Durée",line.split(":")[1].trim());
					                          Messages.setDuration(line.split(":")[1].trim());
					                          break;
					case "Bit rate"         : addRow(vbox, "Bitrate"  , line.split(":")[1].trim());
	                                          break;
					case "Width"            : addRow(vbox, "Largeur", line.split(":")[1].trim());
                                              break;
					case "Height"           : addRow(vbox, "Hauteur", line.split(":")[1].trim());
                                              break;
				    case "Display aspect ratio" : addRow(vbox, "Ratio",  String.format("%s:%s", line.split(":")[1].trim(),line.split(":")[2].trim()));
                                              break;
				    case "Frame rate"       : addRow(vbox, "Framerate", line.split(":")[1].trim());
                                              break;     
				    case "Color space"      : color =  line.split(":")[1].trim();
                                              break;
				    case "Chroma subsampling" : addRow(vbox, "Couleurs", String.format("%s:%s:%s", line.split(":")[1].trim(), 
				    		                                                                       line.split(":")[2].trim(),
				    		                                                                       line.split(":")[3].trim()));
                                              break;
				    case "Scan type"        : addRow(vbox, "Entrelacement", ":",  line.split(":")[1].trim(), true);
                                              break;
					}
				}
				else if (audio){
					switch(tag){
					case "Format"           : addRow(vbox, "Codec", line.split(":")[1].trim());
	                                          break;
					case "Format settings, Endianness" : addRow(vbox, "Endian", line.split(":")[1].trim());
                                              break;
					case "Bit rate"         : addRow(vbox, "Bitrate", line.split(":")[1].trim());
                                              break;
					case "Channel(s)"       : addRow(vbox, "Canaux", line.split(":")[1].trim());
                                              break;
					case "Sampling rate"    : addRow(vbox, "Echantillonage", line.split(":")[1].trim());
                                              break;
					}
				
				}
                else if (audio1){	
                	switch(tag){
					case "Format"           : addRow(vbox, "Codec", line.split(":")[1].trim());
	                                          break;
					case "Format settings, Endianness" : addRow(vbox, "Endian", line.split(":")[1].trim());
                                              break;
					case "Bit rate"         : addRow(vbox, "Bitrate", line.split(":")[1].trim());
                                              break;
					case "Channel(s)"       : addRow(vbox, "Canaux", line.split(":")[1].trim());
                                              break;
					case "Sampling rate"    : addRow(vbox, "Echantillonage", line.split(":")[1].trim());
                                              break;
					}
					
				}
                else if (audio2){
                	switch(tag){
					case "Format"           : addRow(vbox, "Codec", line.split(":")[1].trim());
	                                          break;
					case "Format settings, Endianness" : addRow(vbox, "Endian", line.split(":")[1].trim());
                                              break;
					case "Bit rate"         : addRow(vbox, "Bitrate", line.split(":")[1].trim());
                                              break;
					case "Channel(s)"       : addRow(vbox, "Canaux", line.split(":")[1].trim());
                                              break;
					case "Sampling rate"    : addRow(vbox, "Echantillonage", line.split(":")[1].trim());
                                              break;
					}
					
				}
                else if (audio3){	
                	switch(tag){
					case "Format"           : addRow(vbox, "Codec", line.split(":")[1].trim());
	                                          break;
					case "Format settings, Endianness" : addRow(vbox, "Endian", line.split(":")[1].trim());
                                              break;
					case "Bit rate"         : addRow(vbox, "Bitrate", line.split(":")[1].trim());
                                              break;
					case "Channel(s)"       : addRow(vbox, "Canaux", line.split(":")[1].trim());
                                              break;
					case "Sampling rate"    : addRow(vbox, "Echantillonage", line.split(":")[1].trim());
                                              break;
					}
					
				}
                else if (audio4){
                	switch(tag){
					case "Format"           : addRow(vbox, "Codec", line.split(":")[1].trim());
	                                          break;
					case "Format settings, Endianness" : addRow(vbox, "Endian", line.split(":")[1].trim());
                                              break;
					case "Bit rate"         : addRow(vbox, "Bitrate", line.split(":")[1].trim());
                                              break;
					case "Channel(s)"       : addRow(vbox, "Canaux", line.split(":")[1].trim());
                                              break;
					case "Sampling rate"    : addRow(vbox, "Echantillonage", line.split(":")[1].trim());
                                              break;
					}
					
				}
                else if (autre){	
                	switch(tag){
					case "Type"             : addRow(vbox, "Type", line.split(":")[1].trim());
                                              break;
					case "Format"           : addRow(vbox, "Format", line.split(":")[1].trim());
                                              break;
					case "Time code settings" : addRow(vbox, "setting", line.split(":")[1].trim());
                                              break;
					}
					
				}
                else if (autre1){	
                	switch(tag){
					case "Type"             : addRow(vbox, "Type", line.split(":")[1].trim());
                                              break;
					case "Format"           : addRow(vbox, "Format", line.split(":")[1].trim());
                                              break;
					case "Time code settings" : addRow(vbox, "setting", line.split(":")[1].trim());
                                              break;
					}
					
				}
                else if (autre2){	
                	switch(tag){
					case "Type"             : addRow(vbox, "Type", line.split(":")[1].trim());
                                              break;
					case "Format"           : addRow(vbox, "Format", line.split(":")[1].trim());
                                              break;
					case "Time code settings" : addRow(vbox, "setting", line.split(":")[1].trim());
                                              break;
					}
				}
                else if (autre3){	
                	switch(tag){
					case "Type"             : addRow(vbox, "Type", line.split(":")[1].trim());
                                              break;
					case "Format"           : addRow(vbox, "Format", line.split(":")[1].trim());
                                              break;
					case "Time code settings" : addRow(vbox, "setting", line.split(":")[1].trim());
                                              break;
					}
				}
                else if (autre4){	
                	switch(tag){
					case "Type"             : addRow(vbox, "Type", line.split(":")[1].trim());
                                              break;
					case "Format"           : addRow(vbox, "Format", line.split(":")[1].trim());
                                              break;
					case "Time code settings" : addRow(vbox, "setting", line.split(":")[1].trim());
                                              break;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return vbox;		
	}
	
	protected static VBox addRow(VBox vbox, String nom, String valeur){        
        return addRow(vbox, nom, ":", valeur, false);
	}
	
	protected static VBox addRow(VBox vbox){        
        return addRow(vbox, "", "", "", false);
	}
	
    protected static VBox addRow(VBox vbox, String nom, String sep, String valeur, boolean bold){
		
		hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label l = new Label(nom);
        l.setPrefWidth(100);
        Label lb = new Label(valeur);
        if (bold){
            if (valeur.equals("Progressive")){
            	lb.setStyle("-fx-text-fill: green;-fx-font-weight: bold;");
            }
            else{
            	lb.setStyle("-fx-text-fill: red;-fx-font-weight: bold;");
            }
        } 
        hbox.getChildren().addAll(l, new Label(sep), lb);
        vbox.getChildren().add(hbox);
        
        return vbox;
	}
}
