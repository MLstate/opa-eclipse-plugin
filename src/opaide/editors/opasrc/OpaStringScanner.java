package opaide.editors.opasrc;

import java.util.ArrayList;
import java.util.List;

import opaide.editors.ColorManager;
import opaide.editors.opasrc.OpaPartitioner.OPA_PARTITION;
import opaide.editors.preferences.PreferencesInitializer;
import opaide.editors.preferences.PreferencesInitializer.SavedTextAttribute;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.graphics.RGB;

public class OpaStringScanner extends RuleBasedScanner {

	public OpaStringScanner(PreferencesInitializer styleProvider) {
		SavedTextAttribute c = styleProvider.getSavedTextAttribute(OPA_PARTITION.OPA_STRING);
		System.out.println("OpaStringScanner.OpaStringScanner()" + " style= " + c.getStyle());
		IToken stringToken = new Token(new TextAttribute( ColorManager.getColor(c.getColor()), null, c.getStyle() ));
		
		List<IRule> rules = new ArrayList<IRule>();
		//this is already done by the partioner
		//rules.add(new SingleLineRule("\"", "\"", stringToken, '\\'));
		
		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setDefaultReturnToken(stringToken);
		setRules(result);
	}
}
