package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 * Classe appalée pour charger le fichier fxml.
 * 
 */
public class JfxUtils {
	
	/**
	 * Charge le fichier fxml passé en parametre et retourne le conteneur graphique (ou l'élément) correspondant.
	 * @param fxml un fichier 
	 * @return un noeud graphique racine
	 */
	public static Node loadFxml(String fxml) {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(JfxUtils.class.getResource(fxml));
            Node root = (Node) loader.load(Main.class.getResource(fxml).openStream());
            return root;
        } catch (IOException e) {
            throw new IllegalStateException("cannot load FXML screen", e);
        }
    }
}
