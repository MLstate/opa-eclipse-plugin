package opaide.editors.opasrc;

import opaide.editors.ColorManager;

import org.eclipse.ui.editors.text.TextEditor;

public class OpaSrcEditor extends TextEditor {
	
	public OpaSrcEditor() {
		super();
		setSourceViewerConfiguration(new OpaConfiguration());
		setDocumentProvider(new OpaDocumentProvider());
	}
	public void dispose() {
		ColorManager.dispose();
		super.dispose();
	}
}
