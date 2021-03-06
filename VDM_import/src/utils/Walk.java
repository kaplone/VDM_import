package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.GuardedObject;
import java.util.ArrayList;
import java.util.List;

import application.Chart_controller;
import application.VDM_import_controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import models.Cadreur;
import models.Rush;


public class Walk {
	
	private static String extension;
	private static final List<Rush> list = new ArrayList<>();
	private static ObservableList<Rush>  liste_rush;
	private static List<Rush>  liste_plans;
	private static ObservableList<Path>  liste_path;
	
	private static Cadreur cadreur;
	
	public static ObservableList<Path> walk(Path homeFolder, Cadreur cadreur_) throws FileNotFoundException {
		
		Messages.setHomeFolder(homeFolder);
		
		liste_rush = FXCollections.observableArrayList();
		liste_path = FXCollections.observableArrayList();
		liste_plans = new ArrayList<>();

		extension = cadreur_.getExtension();
		cadreur = cadreur_;
		
		list.clear();

		FileVisitor<Path> fileVisitor = new FileSizeVisitor(new Long(50));
		try {
			Files.walkFileTree(homeFolder, fileVisitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//list.stream().forEach(a -> System.out.println("== SORTIE DE WALK == " + a));
//		
//		liste_path.clear();
//		liste_rush.clear();
		
		list.stream()
            .sorted((e1, e2) -> Long.compare(e1.getDebutLong(),
            		                          e2.getDebutLong()))
            .forEach(e -> {
            	 liste_rush.add(e);
            	 liste_path.add(e.toPath());
             });
		
		//liste_path.stream().forEach(a -> System.out.println("== SORTIE DE SORT() == " + a));
		
		liste_rush.stream()
		     .forEach(a-> { 
		    	 TimeStamp.plage(a);
		     });

		dessine_chart(homeFolder);
        
		//liste_plans.clear();
		liste_plans = RushToPlan.rushs_to_plan(liste_rush, Messages.getEcart_min());
		Messages.setListeDesPlans(liste_plans);
		//liste_plans.stream().forEach(a -> System.out.println("== SORTIE DE RushToPlan.rushs_to_plan() == " + a));
		
		Messages.setListeDesPlans(liste_plans);

		return liste_path;
		
	}
  
	public static void dessine_chart(Path homeFolder){
		
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Chart_controller chart = new Chart_controller();
			    chart.initialize(VDM_import_controller.getLocation(), VDM_import_controller.getResources());
			    
				Scene chart_scene = chart.bilan(homeFolder.getFileName().toString(), Messages.getCadreur(), liste_rush);
		        Snapshot.saveAsPng(chart_scene, Messages.getPlan(), cadreur.toString());
			}
		 });
	}
	
	static class FileSizeVisitor implements FileVisitor<Path> {

		private Long size;

		public FileSizeVisitor(Long size) {
			this.size = size;
		}

		/**
		 * This is triggered before visiting a directory.
		 */
		@Override
		public FileVisitResult preVisitDirectory(Path path,
				BasicFileAttributes attrs) throws IOException {
			
			if(extension.equals("MXF") && (path.endsWith("AUDIO")
                    || path.endsWith("CLIP")
                    || path.endsWith("ICON")
                    || path.endsWith("PROXY")
                    || path.endsWith("VOICE")))
			{
			    return FileVisitResult.SKIP_SUBTREE;	
			}
			
			return FileVisitResult.CONTINUE;
		}

		/**
		 * This is triggered when we visit a file.
		 */
		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
				throws IOException {
			
			if(path.toString().toUpperCase().endsWith(extension) && ! path.toString().startsWith("._")){
				
				System.out.println("walk trouvé " + path);
				
				Rush r = MediaInfo.getTimeStamp(path);

				list.add(r);
				
			}
			return FileVisitResult.CONTINUE;
		}

		/**
		 * This is triggered if we cannot visit a Path We log the fact we cannot
		 * visit a specified path .
		 */
		@Override
		public FileVisitResult visitFileFailed(Path path, IOException exc)
				throws IOException {
			// We print the error
			System.err.println("ERROR: Cannot visit path: " + path);
			// We continue the folder walk
			return FileVisitResult.CONTINUE;
		}

		/**
		 * This is triggered after we finish visiting a specified folder.
		 */
		@Override
		public FileVisitResult postVisitDirectory(Path path, IOException exc)
				throws IOException {
			// We continue the folder walk
			return FileVisitResult.CONTINUE;
		}
		
		void ajouter (Path p){

	    	
	    }

	}

}
