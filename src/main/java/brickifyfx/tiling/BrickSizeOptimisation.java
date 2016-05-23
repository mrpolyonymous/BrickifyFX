package brickifyfx.tiling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import brickifyfx.core.Calculation;
import brickifyfx.core.ColorObject;
import brickifyfx.core.ColoredBrick;
import brickifyfx.core.BricksAndColors;
import brickifyfx.core.Brick;
import brickifyfx.core.Mosaic;


/**
 * tiling with brick size optimisation
 * 
 * @author Adrian Schuetz
 */
public class BrickSizeOptimisation implements Tiling {

	private Calculation calculation;

	/**
	 * contructor
	 */
	public BrickSizeOptimisation() {
		this.calculation = new Calculation();
	}

	/**
	 * tiling with brick size optimisation
	 */
	@Override
	public void tiling(BricksAndColors bricksAndColors, Mosaic mosaic) {
		final int mosaicHeight = mosaic.getMosaicHeight();
		final int mosaicWidth = mosaic.getMosaicWidth();

		//build vector of all bricks
		List<Brick> bricksSorted = sortBricksBySize(bricksAndColors.getBricks().getAllBricks());
		//scan the mosaic per random coordinates
		//compute fitting brick for each pixel
		ColorObject currentColor;
		Iterator<Brick> sorted;
		ColoredBrick pixel;
		ColoredBrick pixel2;
		Brick currentBrick;
		boolean brickSet = false;
		List<Integer> brickCoords;
		//coordinates / position where the brick will be set in the mosaic
		int left = 0;
		int top = 0;
		//flags for the left most "true" in the top row of the brick matrix
		int brickLeft = -1;
		int brickTop = -1;
		//current coordinates in the mosaic
		int colorRow, colorColumn;
		//compute random coordinates
		List<Integer> coords = calculation.randomCoordinates(mosaicWidth, mosaicHeight);
		Iterator<Integer> coordsEnum = coords.iterator();
		
		//boolean extraBreak = false;
		
		//scan the mosaic per random coordinates
		while (coordsEnum.hasNext()) {
			colorRow = coordsEnum.next().intValue();
			colorColumn = coordsEnum.next().intValue();

			//current pixel flag
			pixel = mosaic.getMosaic()[colorRow][colorColumn];
			//if the current pixel is not reserved by an other brick yet 
			if (pixel.isColorOnly()) {
				//check the pixel color
				currentColor = pixel.getColor();	
				//checks if the current pixel must be a 1x1 brick
				//(if the 4 neighbor pixel have other colors or if
				//theses pixel are reserved by other bricks
				//[this is only performance tuning!]
				if ((colorRow > 0)
						&& (colorRow < mosaicHeight-1)
						&& (colorColumn > 0)
						&& (colorColumn < mosaicWidth-1)
						//top:
						//pixel: vector contains 1 object and this ist noch the current color
						&& (((mosaic.getMosaic()[colorRow-1][colorColumn].isColorOnly())
								&& !((mosaic.getMosaic()[colorRow-1][colorColumn].getColor()).equals(currentColor)))
								//or:
								//vector contains 2 objects = pixel is reserved
								|| (mosaic.getMosaic()[colorRow-1][colorColumn].isEmptyOrCovered()))
						//bottom:
						&& (((mosaic.getMosaic()[colorRow+1][colorColumn].isColorOnly())
								&& !((mosaic.getMosaic()[colorRow+1][colorColumn].getColor()).equals(currentColor)))
								|| (mosaic.getMosaic()[colorRow+1][colorColumn].isEmptyOrCovered()))
						//right:
						&& (((mosaic.getMosaic()[colorRow][colorColumn+1].isColorOnly())
								&& !((mosaic.getMosaic()[colorRow][colorColumn+1].getColor()).equals(currentColor)))
								|| (mosaic.getMosaic()[colorRow][colorColumn+1].isEmptyOrCovered()))
						//left:
						&& (((mosaic.getMosaic()[colorRow][colorColumn-1].isColorOnly())
								&& !((mosaic.getMosaic()[colorRow][colorColumn-1].getColor()).equals(currentColor)))
								|| (mosaic.getMosaic()[colorRow][colorColumn-1].isEmptyOrCovered()))){
					//change current brick flag to the basic brick
					currentBrick = bricksAndColors.getBasicBrick();

					//set brick to the mosaic
					mosaic.setBrick(colorRow, colorColumn, currentBrick);

				} else {
					//if the current pixel is no 1x1 brick
					//build a size-sorted enumeration of all bricks
					sorted = bricksSorted.iterator();
					brickSet = false;
					while (sorted.hasNext() && !brickSet) {
						currentBrick = sorted.next();
						
						//compute random coordinates for the brick matrix
						brickCoords = calculation.randomCoordinates(currentBrick.getWidth(), currentBrick.getHeight());
						Iterator<Integer> brickCoordsEnum = brickCoords.iterator();
						while (brickCoordsEnum.hasNext() && !brickSet) {
							//compute the position of the brick matrix
							//for testing the brick with the current mosaic position
							top = brickCoordsEnum.next().intValue();
							left = brickCoordsEnum.next().intValue();
							//test only if the the computed position in the brick matrix
							//is "true"
							if (currentBrick.getMatrix()[top][left]) {
								//check mosaic borders:
								//bottom
								if (((colorRow + currentBrick.getHeight()-(top+1)) < mosaicHeight)
											//top
										    && (colorRow - top >= 0)
										    //left
											&& ((colorColumn - left) >= 0)
											//right
											&& ((colorColumn + (currentBrick.getWidth() - (left+1))) < mosaicWidth)){
									//color matching with all "true" positions of the brick matrix
									boolean brickFits = true;
									brickLeft = -1; brickTop = -1;
									for (int brickRow = 0; brickRow < currentBrick.getHeight() && brickFits; brickRow++) {
										for (int brickColumn = 0; brickColumn < currentBrick.getWidth() && brickFits; brickColumn++) {
											if (currentBrick.getMatrix()[brickRow][brickColumn]) {
												//flags: left most "true" position in the top row of the brick matrix
												if (brickLeft == -1) {
													brickLeft = brickColumn;
												}
												if (brickTop == -1) {
													brickTop = brickRow;
												}
												pixel2 = mosaic.getMosaic()[(colorRow + brickRow) - top][(colorColumn + brickColumn) - left];
												if (pixel2.isColorOnly()) {
													if (!(pixel2.getColor()).equals(currentColor)) {
														brickFits = false;
													}
												} else {
													brickFits = false;
												}
											}
										}
									}
									if (brickFits) {
										//set brick to mosaic
										brickSet = true;
										for (int brickRow = 0; brickRow < currentBrick.getHeight(); brickRow++) {
											for (int brickColumn = 0; brickColumn < currentBrick.getWidth(); brickColumn++) {
												if (currentBrick.getMatrix()[brickRow][brickColumn]) {
													mosaic.reinitialize(((colorRow + brickRow) - top), ((colorColumn + brickColumn) - left));
												}
											}
										}
										mosaic.setColor(colorRow - top + brickTop,
												colorColumn - left + brickLeft,
												currentColor);
										mosaic.setBrick(colorRow - top + brickTop,
												colorColumn - left + brickLeft,
												currentBrick);

									}
								}
							}
						}
					}
				}
			}

		}

	}
	
	/**
	 * sorts the bricks by size, from largest to smallest.
	 */
	private List<Brick> sortBricksBySize(Collection<Brick> bricksUnsorted) {
		int size = 0;
		boolean included = false;
		int position;
		List<Brick> bricksSorted = new ArrayList<>();
		//sort bricks (by size)
		for (Brick supportBrick : bricksUnsorted) {
			if (bricksSorted.size() == 0) {
				bricksSorted.add(supportBrick);
			} else {
				size = computeBrickSize(supportBrick);
				position = 0;
				included = false;
				Iterator<Brick> supportEnum = bricksSorted.iterator();
				while (supportEnum.hasNext() && !included) {
					Brick anotherBrick = supportEnum.next();
					if (size >= computeBrickSize(anotherBrick)) {
						bricksSorted.add(position, supportBrick);
						included = true;
					} else {
						position++;
					}
				}
				if (!included) {
					bricksSorted.add(supportBrick);
				}
			}
		}
		return bricksSorted;
	}
	
	/**
	 * computes the size of a brick
	 */
	private int computeBrickSize(Brick brick) {
		int result = 0;
		for (int row = 0; row < brick.getHeight(); row++) {
			for (int column = 0; column < brick.getWidth(); column++) {
				if (brick.getMatrix()[row][column]) {
					result++;
				}
			}
		}
		return result;
	}
}