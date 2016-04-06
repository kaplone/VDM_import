package utils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    
    static Duration duree_partie;
	
	public static void bilan_string (String plan, String cadreur, List<Rush> times){
		
		stage = new Stage();
		
		stage.setTitle("Bar Chart Sample");
        
		bc.setTitle("Bilan tournage");
	    xAxis.setLabel("Heure");       
	    yAxis.setLabel("Durée");
 
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName(plan + " (" + cadreur + ")");     
        times.stream().forEach(a -> {
        	LocalDateTime ldt = LocalDateTime.ofInstant(a.getDebut(), ZoneId.systemDefault());
        	series1.getData().add(new XYChart.Data<>(ldt.format(DateTimeFormatter.ofPattern("HH:mm:ss")), a.getDuree().getSeconds())); 
        });
           

        
        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(series1);
        stage.setScene(scene);
        stage.show();
	}
	
//    public static void bilan_heures (String plan, String cadreur, List<Rush> times){
//		
//		stage = new Stage();
//		
//		stage.setTitle("Bar Chart Sample");
//        
//		bc.setTitle("Bilan tournage");
//	    instantAxis.setLabel("Heure");       
//	    yAxis.setLabel("Durée");
// 
//        XYChart.Series<Instant, Number> series1 = new XYChart.Series<>();
//        series1.setName(plan + " (" + cadreur + ")");     
//        times.stream().forEach(a -> {
//        	LocalDateTime ldt = LocalDateTime.ofInstant(a.getDebut(), ZoneId.systemDefault());
//        	series1.getData().add(new XYChart.Data<>(ldt.format(DateTimeFormatter.ofPattern("HH:mm:ss")), a.getDuree().getSeconds())); 
//        });
//           
//
//        
//        Scene scene  = new Scene(bc,800,600);
//        bc_instant.getData().addAll(series1);
//        stage.setScene(scene);
//        stage.show();
//	}
    
    public static void bilan (String plan, String cadreur, List<Rush> times){
    	
    	index = 0;
		
		stage = new Stage();
		
		stage.setTitle("Bar Chart Sample");

	    dateAxis.setLabel("Heure");       
	    yAxis.setLabel("Durée");
	    
	    precedent = null;
	    
	    ObservableList<XYChart.Series<Date, Number>> series = FXCollections.observableArrayList();
	    
        times.stream().forEach(a -> {
        	
        	if (precedent != null){
        		if (a.getDuree().toMinutes() > 5 && Math.abs(Duration.between(precedent.getDebut().plus(precedent.getDuree()), a.getDebut()).getSeconds()) < 2
        			|| a.getDuree().toMinutes() < 5){
        			Date ldt = Date.from(a.getDebut().minus(Duration.ofHours(2)));
            	    series1Data.add(new Data<Date, Number>(ldt, a.getDuree().getSeconds())); 
            	    duree_partie.plus(a.getDuree());
        		}
        		else {        			
        	        series.add(new XYChart.Series<>(Parties.values()[index++].toString() + affDuree(duree_partie), series1Data));
        	        
        			series1Data = FXCollections.observableArrayList();
        			duree_partie = Duration.ZERO;
            		duree_partie.plus(a.getDuree());
            		
        			Date ldt = Date.from(a.getDebut().minus(Duration.ofHours(2)));
            	    series1Data.add(new Data<Date, Number>(ldt, a.getDuree().getSeconds()));
            	    duree_partie.plus(a.getDuree());
        		}
        		
        	}
        	else{
        		series1Data = FXCollections.observableArrayList();
        		duree_partie = Duration.ZERO;
        		duree_partie.plus(a.getDuree());
        	}
        	precedent = a;
        	
        	
    	    
        });
        
        series.add(new XYChart.Series<>(Parties.values()[index++].toString() + affDuree(duree_partie), series1Data));

        
        
        System.out.println("lineChart addAll");
        lineChart.getData().addAll(series);
		//lineChart.setTitle("Bilan tournage");
        scene  = new Scene(lineChart,1920,500);

        stage.setScene(scene);

        stage.show();
        stage.toFront();
        
        //saveAsPng(plan, cadreur);
        //TODO n'affiche pas les graduations dans l'image exportée
	}
    
    public static void saveAsPng(String plan, String cadreur) {
        WritableImage image = scene.snapshot(null);

        // TODO: probably use a file chooser here
        File file = new File(String.format("/home/autor/Desktop/%s_%s.png", plan, cadreur));

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            // TODO: handle exception here
        }
    }
    
    public static String affDuree(Duration duree){
    	
    	long hours = duree.toHours();
    	long minutes = duree.minusHours(hours).toMinutes();
    	long secondes = duree.minusHours(hours).minusMinutes(minutes).getSeconds();
    		
    	return String.format(" (%2dh %2dm %2ds)", hours, minutes, secondes);
    }

}
