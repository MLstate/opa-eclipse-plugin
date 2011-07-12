package opaide.editors.actions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.eclipse.core.resources.IFile;

public class DoACompilation implements Runnable {
	
	private File workingDir;
	private String execToCall;
	private List<String> arguments;

	public DoACompilation(File workingDir, String execToCall, List<String> arguments) {
		this.workingDir = workingDir;
		this.execToCall = execToCall;
		this.arguments = arguments;
	}
	
	@Override
	public void run() {
		List<String> tmp = new LinkedList<String>();
		tmp.add(execToCall);
		tmp.add("--quiet");
		tmp.addAll(arguments);
		ProcessBuilder pb = new ProcessBuilder(tmp);
		pb.directory(workingDir);
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
