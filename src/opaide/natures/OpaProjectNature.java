package opaide.natures;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class OpaProjectNature implements IProjectNature {
	
	public static final String NATURE_ID = "opaide.natures.OpaProjectNature.id"; //$NON-NLS-1$
	
	private IProject project; 
	
	@Override
	public void configure() throws CoreException {
		// TODO Auto-generated method stub
		assert(false);
	}

	@Override
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
		System.out.println("ProjectNature.setProject()");
	}

}
