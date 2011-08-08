package opaide.editors.opasrc;

import org.eclipse.jface.text.rules.IWordDetector;

public enum OPAKEYWORDS implements ITextualRep {
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
	
	private final EnumImplITextualRep implTextualRep;
	
	private OPAKEYWORDS(String textualRep) {
		this.implTextualRep = new EnumImplITextualRep(textualRep);
	}
	private OPAKEYWORDS() {
		this.implTextualRep = new EnumImplITextualRep(this);
	};

	public static OPAKEYWORDS random() {
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
