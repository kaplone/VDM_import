package utils;

import java.nio.file.Path;
import java.util.List;

import models.Cadreur;
import models.Rush;

public class Messages {
	
	private static String timestamp;
	private static String duration;
	private static Cadreur cadreur;
	private static List<Rush> listeDesPlans;
	private static List<Rush> listeDesRushs;
	private static String plan;
	private static int ecart_min;
	private static Path homeFolder;
	
	
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
		System.out.println("(get) liste_des_plans : " + Messages.listeDesPlans);
		return Messages.listeDesPlans;
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
	public static int getEcart_min() {
		return ecart_min;
	}
	public static void setEcart_min(int ecart_min) {
		Messages.ecart_min = ecart_min;
	}
	public static List<Rush> getListeDesRushs() {
		return listeDesRushs;
	}
	public static void setListeDesRushs(List<Rush> listeDesRushs) {
		Messages.listeDesRushs = listeDesRushs;
	}
	public static Path getHomeFolder() {
		return homeFolder;
	}
	public static void setHomeFolder(Path homeFolder) {
		Messages.homeFolder = homeFolder;
	}
	
	
}
