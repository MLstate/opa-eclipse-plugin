package opaide.editors.actions;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

public class SeeAboutOpa implements IWorkbenchWindowActionDelegate {
	
	private IWorkbenchWindow window ;
	
	@Override
	public void run(IAction action) {

		final Shell aboutSplash = new Shell(SWT.ON_TOP);
		aboutSplash.setText("Opa about");
		aboutSplash.setSize(300, 300);
		FillLayout layout = new FillLayout();
		layout.marginHeight = layout.marginWidth = 10;
		aboutSplash.setLayout(layout);

		final StyledText someStyledText = new StyledText(aboutSplash, SWT.MULTI | SWT.BORDER);
		someStyledText.setEditable(false);
		String link = "Opalang website";
		String theText = "You can visit the " + link;
		someStyledText.setText(theText);
		StyleRange style = new StyleRange();
		style.underline = true;
		style.underlineStyle = SWT.UNDERLINE_LINK;

		int[] ranges = {theText.indexOf(link), link.length()}; 
		StyleRange[] styles = {style};
		someStyledText.setStyleRanges(ranges, styles);

		aboutSplash.open();
		someStyledText.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent event) {
				try {
					int offset = someStyledText.getOffsetAtLocation(new Point (event.x, event.y));
					StyleRange style = someStyledText.getStyleRangeAtOffset(offset);
					if (style != null && style.underline && style.underlineStyle == SWT.UNDERLINE_LINK) {
						window.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL("http://www.opalang.org"));
					}
				} catch (IllegalArgumentException e) {
					// no character under event.x, event.y
				} catch (PartInitException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

}
