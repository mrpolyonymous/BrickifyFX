package brickifyfx.tiling;

import brickifyfx.core.BricksAndColors;
import brickifyfx.core.Mosaic;

/**
 * Interface for classes that tile mosaic after the color for each part of the mosaic has been
 * calculated.
 * 
 * @author Tobias Reichling
 */
public interface Tiling {

	/**
	 * tiling the mosaic
	 * 
	 * @param bricksAndColors
	 * @param mosaic
	 *            the mosaic, with color information computed
	 */
	public void tiling(BricksAndColors bricksAndColors, Mosaic mosaic);
}