package opaide.editors.launch;

import java.io.File;
import java.util.Arrays;

import opaide.editors.actions.RunACompilation;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.*;
import org.eclipse.debug.core.model.*;

public class OpaClassicConfigurationDelegate extends LaunchConfigurationDelegate implements
		ILaunchConfigurationDelegate2 {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		System.out.println("OpaExecutableConfigurationDelegate.launch()");
		String workingDir = configuration.getAttribute(OpaClassicLaunchConfigurationConstants.ATTR_OPA_WORKING_DIR.toString(), "");
		IProject project = null;
		for (IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			if (p.getLocation().toOSString().equals(workingDir)) {
				project = p;
				break;
			} 
		}
		String execToCall = configuration.getAttribute(OpaClassicLaunchConfigurationConstants.ATTR_OPA_MAIN_PROGRAM.toString(), "opa");
		String arguments = configuration.getAttribute(OpaClassicLaunchConfigurationConstants.ATTR_OPA_MAIN_ARGUMENTS.toString(), "");
		if (project != null) {
			new RunACompilation(project, execToCall, Arrays.asList(arguments)).run();
		} else {
			System.err.println("OpaExecutableConfigurationDelegate.launch(); project is  null");
		}
	}


}
