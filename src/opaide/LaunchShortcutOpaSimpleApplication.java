package opaide;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import opaide.editors.actions.RunACompilation;
import opaide.editors.config.BinCompiler;
import opaide.editors.opasrc.OpaSrcEditor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;

public class LaunchShortcutOpaSimpleApplication implements ILaunchShortcut, org.eclipse.debug.ui.actions.ILaunchable {

	@Override
	public void launch(ISelection selection, String mode) {
		 if (selection != null && selection instanceof IStructuredSelection) {
			 IStructuredSelection betterSel = (IStructuredSelection)selection;
			 Object first = betterSel.getFirstElement();
			 /*for (Iterator<Object> i = betterSel.iterator(); i.hasNext(); ) {
				 Object tmp = i.next();
				 System.out
						.println("LaunchShortcutOpaSimpleApplication.launch(); " + String.format("tmp:'%s', tmp:'%s'", tmp.getClass(), tmp.toString()));
				 
			 }*/
			 if (first instanceof IResource) {
				 run((IResource) first);
			 }
	        } 
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput res = editor.getEditorInput();
		if (res instanceof IFileEditorInput) {
			FileEditorInput fileEd = (FileEditorInput) res;
			IResource target = fileEd.getFile();
			run (target);
		} 
	}
	
	private void run(IResource target) {
		IProject project = target.getProject();
		new Thread(new RunACompilation(project, OpaIdePlugin.getDefault().getBinCompiler().getFile().getAbsolutePath(), 
				Arrays.asList(target.getProjectRelativePath().toOSString()))).start();
	}

}
