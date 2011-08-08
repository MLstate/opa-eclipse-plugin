package opaide.editors.opasrc;

public class EnumImplITextualRep implements ITextualRep  {
	
	
	private final String textualRep ;
	
	public EnumImplITextualRep(Enum e) {
		this.textualRep = e.name().toLowerCase();
	};
	public EnumImplITextualRep(String textualRep) {
		assert (textualRep != null);
		assert !(textualRep.isEmpty());
		this.textualRep = textualRep;
	}
	
	public String getTextRep() { 
		return this.textualRep;
	}

}
