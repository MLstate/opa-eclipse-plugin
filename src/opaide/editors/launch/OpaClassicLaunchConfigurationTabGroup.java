package opaide.editors.launch;


import org.eclipse.debug.ui.*;
import org.eclipse.jdt.debug.ui.launchConfigurations.*;
import org.eclipse.swt.widgets.Composite;

public class OpaClassicLaunchConfigurationTabGroup extends
		org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup implements
		//org.eclipse.jdt.debug.ui.launchConfigurations.AppletParametersTab implements
		//extends ProgramBuilderTabGroup
		ILaunchConfigurationTabGroup {

	public OpaClassicLaunchConfigurationTabGroup() {
		super();
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		OpaClassicLaunchConfigurationTab tmp = new OpaClassicLaunchConfigurationTab();
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				//new org.eclipse.jdt.debug.ui.launchConfigurations.JavaMainTab(),
				tmp,
				//new org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab(),
		};
		setTabs(tabs);
		//tmp.activated(workingCopy)
	}

	@Override
	public ILaunchConfigurationTab[] getTabs() {
		return fTabs;
	}
	


}
