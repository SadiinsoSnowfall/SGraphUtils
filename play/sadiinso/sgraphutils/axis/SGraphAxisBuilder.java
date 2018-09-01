package play.sadiinso.sgraphutils.axis;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SGraphAxisBuilder {

	private String[] labels;
	private double[] pos;
	
	private Color color = Color.BLACK;
	private String fontName = "Arial";
	private int fontSize = 16;
	
	private SGraphAxisBuilder(String[] labels, double[] pos) {
		this.labels = labels;
		this.pos = pos;
	}
	
	public SGraphAxisBuilder setStyle(Color color, int fontSize) {
		this.color = color;
		this.fontSize = fontSize;
		return this;
	}
	
	public SGraphAxisBuilder setFont(String font) {
		this.fontName = font;
		return this;
	}
	
	public SGraphAxis build() {
		return new SGraphAxis(color, fontSize, fontName, labels, pos);
	}
	
	public static SGraphAxisBuilder newAxis(List<? extends String> values, List<? extends Number> pos) {
		if (pos.size() != values.size())
			return null;

		String[] labels = new String[values.size()];
		double[] poss = new double[pos.size()];

		int index = 0;
		for (Number nb : pos)
			poss[index++] = nb.doubleValue();

		return new SGraphAxisBuilder(values.toArray(labels), poss);
	}

	/**
	 * Create a new SGraphAxis with the specified values
	 * 
	 * @param color    The color of the axis
	 * @param fontSize The font size of the axis
	 * @param values   The labels of the axis
	 * @param pos      The position corresponding to the values
	 * @return The new SGraphAxis object
	 */
	public static SGraphAxisBuilder newAxis(String[] values, double[] pos) {
		if(values == null) {
			values = new String[pos.length];
			Arrays.fill(values, "");
		}
		return (values.length == pos.length ? new SGraphAxisBuilder(values, pos) : null);
	}

	/**
	 * Create a new SGraphAxis with the specified values
	 * 
	 * @param color    The color of the axis
	 * @param fontSize The font size of the axis
	 * @param values   The labels of the axis
	 * @return The new SGraphAxis object
	 */
	public static SGraphAxisBuilder newAxis(double... values) {
		return newAxis(0, 100, values);
	}

	/**
	 * Create a new SGraphAxis with the specified values
	 * 
	 * @param color     The color of the axis
	 * @param fontSize  The font size of the axis
	 * @param minDigits The minimum fraction digits of the values
	 * @param maxDigits The maximum fraction digits of the values
	 * @param values    The labels of the axis
	 * @return The new SGraphAxis object
	 */
	public static SGraphAxisBuilder newAxis(int minDigits, int maxDigits, double... values) {
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(minDigits);
		df.setMaximumFractionDigits(maxDigits);

		// sort axis positions
		Arrays.sort(values);

		String[] labels = new String[values.length];
		double[] pos = new double[labels.length];

		for (int t = 0; t < values.length; t++) {
			labels[t] = df.format(values[t]);
			pos[t] = values[t];
		}

		return new SGraphAxisBuilder(labels, pos);
	}

	/**
	 * Create a new SGraphAxis with the specified values
	 * 
	 * @param color    The color of the axis
	 * @param fontSize The font size of the axis
	 * @param min      The minimum value of the numeric range
	 * @param max      The maximum value of the numeric range
	 * @param interval The interval of the numeric range
	 * @return The new SGraphAxis object
	 */
	public static SGraphAxisBuilder newNumericRangeAxis(double min, double max, double interval) {
		return newNumericRangeAxis(min, max, interval, 0, 2);
	}

	/**
	 * Create a new SGraphAxis with the specified values
	 * 
	 * @param color     The color of the axis
	 * @param fontSize  The font size of the axis
	 * @param min       The minimum value of the numeric range
	 * @param max       The maximum value of the numeric range
	 * @param interval  The interval of the numeric range
	 * @param minDigits The minimum fraction digits of the values
	 * @param maxDigits The maximum fraction digits of the values
	 * @return The new SGraphAxis object
	 */
	public static SGraphAxisBuilder newNumericRangeAxis(double min, double max, double interval, int minDigits, int maxDigits) {
		if(interval <= 0)
			return null;
		
		if(minDigits > maxDigits)
			maxDigits = minDigits;
		
		DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.ENGLISH);
		df.setMinimumFractionDigits(minDigits);
		df.setMaximumFractionDigits(maxDigits);

		String[] labels = new String[(int) ((max - min) / interval) + 1];
		double[] pos = new double[labels.length];
		double val = min;
		
		for(int index = 0; index < labels.length; index++, val += interval) {
			labels[index] = df.format(val);
			pos[index] = val;
		}
		
		return new SGraphAxisBuilder(labels, pos);
	}
	
}
