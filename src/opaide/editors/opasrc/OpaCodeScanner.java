package opaide.editors.opasrc;

import java.util.ArrayList;

import java.util.List;

import opaide.OpaIdePlugin;
import opaide.editors.ColorManager;
import opaide.preferences.OpaPreferencesInitializer;
import opaide.preferences.OpaPreferencesInitializer.SavedTextAttribute;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.internal.dnd.SwtUtil;
import org.eclipse.ui.internal.texteditor.SWTUtil;

public class OpaCodeScanner extends RuleBasedScanner {
	
	public enum CODE {
		KEYWORD {
			@Override
			public IWordDetector getWordDetector() {
				return OPAKEYWORDS.getWordDetector();
			}
			@Override
			public ITextualRep[] getTextualReps() {
				return OPAKEYWORDS.values();
			}
			@Override
			public SavedTextAttribute getTextAttribute() {
				return new SavedTextAttribute(new RGB(0, 255, 255), createFontData(SWT.BOLD));
			}
		},
		SEPARATOR {
			@Override
			public IWordDetector getWordDetector() {
				return OPASEPARATORS.getWordDetector();
			}
			@Override
			public ITextualRep[] getTextualReps() {
				return OPASEPARATORS.values();
			}
			@Override
			public SavedTextAttribute getTextAttribute() {
				return new SavedTextAttribute(new RGB(124, 62, 71), createFontData(SWT.BOLD));
			}
		},
		GENERIC_WORD {
			@Override
			public IWordDetector getWordDetector() {
				return new IWordDetector() {			
					@Override
					public boolean isWordStart(char c) {
						return ((c=='_') || (c>='a' && c<='z') || (c>='A' && c<='Z'));
					}			
					@Override
					public boolean isWordPart(char c) {
						return ((c>='a' && c<='z') || (c>='A' && c<='Z') || (c>='0' && c<='9') || c=='_');
					}
				};
			}
			@Override
			public ITextualRep[] getTextualReps() {
				return new ITextualRep[]{};
			}
			@Override
			public SavedTextAttribute getTextAttribute() {
				return new SavedTextAttribute(new RGB(20, 20, 20), createFontData(SWT.NORMAL));
			}
		};
		
		@Override
		public String toString() {
			return this.getClass().getCanonicalName() + "." + super.toString();
		}
		
		public abstract IWordDetector getWordDetector();
		public abstract ITextualRep[] getTextualReps();
		
		public abstract SavedTextAttribute getTextAttribute();
		
		private static FontData createFontData(int style) {
			FontData tmp = new FontData("Monospace", 10, style);
			return tmp;
		}
				
	}
	

	public OpaCodeScanner(OpaPreferencesInitializer styleProvider) {
		super();
		
		List<IRule> rules = new ArrayList<IRule>();
		
		for (CODE c : CODE.values()) {
			SavedTextAttribute attrForKeyword = styleProvider.getSavedTextAttribute(c);
			TextAttribute someTextAttribute = SavedTextAttribute.toTextAttribute(OpaIdePlugin.getDefault().getDisplay(), attrForKeyword);
			IToken keywordToken = new Token(someTextAttribute);
			ITextualRep[] values = c.getTextualReps();
			
			WordRule keywordRule = 
					(values.length == 0) ? new WordRule(c.getWordDetector(), keywordToken) : new WordRule(c.getWordDetector());
			for (ITextualRep k : values) {
				keywordRule.addWord(k.getTextRep(), keywordToken);
			}
			rules.add(keywordRule);
		}

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		IToken defaultToken = new Token(new TextAttribute(ColorManager.getColor(new RGB(0, 255, 0))));
		setDefaultReturnToken(defaultToken);
		setRules(result);
	}
}
