package utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import models.Rush;

public class RushToPlan {
	
	private static Rush precedent;
	
	private static Rush plan_courant;
	
	private static List<Rush> liste_des_plans;
	
	public static List<Rush> rushs_to_plan(ObservableList<Rush> liste_des_rushs){
		
		liste_des_plans = new ArrayList<>();
		
		liste_des_rushs.stream()
	        .forEach(a-> {
	        	
	        	System.out.println("rush = " + a);
	        	
	    	    if (precedent != null){
	    	    	joindre(precedent, a);
	    	    	precedent = a;
	    	    }
	    	    else {
	    	    	plan_courant = a.getCopie();
	    	    	precedent = a;
	    	    }
	        });
		liste_des_plans.add(plan_courant);
		
		return liste_des_plans;
	}
	
	public static void joindre (Rush r0, Rush r1){
		
		System.out.println("r0.getDebut() : " + r0.getDebut() + "r0.getDuree() : " + r0.getDuree() + " =  r0.getDebut().plus(r0.getDuree()) : " + r0.getDebut().plus(r0.getDuree()));
		System.out.println("r1.getDebut() : " + r1.getDebut());
		System.out.println("écart : " + Math.abs(Duration.between(r0.getDebut().plus(r0.getDuree()), r1.getDebut()).toMillis()));
		
		if (Math.abs(Duration.between(r0.getDebut().plus(r0.getDuree()), r1.getDebut()).toMillis()) < 1200){
			plan_courant.add(r1);
		}
		else {
			liste_des_plans.add(plan_courant);
			plan_courant = r1.getCopie();
		}
		
	}

}
