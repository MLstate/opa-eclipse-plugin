package opaide.editors.opasrc;

import org.eclipse.jface.text.rules.IWordDetector;

public enum OPASEPARATORS implements ITextualRep {
	PLUS ("+"),
	STAR ("*"),
	L_BRACKET ("{"),
	R_BRACKET ("}"),
	SEMICOLON (";"),
	BACKSLASH ("\\"),
	COMMA (","),
	ARROW_TO_RIGHT ("->"),
	ARROW_TO_LEFT ("<-"),
	PIPE ("|"),
	MINUS("-"),
	TILDE ("~"),
	SHARP ("#"),
	L_PAREN ("("),
	R_PAREN (")"),
	COLON (":"),
	SLASH ("/"),
	DOT ("."),
	EQUAL ("="),
	NOTEQUAL ("!="),
	BOOL_AND ("&&"),
	BOOL_OR ("||");
	
	@Override
	public String toString() {
		return this.getClass().getCanonicalName() + "." + super.toString();
	}
	
	private final EnumImplITextualRep implTextualRep;
	
	private OPASEPARATORS(String textualRep) {
		this.implTextualRep = new EnumImplITextualRep(textualRep);
	}
	private OPASEPARATORS() {
		this.implTextualRep = new EnumImplITextualRep(this);
	};

	public static OPASEPARATORS random() {
		return EnumImplRandom.random(values());
	}
	
	public String getTextRep() {
		return this.implTextualRep.getTextRep();
	}
	
	private static final IWordDetector implIWordDetector = new EnumImplIWordDetector(values());
	
	public static IWordDetector getWordDetector() {
		return implIWordDetector;
	}

}
