package application;

import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.stage.Stage;
import models.Cadreur;
import models.Parties;
import models.Rush;
import utils.DateAxis;

public class Chart_controller implements Initializable{
	
	private Stage stage;
	private Scene scene;

	private DateAxis dateAxis= new DateAxis();
	private NumberAxis yAxis = new NumberAxis();
	private LineChart<Date, Number> lineChart = new LineChart<>(dateAxis, yAxis);
    
	private Rush precedent;
	private ObservableList<XYChart.Data<Date, Number>> series1Data;
	private int index;
    
	private String extension ;
	private int duree_coupure;
    
	private boolean partie;
    
	private Duration duree_partie;
	
    public Scene bilan (String plan, Cadreur cadreur, List<Rush> times){
    	
    	index = 0;
    	
    	precedent = null;
	    partie = false;

	    extension = cadreur.getExtension();
	    if (extension.equals("MXF")){
	    	duree_coupure = 294;
	    	
	    }
	    else {
	    	duree_coupure = 300;
	    }

	    ObservableList<XYChart.Series<Date, Number>> series = FXCollections.observableArrayList();
	    
        times.stream().forEach(a -> {
        	
        	if (precedent != null){
        		
        		// cas général des bouts consécutifs dans une partie
        		if (partie && Math.abs(Duration.between(precedent.getDebut().plus(precedent.getDuree()), a.getDebut()).toMillis()) < 1200){
        			
        			System.out.println("cas général des bouts consécutifs dans une partie");
        			
        			Date ldt = Date.from(a.getDebut().minus(Duration.ofHours(2)));
            	    series1Data.add(new Data<Date, Number>(ldt, a.getDuree().getSeconds())); 
            	    duree_partie = duree_partie.plus(a.getDuree());
            	    
            	    partie = true;
        		}
        		// cas bouts avant/apres entracte
        		// else if(partie && a.getDuree().getSeconds() > duree_coupure){
        		else if(partie){
        			
        			System.out.println(partie + ", " +a.getDuree() + "," + a.getDuree().getSeconds() + ", " + duree_coupure);
        			System.out.println("cas bouts avant/apres entracte");
        			
        			Date ldt = Date.from(precedent.getDebut().plus(precedent.getDuree()).minus(Duration.ofHours(2)));
            	    series1Data.add(new Data<Date, Number>(ldt, 0));	
            	  //flush partie terminée
                    series.add(new XYChart.Series<>(Parties.values()[index++].toString() + affDuree(duree_partie), series1Data));
        	        
        			series1Data = FXCollections.observableArrayList();
        			duree_partie = Duration.ZERO;
        			ldt = Date.from(a.getDebut().minus(Duration.ofHours(2)));
        			series1Data.add(new Data<Date, Number>(ldt, a.getDuree().getSeconds())); 
        			duree_partie = duree_partie.plus(a.getDuree());
        		}
        		// cas du premier bout partie apres bonus
                else if(a.getDuree().getSeconds() > duree_coupure){
                	
                	System.out.println(a.getDuree() + "," + a.getDuree().getSeconds() + ", " + duree_coupure);
                	System.out.println("cas du premier bout partie apres bonus");
                	
                	//flush des bonus
                	series.add(new XYChart.Series<>(Parties.values()[index++].toString() + affDuree(duree_partie), series1Data));
                	
        			series1Data = FXCollections.observableArrayList();
        			duree_partie = Duration.ZERO;
        			Date ldt = Date.from(a.getDebut().minus(Duration.ofHours(2)));
        			series1Data.add(new Data<Date, Number>(ldt, a.getDuree().getSeconds())); 
        			duree_partie = duree_partie.plus(a.getDuree());
        			
        			partie = true;
        		}
        		//cas bouts non consécutifs (bonus)
        		else if(a.getDuree().getSeconds() < duree_coupure){
        			
        			System.out.println("cas bouts non consécutifs (bonus)");
        			
        			if (partie){
        				//flush partie terminée
                        series.add(new XYChart.Series<>(Parties.values()[index++].toString() + affDuree(duree_partie), series1Data));
            	        
            			series1Data = FXCollections.observableArrayList();
            			duree_partie = Duration.ZERO;
        			}
        			
        			Date ldt = Date.from(a.getDebut().minus(Duration.ofHours(2)));
            	    series1Data.add(new Data<Date, Number>(ldt, a.getDuree().getSeconds())); 
            	    duree_partie = duree_partie.plus(a.getDuree());
            	    
            	    partie = false;
        		}
        		// cas général (??)
        		else {    
        			
        			System.out.println("cas général");
        	        		
        			Date ldt = Date.from(a.getDebut().minus(Duration.ofHours(2)));
            	    series1Data.add(new Data<Date, Number>(ldt, a.getDuree().getSeconds()));
            	    duree_partie = duree_partie.plus(a.getDuree());
            	    
            	    partie = false;
            	    
        		}
        		
        	}
        	// initialisation
        	else{
        		series1Data = FXCollections.observableArrayList();
        		duree_partie = Duration.ZERO;
        		duree_partie = duree_partie.plus(a.getDuree());
        		partie = a.getDuree().getSeconds() > duree_coupure;
        	}
        	precedent = a;
        	
        	
    	    
        });
        
        Date ldt = Date.from(precedent.getDebut().plus(precedent.getDuree()).minus(Duration.ofHours(2)));
	    series1Data.add(new Data<Date, Number>(ldt, 0));
	  //flush dernière partie
        series.add(new XYChart.Series<>(Parties.values()[index++].toString() + affDuree(duree_partie), series1Data));
        
        lineChart.getData().clear();
        
        dateAxis= new DateAxis();
		yAxis = new NumberAxis();
		lineChart = new LineChart<>(dateAxis, yAxis);
        lineChart.getData().addAll(series);
		lineChart.setTitle(String.format("Bilan tournage %s (%s)", plan, cadreur));
		
		scene  = new Scene(lineChart,1920,500);

	    stage.setScene(scene);
        stage.show();
        stage.toFront();

		return scene;

	}
    
    public static String affDuree(Duration duree){	
    	
    	long hours = duree.toHours();
    	long minutes = duree.minusHours(hours).toMinutes();
    	long secondes = duree.minusHours(hours).minusMinutes(minutes).getSeconds();
    		
    	return String.format(" (%2dh %02dm %02ds)", hours, minutes, secondes);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		System.out.println("________________ INITIALISATION CHART _____________________");
		
        stage = new Stage();
		
		stage.setTitle("BILAN");

	    dateAxis.setLabel("Heure");       
	    yAxis.setLabel("Durée");

	}

}
