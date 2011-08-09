package opaide.editors.opasrc;

import java.util.ArrayList;
import java.util.List;

import opaide.OpaIdePlugin;
import opaide.editors.ColorManager;
import opaide.editors.opasrc.OpaPartitioner.OPA_PARTITION;
import opaide.preferences.OpaPreferencesInitializer;
import opaide.preferences.OpaPreferencesInitializer.SavedTextAttribute;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.graphics.RGB;

public class OpaCommentBlockScanner extends RuleBasedScanner {

	public OpaCommentBlockScanner(OpaPreferencesInitializer styleProvider) {
		SavedTextAttribute c = styleProvider.getSavedTextAttribute(OPA_PARTITION.OPA_COMMENT_BLOCK);
		TextAttribute someTextAttribute = SavedTextAttribute.toTextAttribute(OpaIdePlugin.getDefault().getDisplay(), c);
		IToken stringToken = new Token(someTextAttribute);
		
		List<IRule> rules = new ArrayList<IRule>();
		
		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setDefaultReturnToken(stringToken);
		setRules(result);
	}
}
