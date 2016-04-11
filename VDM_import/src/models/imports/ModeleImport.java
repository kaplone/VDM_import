package models.imports;

import java.io.File;
import java.util.List;

import models.Cadreur;
import models.Rush;

public interface ModeleImport {
	
	public void import_rushs(File dossier, Cadreur cadreur);

}
