package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MediaInfo {
	
	public static VBox getInfos(Path fichier, VBox vbox){
		
		boolean general = false;
		boolean video = false;
		boolean audio = false;
		boolean audio1 = false;
		boolean audio2 = false;
		boolean autre1 = false;
		boolean autre2 = false;
		boolean autre3 = false;
		
		String color = "";
		
		vbox.getChildren().clear();
		
		String[] mediaInfo = new String[] {"mediainfo", 
                "--Inform=ressources/templates/template1.txt",
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
				case "Video" : video = true;
				               general = false;
				               break;
				case "Audio" : audio = true;
	                           video = false;
	                           break;
				case "Audio #1" : audio1 = true;
				                  video = false;
                                  break;
				case "Audio #2" : audio2 = true;
                                  audio1 = false;
                                  break;
				case "Other #1" : autre1 = true;
				                  audio2 = false;
                                  break;
				}
                
				if (general){	
					switch(tag){
					
					case "Complete name"    : vbox.getChildren().add(new Label(String.format("Nom complet  : %s", line.split(":")[1].trim())));
					                          vbox.getChildren().add(new Label());
                                              break;
					case "Encoded date"     : vbox.getChildren().add(new Label(String.format("Timestamp      : %s", line.split(":")[1].trim())));
                                              break;
					}
				}
				else if (video){
					switch(tag){
					
					case "Commercial name"  : vbox.getChildren().add(new Label(String.format("Encodage        : %s", line.split(":")[1].trim())));
					                          break;
					case "Duration"         : vbox.getChildren().add(new Label(String.format("Durée              : %s", line.split(":")[1].trim())));
					                          break;
					case "Bit rate"         : vbox.getChildren().add(new Label(String.format("Bitrate             : %s", line.split(":")[1].trim())));
	                                          break;
					case "Width"            : vbox.getChildren().add(new Label(String.format("Largeur          : %s", line.split(":")[1].trim())));
                                              break;
					case "Height"           : vbox.getChildren().add(new Label(String.format("Hauteur           : %s", line.split(":")[1].trim())));
                                              break;
				    case "Display aspect ratio" : vbox.getChildren().add(new Label(String.format("%-15s : %s:%s", "Ratio",  line.split(":")[1].trim(),
				    		                                                                                                    line.split(":")[2].trim())));
                                              break;
				    case "Frame rate"       : vbox.getChildren().add(new Label(String.format("%-15s : %s", "Framerate", line.split(":")[1].trim())));
                                              break;     
				    case "Color space"      : color =  line.split(":")[1].trim();
                                              break;
				    case "Chroma subsampling" : vbox.getChildren().add(new Label(String.format("%-15s : %s %s:%s:%s", "Couleurs", color, line.split(":")[1].trim(), 
				    		                                                                                                            line.split(":")[2].trim(),
				    		                                                                                                            line.split(":")[3].trim())));
                                              break;
				    case "Scan type"        : vbox.getChildren().add(new Label(String.format("%-15s : %s", "Entrelacement", line.split(":")[1].trim())));
                                              break;
					}
				}
				else if (audio){	
					
				}
                else if (audio1){	
					
				}
                else if (audio2){	
					
				}
                else if (autre1){	
					
				}
                else if (autre2){	
					
				}
                else if (autre3){	
					
				}
				
				
				System.out.println(line + "\n");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return vbox;		
	}

}
