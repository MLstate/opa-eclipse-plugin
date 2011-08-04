package opaide.editors.config;

import java.io.File;
import opaide.preferences.OpaPreferencesInitializer;

public class OpaBinCompiler {
	private OpaPreferencesInitializer prefs;
	public OpaBinCompiler(OpaPreferencesInitializer prefs) {
		this.prefs = prefs;
	}
	public File getFile() {
		return new File(prefs.getOpaCompilerPath());
	}
}
