package opaide.editors.actions;

import opaide.OpaIdePlugin;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessBuilder;
import java.util.Arrays;

public class LaunchOpaCompilationActionDelegate implements
		IWorkbenchWindowActionDelegate {
	
	
	private class DoACompilation implements Runnable {
		private File f;
		private IPath target;
		
		public DoACompilation(File f, IPath target) {
			this.f = f;
			this.target = target;
		}

		@Override
		public void run() {
			ProcessBuilder pb = new ProcessBuilder(f.getAbsolutePath(), "--quiet", target.toOSString());
			pb.directory(new File(target.removeLastSegments(1).makeAbsolute().toOSString()));
			String direc;
			if (pb.directory() == null) { direc = "?"; } else { direc = pb.directory().toString(); };
			System.out
					.println("LaunchOpaCompilationActionDelegate.DoACompilation.run()"
							+ " in directory='" + direc + "'"
							+ " with command='" + Arrays.toString(pb.command().toArray()) + "'");
			try {
				Process p = pb.start();
				p.waitFor();
				InputStream errorStream = p.getErrorStream();
				int toread = errorStream.available();
				byte[] tmp_byte = new byte[toread];
				errorStream.read(tmp_byte, 0, toread);
				String cs = new String(tmp_byte);
				System.out.println(cs);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	private IWorkbenchWindow window;
	

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		File f = OpaIdePlugin.getDefault().getBinCompiler().getFile();
		if (! f.exists()) {
			System.err.println(String.format("the file '%s' does not exists", f.getAbsolutePath()));
			return ;
		}
		if (! f.isFile() && f.canExecute()) {
			System.err.println(String.format("the file '%s' is not a file or is not executable", f.getAbsolutePath()));
			return ;
		}
		IEditorInput input = window.getActivePage().getActiveEditor().getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFileEditorInput fileInput = (IFileEditorInput) input;
			new Thread(new DoACompilation(f, fileInput.getFile().getLocation())).start();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

}
