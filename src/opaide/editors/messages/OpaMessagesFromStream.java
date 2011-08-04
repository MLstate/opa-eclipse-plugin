package opaide.editors.messages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PipedReader;
import java.util.*;
import java.util.regex.*;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

import opaide.OpaIdePlugin;
import opaide.editors.messages.ast.OpaErrorMessage;
import opaide.editors.messages.ast.OpaMessage;
import opaide.editors.messages.ast.OpaNewCompilationLaunched;
import opaide.editors.messages.ast.util.OpaSrcLocation;

public class OpaMessagesFromStream implements Runnable {
	
	private Scanner errorScan;
	private OpaMessageListenersList listenersList;
	private IProject project;

	public OpaMessagesFromStream(IProject project, InputStream errorStream) {
		Scanner errorScan = new Scanner(errorStream);
		this.project = project;
		this.errorScan = errorScan;
		this.listenersList = new OpaMessageListenersList();
	}
	/*
	public OpaMessagesFromStream(IPath location, InputStream errorStream) {
		IProject project = null;
		for (IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			IResource tmp = p.findMember(location);
			//System.out.println("OpaMessagesFromStream.OpaMessagesFromStream() testing location '" + location.toOSString() + "'");
			if (tmp != null || p.getLocation().equals(location)) {
				project = p;
				break;
			}
		};
		this.project = project;
		Scanner errorScan = new Scanner(errorStream);
		this.errorScan = errorScan;
		this.listenersList = new OpaMessageListenersList();		
	}
	*/
	public OpaMessagesFromStream(IPath location, PipedReader errorReader) {
		IProject project = null;
		for (IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			IResource tmp = p.findMember(location);
			//System.out.println("OpaMessagesFromStream.OpaMessagesFromStream() testing location '" + location.toOSString() + "'");
			if (tmp != null || p.getLocation().equals(location)) {
				project = p;
				break;
			}
		};
		this.project = project;
		Scanner errorScan = new Scanner(errorReader);
		this.errorScan = errorScan;
		this.listenersList = new OpaMessageListenersList();		
	}
/*
	public OpaMessagesFromStream(IPath location, File errorFile) {
		IProject project = null;
		for (IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			IResource tmp = p.findMember(location);
			//System.out.println("OpaMessagesFromStream.OpaMessagesFromStream() testing location '" + location.toOSString() + "'");
			if (tmp != null || p.getLocation().equals(location)) {
				project = p;
				break;
			}
		}
		//Scanner errorScanner = new Scanner(errorStream).reset();
		Scanner errorScanner = null;
		try {
			errorScanner = new Scanner(new FileReader(errorFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.project = project;
		this.errorScan = errorScanner;
		this.listenersList = new OpaMessageListenersList();		
	}
	*/
	public void addMyEventListener(IOpaMessageListener listener) {
		this.listenersList.addMyEventListener(listener);
	}
	
	private class LocationParser {
		private Pattern errorPattern;

		public LocationParser() {
			Pattern errorPattern = Pattern.compile("^File \"(.*)\", line (\\d+), characters (\\d+)-(\\d+), [(](\\d+):(\\d+)-(\\d+):(\\d+) [|] (\\d+)-(\\d+)[)].*$");
			this.errorPattern = errorPattern;
		}
		
		public OpaSrcLocation toOpaSrcLocation(String src) {
			Matcher mat = errorPattern.matcher(src);
			OpaSrcLocation srcLocation = null;
			if (mat.matches()) {
				String theFile = mat.group(1);
				Integer theLine = Integer.parseInt(mat.group(2));
				Integer leftCharacterForTheLine = Integer.parseInt(mat.group(3));
				Integer rightCharacterForTheLine = Integer.parseInt(mat.group(4));
				Integer preciseLineStart = Integer.parseInt(mat.group(5));
				Integer preciseLineCharPosStart = Integer.parseInt(mat.group(6));
				Integer preciseLineEnd = Integer.parseInt(mat.group(7));
				Integer preciseLineCharPosEnd = Integer.parseInt(mat.group(8));
				Integer globalCharStart = Integer.parseInt(mat.group(9));
				Integer globalCharEnd = Integer.parseInt(mat.group(10));
				String tmp = String.format("OpaErrorsFromStream.run(); file='%s'; line='%d' (%d-%d); \n" +
						"\tpreciseStart='%d:%d'; preciseEnd='%d:%d'" +
						"\tglobalCharStart='%d'; globalCharEnd='%d'",
						theFile, theLine, leftCharacterForTheLine, rightCharacterForTheLine,
						preciseLineStart, preciseLineCharPosStart, preciseLineEnd, preciseLineCharPosEnd,
						globalCharStart, globalCharEnd);
				System.out.println(tmp);
				
				IResource tempo = project.findMember(theFile);
				srcLocation = new OpaSrcLocation(tempo, theLine, leftCharacterForTheLine, rightCharacterForTheLine,
						preciseLineStart, preciseLineCharPosStart, preciseLineEnd, preciseLineCharPosEnd,
						globalCharStart, globalCharEnd);
			};
			return srcLocation;
		}
	}
	
	public void run() {
		if (project == null) {
			System.err.println("OpaMessagesFromStream.run(), incorrect project");
			return;
		}
		if (errorScan == null) {
			System.err.println("OpaMessagesFromStream.run(), incorrect errorScan");
			return;
		}
		listenersList.fireMyEvent(new OpaNewCompilationLaunched(this, project));
		//Pattern errorPattern = Pattern.compile("^File \"(.*)\", line (\\d+), characters (\\d+)-(\\d+), [(](\\d+):(\\d+)-(\\d+):(\\d+) [|] (\\d+)-(\\d+)[)].*$");
		LocationParser locationParser = new LocationParser();
		try {
			System.out.println("OpaMessagesFromStream.run() starting while");
			while (errorScan.hasNext()) {
				System.out.println("OpaErrorsFromStream.run() in hasNext");
				String line = errorScan.nextLine();
				if (Pattern.compile("^Error$").matcher(line).matches()) {
					System.out.println("OpaErrorsFromStream.run() 'Error' found");
					line = errorScan.nextLine();
					OpaSrcLocation srcLocation = locationParser.toOpaSrcLocation(line);
					if (srcLocation == null) {
						System.out.println("OpaErrorsFromStream.run() ... file not found in '" + line + "'");
					} else {
						OpaErrorMessage msg = new OpaErrorMessage(this, project, srcLocation, errorScan.nextLine() + errorScan.nextLine() + errorScan.nextLine());
						this.listenersList.fireMyEvent(msg);
					}
				} else {
					System.out.println("OpaErrorsFromStream.run() skipping '" + line + "'");
				}

			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		};
		return;
	}
	/*
	public static void main(String[] args) {
		System.out.println("plop");
		OpaErrorsFromStream test = new OpaErrorsFromStream(System.in);
		test.run();
	}
	*/

}
