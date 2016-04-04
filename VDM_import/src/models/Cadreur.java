package models;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import models.imports.*;

public enum Cadreur {
	
	ALBARET("MXF", false, ModeleMXF_XDCAM.class),
	AMOUROUX("M2T", true, ModeleM2T.class),
	ARMATOL("MP4", false, ModeleMP4_CLPR.class),
	BATMAN("MXF", true, ModeleMXF_P2.class);
	
	private String extension;
	private boolean deint;
	private Class modele_import;
	
	Cadreur (String ext, boolean deint, Class modele_import){
		this.extension = ext;
		this.deint = deint;
		this.modele_import = modele_import;
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

	public static ObservableList<String> getExtensions () {
		
		Set<String> s = new HashSet<String>();
		
		for (Cadreur c : Cadreur.values()){
			s.add(c.getExtension());
		}
		
		return FXCollections.observableArrayList(s.stream().collect(Collectors.toList()));
		
	}
	
}
