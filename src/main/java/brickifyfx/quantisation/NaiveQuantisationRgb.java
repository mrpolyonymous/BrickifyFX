package brickifyfx.quantisation;

import brickifyfx.core.ColorObject;
import brickifyfx.core.BricksAndColors;
import brickifyfx.core.Mosaic;


/**
 * Simple quantisation implementation using the minimum Euclidean distance in sRGB colour space.
 * 
 * @author Adrian Schuetz
 */
public class NaiveQuantisationRgb implements Quantisation {
	

	/**
	 * constructor
	 * 
	 */
	public NaiveQuantisationRgb() {
	}

	/**
	 * Quantisation implementation using the minimum Euclidean distance in sRGB colour space.
	 * 
	 */
	@Override
	public void quantisation(BricksAndColors bricksAndColors, Mosaic mosaic, int[] rgbPixels) {
		int mosaicWidth = mosaic.getMosaicWidth();
		int mosaicHeight = mosaic.getMosaicHeight();
		
		//color matching
		int red, green, blue;
		int offset = 0;
		for (int mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {
			for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
				double minimumDifferenceSquared = 500000;
				red = (rgbPixels[offset] >> 16) & 0xFF;
				green = (rgbPixels[offset] >> 8) & 0xFF;
				blue = rgbPixels[offset] & 0xFF;
				++offset;

				//find the nearest color in the color vector
				ColorObject closestColor = null;
				for (ColorObject color : bricksAndColors.getColorPalette().getColors()) {
					//compute difference and save color with minimum difference
					double rDiff = red - color.getRGB().getRed() * 255.0;
					double gDiff = green - color.getRGB().getGreen() * 255.0;
					double bDiff = blue - color.getRGB().getBlue() * 255.0;
					double differenceSquared = rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
					if (differenceSquared < minimumDifferenceSquared) {
						minimumDifferenceSquared = differenceSquared;
						closestColor = color;
					}
				}
				//set color to the mosaic
				mosaic.setColor(mosaicRow, mosaicColumn, closestColor);
			}
		}
	}
}
