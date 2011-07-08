package opaide;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.*;
import org.eclipse.jdt.debug.ui.launchConfigurations.*;
import org.eclipse.swt.widgets.Composite;

public class AbstractLaunchConfigurationTabGroup extends
		org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup implements
		//org.eclipse.jdt.debug.ui.launchConfigurations.AppletParametersTab implements
		ILaunchConfigurationTabGroup {

	public AbstractLaunchConfigurationTabGroup() {
		super();
		//OpaIdePlugin.getDefault()
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		// TODO Auto-generated method stub
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new org.eclipse.jdt.debug.ui.launchConfigurations.JavaMainTab(),
				new org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab(),
		};
		
		setTabs(tabs);
	}

	@Override
	public ILaunchConfigurationTab[] getTabs() {
		// TODO Auto-generated method stub
		return fTabs;
	}


}
