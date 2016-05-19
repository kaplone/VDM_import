package utils;

import java.util.List;

import models.Cadreur;
import models.Rush;

public class Messages {
	
	private static String timestamp;
	private static String duration;
	private static Cadreur cadreur;
	private static List<Rush> listeDesPlans;
	private static String plan;
	
	
	public static String getTimestamp() {
		return timestamp;
	}
	public static void setTimestamp(String timestamp) {
		Messages.timestamp = timestamp;
	}
	public static String getDuration() {
		return duration;
	}
	public static void setDuration(String duration) {
		Messages.duration = duration;
	}
	public static Cadreur getCadreur() {
		return cadreur;
	}
	public static void setCadreur(Cadreur cadreur) {
		Messages.cadreur = cadreur;
	}
	public static List<Rush> getListeDesPlans() {
		System.out.println("(get) liste_des_plans : " + listeDesPlans);
		return listeDesPlans;
	}
	public static void setListeDesPlans(List<Rush> listeDesPlans) {
		System.out.println("(set) liste_des_plans : " + listeDesPlans);
		Messages.listeDesPlans = listeDesPlans;
	}
	public static String getPlan() {
		return plan;
	}
	public static void setPlan(String plan) {
		Messages.plan = plan;
	}
	
	
}
