package play.sadiinso.sgraphutils.draw;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import play.sadiinso.sgraphutils.dataset.SGraphDataSet;

public class SBarGraphBuilder extends AbstractSGraphBuilder {

	public SBarGraphBuilder(SGraphBackground graphBackground) {
		super(graphBackground);
	}

	@Override
	public BufferedImage build() {
		// check data sets
		SGraphDataSet[] sets = dataSets.toArray(new SGraphDataSet[dataSets.size()]);
		int len = sets[0].getDataLength();
		
		for(int t = 1; t < sets.length; t++)
			if(sets[t].getDataLength() != len)
				throw new UnsupportedOperationException("Can't bind dataSets with differents length in a bar graph");
		
		BufferedImage img = bg.getBackgroundCopy();
		Graphics2D g = img.createGraphics();
		
		return img;
	}
	
}
