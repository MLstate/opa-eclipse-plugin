package opaide.editors.messages.ast;

import opaide.editors.messages.ast.util.OpaSrcLocation;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;


public class OpaErrorMessage extends OpaMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3665188292153986669L;
	
	

	private String errorMsg;
	private OpaSrcLocation srcLocation;

	public OpaErrorMessage(Object source) {
		super(source);
	}
	
	public OpaErrorMessage(Object source, IProject project, OpaSrcLocation srcLocation, String errorMsg) {
		super(source, project);
		this.srcLocation = srcLocation;
		this.errorMsg = errorMsg;
	}
	
	public String getRawErrorMsg() {
		return this.errorMsg;
	}

	public OpaSrcLocation getOpaSrcLocation() {
		return this.srcLocation;
	}

}
