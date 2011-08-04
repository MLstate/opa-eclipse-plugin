package opaide.editors.messages.ast;

import org.eclipse.core.resources.IProject;


public class OpaNewCompilationLaunched extends OpaMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2934091654426030682L;

	public OpaNewCompilationLaunched(Object source, IProject project) {
		super(source, project);
	}

}
