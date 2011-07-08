package opaide.editors.config;

import java.io.File;

import opaide.editors.preferences.PreferencesInitializer;

public class BinCompiler {
	
	private PreferencesInitializer prefs;

	public BinCompiler(PreferencesInitializer prefs) {
		this.prefs = prefs;
	}
	
	public File getFile() {
		return new File(prefs.getOpaCompilerPath());
	}

}
