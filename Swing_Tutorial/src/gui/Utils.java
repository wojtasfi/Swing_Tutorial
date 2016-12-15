package gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;

public class Utils {

	public static String getFileExtension(String name) {

		int pointIndex = name.lastIndexOf(".");

		if (pointIndex == -1) { // does not exist
			return null;
		}

		if (pointIndex == name.length() - 1) { // . is the end of string
			return null;
		}

		return name.substring(pointIndex + 1, name.length()); // string between
																// first
																// character
																// after dot to
																// the end
	}

	// Loading images
	public static ImageIcon createIcon(String path) {

		URL url = System.class.getResource(path);

		if (url == null) {
			// JOptionPane.showMessageDialog(Toolbar.this, "Unable to load
			// resuroce: " + path,"Icon problem",
			// JOptionPane.ERROR_MESSAGE);
		}
		ImageIcon icon = new ImageIcon(url);
		return icon;
	}
	
	
	public static Font createFont(String path) {

		URL url = System.class.getResource(path);

		if (url == null) {
			// JOptionPane.showMessageDialog(Toolbar.this, "Unable to load
			// resuroce: " + path,"Font problem",
			// JOptionPane.ERROR_MESSAGE);
		}
		
		
		Font font = null;
		/*
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		*/
		return font;
		
	}
}
