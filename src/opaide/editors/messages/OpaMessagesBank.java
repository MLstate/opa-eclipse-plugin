package opaide.editors.messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.sun.org.apache.bcel.internal.generic.NEW;

import opaide.OpaIdePlugin;
import opaide.editors.messages.ast.OpaErrorMessage;
import opaide.editors.messages.ast.OpaMessage;
import opaide.editors.messages.ast.OpaNewCompilationLaunched;
import opaide.editors.opasrc.OpaSrcEditor;

public class OpaMessagesBank {

	/*
	 * When an error stream run, the parsed errors and warning will be fired to this listener
	 */
	private IOpaMessageListener myOpaMessageListener = new IOpaMessageListener() {		
		@Override
		public void newMessage(OpaMessage someEvt) {
			addMessage(someEvt);
		}
	};
	public IOpaMessageListener getOpaMessageListener(){ return myOpaMessageListener; };

	// for a project, for a resource (~ a file) we save message
	private HashMap<IProject, HashMap<IResource, List<OpaMessage>>> pendingMessages = new HashMap<IProject, HashMap<IResource, List<OpaMessage>>>();
	
	private HashMap<IResource, List<OpaMessage>> retrieveMessagesForProject(IProject project) {
		HashMap<IResource, List<OpaMessage>> target = this.pendingMessages.get(project);
		return target;
	}
	
	private void initializeProject(IProject project) {
		HashMap<IResource, List<OpaMessage>> target = retrieveMessagesForProject(project);
		if (target == null) {
			pendingMessages.put(project, new HashMap<IResource, List<OpaMessage>>());
		}
	}
	
	private void addMessage(OpaMessage omsg) {
		System.out.println("OpaMessagesBank.addMessage()");
		IProject theTargetProject = omsg.getProject(); 
		initializeProject(theTargetProject);
		// we must add a message to a specific resource
		HashMap<IResource, List<OpaMessage>> target = retrieveMessagesForProject(theTargetProject);
		// then perhaps this resource is open in an editor
		HashMap<OpaSrcEditor, OpaMessageListenersList> possibleOpenEditor = opaMessageListenersListFromBank.get(theTargetProject);
		// initialize prevent this
		if (target == null) { assert(false); };
		if (omsg instanceof OpaNewCompilationLaunched) {
			// we remove all the old message bound to this resources when we start a new compilation
			target.clear();
			if (possibleOpenEditor != null) {
				// if there is some editor open for this project, we propagate the event
				for (Entry<OpaSrcEditor,OpaMessageListenersList> tmp : possibleOpenEditor.entrySet()) {
					tmp.getValue().fireMyEvent(omsg);
				}
			}
		} else if (omsg instanceof OpaErrorMessage) {
			OpaErrorMessage errorMsg = (OpaErrorMessage) omsg;
			// we register this message under his file
			List<OpaMessage> l = target.get(errorMsg.getOpaSrcLocation().getTheFile());
			try {
				// we to be sure that a red 'x' appear on the icon, if the file was not open before
				IMarker mark = errorMsg.getOpaSrcLocation().getTheFile().createMarker(IMarker.PROBLEM);
				mark.setAttribute(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (l == null) {
				target.put(errorMsg.getOpaSrcLocation().getTheFile(), new ArrayList<OpaMessage>(Arrays.asList(omsg)));
			} else {
				l.add(omsg);
			}
			if (possibleOpenEditor != null) {
				// we traverse the editor open for this project
				for (Entry<OpaSrcEditor,OpaMessageListenersList> tmp : possibleOpenEditor.entrySet()) {
					OpaSrcEditor key = tmp.getKey();
					IResource filekey = key.getUnderlyingResource();
					// if the 2 resources are equal, then the editor is displaying this resource and we must propagete the event
					if (filekey.equals(errorMsg.getOpaSrcLocation().getTheFile())) {
						tmp.getValue().fireMyEvent(omsg);
					}
				}
			}
		}
	}
	
	/*
	 * now a resource can be open or not
	 * so we got a second map that register all the opened editor for each project
	 */
	private HashMap<IProject, HashMap<OpaSrcEditor, OpaMessageListenersList>> opaMessageListenersListFromBank = 
			new HashMap<IProject, HashMap<OpaSrcEditor,OpaMessageListenersList>>();
	
	private OpaMessageListenersList lookForListenerList(IProject forProject, OpaSrcEditor forEditor, IOpaMessageListener listener) {
		if (opaMessageListenersListFromBank.containsKey(forProject)) {
			HashMap<OpaSrcEditor, OpaMessageListenersList> tmp = opaMessageListenersListFromBank.get(forProject);
			if (tmp.containsKey(forEditor)) {
				OpaMessageListenersList l = tmp.get(forEditor);
				return l;
				
			} else {
				tmp.put(forEditor, new OpaMessageListenersList());
				return lookForListenerList(forProject, forEditor, listener);
			}
		} else {
			opaMessageListenersListFromBank.put(forProject, new HashMap<OpaSrcEditor, OpaMessageListenersList>());
			return lookForListenerList(forProject, forEditor, listener);
		}
	}
	
	public void addMyEventListener(IProject forProject, OpaSrcEditor forEditor, IOpaMessageListener listener) {
		OpaMessageListenersList l = lookForListenerList(forProject, forEditor, listener);
		l.addMyEventListener(listener);
	}
	public void removeMyEventListener(IProject forProject, IOpaMessageListener listener) {
		//OpaMessageListenersList l = lookForListenerList(forProject, forEditor, listener);
		//l.removeMyEventListener(listener);
	}
	
	public List<OpaMessage> findMessages(IProject project, IResource resource) {
		List<OpaMessage> result = new ArrayList<OpaMessage>();
		HashMap<IResource, List<OpaMessage>> tmp = retrieveMessagesForProject(project);
		if (tmp != null && tmp.containsKey(resource)) {
			List<OpaMessage> msg = tmp.get(resource);
			result = msg;
		};
		return result;
	}

}
