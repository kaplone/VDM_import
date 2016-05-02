package models.imports;

import java.io.File;
import java.io.IOException;
import java.util.List;

import models.Cadreur;
import models.Rush;
import utils.AfficheurFlux;
import utils.AfficheurFlux2;
import utils.AfficheurFlux3;

public abstract class ModeleImport {
	
	protected File dossier;
	protected Cadreur cadreur;
	protected boolean multithread;
	
	protected List<String[]> liste_des_scripts_fifo;
	protected List<String[]> liste_des_scripts_lecture;
	
	protected AfficheurFlux fluxErreurERR_REMUX;
	protected AfficheurFlux2 fluxInputSTD_LECTURE;
	protected AfficheurFlux3 fluxErreurERR_LECTURE;
	
	protected String[] script_fifo;
	protected String[] script_fifo_dd;
	protected String[] script_rmfifos;
	protected String[] script_lecture;
	protected String[] script_cat;
	protected String[] script_remux;
	protected String[] script_remux_deint;
	protected String concat_des_rush_du_plan;
	protected String outdir;
	protected String outfile;
	protected List<Rush> liste_des_plans;
	protected Process p2;
	protected Rush plan;	
	protected String numero_dossier;
	protected int taille_liste;
	
	protected final File ram = new File("/mnt/ramdisk");
	protected final File temp = new File("/home/david/temp");
	protected final File out = new File("/mnt/2015_rushs");
	
	public void constructeur (File dossier, Cadreur cadreur, boolean multithread){
		
		this.dossier = dossier;
		this.cadreur = cadreur;
		this.multithread = multithread;
		
	}

	public abstract void import_rushs(File dossier, Cadreur cadreur, boolean multithread);
	
    public void init() {
		
        numero_dossier = dossier.toPath().getFileName().toString();
		
		outdir = String.format("%s/%s", out, numero_dossier);
		
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

			Process p00 = Runtime.getRuntime().exec(script_fifo_dd);
			System.out.println("\n**script_fifo_dd**");
			System.out.println(affcommande(script_fifo_dd));

			p00.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
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
