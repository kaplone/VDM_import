package models.imports;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import models.Cadreur;
import models.Rush;
import utils.AfficheurFlux;
import utils.AfficheurFlux2;
import utils.AfficheurFlux3;
import utils.Messages;

public class ModeleM2T_mencoder_desplit implements ModeleImport{
	
	private String[] script_fifo;
	private String[] script_fifo_dd;
	private String[] script_rmfifos;
	private String[] script_lecture;
	private String[] script_dd;
	private String[] script_remux;
	private String[] script_remux_concat;
	private String[] script_remux_deint;
	private String[] script_remux_concat_deint;

	private final File ram = new File("/mnt/ramdisk");
	private final File temp = new File("/home/david/temp");
	private final File out = new File("/mnt/2015_rushs");
	//private final File out = new File("/home/autor/Desktop");
	
	private String concat_des_rush_du_plan;
	private String outdir;
	private String outfile;
	
	private List<Rush> liste_des_plans;
	
	private List<String[]> liste_des_scripts_lecture;
	private List<String[]> liste_des_scripts_dd;
	
	private int taille_liste;
	
	private Cadreur cadreur;
	
	private FileWriter fw;

	private AfficheurFlux fluxErreurERR_REMUX;
	//private AfficheurFlux[] fluxErreurERR_LECTURE;
	private AfficheurFlux2[] fluxInputSTD_LECTURE;
	private AfficheurFlux3[] fluxErreurERR_LECTURE;

	
	private Process p2;
	
	private Rush plan;
	
	private String numero_dossier;
	

	@Override
	public void import_rushs(File dossier, Cadreur cadreur) {
		
		this.cadreur = cadreur;
		
		numero_dossier = dossier.toPath().getFileName().toString();
		
		outdir = String.format("%s/%s", out, numero_dossier);
		
		liste_des_plans = Messages.getListeDesPlans();
		
		for (int i = 0; i < liste_des_plans.size(); i++){
			
			plan = liste_des_plans.get(i);

			init();
			open();
			remux();
			lire();
			close();	
		}
	}
	
	
	public void init() {
		
		taille_liste = plan.getChunks().size();
		
		script_fifo = new String[] {"mkfifo",
                String.format("%s/fifo_%s.avi", ram, plan.getName())
               };
		
		script_fifo_dd = new String[] {"mkfifo",
                String.format("%s/fifo_%s.M2T", ram, plan.getName())
               };

	}
	
	public void open() {
		
		if (plan.getChunks().size() > 1){
			
			dd(plan);

//			ecritureTxt();
//			concat_des_rush_du_plan = ram + String.format("/liste_%s.txt", numero_dossier);
    	}
    	else {
    		concat_des_rush_du_plan = String.format("%s/fifo_%s.avi", ram, plan.getName());
    	}
		
		outfile = plan.getName();
    	
    	script_remux = new String[] {"ffmpeg",
                "-y",	
                "-i",
                concat_des_rush_du_plan,
                "-ss",
                "0.24",
                "-s",
                "720x576",
                "-sws_flags",
                "lanczos",
                "-pix_fmt",
                "yuv420p",
                "-aspect",
                "16:9",
                "-b:a",
                "384k",
                "-vcodec",
                "c:v",
                "-q:v",
                "0",
                String.format("%s/%s.mpg", outdir, outfile) 
                };
    	
    	script_remux_concat = new String[] {"ffmpeg",
                "-y",
                "-f",
                "concat",
                "-safe",
                "0",
                "-i",
                concat_des_rush_du_plan,
                "-ss",
                "0.24",
                "-s",
                "720x576",
                "-sws_flags",
                "lanczos",
                "-pix_fmt",
                "yuv420p",
                "-aspect",
                "16:9",
                "-b:a",
                "384k",
                "-c:v",
                "mpeg2video",
                "-q:v",
                "0",
                String.format("%s/%s.mpg", outdir, outfile) 
                };
    	
    	script_remux_deint = new String[] {"ffmpeg",
                "-y",
                "-i",
                concat_des_rush_du_plan,
                "-ss",
                "0.24",
                "-vf",
                "yadif=0:0:0",
                "-s",
                "720x576",
                "-sws_flags",
                "lanczos",
                "-pix_fmt",
                "yuv420p",
                "-aspect",
                "16:9",
                "-b:a",
                "384k",
                "-c:v",
                "mpeg2video",
                "-q:v",
                "0",
                String.format("%s/%s.mpg", outdir, outfile) 
                };
    	
    	script_remux_concat_deint = new String[] {"ffmpeg",
                "-y",
                "-f",
                "concat",
                "-safe",
                "0",
                "-i",
                concat_des_rush_du_plan,
                "-ss",
                "0.24",
                "-vf",
                "yadif=0:0:0",
                "-s",
                "720x576",
                "-sws_flags",
                "lanczos",
                "-pix_fmt",
                "yuv420p",
                "-aspect",
                "16:9",
                "-b:a",
                "384k",
                "-c:v",
                "mpeg2video",
                "-q:v",
                "0",
                String.format("%s/%s.mpg", outdir, outfile) 
                };
        }

        public void remux(){
        	   		
    		Process [] p0 = new Process [100];
			
			try {
				
				int i = 0;
				while(i < liste_des_scripts_fifo.size()){
					
					p0[i] = Runtime.getRuntime().exec(liste_des_scripts_fifo.get(i));
					System.out.println("\n**script_fifo**");
					System.out.println("\n** " + i);
					System.out.println(affcommande(liste_des_scripts_fifo.get(i)));

					p0[i].waitFor();
					
					i++;

				}
				
				System.out.println("sortie le la boucle de mkfifo " + 0  + " > " + (i -1));
				for (int j = 0;j < liste_des_scripts_fifo.size(); j++){
					System.out.println("isAlive() p0[" + (j) + "] : " +  p0[j].isAlive());	
				}
				
	            System.out.println("\n**script_remux**");
				
				if (plan.getChunks().size() > 1){
					
					p2 = cadreur.isDeint() ? Runtime.getRuntime().exec(script_remux_concat_deint)
	                                       : Runtime.getRuntime().exec(script_remux_concat);
					
					System.out.println(cadreur.isDeint() ? affcommande(script_remux_concat_deint)
	                                                     : affcommande(script_remux_concat));
				}
				else {
					
					p2 = cadreur.isDeint() ? Runtime.getRuntime().exec(script_remux_deint)
	                                       : Runtime.getRuntime().exec(script_remux);
		
		            System.out.println(cadreur.isDeint() ? affcommande(script_remux_deint)
	                                                     : affcommande(script_remux));
				}

				fluxErreurERR_REMUX = new AfficheurFlux(p2.getErrorStream(), "[FFMPEG ERR remux] ", false, p2);
	            new Thread(fluxErreurERR_REMUX).start();
			
            }
			catch (Exception e) {
				System.out.println("une exception !");
				
				e.printStackTrace();
			}
			
			System.out.println("isAlive() p2 : " +  p2.isAlive());
			
	}
		
	public void lire(){

		liste_des_scripts_lecture = new ArrayList<>();

		for (int i = 0; i < taille_liste; i++){
	
			script_lecture = new String[] {"mencoder",
			                    "-oac",
			                    "pcm",
			                    "-ovc",
			                    "lavc",
			                    "-lavcopts",
			                    "vcodec=huffyuv:format=422p",
			                    "-vf",
			                    "scale=1440:1080",
			                    String.format("%s", plan.getChunks().get(i)),
			                    "-o",
			                    String.format("%s/fifo_%s_%d.avi", ram, plan.getName(), i)
			                    };
				
			liste_des_scripts_lecture.add(script_lecture);

		}
	
		Process [] p1 = new Process [100];
    	fluxInputSTD_LECTURE = new AfficheurFlux2[100];
    	fluxErreurERR_LECTURE = new AfficheurFlux3[100];
		
    	try {
          	int i = 0;

			while(i < liste_des_scripts_lecture.size()){

				System.out.println("\n**script_lecture**");
				System.out.println("** " + i);
				
				p1[i] = Runtime.getRuntime().exec(liste_des_scripts_lecture.get(i));
				
				System.out.println(affcommande(liste_des_scripts_lecture.get(i)));
                
				
				fluxErreurERR_LECTURE[i] = new AfficheurFlux3(p1[i].getErrorStream(), "[FFMPEG ERR lecture] ", false, p1[i]);
				fluxInputSTD_LECTURE[i] = new AfficheurFlux2(p1[i].getInputStream(), "[FFMPEG STD lecture] ", false, p1[i], fluxErreurERR_LECTURE[i]);
				
	            new Thread(fluxInputSTD_LECTURE[i]).start();
	            new Thread(fluxErreurERR_LECTURE[i]).start();
	            
	            System.out.println(String.format("Wait fo p1[%d]", i));
            	p1[i].waitFor();

				i++;
		    }
			
			System.out.println("sortie le la boucle de lecture " + 0  + " > " + (i -1));
			for (int j = 0;j < taille_liste; j++){
				System.out.println("isAlive() p1[" + (j) + "] : " +  p1[j].isAlive());	
				fluxErreurERR_LECTURE[j].close();
			}
					
		}
		catch (Exception e) {
			System.out.println("une exception !");
			
			e.printStackTrace();
		}

	}
	
	public void dd(Rush plan){
		
		long seeksize = 0;
		Process p4;
		
		for (Rush r : plan.getChunks()){
			
			script_dd = new String[] {"dd",
	                  String.format("if='%s'", r.getPath()),
	                  String.format("of='%s'", String.format("%s/fifo_%s.M2T", ram, plan.getName())),
	                  String.format("seek=%d", seeksize)};
			
			
			
//            
//			
//			fluxErreurERR_LECTURE = new AfficheurFlux3(p1[i].getErrorStream(), "[FFMPEG ERR lecture] ", false, p1[i]);
//			fluxInputSTD_LECTURE = new AfficheurFlux2(p1[i].getInputStream(), "[FFMPEG STD lecture] ", false, p1[i], fluxErreurERR_LECTURE[i]);
//			
//            new Thread(fluxInputSTD_LECTURE[i]).start();
//            new Thread(fluxErreurERR_LECTURE[i]).start();
            
            System.out.println(String.format("Wait fo p4"));
            
        	try {
        		System.out.println("\n**script_lecture**");
    			
    			p4 = Runtime.getRuntime().exec(script_dd);
    			
    			System.out.println(affcommande(script_dd));
				p4.waitFor();
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}
		
		

	}
	
	public void close(){
		
		Process p3;
		
		try {

			Instant deb = Instant.now();
			
			while (p2.isAlive()){	
			}
			System.out.println("== latence fin remux == " + Duration.between(deb, Instant.now()).toMillis() / 1000.0 + "secondes");
			
			System.out.println("\n**fin process remux**");
			System.out.println("\n**p2 destroy()**");

			for(String f : liste_des_fifos){
				
				System.out.println("\n**script_rmfifos**");
				System.out.println("** " + f);
				
				script_rmfifos = new String[] {"rm",
	                    "-f",
	                    f
	            };
				p3 = Runtime.getRuntime().exec(script_rmfifos);
				System.out.println("isAlive() p3[pre] : " +  p3.isAlive());
	            p3.waitFor();	
	            System.out.println("isAlive() p3[post] : " +  p3.isAlive());
			}

			
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public String affcommande(String[] com){
		
		String s = "";
		
		for (int i = 0; i < com.length; i++){
			s += " ";
			s += com[i];
		}
		return s;
	}
}