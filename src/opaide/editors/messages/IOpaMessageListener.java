package opaide.editors.messages;

import java.util.EventListener;

import opaide.editors.messages.ast.OpaMessage;

public interface IOpaMessageListener extends EventListener {
	public void newMessage(OpaMessage evt);
}
