package opaide;

import org.eclipse.debug.ui.*;
import org.eclipse.jdt.debug.ui.launchConfigurations.*;
import org.eclipse.swt.widgets.Composite;

public class AbstractLaunchConfigurationTabGroup extends
		org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup implements
		//org.eclipse.jdt.debug.ui.launchConfigurations.AppletParametersTab implements
		ILaunchConfigurationTabGroup {

	public AbstractLaunchConfigurationTabGroup() {
		super();
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				//new org.eclipse.jdt.debug.ui.launchConfigurations.JavaMainTab(),
				new LaunchConfigurationTab1(),
				//new org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab(),
		};
		
		setTabs(tabs);
	}

	@Override
	public ILaunchConfigurationTab[] getTabs() {
		return fTabs;
	}


}
