package opaide.editors.preferences;

import opaide.OpaIdePlugin;
import opaide.editors.opasrc.OpaCodeScanner.CODE;
import opaide.editors.opasrc.OpaPartitioner.*;
import org.eclipse.jface.preference.*;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

public class PreferencesInitializer extends AbstractPreferenceInitializer {
	
	public static class SavedTextAttribute {
		private int style;
		private RGB color;
		public SavedTextAttribute(RGB color, int style) {
			this.style = style;
			this.color = color;
		}
		public int getStyle() {
			return style;
		}
		public RGB getColor() {
			return color;
		}
	}
	
	private static String suffix_font_data = "_FONTDATA";
	private static String suffix_color = "_COLOR";	
	
	private void storeSavedTextAttribute(String parent, SavedTextAttribute sta) {
		FontData tmp = new FontData();
		tmp.setStyle(sta.getStyle());
		
		/*System.out.println("PreferencesInitializer.storeSavedTextAttribute()" + " trying to store style=" + tmp.getStyle() + " for " + parent
				+ " versus sta.style=" + sta.getStyle());*/
		
		PreferenceConverter.setDefault(store, parent+suffix_color, sta.getColor());
		PreferenceConverter.setDefault(store, parent+suffix_font_data, tmp);
	};
	
	private  SavedTextAttribute readSavedTextAttribute(String parent) {
		RGB c = PreferenceConverter.getColor(store, parent+suffix_color);
		FontData fd = PreferenceConverter.getFontData(store, parent+suffix_font_data);
		/*System.out.println("PreferencesInitializer.readSavedTextAttribute()" + " reading style=" + fd.getStyle() + " for " + parent);*/
		return new SavedTextAttribute(c, fd.getStyle());
	}

	private static IPreferenceStore store;
	
	public PreferencesInitializer() {
		if (store == null) {
			IPreferenceStore store = OpaIdePlugin.getDefault().getPreferenceStore();
			PreferencesInitializer.store = store;
		}
	}
	
	@Override
	public void initializeDefaultPreferences() {
		//store.addPropertyChangeListener(this);
		
		//store.setDefault(name, "opa");
				
		for (OPA_PARTITION p : OPA_PARTITION.values()) {
			switch (p) {
			case OPA_STRING:
				storeSavedTextAttribute(p.toString(), new SavedTextAttribute(new RGB(64, 144, 225), SWT.ITALIC));
				break;
			case OPA_COMMENT_LINE:
				storeSavedTextAttribute(p.toString(), new SavedTextAttribute(new RGB(171, 74, 0), TextAttribute.STRIKETHROUGH));
				break;
			case OPA_COMMENT_BLOCK:
				storeSavedTextAttribute(p.toString(), new SavedTextAttribute(new RGB(111, 48, 0), SWT.NORMAL));
				break;
			}
		}
		
		for (CODE c : CODE.values()) {
			switch (c) {
			case KEYWORD:
				storeSavedTextAttribute(c.toString(), new SavedTextAttribute(new RGB(0, 255, 255), SWT.BOLD));
				break;
			case SEPARATOR:
				storeSavedTextAttribute(c.toString(), new SavedTextAttribute(new RGB(124, 62, 71), SWT.BOLD));
				break;
			case GENERIC_WORD:
				storeSavedTextAttribute(c.toString(), new SavedTextAttribute(new RGB(20, 20, 20), SWT.NORMAL));
				break;
			}
		}
	}
	
	public SavedTextAttribute getSavedTextAttribute(OPA_PARTITION p) {
		return readSavedTextAttribute(p.toString());
	}
	
	public SavedTextAttribute getSavedTextAttribute(CODE c) {
		return readSavedTextAttribute(c.toString());
	}

	public String getOpaCompilerPath() {
		return "/var/tmp/geoffroy/mlstatelibs/current/bin/opa";
	}

}
