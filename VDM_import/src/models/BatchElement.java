package models;

import java.io.File;

import javax.swing.Box.Filler;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class BatchElement {
	
	private File dossier;
	private Cadreur cadreur;
	private boolean deint;

	public BatchElement(File dossier, Cadreur cadreur, boolean deint) {
		this.dossier = dossier;
		this.cadreur = cadreur;
		this.deint = deint;
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
//        cb.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
//            @Override
//            public ListCell<String> call(ListView<String> param) {
//              final ListCell<String> cell = new ListCell<String>() {
//                {
//                  super.setPrefWidth(100);
//                }
//
//                @Override
//                public void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//                    
//                    setText(item);
//                    
//                    if (item != null) {
//                    	if (item.contains("Oui")) {
//                            setStyle("-fx-text-fill: GREEN;-fx-font-weight: bold;");
//                          } else if (item.contains("Non")) {
//                        	  setStyle("-fx-text-fill: RED;-fx-font-weight: bold;");
//                          }
//                    }
//                    
//
//                }
//              };
//              return cell;
//            }
//          });
		
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
	
	

}
