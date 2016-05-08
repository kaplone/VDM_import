package models;

import java.io.File;

public class BatchElement {
	
	private File dossier;
	private Cadreur cadreur;

	public BatchElement(File dossier, Cadreur cadreur) {
		this.dossier = dossier;
		this.cadreur = cadreur;
	}
	
	public File getDossier() {
		return dossier;
	}
	public void setDossier(File dossier) {
		this.dossier = dossier;
	}
	
	public String getCadreur() {
		return cadreur.name();
	}
	
	public String getMethode() {
		return cadreur.getModele_import().getSimpleName();
	}
	
	public String getDeint() {
		return cadreur.isDeint() ? "Oui" : "Non";
	}
	
	public void setCadreur(Cadreur cadreur) {
		this.cadreur = cadreur;
	}
	
	

}
