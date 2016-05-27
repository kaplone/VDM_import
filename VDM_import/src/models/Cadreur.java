package models;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import models.imports.*;

public enum Cadreur {
	
	ALBARET     ("MXF", false, ModeleMXF_XDCAM.class,              "merging_MXF_pas_P2_06.py", 			  1200),
	AMOUROUX    ("M2T", true,  ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_deint_amouroux.py", 1200),
	ARMATOL     ("MP4", false, ModeleMP4_XDCAM.class ,             "merging_MP4_06_SD_prog.py", 	      1200),
	BATMAN      ("MXF", true,  null              ,                 "merging_MXF_08_SD.py", 			 	  1200),
	BAUDON      ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py", 		  1200),
	BENSEMHOUN  ("MP4", true,  ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_deint.py", 		  1200),
	BIGEAULT    ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py", 		  1200),
	BROCCHI     ("???", false, null, null, 																  1200),
	CRUMEYROLLE ("???", false, null, null, 																  1200),
	DE_KERDREL  ("MXF", true,  ModeleMXF_P2.class,                 "merging_MXF_08_SD.py", 				  1200),
	DUCOING     ("MOV", false, ModeleMOV_ProRes.class, "", 			 									  1200),
	GIRARD      ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py", 		  1200),
	GAUTIER     ("M2T", false, ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_prog_rico.py",	  1200),
	GEOFFRIAU   ("???", false, null, null, 																  1200),
	GORWA       ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py", 		  1200),
	GUILLAUME   ("???", false, null, null, 																  1200),
	HUET        ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py", 		  1200),
	HUGUES      ("MOV", false, ModeleMOV_ProRes.class, "", 												  1200),
	LEUX        ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py", 		  1200),
	LE_YOUDEC   ("MXF", false, ModeleMXF_XDCAM.class,              "merging_MXF_pas_P2_06.py", 			  1200),
	LOPACKI     ("MTS", true,  ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_212_deint.py", 	  1200),
	MAS         ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py", 		  3100),
	MERCHADOU   ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py", 		  1200),
	MERCIER     ("M2T", false, ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_prog_rico.py", 	  1200),
	MICHARD     ("MP4", true,  ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_deint.py", 		  1200),
	MISSANA     ("M2T", true,  ModeleM2T_mencoder_nodesplit.class, "merging_M2T_05_SD_NO_DESPLIT_deint.py",	  1200),
	MOREL       ("MP4", true,  ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_deint.py", 		  1200),
	PARADIS     ("M2T", true,  ModeleM2T_mencoder_nodesplit.class, "merging_M2T_05_SD_NO_DESPLIT_deint.py",	  1200),
	PEIGNARD    ("MXF", true,  null,                               "merging_MXF_08_SD.py", 				  1200),
	PICHON      ("M2T", true,  ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_deint_amouroux.py", 1200),
	QUESADA     ("M2T", true,  ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_deint_amouroux.py", 1200),
	RICO        ("M2T", false, ModeleM2T_mencoder_desplit.class,  "merging_M2T_05_SD_prog_rico.py", 	  1200),
	RONCHAUD    ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py", 		  1200),
	RUGGIU      ("MOV", false, ModeleMOV_XDCAM.class,              "merging_MOV_05_SD_prog.py", 		  1200),
	VARLET      ("MXF", true,  null,                               "merging_MXF_08_SD.py", 				  1200),
	VERNIER     ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py", 		  1200),
	VIALATTE    ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py", 		  1200)
	;
	
	private String extension;
	private boolean deint;
	private Class modele_import;
	private int ecart_minimum;
	
	private String currentUsersHomeDir = System.getProperty("user.home");
	
	private String python_file;
	
	Cadreur (String ext, boolean deint, Class modele_import, String python_file, int ecart_mini){
		this.extension = ext;
		this.deint = deint;
		this.modele_import = modele_import;
		this.python_file = python_file;
		this.ecart_minimum = ecart_mini;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public boolean isDeint() {
		return deint;
	}

	public void setDeint(boolean deint) {
		this.deint = deint;
	}

	public Class getModele_import() {
		return modele_import;
	}

	public void setModele_import(Class modele_import) {
		this.modele_import = modele_import;
	}

	public void setPython_file(String python_file) {
		this.python_file = python_file;
	}
	
	public String getPython_file(){
		return currentUsersHomeDir + File.separator + "encodage" + File.separator + python_file;
	}

	public static ObservableList<String> getExtensions () {
		
		Set<String> s = new HashSet<String>();
		
		for (Cadreur c : Cadreur.values()){
			s.add(c.getExtension());
		}
		
		return FXCollections.observableArrayList(s.stream().collect(Collectors.toList()));
		
	}

	public int getEcart_minimum() {
		return ecart_minimum;
	}

	public void setEcart_minimum(int ecart_minimum) {
		this.ecart_minimum = ecart_minimum;
	}
	
	
	
}
