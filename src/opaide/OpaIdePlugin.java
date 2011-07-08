package opaide;

import opaide.editors.config.BinCompiler;
import opaide.editors.preferences.PreferencesInitializer;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class OpaIdePlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "opaide"; //$NON-NLS-1$

	// The shared instance
	private static OpaIdePlugin plugin;
	
	private BinCompiler binCompiler;
	private PreferencesInitializer prefs;
	
	/**
	 * The constructor
	 */
	public OpaIdePlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		this.prefs = new PreferencesInitializer();
		this.binCompiler = new BinCompiler(this.prefs);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static OpaIdePlugin getDefault() {
		return plugin;
	}

	public BinCompiler getBinCompiler() {
		return binCompiler;
	}

	public PreferencesInitializer getPrefs() {
		return prefs;
	}

}
