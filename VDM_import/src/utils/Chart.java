package utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import models.Rush;

public class Chart {
	
	static Stage stage;
	
	final static Axis<String> xAxis = new CategoryAxis();
    final static NumberAxis yAxis = new NumberAxis();
    final static BarChart<String, Number> bc = 
        new BarChart<>(xAxis,yAxis);
	
	public static void bilan (String plan, String cadreur, List<Rush> times){
		
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

}
