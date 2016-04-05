package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Walk {
	
	private static String extension;
	private static List<Path> list;
	private static ObservableList<Path>  liste;
	
	public static ObservableList<Path> walk(Path homeFolder, String extension_) throws FileNotFoundException {
		
		list = new ArrayList<>();
		liste = FXCollections.observableArrayList();
		
		extension = extension_;

		FileVisitor<Path> fileVisitor = new FileSizeVisitor(new Long(50));
		try {
			Files.walkFileTree(homeFolder, fileVisitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		list .stream()
             .sorted((e1, e2) -> Long.compare(MediaInfo.getTimeStamp(e1),
            		                             MediaInfo.getTimeStamp(e2)))
             .forEach(e -> liste.add(e));
		
		liste.stream()
		     .forEach(a-> {
		    	 System.out.println(a);
		    	 TimeStamp.plage(a);
		     });
				
				
		
		return liste;
		
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
			return FileVisitResult.CONTINUE;
		}

		/**
		 * This is triggered when we visit a file.
		 */
		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
				throws IOException {

			if(path.toString().toUpperCase().endsWith(extension)){
				
				list.add(path);
				
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
