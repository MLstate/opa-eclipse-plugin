package opaide.editors.config;

import java.io.File;
import opaide.preferences.PreferencesInitializer;

public class OpaBinCompiler {
	private PreferencesInitializer prefs;
	public OpaBinCompiler(PreferencesInitializer prefs) {
		this.prefs = prefs;
	}
	public File getFile() {
		return new File(prefs.getOpaCompilerPath());
	}
}
