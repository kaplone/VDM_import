package application;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import models.BatchElement;
import models.Cadreur;
import models.Rush;
import utils.Import_java;
import utils.Import_python;

public class BatchList_controller implements Initializable{
	
	private Stage stage;
	private static ObservableList<BatchElement> observable_liste_1;
	private static ObservableList<BatchElement> observable_liste_2;
	private TableView<BatchElement> liste_1;
	private TableView<BatchElement> liste_2;
	
	private VBox vbox;
	private Label label_1;
	private Label label_2;
	
	private Button lancer;
	
	private Thread import_thread;
	private Service import_runnable;
	
	public void afficher(){
		stage.show();
	}
	
	public void ajouter(File dossier, String modele, Cadreur cadreur, boolean deint, List<Rush> listeDesPlans){
		
		String modele_import = "";
		String multi = "Non";
		
		if (modele != null){
			switch (modele){
			
			case "python" : modele_import = cadreur.getPython_file();
			                multi = "Non";
			                break;
			case "java_1" : multi = "Non";
			                modele_import = cadreur.getModele_import().getSimpleName();
                            break;
			case "java_n" : multi = "Oui";
				            modele_import = cadreur.getModele_import().getSimpleName();
			                break;
			}
		}
		
		observable_liste_1.add(new BatchElement(dossier, modele, multi, cadreur, deint, listeDesPlans));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		stage = new Stage();
		BorderPane root = new BorderPane();
	
		stage.setHeight(800);
		stage.setWidth(1200);
		
		vbox = new VBox();
		
		label_1 = new Label("Imports en cours ...");
		label_2 = new Label("Imports terminés");
		
		liste_1 = new TableView<>();
		TableColumn<BatchElement, String> dossier_1 = new TableColumn<>("Dossier");
		TableColumn<BatchElement, String> cadreur_1 = new TableColumn<>("Cadreur");
		TableColumn<BatchElement, String> methode_1 = new TableColumn<>("Méthode");
		TableColumn<BatchElement, String> multi_thread_1 = new TableColumn<>("Multi-thread");
		TableColumn<BatchElement, ChoiceBox<String>> deint_1 = new TableColumn<>("Déint");
		TableColumn<BatchElement, String> status_1 = new TableColumn<>("Status");
		TableColumn<BatchElement, Button> delete_1 = new TableColumn<>("Supprimer");
		liste_1.getColumns().addAll(dossier_1, cadreur_1, methode_1, multi_thread_1, deint_1, status_1, delete_1);
		
		observable_liste_1 = FXCollections.observableArrayList();
		
		liste_1.setItems(observable_liste_1);

		dossier_1.setCellValueFactory(
			    new PropertyValueFactory<>("dossier")
			);
		
		cadreur_1.setCellValueFactory(
			    new PropertyValueFactory<>("cadreur")
			);
		
		methode_1.setCellValueFactory(
			    new PropertyValueFactory<>("methode")
			);
		
		multi_thread_1.setCellValueFactory(
			    new PropertyValueFactory<>("multi")
			);
		
		deint_1.setCellValueFactory(
			    new PropertyValueFactory<>("deint")
			);
		
		status_1.setCellValueFactory(
			    new PropertyValueFactory<>("status")
			);
		
		delete_1.setCellValueFactory(
			    new PropertyValueFactory<>("supprimer")
			);
		
		liste_2 = new TableView<>();
		TableColumn<BatchElement, String> dossier_2 = new TableColumn<>("Dossier");
		TableColumn<BatchElement, String> cadreur_2 = new TableColumn<>("Cadreur");
		TableColumn<BatchElement, String> methode_2 = new TableColumn<>("Méthode");
		TableColumn<BatchElement, String> multi_thread_2 = new TableColumn<>("Multi-thread");
		TableColumn<BatchElement, ChoiceBox<String>> deint_2 = new TableColumn<>("Déint");
		TableColumn<BatchElement, String> status_2 = new TableColumn<>("Status");
		liste_2.getColumns().addAll(dossier_2, cadreur_2, methode_2, multi_thread_2, deint_2, status_2);
		
		observable_liste_2 = FXCollections.observableArrayList();
		
		liste_2.setItems(observable_liste_2);

		dossier_2.setCellValueFactory(
			    new PropertyValueFactory<>("dossier")
			);
		
		cadreur_2.setCellValueFactory(
			    new PropertyValueFactory<>("cadreur")
			);
		
		methode_2.setCellValueFactory(
			    new PropertyValueFactory<>("methode")
			);
		
		multi_thread_2.setCellValueFactory(
			    new PropertyValueFactory<>("multi")
			);
		
		deint_2.setCellValueFactory(
			    new PropertyValueFactory<>("deint")
			);
		
		status_2.setCellValueFactory(
			    new PropertyValueFactory<>("status_")
			);
		
		HBox boutons = new HBox();
		boutons.setAlignment(Pos.CENTER);
		boutons.setSpacing(100);
		boutons.setPadding(new Insets(20, 0, 50, 0));
		lancer = new Button("Lancer la liste");
		boutons.getChildren().add(lancer);
		
		lancer.setOnAction(a -> lancer());
		
		vbox.getChildren().addAll(label_1, liste_1, boutons, label_2, liste_2);

		root.setCenter(vbox);
	
		Scene scene = new Scene((Parent) root);
		stage.setScene(scene);
		
		stage.hide();

	
	}
	
	public void lancer(){
		
		lancer.setText("Mettre en pause le traitement");
		
		import_runnable = new Service() {
			
			@Override
	        protected Task<String> createTask() {
	            return new Task<String>() {
	                @Override
	                protected String call(){
	                	
	                	while (observable_liste_1.size() > 0){
	                		
	                		lancer.setOnAction(a -> pause());
	    					
	    					System.out.println("lancement de : " + observable_liste_1.get(0).getDossier());
	    					System.out.println("pour le switch : " + observable_liste_1.get(0).getMessage());
	    					
	    					
	    					observable_liste_1.get(0).setStatus("Import en cours ...");
	    					
	    					switch (observable_liste_1.get(0).getMessage()){
	    					
	    					case "python" : Import_python.importer(observable_liste_1.get(0).getDossier(),
	    							                               observable_liste_1.get(0).getCadreur());
	    					                break;
	    					case "java_1" : Import_java.importer(observable_liste_1.get(0).getDossier(),
	    							                             observable_liste_1.get(0).getCadreur(),
	    							                             observable_liste_1.get(0).isMulti()); // multi != deint
	    					                break;
	    					case "java_n" : Import_java.importer(observable_liste_1.get(0).getDossier(),
	    							                             observable_liste_1.get(0).getCadreur(),
	    							                             observable_liste_1.get(0).isMulti()); // multi != deint
	    					                break;
	    					}

	    					observable_liste_2.add(observable_liste_1.get(0));
	    					observable_liste_1.remove(0);
	    				}
	    				
	                	Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								lancer.setText("Lancer le traitement");
							}
						});
	    				
	    				lancer.setOnAction(a -> lancer());
						return "OK";
	    			};		
	             };
	                	
	         }
				
		};
		
		import_runnable.start();

	}
	
	public void pause(){
		lancer.setText("Lancer le traitement");
		import_runnable.cancel();
		lancer.setOnAction(a -> relancer());
	}
	
	public void relancer(){
		lancer.setText("Relancer le traitement");
		import_runnable.start();
		lancer.setOnAction(a -> pause());
	}

	public static ObservableList<BatchElement> getObservable_liste_1() {
		return observable_liste_1;
	}

	public static void setObservable_liste_1(ObservableList<BatchElement> observable_liste) {
		BatchList_controller.observable_liste_1 = observable_liste;
	}
	
	public static ObservableList<BatchElement> getObservable_liste_2() {
		return observable_liste_2;
	}

	public static void setObservable_liste_2(ObservableList<BatchElement> observable_liste) {
		BatchList_controller.observable_liste_2 = observable_liste;
	}

}
