package opaide.editors.opasrc;

import java.util.ArrayList;
import java.util.List;

import opaide.editors.ColorManager;
import opaide.editors.opasrc.OpaPartitioner.OPA_PARTITION;
import opaide.preferences.PreferencesInitializer;
import opaide.preferences.PreferencesInitializer.SavedTextAttribute;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.graphics.RGB;

public class OpaCommentBlockScanner extends RuleBasedScanner {

	public OpaCommentBlockScanner(PreferencesInitializer styleProvider) {
		SavedTextAttribute c = styleProvider.getSavedTextAttribute(OPA_PARTITION.OPA_COMMENT_BLOCK);
		System.out.println("OpaCommentBlockScanner.OpaCommentBlockScanner()" + " style= " + c.getStyle());
		IToken stringToken = new Token(new TextAttribute( ColorManager.getColor(c.getColor()), null, c.getStyle()));
		
		List<IRule> rules = new ArrayList<IRule>();
		
		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setDefaultReturnToken(stringToken);
		setRules(result);
	}
}
