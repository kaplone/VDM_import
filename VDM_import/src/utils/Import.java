package utils;

import java.io.File;

import models.Cadreur;
import models.imports.ModeleImport;

public class Import {
    
	private static ModeleImport modele;
	
	public static void importer(File dossier, Cadreur cadreur){
		
		
		try {
			modele = (ModeleImport) cadreur.getModele_import().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		modele.import_rushs(dossier, cadreur);
		
	}
}
