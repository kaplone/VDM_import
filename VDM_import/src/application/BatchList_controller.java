package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import models.BatchElement;
import models.Cadreur;

public class BatchList_controller implements Initializable{
	
	private Stage stage;
	private ObservableList<BatchElement> observable_liste;
	private TableView<BatchElement> liste;
	
	public void afficher(){
		stage.show();
	}
	
	public void ajouter(File dossier, Cadreur cadreur){
		observable_liste.add(new BatchElement(dossier, cadreur));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		stage = new Stage();
		BorderPane root = new BorderPane();
	
		stage.setHeight(800);
		stage.setWidth(1200);
		
		liste = new TableView<>();
		TableColumn<BatchElement, String> dossier = new TableColumn<>("Dossier");
		TableColumn<BatchElement, String> cadreur = new TableColumn<>("Cadreur");
		TableColumn<BatchElement, String> methode = new TableColumn<>("Méthode");
		TableColumn<BatchElement, String> deint = new TableColumn<>("Déint");
		liste.getColumns().addAll(dossier, cadreur, methode, deint);
		observable_liste = FXCollections.observableArrayList();
		liste.setItems(observable_liste);
		
		dossier.setCellValueFactory(
			    new PropertyValueFactory<>("dossier")
			);
		
		cadreur.setCellValueFactory(
			    new PropertyValueFactory<>("cadreur")
			);
		
		methode.setCellValueFactory(
			    new PropertyValueFactory<>("methode")
			);
		
		deint.setCellValueFactory(
			    new PropertyValueFactory<>("deint")
			);
		
		observable_liste.add(new BatchElement(new File("/home/blabla/444"), Cadreur.AMOUROUX));
		
		root.setCenter(liste);
		
		Scene scene = new Scene((Parent) root);
		stage.setScene(scene);
		
		stage.hide();
		
		
		
	}

}
