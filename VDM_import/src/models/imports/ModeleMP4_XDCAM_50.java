package models.imports;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import models.BatchElement;
import models.Cadreur;
import models.Rush;
import utils.AfficheurFlux;
import utils.AfficheurFlux2;
import utils.AfficheurFlux3;
import utils.AfficheurFlux4;
import utils.Messages;

public class ModeleMP4_XDCAM_50 extends ModeleImport{
	
	private List<String> liste_des_fifos;
	private List<Rush> liste_des_plans;
	
	private String[] script_remux_concat;
	private String[] script_remux_concat_deint;

	private FileWriter fw;
	
	private AfficheurFlux fluxErreurERR_REMUX;
	private AfficheurFlux2[] fluxInputSTD_LECTURE;
	private AfficheurFlux3[] fluxErreurERR_LECTURE;

	
	public void ecritureTxt(){
		
		File txt = new File(ram + String.format("/liste_%s.txt", numero_dossier));
		
		try {
			fw = new FileWriter(txt);
			for (Rush r : plan.getChunks()){
				fw.write(String.format("file '%s'\n", r.getAbsolutePath() ));
			}
			fw.close();
			
			System.out.println(String.format("liste_%s.txt [CLOSED]", numero_dossier));
		}
		
		catch (IOException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public void import_rushs(File dossier, Cadreur cadreur, boolean multithread) {
		
		super.constructeur(dossier, cadreur, multithread);
		
		liste_des_plans = Messages.getListeDesPlans();
		
		for (int i = 0; i < liste_des_plans.size(); i++){
			
			plan = liste_des_plans.get(i);
			
			System.out.println("[BOUCLE] : " + plan.getName());

			try {
				System.out.println("[BOUCLE (début)]");
				init();
				System.out.println("[BOUCLE init() -> open()]");
				open();		
				System.out.println("[BOUCLE open() -> lire()]");
//				Thread.sleep(2000);
//				lire();
				System.out.println("[BOUCLE lire() -> remux()]");
				Thread.sleep(2000);
				remux(multithread);
				System.out.println("[BOUCLE remux() -> close()]");
				close();
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
			
			System.out.println("[BOUCLE] : " + plan.getName());

			try {
				System.out.println("[BOUCLE (début)]");
				init();
				System.out.println("[BOUCLE init() -> open()]");
				open();		
				System.out.println("[BOUCLE open() -> lire()]");
//				Thread.sleep(2000);
//				lire();
				System.out.println("[BOUCLE lire() -> remux()]");
				Thread.sleep(2000);
				remux(multithread);
				System.out.println("[BOUCLE remux() -> close()]");
				close();
				System.out.println("[BOUCLE (fin)]");
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
		
//		liste_des_fifos = new ArrayList<>();
//		liste_des_scripts_fifo = new ArrayList<>();
//		
//        for (int i = 0; i < taille_liste; i++){
//			
//			script_fifo = new String[] {"mkfifo",
//					                    String.format("%s/fifo_%s_%d.avi", ram, plan.getName(), i)
//			                           };
//			
//			liste_des_fifos.add(String.format("%s/fifo_%s_%d.avi", ram, plan.getName(), i));
//
//			liste_des_scripts_fifo.add(script_fifo);
//        }
//        
//        Process [] p0 = new Process [100];
//		
//		try {
//			
//			int i = 0;
//			while(i < liste_des_scripts_fifo.size()){
//				
//				p0[i] = Runtime.getRuntime().exec(liste_des_scripts_fifo.get(i));
//				System.out.println("\n**script_fifo**");
//				System.out.println("\n** " + i);
//				System.out.println(affcommande(liste_des_scripts_fifo.get(i)));
//				p0[i].waitFor();
//				i++;
//
//			}
//			
//			System.out.println("sortie le la boucle de mkfifo " + 0  + " > " + (i -1));
//			for (int j = 0;j < liste_des_scripts_fifo.size(); j++){
//				System.out.println("isAlive() p0[" + (j) + "] : " +  p0[j].isAlive());	
//			}
//		}
//		catch (Exception e) {
//			System.out.println("une exception !");
//			
//			e.printStackTrace();
//		}
	}  
	
	@Override
	public void open() {
		
		if (taille_liste > 1){

			ecritureTxt();
			concat_des_rush_du_plan = ram + String.format("/liste_%s.txt", numero_dossier);
    	}
    	else {
    		concat_des_rush_du_plan = plan.getAbsolutePath();
    	}
		
		outfile = plan.getName();
    	
    	script_remux = new String[] {"ffmpeg",
                "-y",
                "-r",
                "50",
                "-i",
                concat_des_rush_du_plan,
                "-ss",
                "0.12",
                "-r",
                "25",
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
    	
    	script_remux_concat = new String[] {"ffmpeg",
                "-y",
                "-r",
                "50",
                "-f",
                "concat",
                "-safe",
                "0",
                "-i",
                concat_des_rush_du_plan,
                "-ss",
                "0.12",
                "-r",
                "25",
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
                "-r",
                "50",
                "-i",
                concat_des_rush_du_plan,
                "-ss",
                "0.12",
                "-r",
                "25",
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
                "-r",
                "50",
                "-f",
                "concat",
                "-safe",
                "0",
                "-i",
                concat_des_rush_du_plan,
                "-ss",
                "0.12",
                "-r",
                "25",
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

        public void remux(boolean multithread){
        	
        	System.out.println("================= Multithread : " + multithread);
        	
        	if (multithread){
        		Runnable remux_runnable = new Runnable() {
        			
        			@Override
        			public void run() {
        				try {
        					
        		            System.out.println("\n**script_remux**");
        					
        					if (taille_liste > 1){
        						
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
        		};
        		Thread t_remux = new Thread(remux_runnable);
        		//t_remux.setPriority(Thread.MAX_PRIORITY);
        		t_remux.start();
        	}
        	else {
        		try {
    				
    	            System.out.println("\n**script_remux**");
    				
    				if (taille_liste > 1){
    					
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

    				fluxErreurERR_REMUX = new AfficheurFlux(p2.getErrorStream(), "[FFMPEG ERR remux] ", true, p2);
    	            new Thread(fluxErreurERR_REMUX).start();
     
    	            System.out.println("isAlive() p2 : " +  p2.isAlive());
    			
    			
                }
    			catch (Exception e) {
    				System.out.println("une exception !");
    				
    				e.printStackTrace();
    			}
    			
    			System.out.println("isAlive() p2 : " +  p2.isAlive());
        	}	
	}
		
}