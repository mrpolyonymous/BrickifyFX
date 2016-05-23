package brickifyfx.quantisation;

import brickifyfx.core.Calculation;
import brickifyfx.core.ColorObject;
import brickifyfx.core.BricksAndColors;
import brickifyfx.core.Mosaic;

/**
 * slicing (n-level-quantisation; pseudocolor-quantisation;
 *                   gray-level-to-color-transformation)
 * @author           Tobias Reichling
 */
public class Slicing
implements Quantisation {
	
	private Calculation calculation;
	private SlicingOptions selection;

	/**
	 * constructor
	 */
	public Slicing(SlicingOptions selection) {
		this.calculation = new Calculation();
		this.selection = selection;
	}

	/**
	 * slicing (n-level-quantisation; pseudocolor-quantisation; gray-level-to-color-transformation)
	 */
	@Override
	public void quantisation(BricksAndColors bricksAndColors, Mosaic mosaic, int[] rgbPixels) {
		int mosaicWidth = mosaic.getMosaicWidth();
		int mosaicHeight = mosaic.getMosaicHeight();

		//put colors and thresholds to an arry
		int colorNumber = selection.getThresholds().size();
		int[] thresholds = new int[colorNumber + 1];
		ColorObject[] colors = new ColorObject[colorNumber];
		int i = 0;
		for (SlicingColorThreshold threshold : selection.getThresholds()) {
			colors[i] = threshold.getColor();
			thresholds[i] = threshold.getThreshold();
			++i;
		}
		//add a last threshold value (100) to this array
		thresholds[colorNumber] = 100;

		//compute a workingMosaic only with luminance information
		//(compute srgb colors to cielab colors (luminance value))
		double[][] workingMosaic = new double[mosaicHeight][mosaicWidth];
		int red, green, blue;
		int offset = 0;
		for (int mosaicRowIndex = 0; mosaicRowIndex < mosaicHeight; mosaicRowIndex++) {
			for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
				red = (rgbPixels[offset] >> 16) & 0xFF;
				green = (rgbPixels[offset] >> 8) & 0xFF;
				blue = rgbPixels[offset] & 0xFF;
				++offset;
				workingMosaic[mosaicRowIndex][mosaicColumn] = (calculation.rgbToLab(red, green, blue).getL());
			}
		}
		//color matching
		//the source pixel luminance value must be equal or greater than
		//the lower threshold AND less than the higher threshold of
		//a color
		for (int mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {

			for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
				int indicator = 0;
				for (int x = 0; x < colorNumber; x++) {
					if ((thresholds[x] <= workingMosaic[mosaicRow][mosaicColumn])
							&& (thresholds[x + 1] > workingMosaic[mosaicRow][mosaicColumn])) {
						mosaic.setColor(mosaicRow, mosaicColumn, colors[x]);
						indicator = 1;
					}
				}
				//if a luminance value is 100.0 it cant be fount
				//in the threshold array. so this value must be
				//processed here:
				if (indicator == 0) {
					mosaic.setColor(mosaicRow, mosaicColumn, colors[colorNumber - 1]);
				}
			}
		}

	}
}