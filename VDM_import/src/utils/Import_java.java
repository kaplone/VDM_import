package utils;

import java.io.File;

import models.BatchElement;
import models.Cadreur;
import models.imports.Import_with_python;
import models.imports.ModeleImport;

public class Import_java {
    
	private static ModeleImport modele;
	
	public static void importer(File dossier, Cadreur cadreur, boolean multithread ){
		
		
		try {
			modele = (ModeleImport) cadreur.getModele_import().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		modele.import_rushs(dossier, cadreur, multithread);
		
	}
	
    public static void importer(BatchElement element){
		
		
		try {
			modele = (ModeleImport) element.getCadreur().getModele_import().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		modele.import_rushs(element);
		
	}
}