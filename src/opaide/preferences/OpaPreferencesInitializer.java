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
		private RGB color;
		private FontData fontData;
		public SavedTextAttribute(RGB color, FontData fontData) {
			this.color = color;
			this.fontData = fontData;
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
	
	private  SavedTextAttribute readSavedTextAttribute(String parent) {
		RGB c = PreferenceConverter.getColor(store, parent+suffix_color);
		FontData fd = PreferenceConverter.getFontData(store, parent+suffix_font_data);
		/*System.out.println("PreferencesInitializer.readSavedTextAttribute()" + " reading style=" + fd.getStyle() + " for " + parent);*/
		return new SavedTextAttribute(c, fd);
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
			setDefaultTextAttribute(p.toString(), p.getTextAttribute().getColor(), p.getTextAttribute().getFontData());
		}
		
		for (CODE c : CODE.values()) {
			setDefaultTextAttribute(c.toString(), c.getTextAttribute().getColor(), c.getTextAttribute().getFontData());
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
