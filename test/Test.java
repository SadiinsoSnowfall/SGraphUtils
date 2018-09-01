package test;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import play.sadiinso.sgraphutils.axis.AxisPosition;
import play.sadiinso.sgraphutils.axis.SGraphAxisBuilder;
import play.sadiinso.sgraphutils.dataset.DataStyle;
import play.sadiinso.sgraphutils.dataset.DataStyle.PointStyle;
import play.sadiinso.sgraphutils.dataset.DataStyle.StrokeStyle;
import play.sadiinso.sgraphutils.dataset.DataStyleBuilder;
import play.sadiinso.sgraphutils.dataset.SGraphDataSetBuilder;
import play.sadiinso.sgraphutils.draw.BackgroundFlavour;
import play.sadiinso.sgraphutils.draw.GridStyle;
import play.sadiinso.sgraphutils.draw.SGraphBackgroundBuilder;
import play.sadiinso.sgraphutils.draw.SLineGraphBuilder;

public class Test {
	
	public static void main(String[] args) {
		//Color.decode("#FFAB0F")
		var yAxis = SGraphAxisBuilder.newNumericRangeAxis(0, 100, 20)
						.setStyle(Color.GRAY, 20)
						.setFont("Oswald")
						.build();

		var ds1 = SGraphDataSetBuilder.newSimpleSet(Color.ORANGE, "CPU load");
		var ds2 = SGraphDataSetBuilder.newDataSet(Color.BLUE, "RAM load");
		

		var background = new SGraphBackgroundBuilder(500, 300)
								.setMargin(10)
								.setAxis(yAxis, AxisPosition.LEFT)
								.setAxis(yAxis, AxisPosition.BOTTOM)
								.setGridStyle(Color.GRAY, GridStyle.DASHED, 1, 3)
								.addDataLabel(ds1)
								.setBackgroundFlavour(BackgroundFlavour.LINE)
								.build();

		ds1.setValues(15, 20, 85, 100, 80, 20, 5);

		var x = new double[11];
		var y = new double[11];
		for(int t = 0; t < 11; t++) {
			x[t] = t * 10;
			y[t] = Math.pow(t * 10, 2);
		}
		
		ds2.setValues(x, y);
		
		DataStyle style = new DataStyleBuilder(StrokeStyle.PLAIN, PointStyle.SQUARE).setAlphaFill("0%").build();
		
		var graph = new SLineGraphBuilder(background)
							.addDataSet(ds1)
							.setDataStyle(style)
							.build();
		
		copyToClipboard(graph);
		System.out.println("Done!");
	}
	
	public static void copyToClipboard(Image img) {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new TransferableImage(img), null);
	}
	
	private static class TransferableImage implements Transferable {
		Image i;

		public TransferableImage(Image i) {
			this.i = i;
		}

		public Object getTransferData(DataFlavor flavor) {
			return i;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.imageFlavor };
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			DataFlavor[] flavors = getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++)
				if (flavor.equals(flavors[i]))
					return true;
			return false;
		}
	}

}
