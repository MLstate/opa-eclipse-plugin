package opaide.editors.opasrc;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.text.rules.IWordDetector;

public class EnumImplIWordDetector implements IWordDetector {
	
	private final String textualRep ;
	private Enum e;
	
	public EnumImplIWordDetector(Enum e) {
		this.e = e;
		this.textualRep = e.name().toLowerCase();
	};
	public EnumImplIWordDetector(Enum e, String textualRep) {
		this.e = e;
		assert (textualRep != null);
		assert !(textualRep.isEmpty());
		this.textualRep = textualRep;
	}
	
	public String getTextRep() {
		return this.textualRep;
	}
	
	private static Set<Character> allStartingChars;
	public static Set<Character> getAllStartingChars() {
		if (allStartingChars == null) {
			Set <Character> result = new HashSet<Character>();
			for(OPAKEYWORDS k : OPAKEYWORDS.values()) {
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
			for(OPAKEYWORDS k : OPAKEYWORDS.values()) {
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
	
	@Override
	public boolean isWordStart(char c) {
		return getAllStartingChars().contains(new Character(c));
	}
	@Override
	public boolean isWordPart(char c) {
		return getAllPossibleChars().contains(new Character(c));
	}

}
