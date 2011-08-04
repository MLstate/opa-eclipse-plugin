package opaide.editors.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsoleStream;

import opaide.OpaIdePlugin;
import opaide.editors.ColorManager;
import opaide.editors.messages.OpaMessagesFromStream;

public class RunACompilation implements Runnable {
	
	private String execToCall;
	private List<String> arguments;
	private IProject project;

	public RunACompilation(IProject project, String execToCall, List<String> arguments) {
		
		this.project = project;
		this.execToCall = execToCall;
		this.arguments = arguments;
	}
	
	@Override
	public void run() {
		System.out.println("RunACompilation.run()");
		List<String> tmp = new LinkedList<String>();
		tmp.add(execToCall);
		tmp.add("--quiet");
		tmp.addAll(arguments);
		ProcessBuilder pb = new ProcessBuilder(tmp);
		File workingDir = new File(this.project.getLocation().toOSString());
		pb.directory(workingDir);
		String direc;
		if (pb.directory() == null) { direc = "?"; } else { direc = pb.directory().toString(); };
		System.out
				.println("LaunchOpaCompilationActionDelegate.DoACompilation.run()"
						+ " in directory='" + direc + "'"
						+ " with command='" + Arrays.toString(pb.command().toArray()) + "'");
		System.out.flush();
		try {
			Process p = pb.start();				
			InputStream errorStream = p.getErrorStream();
			OpaMessagesFromStream opaErro = new OpaMessagesFromStream(project, errorStream);
			opaErro.addMyEventListener(OpaIdePlugin.getDefault().getOpaMessagesBank().getOpaMessageListener());
			opaErro.run();
			p.waitFor();
			int toread = errorStream.available();
			byte[] tmp_byte = new byte[toread];
			errorStream.read(tmp_byte, 0, toread);
			String cs = new String(tmp_byte);
			System.out.println(cs);
			OpaIdePlugin.getConsole().newMessageStream().print(cs);
			
		} catch (final IOException e) {
			e.printStackTrace();
			// this run method will be put in a new thread, so we need this kind of stuff to avoid thread invalid access
			Display.getDefault().asyncExec( new Runnable() {  
				public void run() { 
					OpaIdePlugin.getConsole().activate();
					MessageConsoleStream stream = OpaIdePlugin.getConsole().newMessageStream();
					Color previousColor = stream.getColor();
					stream.setColor(ColorManager.getColor(new RGB(255, 0, 0)));
					stream.print(e.getMessage());
				};
			});			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

}
