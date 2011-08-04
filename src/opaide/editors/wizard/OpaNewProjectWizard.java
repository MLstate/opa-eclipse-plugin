package opaide.editors.wizard;

import opaide.natures.OpaProjectNature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaMainTab;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.*;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;


public class OpaNewProjectWizard extends Wizard implements INewWizard, IExecutableExtension {
	
	private BasicNewProjectResourceWizard basicWizard;
	//private JavaMainTab j = new org.eclipse.jdt.debug.ui.launchConfigurations.JavaMainTab();

	public OpaNewProjectWizard() {
		super();
		this.basicWizard = new BasicNewProjectResourceWizard();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		basicWizard.init(workbench, selection);
		basicWizard.addPages();
		for (IWizardPage p : basicWizard.getPages()) {
			addPage(p);
			System.out.println("OpaNewWizard.init()");
		}
	}

	@Override
	public boolean performFinish() {
		basicWizard.setContainer(this.getContainer());
		if (basicWizard.performFinish()) {
			//IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = basicWizard.getNewProject();
			try {
				IProjectDescription description = project.getDescription();
				String[] natures = description.getNatureIds () ;
			    String[] newNatures = new String[natures.length + 1] ;
			    System.arraycopy (natures, 0, newNatures, 1, natures.length) ;
			    newNatures[0] = OpaProjectNature.NATURE_ID;
			    description.setNatureIds(newNatures);
				project.setDescription(description, null);
				BasicNewProjectResourceWizard.updatePerspective(_configurationElement);
				return true;
			} catch (CoreException e) {
				e.printStackTrace();
				return false;
			}
		} else { return false; }
	}

	private IConfigurationElement _configurationElement;
	
	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		_configurationElement = config;		
	}
	
	
}
