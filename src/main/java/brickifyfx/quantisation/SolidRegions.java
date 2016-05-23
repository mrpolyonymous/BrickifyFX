package brickifyfx.quantisation;

import brickifyfx.core.BricksAndColors;
import brickifyfx.core.Calculation;
import brickifyfx.core.ColorObject;
import brickifyfx.core.Lab;
import brickifyfx.core.Mosaic;

/**
 * Based on the naive quantisation cielab
 *                   lonely pixels are deleted with a special filter
 *                   result: big monochrome regions (solid regions)   
 * @author           Tobias Reichling
 */
public class SolidRegions
implements Quantisation {
	
	private Calculation calculation;

	/**
	 * constructor
	 */
	public SolidRegions(){
		this.calculation = new Calculation();
	}
	
	/**
	 */
	@Override
	public void quantisation(BricksAndColors bricksAndColors, Mosaic mosaic, int[] rgbPixels) {

		int mosaicWidth = mosaic.getMosaicWidth();
		int mosaicHeight = mosaic.getMosaicHeight();
		// working mosaic gets an one pixel wide border
		// so it is not necessary to compute the mosaic border areas in a special way
		ColorObject[][] workingMosaic = new ColorObject[mosaicHeight + 2][mosaicWidth + 2];
		int red, green, blue;
		int offset = 0;
		Lab lab, lab2;
		ColorObject closestColor = null;

		double differenceSquared;
		for (int mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {
			for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
				double minimumDifferenceSquared = 500000.0;
				red = (rgbPixels[offset] >> 16) & 0xFF;
				green = (rgbPixels[offset] >> 8) & 0xFF;
				blue = rgbPixels[offset] & 0xFF;
				++offset;

				lab = calculation.rgbToLab(red, green, blue);
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
				mosaic.setColor(mosaicRow, mosaicColumn, closestColor);
				workingMosaic[mosaicRow + 1][mosaicColumn + 1] = closestColor;
			}
		}

		//the one pixel wide border is filled with the original border pixel values
		//special cases: border, corner, etc.
		for (int row = 0; row < (mosaicHeight + 2); row++) {
			for (int column = 0; column < (mosaicWidth + 2); column++) {
				//TOP
				if (row == 0) {
					//TOP-left
					if (column == 0) {
	    				workingMosaic[row][column]=workingMosaic[row+1][column+1];
					}
					//TOP-Center
					if (column > 0 && column < (mosaicWidth + 1)) {
						workingMosaic[row][column] = workingMosaic[row + 1][column];
					}
					//TOP-right
					if (column == (mosaicWidth + 1)) {
						workingMosaic[row][column] = workingMosaic[row + 1][column - 1];
					}
	    		}
				//CENTER
				if (row > 0 && row < (mosaicHeight + 1)) {
					//CENTER-left
					if (column == 0) {
						workingMosaic[row][column] = workingMosaic[row][column + 1];
					}
					//CENTER-right
					if (column == (mosaicWidth + 1)) {
						workingMosaic[row][column] = workingMosaic[row][column - 1];
					}
				}
				//BOTTOM
				if (row == (mosaicHeight + 1)) {
					//BOTTOM-left
					if (column == 0) {
						workingMosaic[row][column] = workingMosaic[row - 1][column + 1];
					}
					//BOTTOM-Center
					if (column > 0 && column < (mosaicWidth + 1)) {
						workingMosaic[row][column] = workingMosaic[row - 1][column];
					}
					//BOTTOM-right
					if (column == (mosaicWidth + 1)) {
						workingMosaic[row][column] = workingMosaic[row - 1][column - 1];
					}
				}

			}
		}

		//filter for generation big monochrome regions:
	    //count how often the current color exists in the near 8 pixels
	    //3x3 filter
	    int colorCounter;
	    ColorObject currentColor;
	    for (int rows = 1; rows < mosaicHeight; rows++){
	    	for (int column = 1; column < mosaicWidth; column++){
	    		colorCounter = 0;
	    		currentColor = workingMosaic[rows][column];
	    		if (currentColor.equals(workingMosaic[rows+1][column])){colorCounter++;}
	    		if (currentColor.equals(workingMosaic[rows-1][column])){colorCounter++;}
	    		if (currentColor.equals(workingMosaic[rows][column+1])){colorCounter++;}
	    		if (currentColor.equals(workingMosaic[rows][column-1])){colorCounter++;}
	    		if (currentColor.equals(workingMosaic[rows+1][column+1])){colorCounter++;}
	    		if (currentColor.equals(workingMosaic[rows-1][column-1])){colorCounter++;}
	    		if (currentColor.equals(workingMosaic[rows+1][column-1])){colorCounter++;}
	    		if (currentColor.equals(workingMosaic[rows-1][column+1])){colorCounter++;}
	    		//a color is changed if it exists less than 2 times
	    		//the threshold is 2 because a lower or a higher
	    		//threshold will affect the image quality
	    		//
	    		//computes the color which exists more often than other
	    		//colors in this 3x3 window
	    		//change the current color to this new color
				if (colorCounter < 2) {
					int colorCounter2;
					ColorObject newColor = null;
					int flag = 0;
					ColorObject testColor;
					for (int windowRow = (rows - 1); windowRow < (rows + 2); windowRow++) {
						for (int windowColumn = (column - 1); windowColumn < (column + 2); windowColumn++) {
							testColor = workingMosaic[windowRow][windowColumn];
							colorCounter2 = 0;
							for (int windowRow2 = (rows - 1); windowRow2 < (rows + 2); windowRow2++) {
								for (int windowColumn2 = (column - 1); windowColumn2 < (column + 2); windowColumn2++) {
									if (testColor.equals(workingMosaic[windowRow2][windowColumn2])) {
										colorCounter2++;
									}
								}
							}
							if (colorCounter2 > flag) {
								newColor = testColor;
								flag = colorCounter2;
							}
						}
					}
					//change color
					workingMosaic[rows][column] = newColor;
				}
	    	}
	    }

		//transfer color information from working mosaic to original mosaic
		//(caution: don't transfer the color information from the one pixel wide border)
		for (int rows = 1; rows < mosaicHeight; rows++) {
			for (int column = 1; column < mosaicWidth; column++) {
				mosaic.setColor(rows - 1, column - 1, workingMosaic[rows][column]);
			}
		}

	}
}