package opaide.editors.opasrc;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.eclipse.jface.text.rules.IWordDetector;

public enum KEYWORDS implements IWordDetector {
	TYPE,
	IF,
	MATCH,
	DO,
	PARSER,
	XML_PARSER,
	DATABASE,
	SERVER,
	REC,
	AND,
	AS,
	CSS,
	DB,
	WITH,
	VAL,
	IMPORT,
	IMPORT_PLUGIN ("import-plugin"),	
	PACKAGE,
	
	THEN,
	ELSE,
	END,
	TRUE,
	FALSE,
	OPEN;

	private final String textualRep;
	private KEYWORDS(String textualRep) {
		assert (textualRep != null);
		assert !(textualRep.isEmpty());
		this.textualRep = textualRep;
	}
	private KEYWORDS() {
		this.textualRep = this.name().toLowerCase();
	};
	
	public String getTextRep() {
		return this.textualRep;
	}
	
	private static Set<Character> allStartingChars;
	private static Set<Character> getAllStartingChars() {
		if (allStartingChars == null) {
			Set <Character> result = new HashSet<Character>();
			for(KEYWORDS k : KEYWORDS.values()) {
				result.add(k.getTextRep().charAt(0));
			};
			allStartingChars = result;
		};
		return allStartingChars;
	}
	
	private static Set<Character> allPossibleChars;
	private static Set<Character> getAllPossibleChars() {
		if (allPossibleChars == null) {
			Set<Character> result = new HashSet<Character>();
			for(KEYWORDS k : KEYWORDS.values()) {
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
	
	public static KEYWORDS random() {
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
