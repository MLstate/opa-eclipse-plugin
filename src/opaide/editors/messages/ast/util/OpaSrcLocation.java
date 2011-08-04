package opaide.editors.messages.ast.util;

import org.eclipse.core.resources.IResource;

public class OpaSrcLocation {
	
	private IResource theFile;
	private Integer theLine;
	private Integer leftCharacterForTheLine;
	private Integer rightCharacterForTheLine;
	private Integer preciseLineStart;
	private Integer preciseLineCharPosStart;
	private Integer preciseLineEnd;
	private Integer preciseLineCharPosEnd;
	private Integer globalCharStart;
	private Integer globalCharEnd;
	
	public OpaSrcLocation(IResource theFile, Integer theLine, Integer leftCharacterForTheLine, Integer rightCharacterForTheLine,
			Integer preciseLineStart, Integer preciseLineCharPosStart,
			Integer preciseLineEnd, Integer preciseLineCharPosEnd,
			Integer globalCharStart, Integer globalCharEnd) {
		this.theFile = theFile;
		this.theLine = theLine; this.leftCharacterForTheLine = leftCharacterForTheLine; this.rightCharacterForTheLine = rightCharacterForTheLine;
		this.preciseLineStart = preciseLineStart; this.preciseLineCharPosStart = preciseLineCharPosStart;
		this.preciseLineEnd = preciseLineEnd; this.preciseLineCharPosEnd = preciseLineCharPosEnd;
		this.globalCharStart = globalCharStart; this.globalCharEnd = globalCharEnd;
	}
	
	public IResource getTheFile() {
		return theFile;
	}

	public Integer getTheLine() {
		return theLine;
	}

	public Integer getLeftCharacterForTheLine() {
		return leftCharacterForTheLine;
	}

	public Integer getRightCharacterForTheLine() {
		return rightCharacterForTheLine;
	}

	public Integer getPreciseLineStart() {
		return preciseLineStart;
	}

	public Integer getPreciseLineCharPosStart() {
		return preciseLineCharPosStart;
	}

	public Integer getPreciseLineEnd() {
		return preciseLineEnd;
	}

	public Integer getPreciseLineCharPosEnd() {
		return preciseLineCharPosEnd;
	}

	public Integer getGlobalCharStart() {
		return globalCharStart;
	}

	public Integer getGlobalCharEnd() {
		return globalCharEnd;
	}
}
