package brickifyfx.quantisation;

import brickifyfx.core.BricksAndColors;
import brickifyfx.core.Mosaic;

/**
 * Interface to be implemented by classes the quantise the original input image to choose the colors
 * in the output mosaic
 * 
 * @author Adrian Schuetz
 */
public interface Quantisation {

	/**
	 * Transform the input image pixels in to the colour palette and size of the mosaic.
	 * 
	 * @param rgbImagePixels
	 *            and array containing the pixels to quantize, in ARGB format. The array will have
	 *            length mosaic width * mosaic height, with pixels laid out by by row.
	 * @param bricksAndColors
	 * @param mosaic
	 *            the empty mosaic, which should be updated by this method with the computed
	 *            colour for each output brick.
	 */
	public void quantisation(BricksAndColors bricksAndColors, Mosaic mosaic, int[] rgbImagePixels);
}
