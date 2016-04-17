package models.imports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import models.Cadreur;
import models.Rush;
import utils.AfficheurFlux;
import utils.Messages;

public class ModeleMP4_CLPR implements ModeleImport{
	
	private String[] script_fifo;
	private String[] script_rmfifos;
	private String[] script_merge;
	private String[] script_encode;
	private String[] script_encode_deint;
	private String[] script_remux;
	private String[] script_remux_concat;

	private final File ram = new File("/mnt/ramdisk");
	private final File temp = new File("/home/david/temp");
	private final File out = new File("/mnt/2015_rushs");
	//private final File out = new File("/home/autor/Desktop");
	
	private String concat_des_rush_du_plan;
	private String outdir;
	private String outfile;
	
	private int offset = 0;
	
	private List<String> liste_des_fifos;
	private List<Rush> liste_des_plans;
	private List<Rush> liste_des_rush_du_plan;
	
	private List<String[]> liste_des_scripts_encode;
	private List<String[]> liste_des_scripts_encode_deint;
	private List<String[]> liste_des_scripts_fifo;
	
	private int taille_liste;
	
	private Cadreur cadreur;
	
	private FileWriter fw;
	
	private AfficheurFlux fluxSortieSTD;
	private AfficheurFlux fluxErreurERR;
	
	public void ecritureTxt(){
		
		File txt = new File(ram + "/liste.txt");
		
		try {
			fw = new FileWriter(txt);
			for (String f : liste_des_fifos){
				fw.write(String.format("file '%s'\n", f ));
			}
			fw.close();
		}
		
		catch (IOException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public void import_rushs(File dossier, Cadreur cadreur) {
		
		this.cadreur = cadreur;
		
		outdir = String.format("%s/%s", out, dossier.toPath().getFileName().toString());
		
		liste_des_plans = Messages.getListeDesPlans();
		
		for (int i = 0; i < liste_des_plans.size(); i++){
			
			Process pr = importer(liste_des_plans.get(i));
			Process p3;
			try {
				fluxSortieSTD = new AfficheurFlux(pr.getInputStream());
				fluxErreurERR = new AfficheurFlux(pr.getErrorStream());
				new Thread(fluxSortieSTD).start();
	            new Thread(fluxErreurERR).start();
				pr.waitFor();
				
				System.out.println("\n**script_rmfifos**");
				
				for(String f : liste_des_fifos){
					
					script_rmfifos = new String[] {"rm",
		                    "-f",
		                    f
		            };
					p3 = Runtime.getRuntime().exec(script_rmfifos);
					fluxSortieSTD = new AfficheurFlux(pr.getInputStream());
					fluxErreurERR = new AfficheurFlux(pr.getErrorStream());
					new Thread(fluxSortieSTD).start();
		            new Thread(fluxErreurERR).start();
		            p3.waitFor();		
				}
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		
	public Process importer(Rush plan){
		
		liste_des_fifos = new ArrayList<>();
		liste_des_scripts_encode = new ArrayList<>();
		liste_des_scripts_encode_deint = new ArrayList<>();
		liste_des_scripts_fifo = new ArrayList<>();

		taille_liste = plan.getChunks().size();
		
		for (int i = 0; i < taille_liste; i++){
			
			script_fifo = new String[] {"mkfifo",
					                    String.format("%s/fifo_%s_%d.mpg", ram, plan.getName(), i)
			                           };
			
			script_encode = new String[] {"ffmpeg",
		                    "-y",
		                    "-i",
		                    String.format("%s", plan.getChunks().get(i)),
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
		                    String.format("%s/fifo_%s_%d.mpg", ram, plan.getName(), i)
		                    };

           script_encode_deint = new String[] {"ffmpeg",
		                    "-y",
		                    "-i",
		                    String.format("%s", plan.getChunks().get(i)),
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
		                    String.format("%s/fifo_%s_%d.mpg", ram, plan.getName(), i)
		                    };
			
			liste_des_fifos.add(String.format("%s/fifo_%s_%d.mpg", ram, plan.getName(), i));
			liste_des_scripts_fifo.add(script_fifo);
			liste_des_scripts_encode.add(script_encode);
			liste_des_scripts_encode_deint.add(script_encode_deint);

		}
		
		if (plan.getChunks().size() > 1){

			ecritureTxt();
			concat_des_rush_du_plan = ram + "/liste.txt";
    	}
    	else {
    		concat_des_rush_du_plan = liste_des_fifos.get(0);
    	}
		
		outfile = plan.getName();
    	
    	script_remux = new String[] {"ffmpeg",
                "-y",
                "-i",
                concat_des_rush_du_plan,
                "-acodec",
                "ac3",
                "-b:a",
                "384k",
                "-vcodec",
                "copy",
                String.format("%s/%s.mpg", outdir, outfile) 
                };
    	
    	script_remux_concat = new String[] {"ffmpeg",
                "-y",
                "-f",
                "concat",
                "-i",
                concat_des_rush_du_plan,
                "-acodec",
                "ac3",
                "-b:a",
                "384k",
                "-vcodec",
                "mpeg2video",
                "-q:v",
                "0",
                String.format("%s/%s.mpg", outdir, outfile) 
                };
 	
    	Process [] p0 = new Process [50];
		Process [] p1 = new Process [50];
    	Process p2 = null;
		

		try {
			
			int i = 0;
			while(i < liste_des_scripts_fifo.size()){
				
				p0[i] = Runtime.getRuntime().exec(liste_des_scripts_fifo.get(i));
				System.out.println("\n**script_fifo**");
				System.out.println(affcommande(liste_des_scripts_fifo.get(i)));
				
				fluxSortieSTD = new AfficheurFlux(p0[i].getInputStream());
				fluxErreurERR = new AfficheurFlux(p0[i].getErrorStream());
				new Thread(fluxSortieSTD).start();
	            new Thread(fluxErreurERR).start();
				p0[i].waitFor();
				
				i++;

			}
			
            System.out.println("\n**script_remux**");
			
			if (plan.getChunks().size() > 1){
				p2 = Runtime.getRuntime().exec(script_remux_concat);
				fluxSortieSTD = new AfficheurFlux(p2.getInputStream());
				fluxErreurERR = new AfficheurFlux(p2.getErrorStream());
				new Thread(fluxSortieSTD).start();
	            new Thread(fluxErreurERR).start();
				System.out.println(affcommande(script_remux_concat));
			}
			else {
				p2 = Runtime.getRuntime().exec(script_remux);
				fluxSortieSTD = new AfficheurFlux(p2.getInputStream());
				fluxErreurERR = new AfficheurFlux(p2.getErrorStream());
				new Thread(fluxSortieSTD).start();
	            new Thread(fluxErreurERR).start();
				System.out.println(affcommande(script_remux));
			}
			
			i = 0;
			while(i < liste_des_scripts_fifo.size()){

				System.out.println("\n**script_encode**");
				
				p1[i] = cadreur.isDeint() ? Runtime.getRuntime().exec(liste_des_scripts_encode_deint.get(i))
                           : Runtime.getRuntime().exec(liste_des_scripts_encode.get(i));
				
				System.out.println(cadreur.isDeint() ? affcommande(liste_des_scripts_encode_deint.get(i))
                                                     : affcommande(liste_des_scripts_encode.get(i)));
				
				fluxSortieSTD = new AfficheurFlux(p1[i].getInputStream());
				fluxErreurERR = new AfficheurFlux(p1[i].getErrorStream());
				new Thread(fluxSortieSTD).start();
	            new Thread(fluxErreurERR).start();
				p1[i].waitFor();

				i++;
		    }
			
			
			
//			if (p2 != null && p2.waitFor() == 0){
//				System.out.println("\n**script_rmfifos**");
//				
//				for(String f : liste_des_fifos){
//					
//					script_rmfifos = new String[] {"rm",
//		                    "-f",
//		                    f
//		            };
//					p3 = Runtime.getRuntime().exec(script_rmfifos);
//		            p3.waitFor();
//					
//		            BufferedReader reader = 
//		                    new BufferedReader(new InputStreamReader(p3.getErrorStream()));
//		            BufferedReader reader_ = 
//		                    new BufferedReader(new InputStreamReader(p3.getInputStream()));
//
//		            String line = "";	
//
//					while ((line = reader.readLine())!= null) {
//						System.out.println(line);
//						reader_.readLine();
//					}		
//				}
//			}
			
			
		}
		catch (Exception e) {
			System.out.println("une exception !");
			
			e.printStackTrace();
		}
		
		return p2;

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
