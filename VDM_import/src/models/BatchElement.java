package models;

import java.io.File;

import application.BatchList_controller;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BatchElement {
	
	private File dossier;
	private Cadreur cadreur;
	private String modele;
	private Class classe;
	private boolean deint;
	private String status;
	private String multi;
	private String message;

	public BatchElement(File dossier, String message, String multi, Cadreur cadreur, boolean deint) {
		this.dossier = dossier;
		this.message = message;
		this.cadreur = cadreur;
		this.deint = deint;
		this.multi = multi;
        this.status = "En attente";
        this.classe = cadreur.getModele_import();
        this.modele = cadreur.getPython_file();
	}
//	
//	public BatchElement(File dossier, Cadreur cadreur, boolean deint) {
//		this.dossier = dossier;
//		this.classe = cadreur.getModele_import();
//		this.cadreur = cadreur;
//		this.deint = deint;
//        this.status = "En attente";
//	}
	
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
		return message.equals("python") ? this.modele : this.classe.getSimpleName();
	}

	public ChoiceBox<String> getDeint(){
		
		ChoiceBox<String> cb = new ChoiceBox<>();
	
		cb.getItems().addAll("Oui", "Non");
		
		if (deint){
			cb.getSelectionModel().select(0);
		}
		else {
			cb.getSelectionModel().select(1);
		}
		
		return cb;
		
	}
	
	public boolean getDeint_bool(){
		return deint;
	}
	
	public void setDeint(boolean deint){
		this.deint = deint;
	}
	
	public void setCadreur(Cadreur cadreur) {
		this.cadreur = cadreur;
	}

	public String getStatus() {
		return status;
	}
	
	public String getStatus_() {
		return "Terminé";
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Button getSupprimer(){
		
		Button b = new Button ("Supprimer");
		b.setOnAction(a -> BatchList_controller.getObservable_liste_1().remove(this));
		
		return b;
		
	}

	public String getModele() {
		return cadreur.getPython_file();
	}

	public Class getClasse() {
		return classe;
	}
		
    public String getMessage(){
    	return message;
    }
	
	public boolean isMulti(){
		return multi.equals("Oui") ? true : false;
	}

	public String getMulti() {
		return multi;
	}

	public void setMulti(String multi) {
		this.multi = multi;
	}

	
}
