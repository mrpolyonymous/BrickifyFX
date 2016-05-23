package brickifyfx.quantisation;

import java.util.Iterator;
import java.util.List;

import brickifyfx.core.Calculation;
import brickifyfx.core.ColorObject;
import brickifyfx.core.BricksAndColors;
import brickifyfx.core.Mosaic;

/**
 * quantisation with error diffusion (floyd steinberg) with 2 colors
 * 
 * @author Adrian Schuetz
 */
public class FloydSteinberg implements Quantisation {
	
	private Calculation calculation;
	private FloydSteinbergOptions selection;

	/**
	 * constructor
	 * @param            selection
	 */
	public FloydSteinberg(FloydSteinbergOptions selection){
		this.calculation = new Calculation();
		this.selection = selection;
	}
	
	/**
	 * quantisation with error diffusion (floyd steinberg) with 2 colors
	 */
	@Override
	public void quantisation(BricksAndColors bricksAndColors, Mosaic mosaic, int[] rgbPixels) {
		int mosaicWidth = mosaic.getMosaicWidth();
		int mosaicHeight = mosaic.getMosaicHeight();

		ColorObject dark = selection.getDark();
		ColorObject light = selection.getLight();
		int method = selection.getMethod();

	    //compute source image only with luminance values
	    double[][] workingMosaic = new double[mosaicHeight][mosaicWidth];
	    int red, green, blue;
	    int offset = 0;
		for (int mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {
			for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
				red = (rgbPixels[offset] >> 16) & 0xFF;
				green = (rgbPixels[offset] >> 8) & 0xFF;
				blue = rgbPixels[offset] & 0xFF;
				++offset;
				workingMosaic[mosaicRow][mosaicColumn] = (calculation.rgbToLab(red, green, blue).getL());
			}
		}
		double error;
		if (method == 1) {
			//**********************************************************************************************
			//*********************************** FloydSteinberg Scanline **********************************
			//**********************************************************************************************
			for (int mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {

				for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
					//compute color and error
					if (workingMosaic[mosaicRow][mosaicColumn] < 50) {
						mosaic.setColor(mosaicRow, mosaicColumn, dark);
						error = 0 - workingMosaic[mosaicRow][mosaicColumn];
					} else {
						mosaic.setColor(mosaicRow, mosaicColumn, light);
						error = 100 - workingMosaic[mosaicRow][mosaicColumn];
					}
					//distribute error (caution: mosaic borders!)
					//pixel right:       7/16
					if (!(mosaicColumn == mosaicWidth - 1)) {
						workingMosaic[mosaicRow][mosaicColumn + 1] = errorDistribution(workingMosaic[mosaicRow][mosaicColumn + 1], (7.0 / 16.0), error);
					}
					if (!(mosaicRow == mosaicHeight - 1)) {
						//pixel bottom:        5/16
						workingMosaic[mosaicRow + 1][mosaicColumn] = errorDistribution(workingMosaic[mosaicRow + 1][mosaicColumn], (5.0 / 16.0), error);
						if (!(mosaicColumn == mosaicWidth - 1)) {
							//pixel bottom right: 1/16
							workingMosaic[mosaicRow + 1][mosaicColumn + 1] = errorDistribution(workingMosaic[mosaicRow + 1][mosaicColumn + 1], (1.0 / 16.0), error);
						}
						if (!(mosaicColumn == 0)) {
							//pixel bottom left:  3/16
							workingMosaic[mosaicRow + 1][mosaicColumn - 1] = errorDistribution(workingMosaic[mosaicRow + 1][mosaicColumn - 1], (3.0 / 16.0), error);
						}
					}
				}
			}
		} else if (method == 2) {
			//**********************************************************************************************
			//*********************************** FloydSteinberg Serpentines ******************************* 
			//**********************************************************************************************
			for (int mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {
				for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
					if ((mosaicRow % 2) == 0) {
						//row even -> to the right
						//---------------------------
						//compute color and error
						if (workingMosaic[mosaicRow][mosaicColumn] < 50) {
							mosaic.setColor(mosaicRow, mosaicColumn, dark);
							error = 0 - workingMosaic[mosaicRow][mosaicColumn];
						} else {
							mosaic.setColor(mosaicRow, mosaicColumn, light);
							error = 100 - workingMosaic[mosaicRow][mosaicColumn];
						}
						//distribute error (caution: mosaic borders!)
						//pixel right:       7/16
						if (!(mosaicColumn == mosaicWidth - 1)) {
							workingMosaic[mosaicRow][mosaicColumn + 1] = errorDistribution(workingMosaic[mosaicRow][mosaicColumn + 1], (7.0 / 16.0), error);
						}
						if (!(mosaicRow == mosaicHeight - 1)) {
							//pixel bottom:        5/16
							workingMosaic[mosaicRow + 1][mosaicColumn] = errorDistribution(workingMosaic[mosaicRow + 1][mosaicColumn], (5.0 / 16.0), error);
							if (!(mosaicColumn == mosaicWidth - 1)) {
								//pixel bottom right: 1/16
								workingMosaic[mosaicRow + 1][mosaicColumn + 1] = errorDistribution(workingMosaic[mosaicRow + 1][mosaicColumn + 1], (1.0 / 16.0), error);
							}
							if (!(mosaicColumn == 0)) {
								//pixel bottom left:  3/16
								workingMosaic[mosaicRow + 1][mosaicColumn - 1] = errorDistribution(workingMosaic[mosaicRow + 1][mosaicColumn - 1], (3.0 / 16.0), error);
							}
						}
					} else {
						//row odd -> to the left
						//----------------------------
						//compute color and error
						int column = (mosaicWidth - mosaicColumn) - 1;
						if (workingMosaic[mosaicRow][column] < 50) {
							mosaic.setColor(mosaicRow, column, dark);
							error = 0 - workingMosaic[mosaicRow][column];
						} else {
							mosaic.setColor(mosaicRow, column, light);
							error = 100 - workingMosaic[mosaicRow][column];
						}
						//distribute error (caution: mosaic borders!)
						//pixel left:       7/16
						if (!((column) == 0)) {
							workingMosaic[mosaicRow][(column) - 1] = errorDistribution(workingMosaic[mosaicRow][(column) - 1], (7.0 / 16.0), error);
						}
						if (!(mosaicRow == mosaicHeight - 1)) {
							//pixel bottom:        5/16
							workingMosaic[mosaicRow + 1][(column)] = errorDistribution(workingMosaic[mosaicRow + 1][(column)], (5.0 / 16.0), error);
							if (!((column) == mosaicWidth - 1)) {
								//pixel bottom right: 3/16
								workingMosaic[mosaicRow + 1][(column) + 1] = errorDistribution(workingMosaic[mosaicRow + 1][(column) + 1], (3.0 / 16.0), error);
							}
							if (!((column) == 0)) {
								//pixel bottom left:  1/16
								workingMosaic[mosaicRow + 1][(column) - 1] = errorDistribution(workingMosaic[mosaicRow + 1][(column) - 1], (1.0 / 16.0), error);
							}
						}
					}
				}
			}
		} else if (method == 3) {
			//**********************************************************************************************
			//*********************************** Hilbert curve ********************************************
			//**********************************************************************************************
			List<Integer> coordinates = calculation.hilbertCoordinates(mosaicWidth, mosaicHeight);
			int x0, x1, x2, x3, x4;
			int y0, y1, y2, y3, y4;
			int coordinatesNumber = (coordinates.size() / 2);
			Iterator<Integer> coordinatesEnum = coordinates.iterator();
			//x0 and y0 are the current coordinates
			//x1,y1 ... x4,y4 are the following coordinates
			//----------------------------------
			//init:
			x0 = coordinatesEnum.next().intValue();
			y0 = coordinatesEnum.next().intValue();
			x1 = coordinatesEnum.next().intValue();
			y1 = coordinatesEnum.next().intValue();
			x2 = coordinatesEnum.next().intValue();
			y2 = coordinatesEnum.next().intValue();
			x3 = coordinatesEnum.next().intValue();
			y3 = coordinatesEnum.next().intValue();
			x4 = coordinatesEnum.next().intValue();
			y4 = coordinatesEnum.next().intValue();

			int referenceValue = (mosaicHeight * mosaicWidth) / 100;
			if (referenceValue == 0) {
				referenceValue = 1;
			}
			for (int i = 0; i < (coordinatesNumber); i++) {

				//compute color and error
				if (workingMosaic[x0][y0] < 50) {
					mosaic.setColor(x0, y0, dark);
					error = 0 - workingMosaic[x0][y0];
				} else {
					mosaic.setColor(x0, y0, light);
					error = 100 - workingMosaic[x0][y0];
				}
				x0 = x1;
				y0 = y1;
				//distribute error
				//... 7/16 ... 5/16 ... 3/16 ... 1/16
				//(caution: at the last mosaic pixel, the error can only be distribute
				// to the following last pixels!)
				if (i < (coordinatesNumber - 2)) {
					workingMosaic[x1][y1] = errorDistribution(workingMosaic[x1][y1], (7.0 / 16.0), error);
					x1 = x2;
					y1 = y2;
				}
				if (i < (coordinatesNumber - 3)) {
					workingMosaic[x2][y2] = errorDistribution(workingMosaic[x2][y2], (5.0 / 16.0), error);
					x2 = x3;
					y2 = y3;
				}
				if (i < (coordinatesNumber - 4)) {
					workingMosaic[x3][y3] = errorDistribution(workingMosaic[x3][y3], (3.0 / 16.0), error);
					x3 = x4;
					y3 = y4;
				}
				if (i < (coordinatesNumber - 5)) {
					workingMosaic[x4][y4] = errorDistribution(workingMosaic[x4][y4], (1.0 / 16.0), error);
					x4 = coordinatesEnum.next().intValue();
					y4 = coordinatesEnum.next().intValue();
				}
			}
		}

	}
	
	/**
	 * method:           errorDistribution
	 * description:      errorDistribution Floyd-Steinberg
	 * @author           Adrian Schuetz
	 * @param            luminance (double)
	 * @param            factor (double)
	 * @param            error (double)
	 * @return           result
	 */
	private double errorDistribution(double luminance, double factor, double error){
		double result;
		result = luminance-(error*factor);
		//Farben auf Farbraum begrenzen
		if (result >  100.0){result = 100.0;}
		if (result <    0.0){result =   0.0;}
		return result;
	}
}