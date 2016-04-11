package models.imports;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import models.Cadreur;
import models.Rush;
import utils.Messages;

public class ModeleMP4_CLPR implements ModeleImport{
	
	private String[] script_merge;
	private String[] script_encode;
	private String[] script_encode_deint;
	private String[] script_remux;

	private final File ram = new File("/mnt/ramdisk");
	private final File temp = new File("/home/david/temp");
	//private final File out = new File("/mnt/nfs1_rushs");
	private final File out = new File("/home/autor/Desktop");
	
	private String liste_des_rush_du_plan;
	private String outdir;
	private String outfile;
	
	private int offset = 0;

	@Override
	public void import_rushs(File dossier, Cadreur cadreur) {
		
		outdir = String.format("%s/%s", out, dossier.toPath().getFileName().toString());
		
		
        for (Rush r : Messages.getListeDesPlans()){
        	
        	outfile = r.getName();
        	
        	if (r.getChunks().size() > 1){
        		liste_des_rush_du_plan = r.getChunks()
		                                  .stream()
		                                  .map(a -> a.getAbsolutePath())
		                                  .collect(Collectors.joining("|", "concat:", ""));

        	}
        	else {
        		liste_des_rush_du_plan = r.getAbsolutePath();
        	}
        	
        	script_merge = new String[] {"dd",
        			              String.format("if='%s'", r.toString()),
        			              String.format("of='%s'", outfile),
        			              String.format("seek=%d", offset)};
        	
        	script_encode = new String[] {"ffmpeg",
        			                      "-y",
        			                      "-i",
        			                      String.format("%s", liste_des_rush_du_plan),
        			                      "-s",
        			                      "720x576",
        			                      "-sws_flags",
        			                      "lanczos",
        			                      "-pix_fmt",
        			                      "yuv420p",
        			                      "-b:a",
        			                      "384k",
        			                      "-vcodec",
        			                      "mpeg2video",
        			                      "-q:v",
        			                      "0",
        			                      String.format("%s/%s.mpg", outdir, outfile) 
        			                      };
        	
        	script_encode_deint = new String[] {"ffmpeg",
							                    "-y",
							                    "-i",
							                    String.format("%s", liste_des_rush_du_plan),
							                    "-vf",
							                    "yadif=0:0:0",
							                    "-s",
							                    "720x576",
							                    "-sws_flags",
							                    "lanczos",
							                    "-pix_fmt",
							                    "yuv420p",
							                    "-b:a",
							                    "384k",
							                    "-vcodec",
							                    "mpeg2video",
							                    "-q:v",
							                    "0",
							                    String.format("%s/%s.mpg", outdir, outfile) 
							                    };
        	
        	script_remux = new String[] {"ffmpeg",
        			                     "-y",
        			                     "-i",
        			                     "'%s'",
        			                     "-acodec",
        			                     "ac3",
        			                     "-b:a",
        			                     "384k",
        			                     "-vcodec",
        			                     "copy",
        			                     "'%s'"
        			                     };

        	
        	Process p;
    		try {
    			p = cadreur.isDeint() ? Runtime.getRuntime().exec(script_encode_deint)
    					              : Runtime.getRuntime().exec(script_encode);
    			p.waitFor();
    			
    			BufferedReader reader = 
                        new BufferedReader(new InputStreamReader(p.getErrorStream()));

                String line = "";	

    			while ((line = reader.readLine())!= null) {
    				System.out.println(line);
    			}		
		    }
			catch (Exception e) {
				e.printStackTrace();
			}
	
	    }
	
	}
}
