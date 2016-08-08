package models;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import models.imports.*;

public enum Cadreur {
	
	ALBARET     ("MXF", false, null,                               "merging_MXF_AVCHD_06_SD_prog.py", 	  6100),
	AMOUROUX    ("M2T", true,  ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_deint_amouroux.py", 6100),
	ARMATOL     ("MP4", false, ModeleMP4_XDCAM.class ,             "merging_MP4_06_SD_prog.py", 	      6100),
	BATMAN      ("MXF", false,  null              ,                "merging_MXF_09_SD.py", 			 	  6100),
	BAUDON      ("MP4", true, ModeleMP4_XDCAM.class,               "merging_MP4_06_SD_prog.py", 		  6100),
	BENSEMHOUN  ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py", 		  6100),
	BIGEAULT    ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py", 		  6100),
	CHARLES     ("MXF", true,  null              ,                 "merging_MXF_09_SD.py", 			 	  6100),
	BROCCHI     ("M2T", false,  ModeleM2T_mencoder_desplit.class,  "merging_M2T_05_SD_deint_amouroux.py", 6100),
	CRUMEYROLLE ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py", 		  6100),
	DE_KERDREL_MTS ("MTS", false, ModeleMTS_ffmpeg_desplit.class,  "merging_MTS_05_SD_prog.py", 		  6100),
	DE_KERDREL  ("MXF", true,  null,             			       "merging_MXF_09_SD.py", 				  6100),
	DEBERGUE    ("M2T", false, ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_prog_debergue.py",  6100),
	DUCOING     ("MOV", false, ModeleMOV_ProRes.class, 			   null,								  6100),
	DUGUE       ("M2T", false, ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_prog_rico.py", 	  6100),
	GIRARD      ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py", 		  6100),
	GAUTIER     ("M2T", false, ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_prog_rico.py",	  6100),
	GEOFFRIAU   ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py", 		  6100),
	GORWA       ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py", 		  6100),
	GUILLAUME   ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py", 		  6100),
	HUET        ("MTS", true,  ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py", 		  6100),
	HUET_MOV    ("MOV", true,  ModeleMOV_XDCAM.class,              "merging_MOV_05_SD_deint.py", 	      6100),
	HUGUES      ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py", 		  6100),
	LECLERC     ("MP4", false, ModeleMP4_XDCAM.class ,             "merging_MP4_06_SD_prog.py", 	      6100),
	LEUX        ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py", 		  6100),
	LE_YOUDEC   ("MXF", false, null,              				   "merging_MXF_AVCHD_06_SD_prog.py",     6100),
	LOPACKI     ("MTS", true,  ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_212_deint.py", 	  6100),
	MAS         ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py", 		  6100),
	MERCHADOU   ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py", 		  6100),
	MERCIER     ("M2T", false, ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_prog_rico.py", 	  6100),
	MICHARD     ("MP4", true,  ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_deint.py", 		  6100),
	MISSANA     ("M2T", true,  ModeleM2T_mencoder_nodesplit.class, "merging_M2T_05_SD_NO_DESPLIT_deint.py",	  6100),
	MOREL       ("MP4", true,  ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_deint.py", 		  6100),
	PARADIS_chunk     ("M2T", true,  ModeleM2T_mencoder_desplit.class, "merging_M2T_05_SD_deint_amouroux.py", 6100),
	PARADIS_full      ("M2T", true,  ModeleM2T_mencoder_nodesplit.class, "merging_M2T_05_SD_NO_DESPLIT_deint.py",	  6100),
	PEIGNARD    ("MXF", true,  null,                               "merging_MXF_09_SD.py", 				  6100),
	PICHON      ("M2T", true,  ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_deint_amouroux.py", 6100),
	QUESADA     ("M2T", true,  ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_deint_amouroux.py", 6100),
	RICO        ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py", 		  6100),
	ROCHETA     ("MXF", false, null,				               "merging_MXF_AVCHD_06_SD_prog", 		  6100), //ModeleMXF_XDCAM.class
	RONCHAUD_25 ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py", 		  6100),
	RONCHAUD_50 ("MP4", false, ModeleMP4_XDCAM_50.class,           null,						 		  6100), //merging_MP4_06_SD_prog.py
	RONCHAUD_MVI("MP4", false, ModeleMP4_XDCAM_50.class,           null,  						 		  6100), //merging_MP4_06_SD_prog.py
	RUGGIU      ("MOV", true,  ModeleMOV_XDCAM.class,              "merging_MOV_05_SD_deint.py",		  6100), //merging_MOV_05_SD_deint.py
	ULYSSE      ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py", 		  6100),
	VARLET      ("MXF", true,  null,                               "merging_MXF_09_SD.py", 				  6100),
	VERNIER     ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py", 		  6100),
	VIALATTE    ("M2T", false, ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_prog_rico.py", 	  6100),
	WOJ         ("MOV", false, ModeleMOV_XDCAM.class,              "merging_MOV_05_SD_prog.py", 	      6100)
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
		
		return python_file == null ? null : currentUsersHomeDir + File.separator + "encodage" + File.separator + python_file;
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
