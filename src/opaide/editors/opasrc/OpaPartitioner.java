package opaide.editors.opasrc;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.*;

public class OpaPartitioner extends RuleBasedPartitionScanner {
	public enum OPA_PARTITION {
		OPA_STRING,
		OPA_COMMENT_LINE,
		OPA_COMMENT_BLOCK;
		
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
	};
	
	public OpaPartitioner() {
		List<IPredicateRule> rules = new ArrayList<IPredicateRule>();
		
		IToken opaString = new Token(OPA_PARTITION.OPA_STRING.getContentType());
		rules.add(new MultiLineRule("\"", "\"", opaString, '\\'));
		
		IToken opaCommentLine = new Token(OPA_PARTITION.OPA_COMMENT_LINE.getContentType());
		rules.add(new EndOfLineRule("//", opaCommentLine));
		
		IToken opaCommentBlock = new Token(OPA_PARTITION.OPA_COMMENT_BLOCK.getContentType());
		rules.add(new MultiLineRule("/*", "*/", opaCommentBlock));
		
		IPredicateRule[] result= rules.toArray(new IPredicateRule[rules.size()]);
		setPredicateRules(result);
		
		
	}
}
