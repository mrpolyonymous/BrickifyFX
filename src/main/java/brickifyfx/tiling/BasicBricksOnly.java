package brickifyfx.tiling;

import brickifyfx.core.BricksAndColors;
import brickifyfx.core.Brick;
import brickifyfx.core.Mosaic;

/**
 * A basic tiling implementation that tiles with 1 by 1 bricks
 * 
 * @author Adrian Schuetz
 */
public class BasicBricksOnly implements Tiling {

	/**
	 * Constructor
	 */
	public BasicBricksOnly() {
	}

	/**
	 * Tile every pixel with a 1 by 1 basic brick
	 * 
	 * @param bricksAndColors
	 * @param mosaic
	 */
	@Override
	public void tiling(BricksAndColors bricksAndColors, Mosaic mosaic) {
		Brick basicBrick = bricksAndColors.getBasicBrick();
		final int mosaicHeight = mosaic.getMosaicHeight();
		final int mosaicWidth = mosaic.getMosaicWidth();
		for (int row = 0; row < mosaicHeight; row++) {

			for (int column = 0; column < mosaicWidth; column++) {
				mosaic.setBrick(row, column, basicBrick);
			}
		}

	}
}