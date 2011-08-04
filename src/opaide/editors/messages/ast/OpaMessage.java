package opaide.editors.messages.ast;

import java.util.EventObject;

import org.eclipse.core.resources.IProject;

public abstract class OpaMessage extends EventObject {

	private IProject project;

	/**
	 * 
	 */
	private static final long serialVersionUID = -7145362729411737978L;

	public OpaMessage(Object source) {
		super(source);
	}
	
	public OpaMessage(Object source, IProject project) {
		super(source);
		assert(project != null);
		this.project = project;
	}

	public IProject getProject() { return this.project; }
}
