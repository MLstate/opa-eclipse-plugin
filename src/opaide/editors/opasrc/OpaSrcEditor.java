package opaide.editors.opasrc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import opaide.OpaIdePlugin;
import opaide.editors.ColorManager;
import opaide.editors.messages.IOpaMessageListener;
import opaide.editors.messages.ast.OpaErrorMessage;
import opaide.editors.messages.ast.OpaMessage;
import opaide.editors.messages.ast.OpaNewCompilationLaunched;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.MarkerUtilities;

public class OpaSrcEditor extends TextEditor {
	public static final String RAWMSGKEY = "RAWMSGKEY";
	
	private IProject project;
	
	public OpaSrcEditor() {
		super();
		setSourceViewerConfiguration(new OpaConfiguration(this));
		setDocumentProvider(new OpaDocumentProvider());
	}
	
	public void dispose() {
		ColorManager.dispose();
		super.dispose();
	}
	
	private IOpaMessageListener myOpaMessageListener = new IOpaMessageListener() {
			@Override
			public void newMessage(OpaMessage evt) { addOpaMessageMarker(evt) ; }
		};
	
	@Override
	protected void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		IResource myResource = getUnderlyingResource();
		myResource.deleteMarkers(null, true, IResource.DEPTH_INFINITE);
		System.out.println("OpaSrcEditor.OpaSrcEditor() " + myResource.getFileExtension());
		this.project = myResource.getProject();
		OpaIdePlugin.getDefault().getOpaMessagesBank().removeMyEventListener(this.project, myOpaMessageListener);
		OpaIdePlugin.getDefault().getOpaMessagesBank().addMyEventListener(this.project, this, myOpaMessageListener);
		for (OpaMessage m : OpaIdePlugin.getDefault().getOpaMessagesBank().findMessages(this.project, myResource)) {
			addOpaMessageMarker(m);
		}
		for (String as : project.getDescription().getNatureIds())
			System.out.println("OpaSrcEditor.doSetInput() " + as);
		System.out.println("OpaSrcEditor.OpaSrcEditor() " + project.toString());
		System.out.println("OpaSrcEditor.OpaSrcEditor() " + project.getLocation().toOSString());
	}
	
	private void addOpaMessageMarker(OpaMessage msg){
		if (msg instanceof OpaErrorMessage) {
			addOpaErrorMarker((OpaErrorMessage) msg);
		} else if (msg instanceof OpaNewCompilationLaunched) {
			for (IMarker marker : getMarkers()) {
				try {
					marker.delete();
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private List<IMarker> myMarkers = new ArrayList<IMarker>();
	private List<IMarker> getMarkers() { return myMarkers; };
	
	private void addOpaErrorMarker (OpaErrorMessage msg) {
		IResource file = getUnderlyingResource();
		if (file == null) { return; };
		try {
			IMarker marker = file.createMarker(IMarker.PROBLEM);
			myMarkers.add(marker);
			if (! marker.exists()) { return; };
			marker.setAttribute(IMarker.MESSAGE, "Error:" + "\n" + msg.getRawErrorMsg());
			marker.setAttribute(IMarker.CHAR_START, msg.getOpaSrcLocation().getGlobalCharStart());
			marker.setAttribute(IMarker.CHAR_END, msg.getOpaSrcLocation().getGlobalCharEnd());
			marker.setAttribute(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));
			marker.setAttribute(IMarker.TRANSIENT, Boolean.TRUE);
			marker.setAttribute(IMarker.LINE_NUMBER, msg.getOpaSrcLocation().getTheLine());
			marker.setAttribute(RAWMSGKEY, msg.getRawErrorMsg());
		} catch (CoreException e1) {
			e1.printStackTrace();
		}

	}
		
	public IResource getUnderlyingResource() {
		if (!(getEditorInput() instanceof IFileEditorInput)) { return null; };
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		return file;
	}
	
	public IMarker findMarker(int lineNumber) {
		IMarker res = null;
		lineNumber++; // ??
		System.out.println("OpaSrcEditor.findMarker() lineNumber=" + lineNumber);
		for (IMarker m : myMarkers) {
			Integer tmp;
			try {
				tmp = (Integer) m.getAttribute(IMarker.LINE_NUMBER);
				System.out.println("OpaSrcEditor.findMarker() against: " + tmp.toString());
				if (tmp != null) {
					if (tmp.intValue() == lineNumber) {
						res = m;
						break;
					}
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}		
		}
		return res;
	}
	
	
}
