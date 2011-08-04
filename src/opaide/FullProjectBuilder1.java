package opaide;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.externaltools.internal.launchConfigurations.ExternalToolsUtil;

public class FullProjectBuilder1 extends IncrementalProjectBuilder {

	public FullProjectBuilder1() {
		// TODO Auto-generated constructor stub
	}

	//private ExternalToolsUtil tmp = new org.eclipse.ui.externaltools.internal.model.ExternalToolBuilder();
	
	@Override
	protected IProject[] build(int kind, Map<String, String> args,
			IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

}
