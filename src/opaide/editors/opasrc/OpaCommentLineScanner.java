package opaide.editors.opasrc;

import java.util.ArrayList;
import java.util.List;

import opaide.editors.ColorManager;
import opaide.editors.opasrc.OpaPartitioner.OPA_PARTITION;
import opaide.preferences.OpaPreferencesInitializer;
import opaide.preferences.OpaPreferencesInitializer.SavedTextAttribute;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.graphics.RGB;

public class OpaCommentLineScanner extends RuleBasedScanner {
	
	public OpaCommentLineScanner(OpaPreferencesInitializer styleProvider) {
		SavedTextAttribute c = styleProvider.getSavedTextAttribute(OPA_PARTITION.OPA_COMMENT_LINE);
		System.out.println("OpaCommentLineScanner.OpaCommentLineScanner()" + " style= " + c.getStyle());
		IToken commentLineToken = new Token(new TextAttribute( ColorManager.getColor(c.getColor()), null, c.getStyle()));
		
		List<IRule> rules = new ArrayList<IRule>();
		//we got nothing to parse and colorize
		
		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setDefaultReturnToken(commentLineToken);
		setRules(result);
	}

}
