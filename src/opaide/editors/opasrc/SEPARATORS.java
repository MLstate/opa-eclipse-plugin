package opaide.editors.opasrc;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
	NOTEQUAL ("!=");
	
	@Override
	public String toString() {
		return this.getClass().getCanonicalName() + "." + super.toString();
	}
	
	private final String textualRep;
	private SEPARATORS(String textualRep) {
		assert (textualRep != null);
		assert !(textualRep.isEmpty());
		this.textualRep = textualRep;
	}
	private SEPARATORS() {
		this.textualRep = this.name().toLowerCase();
	};
	
	public String getTextRep() {
		return this.textualRep;
	}
	
	private static Set<Character> allStartingChars;
	public static Set<Character> getAllStartingChars() {
		if (allStartingChars == null) {
			Set <Character> result = new HashSet<Character>();
			for(SEPARATORS k : SEPARATORS.values()) {
				result.add(k.getTextRep().charAt(0));
			};
			allStartingChars = result;
		};
		return allStartingChars;
	}
	
	private static Set<Character> allPossibleChars;
	public static Set<Character> getAllPossibleChars() {
		if (allPossibleChars == null) {
			Set<Character> result = new HashSet<Character>();
			for(SEPARATORS k : SEPARATORS.values()) {
				String tmp = k.getTextRep();
				if (tmp.length() > 1) {
					for (char c : k.getTextRep().substring(1).toCharArray()) { 
						result.add(new Character(c)); 
					};
				};
			};
			allPossibleChars = result;
		};
		return allPossibleChars;
	}
	
	public static SEPARATORS random() {
		Random r = new Random();
		return values()[r.nextInt(values().length)];
	}
	
	@Override
	public boolean isWordStart(char c) {
		return getAllStartingChars().contains(new Character(c));
	}
	@Override
	public boolean isWordPart(char c) {
		return getAllPossibleChars().contains(new Character(c));
	}

}
