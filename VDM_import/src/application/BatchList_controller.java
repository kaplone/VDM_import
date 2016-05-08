package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import models.BatchElement;
import models.Cadreur;

public class BatchList_controller implements Initializable{
	
	private Stage stage;
	private static ObservableList<BatchElement> observable_liste;
	private TableView<BatchElement> liste;
	
	private Button lancer;
	
	public void afficher(){
		stage.show();
	}
	
	public void ajouter(File dossier, Cadreur cadreur, boolean deint){
		observable_liste.add(new BatchElement(dossier, cadreur, deint));
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
		TableColumn<BatchElement, ChoiceBox<String>> deint = new TableColumn<>("Déint");
		TableColumn<BatchElement, String> status = new TableColumn<>("Status");
		TableColumn<BatchElement, Button> delete = new TableColumn<>("Supprimer");
		liste.getColumns().addAll(dossier, cadreur, methode, deint, status, delete);
		
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
		
		status.setCellValueFactory(
			    new PropertyValueFactory<>("status")
			);
		
		delete.setCellValueFactory(
			    new PropertyValueFactory<>("supprimer")
			);
		
		observable_liste.add(new BatchElement(new File("/home/blabla/444"), Cadreur.AMOUROUX, true));
		observable_liste.add(new BatchElement(new File("/home/blabla/444"), Cadreur.PARADIS, true));
		observable_liste.add(new BatchElement(new File("/home/blabla/444"), Cadreur.VIALATTE, false));
		observable_liste.add(new BatchElement(new File("/home/blabla/444"), Cadreur.MERCHADOU, true));

		root.setCenter(liste);
		
		HBox boutons = new HBox();
		boutons.setAlignment(Pos.CENTER);
		boutons.setSpacing(100);
		lancer = new Button("Lancer la liste");
		boutons.getChildren().add(lancer);
		
		lancer.setOnAction(a -> Lancer());
	
		root.setBottom(boutons);
	
		Scene scene = new Scene((Parent) root);
		stage.setScene(scene);
		
		stage.hide();

	
	}
	
	public void Lancer(){
		lancer.setText("Mettre en pause le traitement");
		lancer.setOnAction(a -> Pause());
		
	}
	
	public void Pause(){
		lancer.setText("Lancer le traitement");
		lancer.setOnAction(a -> Lancer());
	}

	public static ObservableList<BatchElement> getObservable_liste() {
		return observable_liste;
	}

	public static void setObservable_liste(ObservableList<BatchElement> observable_liste) {
		BatchList_controller.observable_liste = observable_liste;
	}
	
	

}
