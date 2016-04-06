package utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.stage.Stage;

import models.Rush;

public class Chart {
	
	static Stage stage;
	
	final static Axis<String> xAxis = new CategoryAxis();
	final static InstantAxis instantAxis= new InstantAxis();
	final static DateAxis dateAxis= new DateAxis();
    final static NumberAxis yAxis = new NumberAxis();
    final static BarChart<String, Number> bc = new BarChart<>(xAxis,yAxis);
    final static LineChart<Date, Number> lineChart = new LineChart<>(dateAxis, yAxis);
	
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
		
		stage = new Stage();
		
		stage.setTitle("Bar Chart Sample");

	    dateAxis.setLabel("Heure");       
	    yAxis.setLabel("Durée");
	    
	    ObservableList<XYChart.Series<Date, Number>> series = FXCollections.observableArrayList();
	    ObservableList<XYChart.Data<Date, Number>> series1Data = FXCollections.observableArrayList();
	    
        times.stream().forEach(a -> {
    	    Date ldt = Date.from(a.getDebut());
    	    series1Data.add(new Data<Date, Number>(ldt, a.getDuree().getSeconds())); 
        });
        
        System.out.println("series.add");
        series.add(new XYChart.Series<>("Series1", series1Data));
        
        System.out.println("new scene");
        Scene scene  = new Scene(lineChart,800,600);
        
        System.out.println("lineChart addAll");
        lineChart.getData().addAll(series);
		//lineChart.setTitle("Bilan tournage");
		
		System.out.println("setScene");
        stage.setScene(scene);
        
        System.out.println("show");
        stage.show();
	}

}
