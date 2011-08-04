package opaide;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import opaide.editors.messages.OpaMessagesFromStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.ui.IDebugUIConstants;

import org.eclipse.core.externaltools.internal.IExternalToolConstants;
import org.eclipse.core.externaltools.internal.launchConfigurations.BackgroundResourceRefresher;
import org.eclipse.core.externaltools.internal.launchConfigurations.ExternalToolsCoreUtil;
import org.eclipse.core.externaltools.internal.launchConfigurations.ExternalToolsProgramMessages;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.RefreshUtil;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.console.MessageConsoleStream;

public class OpaProgramLaunchDelegate extends LaunchConfigurationDelegate
	//extends org.eclipse.core.externaltools.internal.launchConfigurations.ProgramLaunchDelegate 
	implements ILaunchConfigurationDelegate {
	
	private static final String ATTR_LAUNCH_IN_BACKGROUND = "org.eclipse.debug.ui.ATTR_LAUNCH_IN_BACKGROUND"; //$NON-NLS-1$
	
	//@Override
	public void launch2(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		System.out.println("OpaProgramLaunchDelegate.launch()");
		//configuration.setAttribute(DebugPlugin.ATTR_CAPTURE_OUTPUT, false);
		ILaunchConfigurationWorkingCopy w = configuration.getWorkingCopy();
		w.setAttribute(IDebugUIConstants.ATTR_CAPTURE_IN_CONSOLE, false);
		w.setAttribute(DebugPlugin.ATTR_CAPTURE_OUTPUT, false);
		w.doSave();
		
		IProcess p = DebugPlugin.newProcess(null, null, null);
		p.getStreamsProxy().getErrorStreamMonitor().addListener(new IStreamListener() {
			
			@Override
			public void streamAppended(String text, IStreamMonitor monitor) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//super.launch(configuration, mode, launch, monitor);
	}
	
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		System.out.println("OpaProgramLaunchDelegate.launch()");
		if (monitor.isCanceled()) {
			return;
		}

		// resolve location
		IPath location = ExternalToolsCoreUtil.getLocation(configuration);

		if (monitor.isCanceled()) {
			return;
		}

		// resolve working directory
		IPath workingDirectory = ExternalToolsCoreUtil
				.getWorkingDirectory(configuration);

		if (monitor.isCanceled()) {
			return;
		}

		// resolve arguments
		String[] arguments = ExternalToolsCoreUtil.getArguments(configuration);

		if (monitor.isCanceled()) {
			return;
		}

		int cmdLineLength = 1;
		if (arguments != null) {
			cmdLineLength += arguments.length;
		}
		String[] cmdLine = new String[cmdLineLength];
		cmdLine[0] = location.toOSString();
		if (arguments != null) {
			System.arraycopy(arguments, 0, cmdLine, 1, arguments.length);
		}

		File workingDir = null;
		if (workingDirectory != null) {
			workingDir = workingDirectory.toFile();
		}

		if (monitor.isCanceled()) {
			return;
		}

		String[] envp = DebugPlugin.getDefault().getLaunchManager()
				.getEnvironment(configuration);

		if (monitor.isCanceled()) {
			return;
		}

		Process p = DebugPlugin.exec(cmdLine, workingDir, envp);
		IProcess process = null;

		// add process type to process attributes
		Map processAttributes = new HashMap();
		String programName = location.lastSegment();
		String extension = location.getFileExtension();
		if (extension != null) {
			programName = programName.substring(0, programName.length()
					- (extension.length() + 1));
		}
		programName = programName.toLowerCase();
		processAttributes.put(IProcess.ATTR_PROCESS_TYPE, programName);

	
		PipedWriter myPipedWriter = new PipedWriter();
		final PrintWriter myPrintWriter = new PrintWriter(myPipedWriter);

		if (p != null) {
			monitor.beginTask(NLS.bind(
					ExternalToolsProgramMessages.ProgramLaunchDelegate_3,
					new String[] { configuration.getName() }),
					IProgressMonitor.UNKNOWN);			
			try {
				PipedReader myPipedReader = new PipedReader(myPipedWriter);

				IStreamListener writeBackInFile = new IStreamListener() {			
					@Override
					public void streamAppended(String text, IStreamMonitor monitor) {
						//System.out
						//	.println("OpaProgramLaunchDelegate.launch(...).new IStreamListener() {...}.streamAppended() " + text);
						if (myPrintWriter != null) {
							myPrintWriter.print(text);
							myPrintWriter.flush();
						}
					}
				};
				OpaMessagesFromStream opaErrorStreamer = new OpaMessagesFromStream(new Path(workingDir.getAbsolutePath()), myPipedReader);
				opaErrorStreamer.addMyEventListener(OpaIdePlugin.getDefault().getOpaMessagesBank().getOpaMessageListener());
				new Thread(opaErrorStreamer).start();
				
				process = DebugPlugin.newProcess(launch, p, location.toOSString(),
						processAttributes);
				process.getStreamsProxy().getErrorStreamMonitor().addListener(writeBackInFile);	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (p == null || process == null) {
			if (p != null)
				p.destroy();
			throw new CoreException(new Status(IStatus.ERROR,
					IExternalToolConstants.PLUGIN_ID,
					IExternalToolConstants.ERR_INTERNAL_ERROR,
					ExternalToolsProgramMessages.ProgramLaunchDelegate_4, null));
		}
		process.setAttribute(IProcess.ATTR_CMDLINE,
				generateCommandLine(cmdLine));

		if (configuration.getAttribute(ATTR_LAUNCH_IN_BACKGROUND, true)) {
			// refresh resources after process finishes
			String scope = configuration.getAttribute(RefreshUtil.ATTR_REFRESH_SCOPE, (String)null);
			if (scope != null) {
				BackgroundResourceRefresher refresher = new BackgroundResourceRefresher(configuration, process);
				refresher.startBackgroundRefresh();
			}
		} else {
			// wait for process to exit
			while (!process.isTerminated()) {
				try {
					if (monitor.isCanceled()) {
						process.terminate();
						break;
					}
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
			}

			// refresh resources
			RefreshUtil.refreshResources(configuration, monitor);
		}
	}
	

	private String generateCommandLine(String[] commandLine) {
		if (commandLine.length < 1)
			return IExternalToolConstants.EMPTY_STRING;
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < commandLine.length; i++) {
			buf.append(' ');
			char[] characters = commandLine[i].toCharArray();
			StringBuffer command = new StringBuffer();
			boolean containsSpace = false;
			for (int j = 0; j < characters.length; j++) {
				char character = characters[j];
				if (character == '\"') {
					command.append('\\');
				} else if (character == ' ') {
					containsSpace = true;
				}
				command.append(character);
			}
			if (containsSpace) {
				buf.append('\"');
				buf.append(command);
				buf.append('\"');
			} else {
				buf.append(command);
			}
		}
		return buf.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.core.model.LaunchConfigurationDelegate#getBuildOrder
	 * (org.eclipse.debug.core.ILaunchConfiguration, java.lang.String)
	 */
	protected IProject[] getBuildOrder(ILaunchConfiguration configuration,
			String mode) throws CoreException {
		IProject[] projects = ExternalToolsCoreUtil.getBuildProjects(
				configuration, null);
		if (projects == null) {
			return null;
		}
		boolean isRef = ExternalToolsCoreUtil.isIncludeReferencedProjects(
				configuration, null);
		if (isRef) {
			return computeReferencedBuildOrder(projects);
		}
		return computeBuildOrder(projects);
	}
	
}
