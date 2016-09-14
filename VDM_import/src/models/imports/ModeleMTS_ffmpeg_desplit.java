package models.imports;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import models.BatchElement;
import models.Cadreur;
import models.Rush;
import utils.AfficheurFlux;
import utils.AfficheurFlux2;
import utils.AfficheurFlux3;
import utils.Messages;

public class ModeleMTS_ffmpeg_desplit extends ModeleImport{
	
	private AfficheurFlux fluxErreurERR_REMUX;
	private AfficheurFlux fluxErreurERR_LECTURE;

	@Override
	public void import_rushs(File dossier, Cadreur cadreur, boolean multithread) {
		
		super.constructeur(dossier, cadreur, multithread);
		
		liste_des_plans = Messages.getListeDesPlans();
		
		for (int i = 0; i < liste_des_plans.size(); i++){
			
			plan = liste_des_plans.get(i);

			try {
				System.out.println("[BOUCLE (début)]");
				init();
				System.out.println("[BOUCLE init() -> open()]");
				open();
				Thread.sleep(500);
				System.out.println("[BOUCLE open() -> dd()]");
				dd();
				Thread.sleep(1000);
				System.out.println("[BOUCLE dd() -> lire()]");
				lire();
				Thread.sleep(1000);
				System.out.println("[BOUCLE lire() -> remux()]");
				remux(multithread);
				System.out.println("[BOUCLE (fin)]");
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	@Override
	public void import_rushs(BatchElement element) {
		
		super.constructeur(element.getDossier(), element.getCadreur(), element.isMulti());
		
		liste_des_plans = element.getListeDesPlans();
		
		for (int i = 0; i < liste_des_plans.size(); i++){
			
			plan = liste_des_plans.get(i);

			try {
				init();
				open();
				Thread.sleep(500);
				dd();
				Thread.sleep(1000);
				lire();
				Thread.sleep(1000);
				remux(multithread);
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	@Override
    public void init() {
		
        numero_dossier = dossier.toPath().getFileName().toString();
		
		outdir = String.format("%s/%s", out.toString(), numero_dossier);
		
		taille_liste = plan.getChunks().size();
		
		script_fifo = new String[] {"mkfifo",
                String.format("%s/fifo_%s.avi", ram, plan.getName())
               };
		
		script_fifo_dd = new String[] {"mkfifo",
                String.format("%s/fifo_%s.MTS", ram, plan.getName())
               };
	
		try {
			Process p0 = Runtime.getRuntime().exec(script_fifo);
			System.out.println("\n**script_fifo**");
			System.out.println(affcommande(script_fifo));

			Process p00 = Runtime.getRuntime().exec(script_fifo_dd);
			System.out.println("\n**script_fifo_dd**");
			System.out.println(affcommande(script_fifo_dd));

			p00.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	@Override
	public void open() {

		concat_des_rush_du_plan = String.format("%s/fifo_%s.avi", ram, plan.getName());
		
		outfile = plan.getRename();
    	
    	script_remux = new String[] {"ffmpeg",
                "-y",	
                "-i",
                concat_des_rush_du_plan,
                "-ss",
                "0.24",
                "-f",
                "dvd",
                "-s",
                "720x576",
                "-sws_flags",
                "lanczos",
                "-pix_fmt",
                "yuv420p",
                "-aspect",
                "16:9",
                "-c:a",
                "pcm_s16be",
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
                "-f",
                "dvd",
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
                "-c:a",
                "pcm_s16be",
                "-c:v",
                "mpeg2video",
                "-q:v",
                "0",
                String.format("%s/%s.mpg", outdir, outfile) 
                };
        }

	public void lire(){
		
		liste_des_scripts_lecture = new ArrayList<>();
		
		String source = plan.getPath();
		if (plan.getChunks().size() > 1){
			source = String.format("%s/fifo_%s.MTS", ram, plan.getName());;
    	}
		
		script_lecture = new String[] {"ffmpeg",
                "-y",
                "-analyzeduration",
                "100M",
                "-probesize",
                "100M",
                "-i",
                source,
                "-map",
                "0:0",
                "-map",
                "0:1",
                "-c:v",
                "huffyuv",
                "-c:a",
                "pcm_s16le",
                "-sn",
                String.format("%s/fifo_%s.avi", ram, plan.getName())
                };

		Runnable lire_runnable = new Runnable() {

			@Override
			public void run() {

		    	try {
		    		
		    		System.out.println("\n**script_lecture**");

		    		Process p1 = Runtime.getRuntime().exec(script_lecture);
					
					System.out.println(affcommande(script_lecture));
		            
		            fluxErreurERR_LECTURE = new AfficheurFlux(p1.getErrorStream(), "[FFMPEG ERR lecture] ", false, p1);
		            new Thread(fluxErreurERR_LECTURE).start();
		            
		            System.out.println("p1.isAlive() : " + p1.isAlive());
							
				}
				catch (Exception e) {
					System.out.println("une exception !");
					
					e.printStackTrace();
				}			
			}		
		};
		Thread t_lire = new Thread(lire_runnable);
		//t_lire.setPriority(Thread.MAX_PRIORITY);
		t_lire.start();
	}
	
	public void dd(){
		
		System.out.println("\n**entrée_dd**");

		Runnable dd_runnable = new Runnable() {
			
			@Override
			public void run() {
				
				script_cat = new String[]{"sh",
						                  "-c",
						                  plan.getChunks()
						                      .stream()
						                      .map(a -> a.getPath())
						                      .collect(Collectors.joining(" ", "cat ", String.format(" > %s/fifo_%s.MTS", ram, plan.getName())))
				};
				
				try {
					System.out.println("\n**script_cat**");
	    			
	    			Process p4 = Runtime.getRuntime().exec(script_cat);
	    			
	    			System.out.println(affcommande(script_cat));			
	    			System.out.println("p4.isAlive() : " + p4.isAlive());
	    			
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		};
		
		Thread t_dd = new Thread(dd_runnable);
		//t_dd.setPriority(Thread.MAX_PRIORITY);
		t_dd.start();
	}	
	
public void remux(boolean multithread){
    	
    	if (multithread){
    		Runnable remux_runnable = new Runnable() {
    			
    			@Override
    			public void run() {
    				try {
    		            System.out.println("\n**script_remux**");
    		            
    		            p2 = cadreur.isDeint() ? Runtime.getRuntime().exec(script_remux_deint)
    	                        : Runtime.getRuntime().exec(script_remux);

    					System.out.println(cadreur.isDeint() ? affcommande(script_remux_deint)
    					                                     : affcommande(script_remux));


    					fluxErreurERR_REMUX = new AfficheurFlux(p2.getErrorStream(), "[FFMPEG ERR remux] ", false, p2);
    		            new Thread(fluxErreurERR_REMUX).start();
    		            
    		            close();
    				
    	            }
    				catch (Exception e) {
    					System.out.println("une exception !");
    					
    					e.printStackTrace();
    				}
    				
    				System.out.println("isAlive() p2 : " +  p2.isAlive());
    			}
        	};
			
        	Thread t_remux = new Thread(remux_runnable);
    		//t_remux.setPriority(Thread.MAX_PRIORITY);
    		t_remux.start();
    	}
    	
    	else {
    		
        	try {
	            System.out.println("\n**script_remux**");
	            
	            p2 = cadreur.isDeint() ? Runtime.getRuntime().exec(script_remux_deint)
                        : Runtime.getRuntime().exec(script_remux);

				System.out.println(cadreur.isDeint() ? affcommande(script_remux_deint)
				                                     : affcommande(script_remux));


				fluxErreurERR_REMUX = new AfficheurFlux(p2.getErrorStream(), "[FFMPEG ERR remux] ", false, p2);
	            new Thread(fluxErreurERR_REMUX).start();
	            
	            System.out.println("[pre] Wait for p2");
	        	p2.waitFor();
	        	System.out.println("[post] Wait for p2");
	            close();
			
            }
			catch (Exception e) {
				System.out.println("une exception !");
				
				e.printStackTrace();
			}
			
        	System.out.println("p2.isAlive() : " + p2.isAlive());
    	}			
    }
    

    @Override
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
			System.out.println("** " + String.format("%s/fifo_%s.MTS", ram, plan.getName()));
			
			script_rmfifos = new String[] {"rm",
	                "-f",
	                String.format("%s/fifo_%s.MTS", ram, plan.getName())
	        };
			p3 = Runtime.getRuntime().exec(script_rmfifos);	
	        
	        System.out.println("** " + String.format("%s/fifo_%s.avi", ram, plan.getName()));
			
			script_rmfifos = new String[] {"rm",
	                "-f",
	                String.format("%s/fifo_%s.avi", ram, plan.getName())
	        };
			p3 = Runtime.getRuntime().exec(script_rmfifos);
	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}  
}