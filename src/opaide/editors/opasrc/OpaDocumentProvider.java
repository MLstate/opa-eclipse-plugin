package opaide.editors.opasrc;


import java.util.List;

import opaide.editors.opasrc.OpaPartitioner.OPA_PARTITION;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class OpaDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			List<String> tmp = OPA_PARTITION.getContentTypes();
			IDocumentPartitioner partitioner =
					new FastPartitioner(new OpaPartitioner(), tmp.toArray(new String[tmp.size()]));
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}