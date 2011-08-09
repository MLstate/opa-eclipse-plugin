package opaide.editors.opasrc;

import java.util.ArrayList;
import java.util.List;

import opaide.preferences.OpaPreferencesInitializer;
import opaide.preferences.OpaPreferencesInitializer.SavedTextAttribute;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

public class OpaPartitioner extends RuleBasedPartitionScanner {
	public enum OPA_PARTITION {
		OPA_STRING {
			@Override
			public IPredicateRule getPredicateRule() {
				IToken token = new Token(this.getContentType());
				return new MultiLineRule("\"", "\"", token, '\\');
			}

			@Override
			public ITokenScanner getTokenScanner(OpaPreferencesInitializer styleProvider) {
				return new OpaStringScanner(styleProvider);
			}

			@Override
			public SavedTextAttribute getTextAttribute() {
				return new SavedTextAttribute(new RGB(64, 144, 225), new FontData("Georgia", 10, SWT.ITALIC));
			}
		},
		OPA_COMMENT_LINE {
			@Override
			public IPredicateRule getPredicateRule() {
				IToken token = new Token(this.getContentType());
				return new EndOfLineRule("//", token);
			}

			@Override
			public ITokenScanner getTokenScanner(OpaPreferencesInitializer styleProvider) {
				return new OpaCommentLineScanner(styleProvider);
			}

			@Override
			public SavedTextAttribute getTextAttribute() {
				return new SavedTextAttribute(new RGB(171, 74, 0), new FontData("Serif", 10, TextAttribute.STRIKETHROUGH));
			}
		},
		OPA_COMMENT_BLOCK {
			@Override
			public IPredicateRule getPredicateRule() {
				IToken token = new Token(this.getContentType());
				return new MultiLineRule("/*", "*/", token);
			}

			@Override
			public ITokenScanner getTokenScanner(OpaPreferencesInitializer styleProvider) {
				return new OpaCommentBlockScanner(styleProvider);
			}

			@Override
			public SavedTextAttribute getTextAttribute() {
				return new SavedTextAttribute(new RGB(111, 48, 0), createDefaultFontData(SWT.NORMAL));
			}
		};
		
		@Override
		public String toString() {
			return this.getClass().getCanonicalName() + "." + super.toString();
		}
		
		public String getContentType() {
			return String.format("__%s_%s", OPA_PARTITION.class.getName(), this.name());
		}
		
		private static List<String> contentTypes;
		public static List<String> getContentTypes() {
			if (contentTypes == null) {
				List<String> result = new ArrayList<String>();
				for (OPA_PARTITION p : OPA_PARTITION.values())
					result.add(p.getContentType());
				contentTypes = result;
			}
			return contentTypes;
		}
		
		public abstract IPredicateRule getPredicateRule();
		public abstract ITokenScanner getTokenScanner(OpaPreferencesInitializer styleProvider);
		public abstract SavedTextAttribute getTextAttribute();
		
		private static FontData createDefaultFontData(int style) {
			return new FontData("Monospace", 10, style);
		}

	};
	
	public OpaPartitioner() {
		final List<IPredicateRule> rules = new ArrayList<IPredicateRule>();
		
		for (OPA_PARTITION p : OPA_PARTITION.values()) {
			rules.add(p.getPredicateRule());
		}
		
		IPredicateRule[] result= rules.toArray(new IPredicateRule[rules.size()]);
		setPredicateRules(result);
		
		
	}
}
