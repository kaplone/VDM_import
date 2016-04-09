package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Cadreur;
import models.Rush;


public class Walk {
	
	private static String extension;
	private static List<Rush> list;
	private static ObservableList<Rush>  liste_rush;
	private static ObservableList<Path>  liste_path;
	
	public static ObservableList<Path> walk(Path homeFolder, Cadreur cadreur) throws FileNotFoundException {
		
		list = new ArrayList<>();
		liste_rush = FXCollections.observableArrayList();
		liste_path = FXCollections.observableArrayList();
		
		extension = cadreur.getExtension();

		FileVisitor<Path> fileVisitor = new FileSizeVisitor(new Long(50));
		try {
			Files.walkFileTree(homeFolder, fileVisitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		list.stream()
            .sorted((e1, e2) -> Long.compare(e1.getDebutLong(),
            		                          e2.getDebutLong()))
            .forEach(e -> {
            	 liste_rush.add(e);
            	 liste_path.add(e.toPath());
             });
		
		liste_rush.stream()
		     .forEach(a-> { 
		    	 TimeStamp.plage(a);
		     });
		
		Chart.bilan(homeFolder.getFileName().toString(), Messages.getCadreur(), liste_rush);

		return liste_path;
		
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
			
			if(path.toString().toUpperCase().endsWith(extension)){
				
				System.out.println("walk trouvé " + path);
				
				Rush r = MediaInfo.getTimeStamp(path);
				
//				Rush r = new Rush(path.toString());
//				r.setDebut(MediaInfo.getTimeStamp(path));
//				r.setDuree(MediaInfo.getDuree());
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
