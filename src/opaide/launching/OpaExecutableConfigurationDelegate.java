package opaide.launching;

import java.io.File;
import java.util.Arrays;

import opaide.editors.actions.DoACompilation;
import opaide.editors.launch.OpaLaunchConfigurationConstants;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.*;
import org.eclipse.debug.core.model.*;

public class OpaExecutableConfigurationDelegate extends LaunchConfigurationDelegate implements
		ILaunchConfigurationDelegate2 {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		System.out.println("OpaExecutableConfigurationDelegate.launch()");
		String workingDir = configuration.getAttribute(OpaLaunchConfigurationConstants.ATTR_OPA_WORKING_DIR.toString(), "");
		String execToCall = configuration.getAttribute(OpaLaunchConfigurationConstants.ATTR_OPA_MAIN_PROGRAM.toString(), "opa");
		String arguments = configuration.getAttribute(OpaLaunchConfigurationConstants.ATTR_OPA_MAIN_ARGUMENTS.toString(), "");
		File workingDirfile = new File(workingDir);
		new DoACompilation(workingDirfile, execToCall, Arrays.asList(arguments)).run();
	}
/*
	@Override
	public ILaunch getLaunch(ILaunchConfiguration configuration, String mode)
			throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean buildForLaunch(ILaunchConfiguration configuration,
			String mode, IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean finalLaunchCheck(ILaunchConfiguration configuration,
			String mode, IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean preLaunchCheck(ILaunchConfiguration configuration,
			String mode, IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}
*/
}
