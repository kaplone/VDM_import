package models;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import models.imports.*;

public enum Cadreur {
	
	ALBARET     ("MXF", false, ModeleMXF_XDCAM.class,              "merging_MXF_pas_P2_06.py"),
	AMOUROUX    ("M2T", true,  ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_deint_amouroux.py"),
	ARMATOL     ("MP4", false, ModeleMP4_XDCAM.class ,             "merging_MP4_06_SD_prog.py"),
	BATMAN      ("MXF", true,  null              ,                 "merging_MXF_08_SD.py"),
	BAUDON      ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py"),
	BENSEMHOUN  ("MP4", true,  ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_deint.py"),
	BIGEAULT    ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py"),
	BROCCHI     ("???", false, null, null),
	CRUMEYROLLE ("???", false, null, null),
	DE_KERDREL  ("MXF", true,  ModeleMXF_P2.class,                 "merging_MXF_08_SD.py"),
	DUCOING     ("MOV", false, ModeleMOV_ProRes.class, ""),
	GIRARD      ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py"),
	GAUTIER     ("M2T", false, ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_prog_rico.py"),
	GEOFFRIAU   ("???", false, null, null),
	GORWA       ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py"),
	GUILLAUME   ("???", false, null, null),
	HUET        ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py"),
	HUGUES      ("MOV", false, ModeleMOV_ProRes.class, ""),
	LEUX        ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py"),
	LE_YOUDEC   ("MXF", false, ModeleMXF_XDCAM.class,              "merging_MXF_pas_P2_06.py"),
	LOPACKI     ("MTS", true,  ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_212_deint.py"),
	MAS         ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py"),
	MERCHADOU   ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py"),
	MERCIER     ("M2T", false, ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_prog_rico.py"),
	MICHARD     ("MP4", true,  ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_deint.py"),
	MISSANA     ("M2T", true,  ModeleM2T_mencoder_nodesplit.class, "merging_M2T_05_SD_NO_DESPLIT_deint.py"),
	MOREL       ("MP4", true,  ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_deint.py"),
	PARADIS     ("M2T", true,  ModeleM2T_mencoder_nodesplit.class, "merging_M2T_05_SD_NO_DESPLIT_deint.py"),
	PEIGNARD    ("MXF", true,  null,                               "merging_MXF_08_SD.py"),
	PICHON      ("M2T", true,  ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_deint_amouroux.py"),
	QUESADA     ("M2T", true,  ModeleM2T_mencoder_desplit.class,   "merging_M2T_05_SD_deint_amouroux.py"),
	RICO        ("M2T", false, ModeleM2T_mencoder_desplit.class,  "merging_M2T_05_SD_prog_rico.py"),
	RONCHAUD    ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py"),
	RUGGIU      ("MOV", false, ModeleMOV_XDCAM.class,              "merging_MOV_05_SD_prog.py"),
	VARLET      ("MXF", true,  null,                               "merging_MXF_08_SD.py"),
	VERNIER     ("MTS", false, ModeleMTS_ffmpeg_desplit.class,     "merging_MTS_05_SD_prog.py"),
	VIALATTE    ("MP4", false, ModeleMP4_XDCAM.class,              "merging_MP4_06_SD_prog.py")
	;
	
	private String extension;
	private boolean deint;
	private Class modele_import;
	
	private String currentUsersHomeDir = System.getProperty("user.home");
	
	private String python_file;
	
	Cadreur (String ext, boolean deint, Class modele_import, String python_file){
		this.extension = ext;
		this.deint = deint;
		this.modele_import = modele_import;
		this.python_file = python_file;
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
	
}
