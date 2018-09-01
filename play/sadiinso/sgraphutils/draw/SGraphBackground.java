package play.sadiinso.sgraphutils.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;

import play.sadiinso.sgraphutils.axis.AxisPosition;
import play.sadiinso.sgraphutils.axis.SGraphAxis;

public class SGraphBackground {

	private static final int BASE_INTER_MARGIN = 6;

	public final static int zX = 0;
	public final static int zY = 1;
	public final static int zMX = 2;
	public final static int zMY = 3;
	public final static int zW = 4;
	public final static int zH = 5;

	// background image
	private final BufferedImage image;

	// draw zone definition (x, y, mx, my, w, h)
	public final int[] drawZone;

	// axis positions (x, y)
	public final double[][] axisPos;

	public SGraphBackground(int w, int h, int bT, int bR, int bB, int bL, int mT, int mR, int mB, int mL, Color borderColor, Color marginColor,
			Color backgroundColor, SGraphAxis aT, SGraphAxis aR, SGraphAxis aB, SGraphAxis aL, int gX, int gY, GridStyle gridStyle, int gT, int gD,
			Color gridColor, ArrayList<Entry<String, Color>> dataLabels, Font dlFont, Color dlFontColor, int dlInnerMargin, RenderQuality settings,
			BackgroundFlavour flavour) {
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();

		int x, y, mx, my;

		/* draw borders */
		g.setColor(borderColor);
		g.fillRect(0, 0, w, h);

		/* draw margins */
		x = bL;
		y = bT;
		mx = w - bR - bL;
		my = h - bB - bT;

		if (marginColor != borderColor) {
			g.setColor(marginColor);
			g.fillRect(x, y, mx, my);
		}

		/* draw background */
		x += mL;
		y += mT;
		mx -= (mR + mL);
		my -= (mB + mT);

		if (backgroundColor != marginColor) {
			g.setColor(backgroundColor);
			g.fillRect(x, y, mx, my);
		}

		DrawUtils.setRenderingHints(g, settings);

		/* draw labels */

		// prepare font & labels
		if (dataLabels.size() > 0) {
			g.setFont(dlFont != null ? dlFont : new Font("Arial", Font.PLAIN, 12));

			String[] dl = new String[dataLabels.size()];
			Color[] dlC = new Color[dl.length];
			int[] labelSw = new int[dl.length];

			int index = 0, maxSize = 0;
			for (Entry<String, Color> entry : dataLabels) {
				dl[index] = entry.getKey();
				dlC[index] = entry.getValue();
				labelSw[index] = g.getFontMetrics().stringWidth(dl[index]);

				if (labelSw[index] > maxSize)
					maxSize = labelSw[index];

				++index;
			}

			// labels holder zone definition
			int lhW = maxSize + g.getFontMetrics().getAscent() + 8;
			int lhMX = x + mx;
			int lhX = lhMX - lhW;
			int lhWmr = lhW + dlInnerMargin;

			// draw labels
			drawDataLabels(g, lhX, y, lhMX, my, dl, dlC, dlFontColor);

			mx -= lhWmr;
		}

		/* draw axis */
		mx += x;
		my += y;

		// whether the axis are present
		boolean left = (aL != null), right = (aR != null), top = (aT != null), bottom = (aB != null);

		// axis to follow (grid drawing)
		SGraphAxis followY = (left ? aL : aR);
		SGraphAxis followX = (bottom ? aB : aT);

		// get font metrics
		FontMetrics mLeft = (left ? g.getFontMetrics(aL.getFont()) : null);
		FontMetrics mRight = (right ? g.getFontMetrics(aR.getFont()) : null);
		FontMetrics mTop = (top ? g.getFontMetrics(aT.getFont()) : null);
		FontMetrics mBottom = (bottom ? g.getFontMetrics(aB.getFont()) : null);

		// compute string width
		int[] wLeft = computeStringWidth(aL, mLeft);
		int[] wRight = computeStringWidth(aR, mRight);
		int[] wTop = computeStringWidth(aT, mTop);
		int[] wBottom = computeStringWidth(aB, mBottom);

		int mswLeft = (left ? Arrays.stream(wLeft).max().getAsInt() : 0);
		int mswRight = (right ? Arrays.stream(wRight).max().getAsInt() : 0);

		// compute axis labels position tweak
		int lTweak = (left ? (int) (mLeft.getAscent() * 0.4) : 0);
		int rTweak = (right ? (int) (mRight.getAscent() * 0.4) : 0);
		int mxTweak = Math.max(lTweak, rTweak);

		int tTweak1 = (top ? (int) (wTop[0] * 0.5) : 0);
		int tTweak2 = (top ? (int) (wTop[wTop.length - 1] * 0.5) : 0);
		int bTweak1 = (bottom ? (int) (wBottom[0] * 0.5) : 0);
		int bTweak2 = (bottom ? (int) (wBottom[wBottom.length - 1] * 0.5) : 0);

		// compute needed inner margin
		int iLeft = (left ? mswLeft + BASE_INTER_MARGIN : Math.max(tTweak1, bTweak1));
		int iRight = (right ? mswRight + BASE_INTER_MARGIN : Math.max(tTweak2, bTweak2));
		int iTop = (top ? mTop.getAscent() + BASE_INTER_MARGIN : mxTweak);
		int iBottom = (bottom ? mBottom.getAscent() + BASE_INTER_MARGIN : mxTweak);

		// define graph inner zone
		int zX = x + iLeft;
		int zY = y + iTop;
		int zMX = mx - iRight;
		int zMY = my - iBottom;
		int zW = zMX - zX;
		int zH = zMY - zY;

		// resolve positions
		int[] rLeft = resolveAxisPos(mLeft, x, zY - lTweak, mx, zMY + lTweak, aL, AxisPosition.LEFT, wLeft);
		int[] rRight = resolveAxisPos(mRight, x, zY - rTweak, mx, zMY + rTweak, aR, AxisPosition.RIGHT, wRight);
		int[] rTop = resolveAxisPos(mTop, zX - tTweak1, y, zMX + tTweak2, my, aT, AxisPosition.TOP, wTop);
		int[] rBottom = resolveAxisPos(mBottom, zX - bTweak1, y, zMX + bTweak2, my, aB, AxisPosition.BOTTOM, wBottom);

		// draw
		drawYAxis(g, x, y, mx, my, aL, AxisPosition.LEFT, rLeft, wLeft, mswLeft);
		drawYAxis(g, x, y, mx, my, aR, AxisPosition.RIGHT, rRight, wRight, mswRight);
		drawXAxis(g, x, y, mx, my, aT, AxisPosition.TOP, rTop);
		drawXAxis(g, x, y, mx, my, aB, AxisPosition.BOTTOM, rBottom);

		// patch positions
		patchXPos(rBottom, wBottom);
		patchXPos(rTop, wTop);
		patchYPos(rLeft, mLeft);
		patchYPos(rRight, mRight);

		/* draw graph zone */
		g.setColor(gridColor);
		g.drawRect(zX, zY, zW, zH);

		/* draw grid */
		if (gridStyle != GridStyle.NONE) {
			float[] dashSize = (gridStyle == GridStyle.DASHED ? new float[] { gD } : null);
			g.setStroke(new BasicStroke(gT, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dashSize, 0));

			int[] followPosX = (followX == aB ? rBottom : rTop);
			int[] followPosY = (followY == aL ? rLeft : rRight);

			// Vertical
			if (gX == -1) {
				if (followX != null) {
					for (int t = 1; t < followPosX.length - 1; t++)
						g.drawLine(followPosX[t], zY, followPosX[t], zMY);
				} else if (followY != null) {
					int interval = zW / (followPosY.length - 1);
					int current = zX + interval;

					for (int t = 1; t < followPosY.length - 1; t++, current += interval)
						g.drawLine(current, zY, current, zMY);
				}
			} else { // NO FOLLOW
				int interval = (int) (zW / (double) (gX + 1));
				int current = zX + interval;
				for (int t = 0; t < gX; t++, current += interval)
					g.drawLine(current, zY, current, zMY);
			}

			// Horizontal
			if (gY == -1) {
				if (followY != null) {
					for (int t = 1; t < followPosY.length - 1; t++)
						g.drawLine(zX, followPosY[t], zMX, followPosY[t]);
				} else if (followX != null) {
					int interval = zH / (followPosX.length - 1);
					int current = zY + interval;

					for (int t = 1; t < followPosX.length - 1; t++, current += interval)
						g.drawLine(zX, current, zMX, current);
				}
			} else { // NO FOLLOW
				int interval = (int) (zH / (double) (gY + 1));
				int current = zY + interval;
				for (int t = 0; t < gY; t++, current += interval)
					g.drawLine(zX, current, zMX, current);
			}
		}

		/* finalize */
		g.dispose();

		/* set private variables */
		drawZone = new int[] { zX, zY, zMX, zMY, zW, zH };
		axisPos = new double[][] { axisPos(aT), axisPos(aR), axisPos(aB), axisPos(aL) };
	}

	/**
	 * Return a copy of the BufferedImage backed by the SGraphBackground
	 */
	public BufferedImage getBackgroundCopy() {
		ColorModel cm = image.getColorModel();
		return new BufferedImage(image.getColorModel(), image.copyData(null), cm.isAlphaPremultiplied(), null);
	}

	/**
	 * Return the draw zone (x, y, mx, my)
	 */
	public int[] getDrawZone() {
		return drawZone;
	}

	/**
	 * Return the position of the axis labels
	 */
	public double[][] getAxisPos() {
		return axisPos;
	}

	private static double[] axisPos(SGraphAxis axis) {
		if (axis == null)
			return null;

		double[] pos = axis.getPos();
		return new double[] { pos[0], pos[pos.length - 1] };
	}

	private static void drawDataLabels(Graphics2D g, int x, int y, int mx, int h, String[] labels, Color[] labelsColor, Color labelFontColor) {
		g.setColor(labelFontColor);

		int a = g.getFontMetrics().getAscent();
		int strX = x + a + 5; // x-distance between the label holder margin and the labels

		double interval = a * 1.5; // y-distance between two label
		double current = h / 2 - (labels.length / 2 * interval) + a;

		int[] pos = new int[labels.length];

		for (int t = 0; t < labels.length; t++, current += interval) {
			pos[t] = (int) (current - a * 0.92);
			g.drawString(labels[t], strX, (int) current);
		}

		for (int t = 0; t < labels.length; t++) {
			g.setColor(labelsColor[t]);
			g.fillRect(x, pos[t], a, a);
		}

	}

	private static int[] computeStringWidth(SGraphAxis a, FontMetrics m) {
		if (a == null)
			return null;

		String[] labels = a.getLabels();
		int[] sw = new int[labels.length];

		for (int t = 0; t < sw.length; t++)
			sw[t] = m.stringWidth(labels[t]);

		return sw;
	}

	private static int[] resolveAxisPos(FontMetrics m, int x, int y, int mx, int my, SGraphAxis a, AxisPosition p, int[] sw) {
		if (a == null)
			return null;

		double[] pos = a.getPos();

		int[] ret = new int[pos.length];
		int last = pos.length - 1;

		// add correction
		double dL = (pos[last] - pos[0]);
		double dP;

		if (p.isSide()) {
			y += m.getAscent() * 0.75;
			dP = (y - my);

			ret[0] = my;
			ret[last] = y;
			for (int t = 1; t < last; t++)
				ret[t] = (int) ((pos[t] - pos[0]) / dL * dP + my);

		} else {
			mx -= sw[last];
			dP = (mx - x);

			ret[0] = x;
			ret[last] = mx;

			for (int t = 1; t < last; t++)
				ret[t] = (int) ((pos[t] - pos[0]) / dL * dP) + x;
		}

		return ret;
	}

	private static void drawXAxis(Graphics2D g, int x, int y, int mx, int my, SGraphAxis a, AxisPosition p, int[] resolved) {
		if (a == null)
			return;

		g.setColor(a.getColor());
		g.setFont(a.getFont());

		String[] labels = a.getLabels();
		FontMetrics m = g.getFontMetrics();

		y += m.getAscent() * 0.75;
		int dy = (p == AxisPosition.TOP ? y : my);

		for (int t = 0; t < labels.length; t++)
			g.drawString(labels[t], resolved[t], dy);
	}

	private static void drawYAxis(Graphics2D g, int x, int y, int mx, int my, SGraphAxis a, AxisPosition p, int[] resolved, int[] sw, int msw) {
		if (a == null)
			return;

		g.setColor(a.getColor());
		g.setFont(a.getFont());

		String[] labels = a.getLabels();

		// calculate X positions
		int[] posX = new int[labels.length];
		if (p == AxisPosition.LEFT) {
			for (int t = 0; t < labels.length; t++)
				posX[t] = x + (int) ((msw - sw[t]) / 2D);
		} else {
			for (int t = 0; t < labels.length; t++)
				posX[t] = mx - sw[t] - (int) ((msw - sw[t]) / 2D);
		}

		for (int t = 0; t < labels.length; t++)
			g.drawString(labels[t], posX[t], resolved[t]);
	}

	private static void patchXPos(int[] pos, int[] sw) {
		if (pos != null)
			for (int t = 0; t < pos.length; t++)
				pos[t] += sw[t] / 2;
	}

	private static void patchYPos(int[] pos, FontMetrics m) {
		if (pos != null) {
			int tmp = (int) (m.getAscent() * 0.35);
			for (int t = 0; t < pos.length; t++)
				pos[t] -= tmp;
		}
	}

}