package play.sadiinso.sgraphutils.draw;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import play.sadiinso.sgraphutils.dataset.DataStyle;
import play.sadiinso.sgraphutils.dataset.SGraphDataSet;

public abstract class AbstractSGraphBuilder {

	protected final SGraphBackground bg;
	protected final ArrayList<SGraphDataSet> dataSets = new ArrayList<>(4);
	protected RenderQuality settings = RenderQuality.HIGH;
	protected DataStyle dataStyle;
	
	protected AbstractSGraphBuilder(SGraphBackground bg) {
		this.bg = bg;
	}
	
	/**
	 * Build the graph (draw the data set onto the background)
	 * @return The graph BufferedImage
	 */
	public abstract BufferedImage build();
	
	/**
	 * Set the render quality
	 * @param settings The render quality
	 * @return The SGraphBuilder object
	 */
	public AbstractSGraphBuilder setRenderQuality(RenderQuality settings) {
		this.settings = settings;
		return this;
	}
	
	/**
	 * Set the style for all the data set
	 * @param DataStyle The style to apply
	 * @return
	 */
	public AbstractSGraphBuilder setDataStyle(DataStyle style) {
		this.dataStyle = style;
		return this;
	}
	
	/**
	 * Bind the specified dataSets to the graph
	 * @param sets The set(s) to bind
	 * @return The SGraphBuilder object
	 */
	public AbstractSGraphBuilder addDataSet(SGraphDataSet... sets) {
		for(SGraphDataSet set : sets)
			dataSets.add(set);
		
		return this;
	}
	
	/**
	 * Bind the specified dataSets to the graph
	 * @param sets The set(s) to bind
	 * @return The SGraphBuilder object
	 */
	public AbstractSGraphBuilder addDataSet(List<? extends SGraphDataSet> sets) {
		for(SGraphDataSet set : sets)
			dataSets.add(set);
		
		return this;
	}
	
	protected static void resolvePos(int[] pos, double from, double to, int rFrom, int rTo, double[] base, boolean isY) {
		double dL = from - to;
		double dP = rFrom - rTo;
		rTo += (isY ? 1 : -1);
		
		for(int t = 0; t < base.length; t++) {
			if(base[t] == to)
				pos[t] = rTo;
			else if(base[t] == from)
				pos[t] = rFrom;
			else
				pos[t] = (int) ((base[t] - from) / dL * dP + rFrom);
		}
	}
	
}
