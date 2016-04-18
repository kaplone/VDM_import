package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AfficheurFlux implements Runnable {
	
	private final InputStream inputStream;
	private String pre;
	private boolean aff;

    public AfficheurFlux(InputStream inputStream, String pre, boolean aff) {
        this.inputStream = inputStream;
        this.pre = pre;
        this.aff = aff; 
    }

    private BufferedReader getBufferedReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    @Override
    public void run() {
        BufferedReader br = getBufferedReader(inputStream);
        String ligne = "";
        try {
            while ((ligne = br.readLine()) != null) {
            	if (aff){
            		System.out.println(pre + ligne);
            	}
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
