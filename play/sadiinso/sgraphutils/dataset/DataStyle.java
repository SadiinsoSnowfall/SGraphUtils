package play.sadiinso.sgraphutils.dataset;

public class DataStyle {

	public static final DataStyle DEFAULT_STYLE = new DataStyle(StrokeStyle.PLAIN, 0, PointStyle.NONE, 0);
	
	public final StrokeStyle strokeStyle;
	public final int alphaFill;
	public final PointStyle pointStyle;
	public final int pointSize;
	
	public DataStyle(StrokeStyle strokeStyle, int alphaFill, PointStyle pointStyle, int pointSize) {
		this.strokeStyle = strokeStyle;
		this.alphaFill = alphaFill;
		this.pointStyle = pointStyle;
		this.pointSize = pointSize;
	}
	
	public static enum StrokeStyle {
		NONE, PLAIN, DASHED;
	}
	
	public static enum PointStyle {
		NONE, SQUARE, CIRCLE;
	}
	
}
