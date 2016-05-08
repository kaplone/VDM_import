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
	private boolean deint;
	private String status;

	public BatchElement(File dossier, Cadreur cadreur, boolean deint) {
		this.dossier = dossier;
		this.cadreur = cadreur;
		this.deint = deint;
        this.status = "En attente";
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
	
	public void setDeint(boolean deint){
		this.deint = deint;
	}
	
	public void setCadreur(Cadreur cadreur) {
		this.cadreur = cadreur;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Button getSupprimer(){
		
		Button b = new Button ("Supprimer");
		b.setOnAction(a -> BatchList_controller.getObservable_liste().remove(this));
		
		return b;
		
	}
	
}
