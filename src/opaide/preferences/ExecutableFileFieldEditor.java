package opaide.preferences;

import java.io.File;

import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class ExecutableFileFieldEditor extends FileFieldEditor {
	
	public ExecutableFileFieldEditor(String name, String labelText,
            boolean enforceAbsolute, int validationStrategy, Composite parent) {
		super(name, labelText, enforceAbsolute, validationStrategy, parent);
	}
	
	@Override
	protected boolean checkState() {
		if (super.checkState()) {
			File tmp = new File(this.getStringValue());
			boolean res = tmp.canExecute();
			if (! res) {
				showErrorMessage("The selected file is not executable");
			}
			System.out.println("ExecutableFileFieldEditor.checkState() " + res);
			return res;
		} else {
			return false;
		}
	}

}
