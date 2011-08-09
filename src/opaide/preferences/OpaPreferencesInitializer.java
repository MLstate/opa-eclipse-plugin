package opaide.preferences;

import opaide.OpaIdePlugin;
import opaide.editors.ColorManager;
import opaide.editors.opasrc.OpaCodeScanner.CODE;
import opaide.editors.opasrc.OpaPartitioner.*;
import org.eclipse.jface.preference.*;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

public class OpaPreferencesInitializer extends AbstractPreferenceInitializer {
	
	public static class SavedTextAttribute {
		private int style;
		private RGB color;
		private FontData fontData;
		public SavedTextAttribute(RGB color, int style, FontData fontData) {
			this.style = style;
			this.color = color;
			this.fontData = fontData;
		}
		public int getStyle() {
			return style;
		}
		public RGB getColor() {
			return color;
		}
		public FontData getFontData() {
			return fontData;
		}
		
		public static TextAttribute toTextAttribute(Device device, SavedTextAttribute stattr) {
			FontData fontData = stattr.getFontData();
			Font theFont = new Font(device, fontData);
			Color foreground = ColorManager.getColor(stattr.getColor());
			return new TextAttribute(foreground, null, fontData.getStyle(), theFont);
		}
	}
	
	private static String suffix_font_data = "_FONTDATA";
	private static String suffix_color = "_COLOR";	
	
	private void setDefaultTextAttribute(String parent, RGB color, FontData font) {
		PreferenceConverter.setDefault(store, parent+suffix_color, color);
		PreferenceConverter.setDefault(store, parent+suffix_font_data, font);
	}
	
	private void storeSavedTextAttribute(String parent, SavedTextAttribute sta) {
		FontData tmp = new FontData("Monospace", 10, sta.getStyle());
		
		setDefaultTextAttribute(parent, sta.getColor(), tmp);
	};
	
	private  SavedTextAttribute readSavedTextAttribute(String parent) {
		RGB c = PreferenceConverter.getColor(store, parent+suffix_color);
		FontData fd = PreferenceConverter.getFontData(store, parent+suffix_font_data);
		/*System.out.println("PreferencesInitializer.readSavedTextAttribute()" + " reading style=" + fd.getStyle() + " for " + parent);*/
		return new SavedTextAttribute(c, fd.getStyle(), fd);
	}

	private static IPreferenceStore store;
	
	public OpaPreferencesInitializer() {
		if (store == null) {
			IPreferenceStore store = OpaIdePlugin.getDefault().getPreferenceStore();
			OpaPreferencesInitializer.store = store;
		}
	}
	
	@Override
	public void initializeDefaultPreferences() {
		
		//store.addPropertyChangeListener(this);
		
		//store.setDefault(name, "opa");
		store.setDefault(OpaPreferencesConstants.P_OPA_COMPILER_PATH, "/usr/bin/opa");
				
		for (OPA_PARTITION p : OPA_PARTITION.values()) {
			switch (p) {
			case OPA_STRING:
				storeSavedTextAttribute(p.toString(), new SavedTextAttribute(new RGB(64, 144, 225), SWT.ITALIC, new FontData()));
				break;
			case OPA_COMMENT_LINE:
				storeSavedTextAttribute(p.toString(), new SavedTextAttribute(new RGB(171, 74, 0), TextAttribute.STRIKETHROUGH, new FontData()));
				break;
			case OPA_COMMENT_BLOCK:
				storeSavedTextAttribute(p.toString(), new SavedTextAttribute(new RGB(111, 48, 0), SWT.NORMAL, new FontData()));
				break;
			}
		}
		
		for (CODE c : CODE.values()) {
			setDefaultTextAttribute(c.toString(), c.getDefaultRGB(), c.getDefaultFontData());
		}

	}
	
	public SavedTextAttribute getSavedTextAttribute(OPA_PARTITION p) {
		return readSavedTextAttribute(p.toString());
	}
	
	public SavedTextAttribute getSavedTextAttribute(CODE c) {
		return readSavedTextAttribute(c.toString());
	}

	public String getOpaCompilerPath() {
		return store.getString(OpaPreferencesConstants.P_OPA_COMPILER_PATH);
	}
	
	public String getOpaMLSTATELIBS() {
		return store.getString(OpaPreferencesConstants.P_OPA_MLSTATELIBS);
	}

}
