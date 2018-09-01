package play.sadiinso.sgraphutils.dataset;

import play.sadiinso.sgraphutils.dataset.DataStyle.PointStyle;
import play.sadiinso.sgraphutils.dataset.DataStyle.StrokeStyle;

public class DataStyleBuilder {

	private StrokeStyle strokeStyle;
	private int alphaFill= 0;
	private PointStyle pointStyle;
	private int pointSize = 8;
	
	public DataStyleBuilder(StrokeStyle stroke, PointStyle points) {
		this.strokeStyle = stroke;
		this.pointStyle = points;
	}
	
	public DataStyleBuilder setAlphaFill(int alpha) {
		this.alphaFill = alpha;
		return this;
	}
	
	public DataStyleBuilder setAlphaFill(String alpha) {
		this.alphaFill = (int) Math.ceil(Double.parseDouble(alpha.substring(0, alpha.length() - 1)) * 2.55);
		return this;
	}
	
	public DataStyleBuilder setPointSize(int pointSize) {
		this.pointSize = pointSize;
		return this;
	}
	
	public DataStyle build() {
		return new DataStyle(strokeStyle, alphaFill, pointStyle, pointSize);
	}
}
