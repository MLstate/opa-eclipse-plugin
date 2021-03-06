package opaide.editors.launch.configuration;

public enum OpaClassicLaunchConfigurationConstants {
	
	ATTR_OPA_WORKING_DIR,
	ATTR_OPA_MAIN_PROGRAM,
	ATTR_OPA_MAIN_ARGUMENTS;
	
	@Override
	public String toString() {
		return this.getDeclaringClass().getCanonicalName() + "." + this.name();
	}

}
