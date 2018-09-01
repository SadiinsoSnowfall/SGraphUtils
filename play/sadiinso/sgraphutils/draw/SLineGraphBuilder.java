package play.sadiinso.sgraphutils.draw;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import play.sadiinso.sgraphutils.dataset.DataSetType;
import play.sadiinso.sgraphutils.dataset.DataStyle;
import play.sadiinso.sgraphutils.dataset.DataStyle.PointStyle;
import play.sadiinso.sgraphutils.dataset.DataStyle.StrokeStyle;
import play.sadiinso.sgraphutils.dataset.SGraphDataSet;
import play.sadiinso.sgraphutils.exceptions.AxisBindingException;

public class SLineGraphBuilder extends AbstractSGraphBuilder {
	
	public SLineGraphBuilder(SGraphBackground graphBackground) {
		super(graphBackground);
	}
	
	@Override
	public BufferedImage build() {
		BufferedImage img = bg.getBackgroundCopy();
		Graphics2D g = img.createGraphics();
		DrawUtils.setRenderingHints(g, settings);
		
		double[][] axisPos = bg.getAxisPos();
		int[] drawZone = bg.getDrawZone();
		
		for(SGraphDataSet set : dataSets) {
			if(set.getType() == DataSetType.SIMPLE) {
				drawSimpleDataSet(g, drawZone, set, axisPos[set.getYBinding().id]);
				
			} else if(set.getType() == DataSetType.XY) {
				drawXYDataSet(g, drawZone, set, axisPos[set.getXBinding().id], axisPos[set.getYBinding().id]);
			}
		}
		
		// finalize
		g.dispose();
		
		return img;
	}
	
	private void drawSimpleDataSet(Graphics2D g, int[] drawZone, SGraphDataSet set, double[] axisPos) {
		if(axisPos == null)
			throw new AxisBindingException("No axis binded to data set \"" + set.getLabel() + "\"");
		
		double[] values = set.getYVals();
		
		// skip empty sets
		if(values == null)
			return;
		
		int[] resX = new int[values.length];
		int[] resY = new int[values.length];
		
		if(set.getYBinding().isSide())		
			fillPosSimple(resX, resY, values, axisPos, drawZone[SGraphBackground.zMY], drawZone[SGraphBackground.zY], drawZone[SGraphBackground.zX], drawZone[SGraphBackground.zMX], true);
		else
			fillPosSimple(resY, resX, values, axisPos, drawZone[SGraphBackground.zX], drawZone[SGraphBackground.zMX], drawZone[SGraphBackground.zY], drawZone[SGraphBackground.zMY], false);
	
		drawFinal(g, resX, resY, drawZone, set);
	}
	
	private static void fillPosSimple(int[] xPos, int[] yPos, double[] values, double[] axisPos, int zMin1, int zMax1, int zMin2, int zMax2, boolean isY) {
		double interval = zMax2 / values.length;
		double current = zMin2 + 1;
		
		resolvePos(yPos, axisPos[0], axisPos[1], zMin1, zMax1, values, isY);
		
		xPos[values.length - 1] = zMax2;
		for(int t = 0; t < values.length - 1; t++, current += interval)
			xPos[t] = (int) current;
		
	}
	
	private void drawXYDataSet(Graphics2D g, int[] drawZone, SGraphDataSet set, double[] axisPosX, double[] axisPosY) {
		if((axisPosX == null) || (axisPosY == null))
			throw new AxisBindingException("No axis binded to data set \"" + set.getLabel() + "\"");
		
		double[] xVals = set.getXVals();
		double[] yVals = set.getYVals();
		
		// skip empty sets
		if((xVals == null) || (yVals == null))
			return;
		
		int[] xPos = new int[xVals.length];
		int[] yPos = new int[xVals.length];
		
		resolvePos(xPos, axisPosX[0], axisPosX[1], drawZone[SGraphBackground.zX], drawZone[SGraphBackground.zMX], xVals, false);
		resolvePos(yPos, axisPosY[0], axisPosY[1], drawZone[SGraphBackground.zMY], drawZone[SGraphBackground.zY], yVals, true);
		
		System.out.println(Arrays.toString(xPos));
		System.out.println(Arrays.toString(yPos));
		
		drawFinal(g, xPos, yPos, drawZone, set);
	}
	
	private void drawFinal(Graphics2D g, int[] resX, int[] resY, int[] drawZone, SGraphDataSet set) {
		DataStyle style = (dataStyle != null ? dataStyle : set.getStyle());
		int t, tp1;
		
		Shape clip = g.getClip();
		g.setClip(drawZone[0] + 1, drawZone[1] + 1, drawZone[2] - 1, drawZone[3] - 1);
		
		// filling
		if(style.alphaFill > 0) {
			g.setColor(DrawUtils.deriveColor(set.getColor(), style.alphaFill));
			for(t = 0, tp1 = 1; t < resX.length - 1; t++, tp1++) {
				if(set.getYBinding().isSide()) {
					g.fillPolygon(new int[] { resX[t], resX[tp1], resX[tp1], resX[t] }, new int[] { resY[t], resY[tp1], drawZone[SGraphBackground.zMY], drawZone[SGraphBackground.zMY] }, 4);
				} else {
					int mx = drawZone[SGraphBackground.zX] + 1;
					g.fillPolygon(new int[] { resX[t], resX[tp1], mx, mx }, new int[] { resY[t], resY[tp1], resY[tp1], resY[t] }, 4);
				}
			}
		}
		
		g.setColor(set.getColor());
		
		// lines
		if(style.strokeStyle != StrokeStyle.NONE)
			for(t = 0, tp1 = 1; t < resX.length - 1; t++, tp1++)
				g.drawLine(resX[t], resY[t], resX[tp1], resY[tp1]);
		
		g.setClip(clip);
		
		// points
		if(style.pointStyle != PointStyle.NONE) {
			int ps2 = style.pointSize >> 1;
			
			for(t = 0, tp1 = 1; t < resX.length; t++, tp1++) {
				if((resX[t] < drawZone[0]) || (resX[t] > drawZone[2]) || (resY[t] < drawZone[1]) || (resY[t] > drawZone[3]))
					continue;
				
				if(style.pointStyle == PointStyle.SQUARE)
					g.fillRect(resX[t] - ps2, resY[t] - ps2, style.pointSize, style.pointSize);
				else if(style.pointStyle  == PointStyle.CIRCLE)
					g.fillOval(resX[t] - ps2, resY[t] - ps2, style.pointSize, style.pointSize);
			}
		}
	}
	
}
