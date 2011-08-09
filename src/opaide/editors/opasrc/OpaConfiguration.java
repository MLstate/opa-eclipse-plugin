package opaide.editors.opasrc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import opaide.OpaIdePlugin;
import opaide.editors.opasrc.OpaPartitioner.OPA_PARTITION;
import opaide.preferences.OpaPreferencesInitializer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;

public class OpaConfiguration extends SourceViewerConfiguration {

	private OpaSrcEditor opaSrcEditor;

	public OpaConfiguration(OpaSrcEditor opaSrcEditor) {
		this.opaSrcEditor = opaSrcEditor;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		ArrayList<String> result = new ArrayList<String>(Arrays.asList(IDocument.DEFAULT_CONTENT_TYPE));
		result.addAll(OPA_PARTITION.getContentTypes());
		
		return result.toArray(new String[result.size()]);
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		
		OpaPreferencesInitializer styleProvider = OpaIdePlugin.getDefault().getPrefs();
		
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new OpaCodeScanner(styleProvider));
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        
        for (OPA_PARTITION p : OPA_PARTITION.values()) {
        	DefaultDamagerRepairer ddr = new DefaultDamagerRepairer(p.getTokenScanner(styleProvider));
        	reconciler.setDamager(ddr, p.getContentType());
        	reconciler.setRepairer(ddr, p.getContentType());
        }

		return reconciler;
	}
	
	@Override
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return new IAnnotationHover() {		
			@Override
			public String getHoverInfo(ISourceViewer sourceViewer, int lineNumber) {
				String tmp = null;
				try {
					IMarker mark = opaSrcEditor.findMarker(lineNumber);
					System.out
							.println("OpaConfiguration.getAnnotationHover(...).new IAnnotationHover() {...}.getHoverInfo()");
					if (mark != null) {
						System.out
								.println("OpaConfiguration.getAnnotationHover(...).new IAnnotationHover() {...}.getHoverInfo()");
						tmp = (String) mark.getAttribute(OpaSrcEditor.RAWMSGKEY);
					}				
				} catch (CoreException e) {
					e.printStackTrace();
				}
				return tmp;
			}
		};
	}


}