package opaide.editors.opasrc;

import java.util.Random;

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
	
	public static KEYWORDS random() {
		Random r = new Random();
		return values()[r.nextInt(values().length)];
	}
	
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
}
