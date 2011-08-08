package opaide.editors.opasrc;

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
	
	private final EnumImplIWordDetector sub;
	
	private KEYWORDS(String textualRep) {
		this.sub = new EnumImplIWordDetector(this, textualRep);
	}
	private KEYWORDS() {
		this.sub = new EnumImplIWordDetector(this);
	};
	
	
	public String getTextRep() {
		return sub.getTextRep();
	}
	
	@Override
	public boolean isWordStart(char c) {
		return sub.isWordStart(c);
	}
	@Override
	public boolean isWordPart(char c) {
		return sub.isWordPart(c);
	}

	public static KEYWORDS random() {
		return EnumRandom.random(values());
	}
	
}
