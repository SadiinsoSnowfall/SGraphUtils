package play.sadiinso.sgraphutils.axis;

import java.awt.Color;
import java.awt.Font;

public class SGraphAxis {
	private final String[] labels;
	private final double[] pos;
	private final Font font;
	private final Color color;
	
	public SGraphAxis(Color color, int fontSize, String fontName, String[] labels, double[] pos) {
		this.font = new Font(fontName, Font.PLAIN, fontSize);
		this.color = color;
		this.labels = labels;
		this.pos = pos;
	}

	/**
	 * Return the labels of the axis
	 */
	public String[] getLabels() {
		return labels;
	}

	/**
	 * return the positions of the labels in the axis
	 */
	public double[] getPos() {
		return pos;
	}

	/**
	 * Return the font size of the axis
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * Return the color of the axis
	 */
	public Color getColor() {
		return color;
	}
}
