package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AfficheurFlux implements Runnable {
	
	private final InputStream inputStream;
	private final String pre;
	private final boolean aff;
	private BufferedReader br;
	private final Process p;

    public AfficheurFlux(InputStream inputStream, String pre, boolean aff, Process p) {
        this.inputStream = inputStream;
        this.pre = pre;
        this.aff = aff; 
        this.p = p;
    }

    private BufferedReader getBufferedReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    @Override
    public void run() {
        br = getBufferedReader(inputStream);
        String ligne = "";
        try {

            while (br != null && ! (ligne = br.readLine()).contains("muxing overhead:")) {
            	if (aff){
            		System.out.println(pre + ligne);
            	}    
            }
            
            System.out.println("___ Derniere ligne " + pre + ligne);
            close();
            if (p != null){
            	p.destroy();
            }
            
            
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void close() {
    	try {
			br.close();
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

}
