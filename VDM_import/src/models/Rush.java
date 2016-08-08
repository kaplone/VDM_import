package models;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import utils.Messages;

public class Rush extends File{
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	private Instant debut;
	private Duration duree;
	private String debut_str;
	private String duree_str;
	private List<Rush> chunks;
	
	private static int compteur = 0;

	public Rush(String pathname) {
		super(pathname);
		this.duree = Duration.ZERO;
		this.chunks = new ArrayList<>();
	}
	
	public Rush(String pathname, List<Rush> chunks) {
		this(pathname);
		this.chunks = chunks;
		this.debut = chunks.get(0).getDebut();
		for(int i= 0; i < chunks.size(); i++){
			duree = duree.plus(chunks.get(i).getDuree());
		}
		
	}
	
	public Rush getCopie(){
		
		Rush copie = new Rush(this.getPath());
		
		copie.addChunk(copie);
		copie.setDebut(this.getDebut());
		copie.setDuree(this.getDuree());
		
		return copie;
		
	}
	
	public void add(Rush ajout){
		chunks.add(ajout);
		duree = duree.plus(ajout.getDuree());
	}
	
	public void addChunk(Rush ajout){
		if (this.chunks.isEmpty() || ! this.chunks.contains(this)){
			this.chunks.add(this);
		}
	}

	public Instant getDebut() {
		return debut;
	}

	public void setDebut(Instant debut) {
		this.debut = debut;
	}
	
	public void setDebut(long debut) {
		this.debut = Instant.ofEpochSecond(debut);
	}
	
	public long getDebutLong() {
		return debut.getEpochSecond();
	}

	public Duration getDuree() {
		return duree;
	}

	public void setDuree(Duration duree) {
		this.duree = duree;
	}

	public List<Rush> getChunks() {
		return chunks;
	}

	public void setChunks(List<Rush> chunks) {
		this.chunks = chunks;
	}

	public String getDebut_str() {
		return debut_str;
	}

	public void setDebut_str(String debut_str) {
		this.debut_str = debut_str;
	}

	public String getDuree_str() {
		return duree_str;
	}

	public void setDuree_str(String duree_str) {
		this.duree_str = duree_str;
	}
	
	public String getRename(){
		
		String suite = this.getPath().split(String.format("%s/", Messages.getPlan()))[1];
		
		String extented_name = "";
		
		if (suite.contains("/PRIVATE/AVCHD/BDMV/STREAM/")){
			extented_name = String.format("%s_%s", suite.split("/PRIVATE/AVCHD/BDMV/STREAM/")[0].replace("/", "_"), this.getName());
		}
		else if (suite.contains("/AVCHD/BDMV/STREAM/")){
			extented_name = String.format("%s_%s", suite.split("/AVCHD/BDMV/STREAM/")[0].replace("/", "_"), this.getName());
		}
		else {
			extented_name = String.format("%05d_%s", compteur ++, this.getName());
		}
		
		return extented_name;
	}
	
	
}
