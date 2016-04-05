package models;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

public class Rush extends File{
	
	private Instant debut;
	private Duration duree;

	public Rush(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
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
	
	
	

}
