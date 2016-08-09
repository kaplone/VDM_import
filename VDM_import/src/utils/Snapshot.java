package utils;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;

public class Snapshot {
	
	public static void saveAsPng(Scene scene, String plan, String cadreur) {

        WritableImage image = new WritableImage(1920, 500);
        image = scene.snapshot(image);

        File file = new File(String.format("/mnt/F/VDM 2016/rapports_CADREURS/%s_%s_%dms.png", plan, cadreur, Messages.getEcart_min()));

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
