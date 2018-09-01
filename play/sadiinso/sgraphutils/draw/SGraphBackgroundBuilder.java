package play.sadiinso.sgraphutils.draw;

import java.awt.Color;
import java.awt.Font;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Map.Entry;

import play.sadiinso.sgraphutils.axis.AxisPosition;
import play.sadiinso.sgraphutils.axis.SGraphAxis;
import play.sadiinso.sgraphutils.dataset.SGraphDataSet;

public class SGraphBackgroundBuilder {
	
	// dimensions
	private int h = 100, w = 200;

	// margins
	private int mT = 0, mR = 0, mB = 0, mL = 0;

	// borders
	private int bT = 0, bR = 0, bB = 0, bL = 0;

	// grid (x space, y space, thickness)
	private int gX = -1, gY = -1, gT = 1, gD = 5;
	private GridStyle gridStyle = GridStyle.DASHED;

	// axis
	private SGraphAxis aT = null, aR = null, aB = null, aL = null;

	// colors
	private Color backgroundColor = Color.WHITE;
	private Color marginColor = Color.WHITE;
	private Color gridColor = Color.GRAY;
	private Color borderColor = Color.BLACK;

	// data labels
	private ArrayList<Entry<String, Color>> dataLabels = new ArrayList<>(4);
	private Font dlFont;
	private Color dlFontColor = Color.GRAY;
	private int dlInnerMargin = 5;

	private RenderQuality settings = RenderQuality.HIGH;
	private BackgroundFlavour flavour = BackgroundFlavour.LINE;
	
	/**
	 * Create a new SGraphBackgroundBuilder with the specified height and width
	 * 
	 * @param width  The graph width
	 * @param height The graph height
	 */
	public SGraphBackgroundBuilder(int width, int height) {
		this.h = height;
		this.w = width;
	}

	/**
	 * Set the margin of the graph
	 * 
	 * @param margin Margin width
	 * @return The SGraphBackgroundBuilder
	 */
	public SGraphBackgroundBuilder setMargin(int margin) {
		return setMargin(margin, margin, margin, margin);
	}

	/**
	 * Set the margins of the graph
	 * 
	 * @param top    Top margin width
	 * @param right  Right margin width
	 * @param bottom Bottom margin width
	 * @param left   Left margin width
	 * @return The SGraphBackgroundBuilder
	 */
	public SGraphBackgroundBuilder setMargin(int top, int right, int bottom, int left) {
		this.mT = top;
		this.mR = right;
		this.mB = bottom;
		this.mL = left;

		return this;
	}

	/**
	 * Set the margin of the graph
	 * 
	 * @param margin Border width
	 * @return The SGraphBackgroundBuilder
	 */
	public SGraphBackgroundBuilder setBorder(int border) {
		return setBorder(border, border, border, border);
	}

	/**
	 * Set the borders of the graph
	 * 
	 * @param top    Top border width
	 * @param right  Right border width
	 * @param bottom Bottom border width
	 * @param left   Left border width
	 * @return The SGraphBackgroundBuilder
	 */
	public SGraphBackgroundBuilder setBorder(int top, int right, int bottom, int left) {
		this.bT = top;
		this.bR = right;
		this.bB = bottom;
		this.bL = left;

		return this;
	}

	/**
	 * Add an axis to the graph
	 * 
	 * @param axis     The axis to add
	 * @param position The position of the axis
	 * @return The SGraphBackgroundBuilder
	 */
	public SGraphBackgroundBuilder setAxis(SGraphAxis axis, AxisPosition position) {
		switch (position) {
			case TOP:
				this.aT = axis;
				break;

			case RIGHT:
				this.aR = axis;
				break;

			case BOTTOM:
				this.aB = axis;
				break;

			case LEFT:
				this.aL = axis;
				break;
		}

		return this;
	}

	/**
	 * Set the colors of the Graph
	 * 
	 * @param background background color
	 * @param grid       grid color
	 * @return The SGraphBackgroundBuilder
	 */
	public SGraphBackgroundBuilder setColors(Color background) {
		return setColors(background, background, this.borderColor);
	}

	/**
	 * Set the colors of the Graph
	 * 
	 * @param background Background color
	 * @param margins    Margin color
	 * @param borders    Border color
	 * @param grid       Grid color
	 * @return The SGraphBackgroundBuilder
	 */
	public SGraphBackgroundBuilder setColors(Color background, Color margins, Color borders) {
		this.backgroundColor = background;
		this.marginColor = margins;
		this.borderColor = borders;
		return this;
	}

	/**
	 * Set the grid intervals
	 * 
	 * @param intervalX (-1 for following top-bottom axis)
	 * @param intervalY (-1 for following left-right axis)
	 * @return The SGraphBackgroundBuilder
	 */
	public SGraphBackgroundBuilder setGrid(int intervalX, int intervalY) {
		this.gX = intervalX;
		this.gY = intervalY;
		return this;
	}

	/**
	 * Set the grid style
	 * 
	 * @param color         The grid color
	 * @param style         The grid style
	 * @param gridThickness The grid Thickness
	 * @param dashSize      Size of the dashes (only if style is set to
	 *                      {@link GridStyle#DASHED}
	 * @return The SGraphBackgroundBuilder
	 */
	public SGraphBackgroundBuilder setGridStyle(Color color, GridStyle style, int gridThickness, int dashSize) {
		this.gridColor = color;
		this.gridStyle = style;
		this.gT = gridThickness;
		this.gD = Math.max(2, dashSize);
		return this;
	}

	/**
	 * Prepare the graph to receive the specified data set (add the label)
	 * 
	 * @param set The data set to prepare
	 * @return The SGraphBackgroundBuilder
	 */
	public SGraphBackgroundBuilder addDataLabel(SGraphDataSet... sets) {
		for (SGraphDataSet set : sets)
			dataLabels.add(new SimpleImmutableEntry<String, Color>(set.getLabel(), set.getColor()));
		return this;
	}

	/**
	 * Prepare the graph to receive the specified data set (add the label)
	 * 
	 * @param label The data label
	 * @param color The color of the data label
	 * @return The SGraphBackgroundBuilder
	 */
	public SGraphBackgroundBuilder addDataLabel(String label, Color color) {
		dataLabels.add(new SimpleImmutableEntry<String, Color>(label, color));
		return this;
	}

	/**
	 * Set the style of the data labels
	 * 
	 * @param font     The font name
	 * @param fontSize The font size
	 * @param color    The font color
	 * @return The SGraphBackgroundBuilder
	 */
	public SGraphBackgroundBuilder setDataLabelsStyle(String font, int fontSize, Color color) {
		this.dlFont = new Font(font, Font.PLAIN, fontSize);
		this.dlFontColor = color;
		return this;
	}

	/**
	 * Set the render quality for the graph background
	 * @param settings The render quality
	 * @return The SGraphBackgroundBuilder
	 */
	public SGraphBackgroundBuilder setRenderQuality(RenderQuality settings) {
		this.settings = settings;
		return this;
	}
	
	/**
	 * Set the graph background flavour (prepare the background for a certain type of graph, {@link BackgroundFlavour#LINE} by default)
	 * @param flavour The BackgroundFlavour
	 * @return The SGraphBackgroundBuilder
	 */
	public SGraphBackgroundBuilder setBackgroundFlavour(BackgroundFlavour flavour) {
		this.flavour = flavour;
		return this;
	}
	
	/**
	 * Build the SGraphBackground object
	 */
	public SGraphBackground build() {
		return new SGraphBackground(w, h, bT, bR, bB, bL, mT, mR, mB, mL, borderColor, marginColor, backgroundColor, aT, aR, aB, aL, gX, gY, gridStyle, gT, gD, gridColor, dataLabels, dlFont, dlFontColor, dlInnerMargin, settings, flavour);
	}

}
