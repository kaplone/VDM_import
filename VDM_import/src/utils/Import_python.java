package utils;

import java.io.File;

import models.Cadreur;
import models.imports.Import_with_python;
import models.imports.ModeleImport;

public class Import_python {
    
	private static ModeleImport modele;
	
	public static void importer(File dossier, Cadreur cadreur){
		
		
		try {
			modele = (ModeleImport) Import_with_python.class.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		modele.import_rushs(dossier, cadreur);
		
	}
}
