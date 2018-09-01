package play.sadiinso.sgraphutils.dataset;

import java.awt.Color;

import play.sadiinso.sgraphutils.axis.AxisPosition;

public class SGraphDataSet {
	
	private final Color color;
	private final String label;
	private final DataStyle style;
	
	private double[] xVals;
	private double[] yVals;
	
	private final AxisPosition xBinding;
	private final AxisPosition yBinding;
	
	public SGraphDataSet(double[] xVals, double[] yVals, AxisPosition xBinding, AxisPosition yBinding, Color color, String label, DataStyle style) {
		this.xVals = xVals;
		this.yVals = yVals;
		this.xBinding = xBinding;
		this.yBinding = yBinding;
		this.color = color;
		this.label = label;
		this.style = style;
	}
	
	/**
	 * Return the color of the data set
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Return the label of the data set
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Return the data set style object
	 */
	public DataStyle getStyle() {
		return style;
	}
	
	/**
	 * Return the X part of the values of the data set
	 */
	public double[] getXVals() {
		return xVals;
	}
	
	/**
	 * Return the Y part of the values of the data set
	 */
	public double[] getYVals() {
		return yVals;
	}
	
	/**
	 * Return the Axis binded to the X part of the values of the data set
	 */
	public AxisPosition getXBinding() {
		return xBinding;
	}
	
	/**
	 * Return the Axis binded to the Y part of the values of the data set
	 */
	public AxisPosition getYBinding() {
		return yBinding;
	}
	
	/**
	 * Set the values of the data set
	 * @param xVals
	 * @param yVals
	 */
	public void setValues(double[] xVals, double[] yVals) {
		this.xVals = xVals;
		this.yVals = yVals;
	}
	
	/**
	 * Set the values of the data set (y-values only, for simple data set)
	 * @param values The graph values
	 */
	public void setValues(double... values) {
		setValues(null, values);
	}
	
	/**
	 * Return the number of data points in the data set
	 */
	public int getDataLength() {
		return yVals.length;
	}
	
	/**
	 * Return the type of the data set
	 */
	public DataSetType getType() {
		return (xVals == null ? DataSetType.SIMPLE : DataSetType.XY);
	}
	
}
