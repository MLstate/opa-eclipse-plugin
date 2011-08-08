package opaide.editors.opasrc;

import java.util.Random;

import org.eclipse.jface.text.rules.IWordDetector;

public enum SEPARATORS implements IWordDetector {
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
	
	private final EnumImplIWordDetector sub;
	
	private SEPARATORS(String textualRep) {
		this.sub = new EnumImplIWordDetector(this, textualRep);
	}
	private SEPARATORS() {
		this.sub = new EnumImplIWordDetector(this);
	};
	
	public String getTextRep() {
		return sub.getTextRep();
	}
		
	public static SEPARATORS random() {
		Random r = new Random();
		return values()[r.nextInt(values().length)];
	}
	
	public boolean isWordStart(char c) {
		return sub.isWordStart(c);
	}

	public boolean isWordPart(char c) {
		return sub.isWordPart(c);
	}

}
