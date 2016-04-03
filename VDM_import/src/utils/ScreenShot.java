package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;


public class ScreenShot {
	
	public static void ffmpeg_screenshot(Path fichier, int at_frame){
		String[] ffmpeg = new String[] {"ffmpeg", 
                "-y", 
                "-ss", 
                Float.toString(at_frame/25.0F), 
                "-i", 
                fichier.toString(), 
                "-f", 
                "image2", 
                "-vframes", 
                "1",
                "-q:v",
                "0",
                "/mnt/ramdisk/output.jpg"};

		Process p;
		try {
			p = Runtime.getRuntime().exec(ffmpeg);
			p.waitFor();
			
			BufferedReader reader = 
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";			
			while ((line = reader.readLine())!= null) {
				System.out.println(line + "\n");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
