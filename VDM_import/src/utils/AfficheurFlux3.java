package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AfficheurFlux3 implements Runnable {
	
	private final InputStream inputStream;
	private final String pre;
	private final boolean aff;
	private final Process p;
	private BufferedReader br;
	
	private boolean running;

    public AfficheurFlux3(InputStream inputStream, String pre, boolean aff, Process p) {
        this.inputStream = inputStream;
        this.pre = pre;
        this.aff = aff; 
        this.p = p;
        this.running = true;
    }

    private BufferedReader getBufferedReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    @Override
    public void run() {
        br = getBufferedReader(inputStream);
        String ligne = "";
        try {
            while (running) {
            	ligne = br.readLine();
            	if (aff){
            		System.out.println(pre + ligne);
            	}    
            }
            
            close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
    	
    	running = false;
    	try {
			br.close();
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

}
