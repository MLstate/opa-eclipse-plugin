package opaide;

import opaide.editors.launch.OpaLaunchConfigurationConstants;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.*;
import org.eclipse.debug.ui.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class LaunchConfigurationTab1 
	//extends org.eclipse.jdt.debug.ui.launchConfigurations.AppletMainTab
	extends AbstractLaunchConfigurationTab
	implements ILaunchConfigurationTab {

	private Text fWorkingDirectoryText;
	private Text fProgramText;
	private Text fArgumentsText;

	@Override
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		comp.setLayout(new GridLayout());
		createVerticalSpacer(comp, 3);
		
		{	Label workingDirectoryLabel = new Label(comp, SWT.NONE);
			workingDirectoryLabel.setText("&Working directory:");
			GridData gd = new GridData(GridData.BEGINNING);
			workingDirectoryLabel.setLayoutData(gd);
		
			fWorkingDirectoryText = new Text(comp, SWT.SINGLE | SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			fWorkingDirectoryText.setLayoutData(gd);
		}
		{	Label programLabel = new Label(comp, SWT.NONE);
			programLabel.setText("&Program to call:");
			GridData gd = new GridData(GridData.BEGINNING);
			programLabel.setLayoutData(gd);
		
			fProgramText = new Text(comp, SWT.SINGLE | SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			fProgramText.setLayoutData(gd);
		}
		{	Label workingDirectoryLabel = new Label(comp, SWT.NONE);
			workingDirectoryLabel.setText("&Arguments:");
			GridData gd = new GridData(GridData.BEGINNING);
			workingDirectoryLabel.setLayoutData(gd);
	
			fArgumentsText = new Text(comp, SWT.SINGLE | SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			fArgumentsText.setLayoutData(gd);
		}
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		IProject[] p = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		String forWorkingdir;
		if (p == null || p.length == 0) {
			forWorkingdir = "/tmp";
		} else {
			forWorkingdir = p[0].getLocation().toOSString();
		}
		configuration.setAttribute(OpaLaunchConfigurationConstants.ATTR_OPA_WORKING_DIR.toString(), 
				forWorkingdir);		
		configuration.setAttribute(OpaLaunchConfigurationConstants.ATTR_OPA_MAIN_PROGRAM.toString(), 
				"opa");
		configuration.setAttribute(OpaLaunchConfigurationConstants.ATTR_OPA_MAIN_ARGUMENTS.toString(), 
				"--quiet");
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		for (OpaLaunchConfigurationConstants c : OpaLaunchConfigurationConstants.values()) {
			try {
				String tmp = configuration.getAttribute(c.toString(), "");
				switch (c) {
					case ATTR_OPA_WORKING_DIR:
						fWorkingDirectoryText.setText(tmp);
						break;
					case ATTR_OPA_MAIN_PROGRAM:
						fProgramText.setText(tmp);
						break;
					case ATTR_OPA_MAIN_ARGUMENTS:
						fArgumentsText.setText(tmp);
						break;
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
			
		}
			
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(OpaLaunchConfigurationConstants.ATTR_OPA_WORKING_DIR.toString(),
				fWorkingDirectoryText.getText());
		configuration.setAttribute(OpaLaunchConfigurationConstants.ATTR_OPA_MAIN_PROGRAM.toString(),
				fProgramText.getText());
		configuration.setAttribute(OpaLaunchConfigurationConstants.ATTR_OPA_MAIN_ARGUMENTS.toString(),
				fArgumentsText.getText());
	}

	@Override
	public String getName() {
		return "Main";
	}
	
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		return true;
	}
	
	@Override
	public boolean canSave() {
		return true;
	}

}
