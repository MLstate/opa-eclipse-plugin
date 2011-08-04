package opaide.editors.launch;


import java.awt.FileDialog;
import java.io.File;

import opaide.preferences.ExecutableFileFieldEditor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.*;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationSelectionDialog;
import org.eclipse.debug.ui.*;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class LaunchConfigurationTab1 
	//extends org.eclipse.jdt.debug.ui.launchConfigurations.AppletMainTab
	extends AbstractLaunchConfigurationTab
	implements ILaunchConfigurationTab {

	//private Text fWorkingDirectoryText;
	private Text fProgramText;
	private Text fArgumentsText;
	private Text fWorkingProjectText;
	private boolean gotError = false;
	
	private ModifyListener fListener = new ModifyListener() {	
		@Override
		public void modifyText(ModifyEvent e) {
			updateLaunchConfigurationDialog();
		}
	};

	@Override
	public void createControl(Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		comp.setLayout(new GridLayout(1, true));
		createVerticalSpacer(comp, 3);

		{ 	Label workingProjectLabel = new Label(comp, SWT.NONE);
			workingProjectLabel.setText("Working &project:");
			GridData gd = new GridData(GridData.BEGINNING);
			workingProjectLabel.setLayoutData(gd);
			
			fWorkingProjectText = new Text(comp, SWT.SINGLE | SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			fWorkingProjectText.setLayoutData(gd);
			fWorkingProjectText.setEditable(false);
			fWorkingProjectText.addModifyListener(fListener);
			
			Button button1 = new Button(comp, SWT.PUSH);
			button1.setText("Select a project");
			button1.setLayoutData(gd);
			button1.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent e) {
					IProject[] ps = ResourcesPlugin.getWorkspace().getRoot().getProjects();	
					LaunchConfigurationSelectionDialog tmp = new LaunchConfigurationSelectionDialog(comp.getShell(), ps);
					tmp.open();
					if (tmp.getResult() == null || tmp.getResult().length == 0) { return; }
					Object oRes = tmp.getResult()[0];
					System.out
							.println("LaunchConfigurationTab1.createControl(...).new MouseListener() {...}.mouseUp() res: " + oRes.getClass() + " ;  res: " + oRes.toString());
					if (oRes instanceof IProject) {
						IProject res = (IProject) oRes;
						System.out
								.println("LaunchConfigurationTab1.createControl(...).new MouseListener() {...}.mouseUp()");
						fWorkingProjectText.setText(res.getLocation().toOSString());
					}

				}
				
				@Override
				public void mouseDown(MouseEvent e) {}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {}
			});			
		}
		
		/*{	Label workingDirectoryLabel = new Label(comp, SWT.NONE);
			workingDirectoryLabel.setText("&Working directory:");
			GridData gd = new GridData(GridData.BEGINNING);
			workingDirectoryLabel.setLayoutData(gd);
			
			fWorkingDirectoryText = new Text(comp, SWT.SINGLE | SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			fWorkingDirectoryText.setLayoutData(gd);
			fWorkingDirectoryText.addModifyListener(fListener);
		}*/
		/*{  	Group group = new Group(comp, SWT.SHADOW_ETCHED_IN);
			group.setLayout(new FillLayout());
			org.eclipse.swt.widgets.FileDialog toto = new org.eclipse.swt.widgets.FileDialog(group.getShell());
			
			final ExecutableFileFieldEditor mainFileFieldEditor = new ExecutableFileFieldEditor(OpaLaunchConfigurationConstants.ATTR_OPA_MAIN_PROGRAM.toString(), 
					"&Program to call:", true, FileFieldEditor.VALIDATE_ON_KEY_STROKE, group);
			final LaunchConfigurationTab1 org = this ;
			
			mainFileFieldEditor.setPropertyChangeListener(new IPropertyChangeListener() {			
				@Override
				public void propertyChange(PropertyChangeEvent event) {
					org.setMessage(mainFileFieldEditor.getErrorMessage());
					org.setErrorMessage("iehzehrzeuzeuiorhzeuigr eurg euig euirfg ");
				}
			});
		}*/
		{	final org.eclipse.swt.widgets.FileDialog fileD = new org.eclipse.swt.widgets.FileDialog(comp.getShell());
		
			Label programLabel = new Label(comp, SWT.NONE);
			programLabel.setText("&Program to call:");
			GridData gd = new GridData(GridData.BEGINNING);
			programLabel.setLayoutData(gd);
		
			fProgramText = new Text(comp, SWT.SINGLE | SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			fProgramText.setLayoutData(gd);
			fProgramText.setEditable(false);
			fProgramText.addModifyListener(fListener);
			
			Button button1 = new Button(comp, SWT.PUSH);
			button1.setText("Select a file");
			button1.setLayoutData(gd);
			button1.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent e) {
					IPath p = new Path(fProgramText.getText());
					fileD.setFileName(p.lastSegment());
					fileD.setFilterPath(p.removeLastSegments(1).toOSString());
					fileD.open();
					p = (new Path(fileD.getFilterPath())).append(fileD.getFileName());
					fProgramText.setText(p.toOSString());
				}
				
				@Override
				public void mouseDown(MouseEvent e) {}				
				@Override
				public void mouseDoubleClick(MouseEvent e) {}
			});
		}
		{	Label workingDirectoryLabel = new Label(comp, SWT.NONE);
			workingDirectoryLabel.setText("&Arguments:");
			GridData gd = new GridData(GridData.BEGINNING);
			workingDirectoryLabel.setLayoutData(gd);
	
			fArgumentsText = new Text(comp, SWT.SINGLE | SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			fArgumentsText.setLayoutData(gd);
			fArgumentsText.addModifyListener(fListener);
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
						fWorkingProjectText.setText(tmp);
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
				fWorkingProjectText.getText());
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
		System.out.println("LaunchConfigurationTab1.isValid()");
		setErrorMessage(null);
		boolean result = true;
		String msg = null;
		for (OpaLaunchConfigurationConstants c : OpaLaunchConfigurationConstants.values()) {
			try {
				String tmp = launchConfig.getAttribute(c.toString(), "");
				switch (c) {
					case ATTR_OPA_WORKING_DIR:
						File dir = new File(tmp);
						if (! dir.isDirectory()) {
							result = false;
							msg = "the working directory is invalid";
						}
						break;
					case ATTR_OPA_MAIN_PROGRAM:
						File exec = new File(tmp);
						if (! exec.canExecute()) {
							result = false;
							msg = "the program to call is invalid";
						}
						break;
					case ATTR_OPA_MAIN_ARGUMENTS:
						break;
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (! result) { 
				setErrorMessage(msg);
				break; 
			};
		}
		this.gotError = super.isValid(launchConfig) && result;
		return (this.gotError);
	}
/*
	@Override
	public boolean canSave() {
		System.out.println("LaunchConfigurationTab1.canSave()");
		return (super.canSave() && (! gotError));
	}
*/

}
