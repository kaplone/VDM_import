package utils;

import models.Cadreur;

public class Messages {
	
	private static String timestamp;
	private static String duration;
	private static Cadreur cadreur;
	
	
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
}
