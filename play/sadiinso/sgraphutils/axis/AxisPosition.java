package play.sadiinso.sgraphutils.axis;

public enum AxisPosition {
	TOP(0),
	RIGHT(1),
	BOTTOM(2),
	LEFT(3);
	
	public final int id;
	private AxisPosition(int id) {
		this.id = id;
	}
	
	/**
	 * Return true if the given position if LEFT or RIGHT else false
	 */
	public boolean isSide() {
		return ((this == RIGHT) || (this == LEFT));
	}
	
	
}
