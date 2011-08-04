package opaide.preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationSelectionDialog;
import org.eclipse.debug.internal.ui.launchConfigurations.SelectLaunchModesDialog;
import org.eclipse.debug.internal.ui.sourcelookup.browsers.ProjectSourceContainerBrowser;
import org.eclipse.debug.internal.ui.sourcelookup.browsers.ProjectSourceContainerDialog;
import org.eclipse.jdt.internal.debug.ui.actions.ProjectSelectionDialog;
import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import opaide.OpaIdePlugin;

public class OpaWorkbenchPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	private IWorkbench workbench;

	public OpaWorkbenchPreferencePage() {
		super(org.eclipse.jface.preference.FieldEditorPreferencePage.GRID);
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		System.out.println("WorkbenchPreferencePage1.createFieldEditors()");
		/*
		IProject[] ps = ResourcesPlugin.getWorkspace().getRoot().getProjects();	
		LaunchConfigurationSelectionDialog tmp = new LaunchConfigurationSelectionDialog(this.getShell(), ps);
		tmp.open();
		*/

		ExecutableFileFieldEditor opaFileFieldEditor = new ExecutableFileFieldEditor(OpaPreferencesConstants.P_OPA_COMPILER_PATH, "Opa compiler:", true, 
				FileFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent());
		addField(opaFileFieldEditor);
		
		DirectoryFieldEditor opaMLLIBSFieldEditor = new DirectoryFieldEditor(OpaPreferencesConstants.P_OPA_MLSTATELIBS, "MLSTATELIBS env variable:", getFieldEditorParent());
		opaMLLIBSFieldEditor.setEmptyStringAllowed(false);
		opaMLLIBSFieldEditor.setValidateStrategy(DirectoryFieldEditor.VALIDATE_ON_KEY_STROKE);
		addField(opaMLLIBSFieldEditor);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		this.workbench = workbench;
		setDescription("The preferences page of Opa");
		setPreferenceStore(OpaIdePlugin.getDefault().getPreferenceStore());
	}
	//WorkbenchPreferencePage1.isValid()
	@Override
	protected void initialize() {
		System.out.println("WorkbenchPreferencePage1.initialize()");
		super.initialize();
	}

	
}
