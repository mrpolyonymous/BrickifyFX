package brickifyfx.quantisation;

import brickifyfx.core.BricksAndColors;
import brickifyfx.core.Calculation;
import brickifyfx.core.ColorObject;
import brickifyfx.core.Lab;
import brickifyfx.core.Mosaic;

/**
 * simple quantisation by computing the minumum Euclidean distance in CieLAB color space
 * 
 * @author Adrian Schuetz
 */
public class NaiveQuantisationLab implements Quantisation {

	private Calculation calculation;

	/**
	 * constructor
	 */
	public NaiveQuantisationLab() {
		this.calculation = new Calculation();
	}

	/**
	 * Quantisation implementation using the minimum Euclidean distance in LAB colour space.
	 */
	@Override
	public void quantisation(BricksAndColors bricksAndColors, Mosaic mosaic, int[] rgbPixels) {
		int mosaicWidth = mosaic.getMosaicWidth();
		int mosaicHeight = mosaic.getMosaicHeight();
		
		// Match by computing the minumum Euclidean distance from each pixel
		// to the given color palette in the CieLAB color space
		int red, green, blue;
		Lab lab = null, lab2 = null;
		double differenceSquared;
		int offset = 0;
		for (int mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {
			for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
				double minimumDifferenceSquared = 500000;
				red = rgbPixels[offset] >> 16 & 0xFF;
				green = rgbPixels[offset] >> 8 & 0xFF;
				blue = rgbPixels[offset] & 0xFF;
				++offset;
				lab = calculation.rgbToLab(red, green, blue);
				ColorObject closestColor = null;
				for (ColorObject colorObject : bricksAndColors.getColorPalette().getColors()) {
					lab2 = colorObject.getLabColor();
					double lDiff = lab.getL() - lab2.getL();
					double aDiff = lab.getA() - lab2.getA();
					double bDiff = lab.getB() - lab2.getB();
					differenceSquared = lDiff * lDiff + aDiff * aDiff + bDiff * bDiff;
					if (differenceSquared < minimumDifferenceSquared) {
						minimumDifferenceSquared = differenceSquared;
						closestColor = colorObject;
					}
				}
				//sets found color to the mosaic
				mosaic.setColor(mosaicRow, mosaicColumn, closestColor);
			}
		}
	}
}