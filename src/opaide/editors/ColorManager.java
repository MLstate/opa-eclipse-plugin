package opaide.editors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public final class ColorManager {

	protected static Map<RGB, Color> fColorTable = new HashMap<RGB, Color>(10);

	public static void dispose() {
		Iterator<Color> e = fColorTable.values().iterator();
		while (e.hasNext()) {
			e.next().dispose();
		}			 
	}
	public static Color getColor(RGB rgb) {
		Color color = (Color) fColorTable.get(rgb);
		if (color == null || color.isDisposed()) {
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
}
