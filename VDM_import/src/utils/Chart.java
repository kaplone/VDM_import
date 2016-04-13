package utils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import models.Cadreur;
import models.Parties;
import models.Rush;

public class Chart {
	
	static Stage stage;
	static Scene scene;
	
	final static Axis<String> xAxis = new CategoryAxis();
	final static InstantAxis instantAxis= new InstantAxis();
	final static DateAxis dateAxis= new DateAxis();
    final static NumberAxis yAxis = new NumberAxis();
    final static BarChart<String, Number> bc = new BarChart<>(xAxis,yAxis);
    final static LineChart<Date, Number> lineChart = new LineChart<>(dateAxis, yAxis);
    
    static Rush precedent;
    static ObservableList<XYChart.Data<Date, Number>> series1Data;
    static int index;
    
    static String extension ;
    static int duree_coupure;
    static int duree_min;
    
    static boolean partie;
    
    static Duration duree_partie;
	
    public static void bilan (String plan, Cadreur cadreur, List<Rush> times){
    	
    	index = 0;
		
		stage = new Stage();
		
		stage.setTitle("Bar Chart Sample");

	    dateAxis.setLabel("Heure");       
	    yAxis.setLabel("Durée");
	    
	    precedent = null;
	    partie = false;
	    
	    duree_min = 90;
	    
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

        lineChart.getData().addAll(series);
		//lineChart.setTitle("Bilan tournage");
        
        scene  = new Scene(lineChart,1920,500);
        stage.setScene(scene);

        stage.show();
        stage.toFront();
        
        saveAsPng(plan, cadreur.toString(), scene);
      //TODO n'affiche pas les graduations dans l'image exportée
	}
    
    public static void saveAsPng(String plan, String cadreur, Scene sceneAsPng) {
        WritableImage image = sceneAsPng.snapshot(null);

        // TODO: probably use a file chooser here
//        File file = new File(String.format("/home/david/Desktop/bilans/%s_%s.png", plan, cadreur));
//
//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//        } catch (IOException e) {
//            // TODO: handle exception here
//        }
    }
    
    public static String affDuree(Duration duree){	
    	
    	long hours = duree.toHours();
    	long minutes = duree.minusHours(hours).toMinutes();
    	long secondes = duree.minusHours(hours).minusMinutes(minutes).getSeconds();
    		
    	return String.format(" (%2dh %02dm %02ds)", hours, minutes, secondes);
    }

}
