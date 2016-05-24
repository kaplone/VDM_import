package models.imports;

import java.io.File;


import models.Cadreur;
import utils.AfficheurFlux;

public class Import_with_python extends ModeleImport{
	
	
	private String[] script_python;

	private String outdir;
	private String outfile;

	private Cadreur cadreur;
	
	private AfficheurFlux fluxSortieSTD;
	private AfficheurFlux fluxErreurERR;
	
	private Process process;


	@Override
	public void import_rushs(File dossier, Cadreur cadreur, boolean multi) {
		
		this.cadreur = cadreur;
		
		numero_dossier = dossier.toPath().getFileName().toString();
		
		script_python = new String[] {"python2",
                                      cadreur.getPython_file(),
                                      dossier.toString()};
		
		encode();
		
	}

    public void encode(){
    	
    	try {
    		process = Runtime.getRuntime().exec(script_python);
			System.out.println("\n**script_encode**");
			System.out.println(affcommande(script_python));
			
			fluxSortieSTD = new AfficheurFlux(process.getInputStream(), "[PYTHON STD] ", true, null);
			fluxErreurERR = new AfficheurFlux(process.getErrorStream(), "[PYTHON ERR] ", true, null);
			new Thread(fluxSortieSTD).start();
            new Thread(fluxErreurERR).start();
            process.waitFor();
    	}
    	
		catch (Exception e) {
			System.out.println("une exception !");
			
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
