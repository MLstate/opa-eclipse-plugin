package opaide.editors.opasrc;

import java.util.ArrayList;

import java.util.List;

import opaide.editors.ColorManager;
import opaide.preferences.OpaPreferencesInitializer;
import opaide.preferences.OpaPreferencesInitializer.SavedTextAttribute;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.graphics.RGB;

public class OpaCodeScanner extends RuleBasedScanner {
	
	public enum CODE {
		KEYWORD,
		SEPARATOR,
		GENERIC_WORD;
		@Override
		public String toString() {
			return this.getClass().getCanonicalName() + "." + super.toString();
		}
	}
	

	public OpaCodeScanner(OpaPreferencesInitializer styleProvider) {
		super();
		
		List<IRule> rules = new ArrayList<IRule>();
		
		{	SavedTextAttribute attrForKeyword = styleProvider.getSavedTextAttribute(CODE.KEYWORD);
			IToken keywordToken = new Token(new TextAttribute( ColorManager.getColor(attrForKeyword.getColor())));
			WordRule keywordRule = new WordRule(OPAKEYWORDS.random());
			for (OPAKEYWORDS k : OPAKEYWORDS.values()) {
				keywordRule.addWord(k.getTextRep(), keywordToken);
			}
			rules.add(keywordRule);
		};
		
		{	SavedTextAttribute attrForSeparator = styleProvider.getSavedTextAttribute(CODE.SEPARATOR);
			IToken separatorsToken = new Token(new TextAttribute( ColorManager.getColor(attrForSeparator.getColor())));
			WordRule separatorsRule = new WordRule(SEPARATORS.random());
			for (SEPARATORS k : SEPARATORS.values()) {
				separatorsRule.addWord(k.getTextRep(), separatorsToken);
			}
			rules.add(separatorsRule);
		};
		
		{	SavedTextAttribute attrForGenericWord = styleProvider.getSavedTextAttribute(CODE.GENERIC_WORD);
			IToken genericWordToken = new Token(new TextAttribute( ColorManager.getColor(attrForGenericWord.getColor())));
			WordRule basicWordRule = new WordRule(new IWordDetector() {			
				@Override
				public boolean isWordStart(char c) {
					return ((c=='_') || (c>='a' && c<='z') || (c>='A' && c<='Z'));
				}			
				@Override
				public boolean isWordPart(char c) {
					return ((c>='a' && c<='z') || (c>='A' && c<='Z') || (c>='0' && c<='9') || c=='_');
				}
			}, genericWordToken);
			rules.add(basicWordRule);
		};
		
		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		IToken defaultToken = new Token(new TextAttribute(ColorManager.getColor(new RGB(0, 255, 0))));
		setDefaultReturnToken(defaultToken);
		setRules(result);
	}
}
