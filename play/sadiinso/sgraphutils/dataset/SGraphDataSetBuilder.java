package play.sadiinso.sgraphutils.dataset;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import play.sadiinso.sgraphutils.axis.AxisPosition;

public class SGraphDataSetBuilder {

	private final double[] xVals;
	private final double[] yVals;
	
	private AxisPosition bindingX = AxisPosition.BOTTOM;
	private AxisPosition bindingY = AxisPosition.LEFT;
	
	private Color color;
	private String label;
	
	private DataStyle style = DataStyle.DEFAULT_STYLE;
	
	/**
	 * Create a new empty simple data set
	 * @param color The color representing the set
	 * @param label The label representing the set
	 * @return The new data set
	 */
	public static SGraphDataSet newSimpleSet(Color color, String label) {
		return new SGraphDataSetBuilder(null, null).setLabel(label).setStyle(color).build();
	}
	
	/**
	 * Create a new SGraphDataSet with only Y values
	 * @param values Y values of the set
	 * @return The new SGraphDataSet object
	 */
	public static SGraphDataSetBuilder newSimpleSet(List<? extends Number> values) {
		double[] yVals = new double[values.size()];
		
		int index = 0;
		for(Number nb : values)
			yVals[index++] = nb.doubleValue();
		
		return new SGraphDataSetBuilder(null, yVals);
	}
	
	/**
	 * Create a new SGraphDataSet with only Y values
	 * @param values Y values of the set
	 * @return The new SGraphDataSet object
	 */
	public static SGraphDataSetBuilder newSimpleSet(double... values) {
		return new SGraphDataSetBuilder(null, values);
	}
	
	/**
	 * Create a new empty XY data set
	 * @param color The color representing the set
	 * @param label The label representing the set
	 * @return The new data set
	 */
	public static SGraphDataSet newDataSet(Color color, String label) {
		return new SGraphDataSetBuilder(null, null).setLabel(label).setStyle(color).build();
	}
	
	/**
	 * Create a new SGraphDataSet with pairs of XY values
	 * @param xValues The list of X values
	 * @param yValues The list of Y values
	 * @return The new SGraphDataSet object or null if the two list don't have the same size
	 */
	public static SGraphDataSetBuilder newDataSet(List<? extends Number> xValues, List<? extends Number> yValues) {
		if(xValues.size() != yValues.size())
			return null;
		
		double[] xVals = new double[xValues.size()];
		double[] yVals = new double[xVals.length];
		
		int index = 0;
		for(Number nb : xValues)
			xVals[index++] = nb.doubleValue();
		
		index = 0;
		for(Number nb : yValues)
			yVals[index++] = nb.doubleValue();
		
		return new SGraphDataSetBuilder(xVals, yVals);
	}
	
	/**
	 * Create a new SGraphDataSet with pairs of XY values
	 * @param values The map containing the pairs of XY values
	 * @return The new SGraphDataSet object
	 */
	public static SGraphDataSetBuilder newDataSet(Map<? extends Number, ? extends Number> values) {
		double[] xVals = new double[values.size()];
		double[] yVals = new double[xVals.length];
		
		int index = 0;
		
		// test if values are already sorted
		if(values instanceof SortedMap) {
			for(Entry<? extends Number, ? extends Number> entry : values.entrySet()) {
				xVals[index] = entry.getKey().doubleValue();
				yVals[index] = entry.getValue().doubleValue();
				++index;
			}
		} else {
			// TODO: implement a better way to sort the map (without creating a new map)
			for(Entry<Number, Number> entry : new TreeMap<Number, Number>(values).entrySet()) {
				xVals[index] = entry.getKey().doubleValue();
				yVals[index] = entry.getValue().doubleValue();
				++index;
			}
		}
		
		return new SGraphDataSetBuilder(xVals, yVals);
	}
	
	public static SGraphDataSetBuilder newDataSet(double[] xVals, double[] yVals) {
		return (xVals.length == yVals.length ? new SGraphDataSetBuilder(xVals, yVals) : null);
	}
	
	// private internal constructor
	private SGraphDataSetBuilder(double[] xVals, double[] yVals) {
		this.xVals = xVals;
		this.yVals = yVals;
	}
	
	/**
	 * Set the axis binding (SimpleDataSet only)
	 */
	public SGraphDataSetBuilder setAxisBinding(AxisPosition axis) {
		return setAxisBinding(null, axis);
	}
	
	/**
	 * Set the axis binding (XYDataSet only)
	 */
	public SGraphDataSetBuilder setAxisBinding(AxisPosition xAxis, AxisPosition yAxis) {
		this.bindingX = xAxis;
		this.bindingY = yAxis;
		return this;
	}
	
	/**
	 * Set the data style
	 * @param style The data style
	 */
	public SGraphDataSetBuilder setDataStyle(DataStyle style) {
		this.style = style;
		return this;
	}
	
	/**
	 * Set the dataSet label
	 * @param label
	 * @return
	 */
	public SGraphDataSetBuilder setLabel(String label) {
		this.label = label;
		return this;
	}
	
	/**
	 * Set the dataSet Color
	 */
	public SGraphDataSetBuilder setStyle(Color color) {
		this.color = color;
		return this;
	}
	
	/**
	 * Build and return the SGraphDataSet object
	 */
	public SGraphDataSet build() {
		return new SGraphDataSet(xVals, yVals, bindingX, bindingY, color, label, style);
	}
	
}
