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
	private AfficheurFlux2 fluxInputSTD_LECTURE;
	private AfficheurFlux3 fluxErreurERR_LECTURE;

	
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
	
		try {
			Process p0 = Runtime.getRuntime().exec(script_fifo);
			System.out.println("\n**script_fifo**");
			System.out.println(affcommande(script_fifo));

			p0.waitFor();
			
			Process p00 = Runtime.getRuntime().exec(script_fifo_dd);
			System.out.println("\n**script_fifo_dd**");
			System.out.println(affcommande(script_fifo_dd));

			p00.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public void open() {

		concat_des_rush_du_plan = String.format("%s/fifo_%s.avi", ram, plan.getName());
		
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
        }

        public void remux(){
			
			try {
	            System.out.println("\n**script_remux**");
	            
	            p2 = cadreur.isDeint() ? Runtime.getRuntime().exec(script_remux_deint)
                        : Runtime.getRuntime().exec(script_remux);

				System.out.println(cadreur.isDeint() ? affcommande(script_remux_deint)
				                                     : affcommande(script_remux));


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
		
		String source = plan.getPath();
		if (plan.getChunks().size() > 1){
			
			dd();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			source = String.format("%s/fifo_%s.M2T", ram, plan.getName());;
    	}

		script_lecture = new String[] {"mencoder",
                "-oac",
                "pcm",
                "-ovc",
                "lavc",
                "-lavcopts",
                "vcodec=huffyuv:format=422p",
                "-vf",
                "scale=1440:1080",
                source,
                "-o",
                String.format("%s/fifo_%s.avi", ram, plan.getName())
                };

		Process p1;
		
    	try {
    		
    		System.out.println("\n**script_lecture**");
    		
    		
			
			p1 = Runtime.getRuntime().exec(script_lecture);
			
			System.out.println(affcommande(script_lecture));
            
			
			fluxErreurERR_LECTURE = new AfficheurFlux3(p1.getErrorStream(), "[FFMPEG ERR lecture] ", false, p1);
			fluxInputSTD_LECTURE = new AfficheurFlux2(p1.getInputStream(), "[FFMPEG STD lecture] ", false, p1, fluxErreurERR_LECTURE);
			
            new Thread(fluxInputSTD_LECTURE).start();
            new Thread(fluxErreurERR_LECTURE).start();
            
            System.out.println(String.format("Wait fo p1"));
        	p1.waitFor();
					
		}
		catch (Exception e) {
			System.out.println("une exception !");
			
			e.printStackTrace();
		}

	}
	
	public void dd(){
		
		System.out.println("\n**entrée_dd**");

		Runnable dd_runnable = new Runnable() {
			
			@Override
			public void run() {
				
				long seeksize = 0;
				long reste = 0;
				Process p4;
				
				for (Rush r : plan.getChunks()){
					
					script_dd = new String[] {"dd",
			                  String.format("if='%s'", r.getPath()),
			                  String.format("of='%s'", String.format("%s/fifo_%s.M2T", ram, plan.getName())),
			                  String.format("seek=%d", seeksize)};

		        	try {
		        		System.out.println("\n**script_dd**");
		    			
		    			p4 = Runtime.getRuntime().exec(script_dd);
		    			
		    			System.out.println(affcommande(script_dd));
						p4.waitFor();
						System.out.println(String.format("Wait fo p4"));
						
						reste = r.length() % 512;
						if(reste != 0){
							seeksize += (r.length() / 512) + 1;
						}
						else {
							seeksize += (r.length() / 512);
						}
						System.out.println("seeksize : " + seeksize);
						
					} catch (InterruptedException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
				}
				
			}
		};
		
		
		new Thread(dd_runnable).start();
		

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
			
			System.out.println("\n**script_rmfifos**");
			System.out.println("** " + String.format("%s/fifo_%s.M2T", ram, plan.getName()));
			
			script_rmfifos = new String[] {"rm",
                    "-f",
                    String.format("%s/fifo_%s.M2T", ram, plan.getName())
            };
			p3 = Runtime.getRuntime().exec(script_rmfifos);
            p3.waitFor();	
            
            System.out.println("** " + String.format("%s/fifo_%s.avi", ram, plan.getName()));
			
			script_rmfifos = new String[] {"rm",
                    "-f",
                    String.format("%s/fifo_%s.avi", ram, plan.getName())
            };
			p3 = Runtime.getRuntime().exec(script_rmfifos);
            p3.waitFor();

			
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