package opaide.editors.opasrc;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.text.rules.IWordDetector;

public class EnumImplIWordDetector implements IWordDetector {
	
	private final Set<Character> allStartingChars;
	private final Set<Character> allPossibleChars;
	
	public EnumImplIWordDetector(ITextualRep[] values) {

		Set <Character> tmpAllStartingChars = new HashSet<Character>();
		for(ITextualRep k : values) {
			tmpAllStartingChars.add(k.getTextRep().charAt(0));
		};
		this.allStartingChars = tmpAllStartingChars;
		
		Set <Character> tmpAllPossibleChars = new HashSet<Character>();
		for(ITextualRep k : values) {
			String tmp = k.getTextRep();
			if (tmp.length() > 1) {
				for (char c : k.getTextRep().substring(1).toCharArray()) { 
					tmpAllPossibleChars.add(new Character(c)); 
				};
			};
		};
		this.allPossibleChars = tmpAllPossibleChars;
	}
	
	@Override
	public boolean isWordStart(char c) {
		return allStartingChars.contains(new Character(c));
	}
	@Override
	public boolean isWordPart(char c) {
		return allPossibleChars.contains(new Character(c));
	}
}
