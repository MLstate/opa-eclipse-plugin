package opaide.editors.opasrc;

import java.util.ArrayList;
import java.util.Arrays;

import opaide.OpaIdePlugin;
import opaide.editors.opasrc.OpaPartitioner.OPA_PARTITION;
import opaide.editors.preferences.PreferencesInitializer;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class OpaConfiguration extends SourceViewerConfiguration {

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		ArrayList<String> result = new ArrayList<String>(Arrays.asList(IDocument.DEFAULT_CONTENT_TYPE));
		result.addAll(OPA_PARTITION.getContentTypes());
		
		return result.toArray(new String[result.size()]);
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		
		PreferencesInitializer styleProvider = OpaIdePlugin.getDefault().getPrefs();
		
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new OpaCodeScanner(styleProvider));
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        
        DefaultDamagerRepairer drForString = new DefaultDamagerRepairer(new OpaStringScanner(styleProvider));
        reconciler.setDamager(drForString, OPA_PARTITION.OPA_STRING.getContentType());
        reconciler.setRepairer(drForString, OPA_PARTITION.OPA_STRING.getContentType());
        
        DefaultDamagerRepairer drForCommentLine = new DefaultDamagerRepairer(new OpaCommentLineScanner(styleProvider));
        reconciler.setDamager(drForCommentLine, OPA_PARTITION.OPA_COMMENT_LINE.getContentType());
        reconciler.setRepairer(drForCommentLine, OPA_PARTITION.OPA_COMMENT_LINE.getContentType());
        
        DefaultDamagerRepairer drForCommentBlock = new DefaultDamagerRepairer(new OpaCommentBlockScanner(styleProvider));
        reconciler.setDamager(drForCommentBlock, OPA_PARTITION.OPA_COMMENT_BLOCK.getContentType());
        reconciler.setRepairer(drForCommentBlock, OPA_PARTITION.OPA_COMMENT_BLOCK.getContentType());
        
		return reconciler;
	}

}