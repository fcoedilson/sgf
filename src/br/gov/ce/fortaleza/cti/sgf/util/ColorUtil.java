package br.gov.ce.fortaleza.cti.sgf.util;

public class ColorUtil {

	private static final String RED = "0000FF";
	private static final String LIME = "00FF00";
	private static final String BLUE = "FF0000";
	private static final String AQUA = "FFFF00";
	private static final String YELLOW = "00FFFF";
	private static final String FUCHSIA = "FF00FF";
	private static final String WHITE = "FFFFFF";
	private static final String MAROON = "000080";
	private static final String GREEN = "008000";
	private static final String NAVY = "800000";
	private static final String OLIVE = "008080";
	private static final String TEAL = "808000";
	private static final String PURPLE = "800080";
	private static final String GRAY = "808080";
	private static final String SILVER = "C0C0C0";
	private static final String BLACK = "000000";

	private static final String[] color = {RED, LIME, BLUE, AQUA, YELLOW, FUCHSIA, WHITE, MAROON, 
										 GREEN, NAVY, OLIVE, TEAL, PURPLE, GRAY, SILVER, BLACK};

	public static String getColor(int i) {
		while (i >= color.length) {
			i = i - color.length;
		}
		return color[i];
	}
}