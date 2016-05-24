package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AfficheurFlux4 implements Runnable {
	
	private final InputStream inputStream;
	private final String pre;
	private final boolean aff;
	private BufferedReader br;
	private final Process p;
	private final AfficheurFlux3 err;

    public AfficheurFlux4(InputStream inputStream, String pre, boolean aff, Process p, AfficheurFlux3 err) {
        this.inputStream = inputStream;
        this.pre = pre;
        this.aff = aff; 
        this.p = p;
        this.err = err;
    }

    private BufferedReader getBufferedReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    @Override
    public void run() {
        br = getBufferedReader(inputStream);
        String ligne = "";
        try {

            while (! (ligne = br.readLine()).contains("muxing overhead: ")) {
            	if (aff){
            		System.out.println(pre + ligne);
            	}    
            }
            System.out.println("___ Derniere ligne " + pre + ligne);
            err.close();
            close();
            if (p != null){
            	p.destroy();
            }
            
        } catch (IOException e) {
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
