package opaide.editors.messages;

import javax.swing.event.EventListenerList;

import org.eclipse.core.commands.State;
import org.eclipse.core.commands.common.EventManager;

import opaide.editors.messages.ast.OpaMessage;


public class OpaMessageListenersList extends EventManager {

	protected EventListenerList listenerList = new EventListenerList();

	public void addMyEventListener(IOpaMessageListener listener) {
		listenerList.add(IOpaMessageListener.class, listener);
	}
	public void removeMyEventListener(IOpaMessageListener listener) {
		listenerList.remove(IOpaMessageListener.class, listener);
	}
	public void fireMyEvent(OpaMessage evt) {
		for (IOpaMessageListener listener : listenerList.getListeners(IOpaMessageListener.class)) {
			listener.newMessage(evt);
		};
	}	
}
