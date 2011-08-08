package opaide.editors.opasrc;

import java.util.Random;

public class EnumRandom {
	
	private static Random r = new Random();
	
	public static <X> X random(X[] src) {
		return src[r.nextInt(src.length)];
	}
	
}
