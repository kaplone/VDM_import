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

public class ModeleMP4_XDCAM extends ModeleImport{
	
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
                "-i",
                concat_des_rush_du_plan,
                "-ss",
                "0.12",
                "-f",
                "dvd",
                "-r",
                "25",
                "-filter:v",
                "setpts=N/(25*TB)",
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
    	
    	script_remux_concat = new String[] {"ffmpeg",
                "-y",
                "-f",
                "concat",
                "-safe",
                "0",
                "-i",
                concat_des_rush_du_plan,
                "-ss",
                "0.12",
                "-f",
                "dvd",
                "-r",
                "25",
                "-filter:v",
                "setpts=N/(25*TB)",
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
                "0.12",
                "-f",
                "dvd",
                "-r",
                "25",
                "-filter:v",
                "setpts=N/(25*TB)",
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
    	
    	script_remux_concat_deint = new String[] {"ffmpeg",
                "-y",
                "-f",
                "concat",
                "-safe",
                "0",
                "-i",
                concat_des_rush_du_plan,
                "-ss",
                "0.12",
                "-f",
                "dvd",
                "-r",
                "25",
                "-filter:v",
                "setpts=N/(25*TB)",
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
        				
        				
//        	        	try {
//        	        		System.out.println("isAlive() p2 : " +  p2.isAlive());
//            				System.out.println("[pre] Wait for p2");
//							p2.waitFor();
//							System.out.println("[post] Wait for p2");
//	        	            close();
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
        				
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
    	            
//    	            System.out.println("[pre] Wait for p2");
//    	        	p2.waitFor();
//    	        	System.out.println("[post] Wait for p2");
//    	            close();
    	            
    	            System.out.println("isAlive() p2 : " +  p2.isAlive());
    			
    			
                }
    			catch (Exception e) {
    				System.out.println("une exception !");
    				
    				e.printStackTrace();
    			}
    			
    			System.out.println("isAlive() p2 : " +  p2.isAlive());
        	}	
	}
		
//	public void lire(){
//
//		liste_des_scripts_lecture = new ArrayList<>();
//
//		for (int i = 0; i < taille_liste; i++){
//
////			script_lecture = new String[] {"ffmpeg",
////			                    "-y",
////			                    "-analyzeduration",
////			                    "100M",
////			                    "-probesize",
////			                    "100M",
////			                    "-i",
////			                    String.format("%s", plan.getChunks().get(i)),
////			                    "-c:v",
////			                    "huffyuv",
////			                    "-c:a",
////			                    "pcm_s16le",
////			                    String.format("%s/fifo_%s_%d.avi", ram, plan.getName(), i)
////			                    };
//			
//			script_lecture = new String[] {"mencoder",
//	                "-oac",
//	                "pcm",
//	                "-ovc",
//	                "raw",
//	                "-vf",
//	                "scale=1440:1080",
//	                plan.getChunks().get(i).toString(),
//	                "-o",
//	                String.format("%s/fifo_%s_%d.avi", ram, plan.getName(), i)
//	                };	
//				
//
//			liste_des_scripts_lecture.add(script_lecture);
//
//		}
//
//    	
//		Process [] p1 = new Process [100];
//    	fluxErreurERR_LECTURE = new AfficheurFlux3[100];
//    	fluxInputSTD_LECTURE = new AfficheurFlux2[100];
//        
//    	Runnable lire_runnable = new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//		          	int i = 0;
//
//					while(i < taille_liste){
//
//						System.out.println("\n**script_lecture**");
//						System.out.println("** " + i);
//						
//						p1[i] = Runtime.getRuntime().exec(liste_des_scripts_lecture.get(i));
//						
//						System.out.println(affcommande(liste_des_scripts_lecture.get(i)));
//						
//						fluxErreurERR_LECTURE[i] = new AfficheurFlux3(p1[i].getErrorStream(), "[MENCODER ERR lecture] ", true, p1[i]);
//						fluxInputSTD_LECTURE[i] = new AfficheurFlux2(p1[i].getInputStream(), "[MENCODER STD lecture] ", false, p1[i], fluxErreurERR_LECTURE[i]);
//
//			            new Thread(fluxErreurERR_LECTURE[i]).start();
//			            new Thread(fluxInputSTD_LECTURE[i]).start();
//			            
//			            System.out.println(String.format("Wait fo p1[%d]", i));
//		            	p1[i].waitFor();
//			         	            
//						i++;
//				    }
////					
////					System.out.println("sortie le la boucle de lecture " + 0  + " > " + (i -1));
////					for (int j = 0;j < liste_des_scripts_fifo.size(); j++){
////						System.out.println("isAlive() p1[" + (j) + "] : " +  p1[j].isAlive());	
////						fluxErreurERR_LECTURE[j].close();
////					}
////							
//				}
//				catch (Exception e) {
//					System.out.println("une exception !");
//					
//					e.printStackTrace();
//				}
//			}
//    	};
//    	Thread t_lire = new Thread(lire_runnable);
//		//t_lire.setPriority(Thread.MAX_PRIORITY);
//		t_lire.start();    	
//
//	}
	
//    public void close(){
//		
//		Process p3;
//		
//		try {
//
//			Instant deb = Instant.now();
//			
//			while (p2.isAlive()){	
//			}
//			System.out.println("== latence fin remux == " + Duration.between(deb, Instant.now()).toMillis() / 1000.0 + "secondes");
//			
//			System.out.println("\n**script_rmfifos**");
//			System.out.println("** " + String.format("%s/fifo_%s_*", ram, plan.getName()));
//			
//			script_rmfifos = new String[] {"rm",
//                    "-f",
//                    String.format("%s/fifo_%s_*", ram, plan.getName())
//            };
//			p3 = Runtime.getRuntime().exec(script_rmfifos);	
//           
//
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}