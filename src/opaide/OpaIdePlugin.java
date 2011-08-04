package opaide;

import java.io.Console;
import java.util.HashMap;

import opaide.editors.config.BinCompiler;
import opaide.editors.messages.IOpaMessageListener;
import opaide.editors.messages.OpaMessagesBank;
import opaide.editors.messages.ast.OpaErrorMessage;
import opaide.editors.messages.ast.OpaMessage;
import opaide.editors.messages.ast.OpaNewCompilationLaunched;
import opaide.preferences.PreferencesInitializer;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
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
	private static MessageConsole console;
	
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
		
		IConsoleManager conMan = ConsolePlugin.getDefault().getConsoleManager();
		MessageConsole myConsole = new MessageConsole("Opa output", null);
		conMan.addConsoles(new IConsole[]{ myConsole });
		this.console = myConsole;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		console = null;
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
	
	public static MessageConsole getConsole() {
		return console;
	}
	
	private OpaMessagesBank myOpaMessagesBank = new OpaMessagesBank();	
	public OpaMessagesBank getOpaMessagesBank(){ return myOpaMessagesBank; };
	
}
