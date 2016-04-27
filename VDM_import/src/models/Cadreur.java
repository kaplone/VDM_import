package models;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import models.imports.*;

public enum Cadreur {
	
	ALBARET   ("MXF", false, ModeleMXF_XDCAM.class, ""),
	AMOUROUX  ("M2T", true,  ModeleM2T_mencoder_desplit.class,       ""),
	ARMATOL   ("MP4", false, ModeleMP4_CLPR.class , ""),
	BATMAN    ("MXF", true,  ModeleMXF_P2.class, ""),
	BAUDON    ("MP4", false, ModeleMP4_CLPR.class, ""),
	BIGEAULT  ("MP4", false, ModeleMP4_CLPR.class, ""),
	DE_KERDREL("MXF", true,  ModeleMXF_P2.class, ""),
	DUCOING   ("MOV", false, ModeleMOV_ProRes.class, ""),
	GIRARD    ("MP4", false, ModeleMP4_CLPR.class, ""),
	GAUTIER   ("M2T", false, ModeleM2T_mencoder_desplit.class, ""),
	GORWA     ("MTS", false, ModeleMTS.class, ""),
	HUET      ("MTS", false, ModeleMTS.class, ""),
	HUGUES    ("MOV", false, ModeleMOV_ProRes.class, ""),
	LEUX      ("MTS", false, ModeleMTS.class, ""),
	LE_YOUDEC ("MXF", false, ModeleMXF_XDCAM.class, ""),
	LOPACKI   ("MTS", true,  ModeleMTS.class, ""),
	MAS       ("MP4", false, ModeleMP4_CLPR.class, ""),
	MERCHADOU ("MP4", false, ModeleMP4_CLPR.class, ""),
	MERCIER   ("M2T", false, ModeleM2T_mencoder_desplit.class, ""),
	MICHARD   ("MP4", true,  ModeleMP4_CLPR.class, ""),
	MISSANA   ("M2T", true,  ModeleM2T_NO_DESPLIT_drop20f.class, "merging_M2T_05_SD_NO_DESPLIT_deint.py"),
	MOREL     ("MP4", true,  ModeleMP4_CLPR.class, "merging_MP4_06_SD_deint.py"),
	PARADIS   ("M2T", true,  ModeleM2T_NO_DESPLIT_drop20f.class, ""),
	PICHON    ("M2T", true,  ModeleM2T_mencoder_desplit.class, ""),
	QUESADA   ("M2T", true,  ModeleM2T_mencoder_desplit.class, ""),
	RICO      ("M2T", true,  ModeleM2T_mencoder_desplit.class, "")
	;
	
	private String extension;
	private boolean deint;
	private Class modele_import;
	
	private String currentUsersHomeDir = System.getProperty("user.home");
	
	private String python_file;
	
	Cadreur (String ext, boolean deint, Class modele_import, String python_file){
		this.extension = ext;
		this.deint = deint;
		this.modele_import = modele_import;
		this.python_file = python_file;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public boolean isDeint() {
		return deint;
	}

	public void setDeint(boolean deint) {
		this.deint = deint;
	}

	public Class getModele_import() {
		return modele_import;
	}

	public void setModele_import(Class modele_import) {
		this.modele_import = modele_import;
	}

	public void setPython_file(String python_file) {
		this.python_file = python_file;
	}
	
	public String getPython_file(){
		return currentUsersHomeDir + File.separator + "encodage" + File.separator + python_file;
	}

	public static ObservableList<String> getExtensions () {
		
		Set<String> s = new HashSet<String>();
		
		for (Cadreur c : Cadreur.values()){
			s.add(c.getExtension());
		}
		
		return FXCollections.observableArrayList(s.stream().collect(Collectors.toList()));
		
	}
	
}
