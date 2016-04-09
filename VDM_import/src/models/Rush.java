package models;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Rush extends File{
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	private Instant debut;
	private Duration duree;
	private List<Rush> chunks;

	public Rush(String pathname) {
		super(pathname);
		this.duree = Duration.ZERO;
	}
	
	public Rush(String pathname, List<Rush> chunks) {
		this(pathname);
		this.chunks = chunks;
		this.debut = chunks.get(0).getDebut();
		for(int i= 0; i < chunks.size(); i++){
			duree = duree.plus(chunks.get(i).getDuree());
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
	
}
