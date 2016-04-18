package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import models.Cadreur;
import models.imports.ModeleImport;
import utils.Import;
import utils.MediaInfo;
import utils.Messages;
import utils.ScreenShot;
import utils.Walk;

public class VDM_import_controller implements Initializable{
	@FXML
	private Button dossier_source_button;
	@FXML
	private ChoiceBox<Cadreur> cadreur_choicebox;
	@FXML
	private ChoiceBox<String> extension_choicebox;
	@FXML
	private ChoiceBox<Path> sample_choicebox;
	@FXML
	private CheckBox deint_checkbox;
	@FXML
	private ImageView imageview;
	@FXML
	private Slider mediaview_slider;
	@FXML
	private VBox resume_vbox;
	@FXML
	private Button importer_button;
	
	private Image im;
	private File repPreview;
	private ObservableList<Path> liste_sample;
	
	private ModeleImport modele_import;

    protected File chooseRepLec(String s){
		
		Stage newStage = new Stage();
		
		DirectoryChooser dirChooser = new DirectoryChooser();
		
        if(repPreview != null){
        	dirChooser.setInitialDirectory(repPreview.getParentFile());
    	}
        else {
        	//dirChooser.setInitialDirectory(new File("/mnt/nfs2"));
        	//dirChooser.setInitialDirectory(new File("/mnt/2015_cartes"));
        	dirChooser.setInitialDirectory(new File("/mnt"));
        }
				
		dirChooser.setTitle(s);
		File selectedDir = dirChooser.showDialog(newStage);
		 if (selectedDir != null) {
			 return selectedDir;
		 }
		 else {
			 return (File) null;
		 }
		
	}
    
    protected void select_dossier(){
    	
    	repPreview = chooseRepLec("Répertoire sources");
    	dossier_source_button.setText(repPreview.toString());
    	try {
			peupler_samples();
		}
		catch (NullPointerException npe){
			
		}
    }
    
    protected void peupler_samples(){
    	
    	System.out.println("peupler liste des samples");
    	
    	Messages.setCadreur(cadreur_choicebox.getValue());
    	
    	try {
    		liste_sample.clear();
    		liste_sample = Walk.walk(repPreview.toPath(), cadreur_choicebox.getValue());
			sample_choicebox.setItems(liste_sample);
			sample_choicebox.getSelectionModel().select(0);
			affichePreview(100);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    protected void affichePreview(int frame){
    	ScreenShot.ffmpeg_screenshot(sample_choicebox.getValue(), frame);
        File file = new File("/mnt/ramdisk/output.jpg");
		try {
			im = new Image(file.toURI().toURL().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		imageview.setImage(im);
    }
    
    protected void afficherResume(){
    	
    	MediaInfo.getInfos(sample_choicebox.getValue(), resume_vbox);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		liste_sample = FXCollections.observableArrayList();
		
		dossier_source_button.setOnAction(a -> select_dossier());
		
		cadreur_choicebox.setItems(FXCollections.observableArrayList(Cadreur.values()));
		cadreur_choicebox.setOnAction(a -> {
			deint_checkbox.setSelected(cadreur_choicebox.getValue().isDeint());
			extension_choicebox.getSelectionModel().select(cadreur_choicebox.getValue().getExtension());
			Class modele = cadreur_choicebox.getValue().getModele_import();
			
			try {
				modele.newInstance();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		extension_choicebox.setItems(Cadreur.getExtensions());
		extension_choicebox.setOnAction(a -> {
			try {
				peupler_samples();
			}
			catch (NullPointerException npe){
				
			}
		});
		
		sample_choicebox.setOnAction(a -> {
            affichePreview(100);
            afficherResume();
		});
		
		resume_vbox.setPrefWidth(700);
		
		mediaview_slider.setMax(1000);
		mediaview_slider.setSnapToTicks(true);
		mediaview_slider.setMinorTickCount(1000);
		mediaview_slider.setOnMouseReleased(a -> affichePreview(Integer.parseInt(Math.round((double)mediaview_slider.getValue()) + "")));
//		mediaview_slider.valueProperty().addListener(new ChangeListener<Number>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//				affichePreview(Integer.parseInt(Math.round((double)newValue) + ""));
//			}
//		});
		
		importer_button.setOnAction(a -> Import.importer(repPreview, cadreur_choicebox.getValue()));
		
	
	}

}
