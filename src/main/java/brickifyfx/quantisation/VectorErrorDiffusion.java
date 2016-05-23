package brickifyfx.quantisation;

import brickifyfx.MathUtils;
import brickifyfx.core.BricksAndColors;
import brickifyfx.core.Calculation;
import brickifyfx.core.ColorObject;
import brickifyfx.core.Lab;
import brickifyfx.core.Mosaic;


/**
 * Quantization using Floyd-Steinberg error diffusion in cielab color space
 * 
 * @author Tobias Reichling
 */
public class VectorErrorDiffusion implements Quantisation {
	
	private Calculation calculation;

	/**
	 * constructor
	 */
	public VectorErrorDiffusion() {
		this.calculation = new Calculation();
	}
	
	/*
	 * Vector error diffusion (floyd steinberg) - cielab
	 */
	@Override
	public void quantisation(BricksAndColors bricksAndColors, Mosaic mosaic, int[] rgbPixels) {

		// compute working mosaic with lab color information (from srgb colors)
	    Lab[][] workingMosaic = new Lab[mosaic.getMosaicHeight()][mosaic.getMosaicWidth()];
	    int red, green, blue;
	    int offset = 0;
		for (int mosaicRow = 0; mosaicRow < mosaic.getMosaicHeight(); mosaicRow++) {
			for (int mosaicColumn = 0; mosaicColumn < mosaic.getMosaicWidth(); mosaicColumn++) {
				red = (rgbPixels[offset] >> 16) & 0xFF;
				green = (rgbPixels[offset] >> 8) & 0xFF;
				blue = rgbPixels[offset] & 0xFF;
				++offset;
				workingMosaic[mosaicRow][mosaicColumn] = calculation.rgbToLab(red, green, blue);
			}
		}
	    //computing the colors (minimum Euclidean distance)
	    //then floyd steinberg error diffusion (error vector)
	    Lab lab1 = null;
	    Lab lab2 = null;
	    Lab newColor = null;
	    double differenceSquared;
	    for (int mosaicRow = 0; mosaicRow < mosaic.getMosaicHeight(); mosaicRow++){
			//submit progress bar refresh-function to the gui-thread
			for (int mosaicColumn = 0; mosaicColumn < mosaic.getMosaicWidth(); mosaicColumn++){
				double minimumDifference = 500000.0;
				ColorObject closestColor = null;
				for (ColorObject colorObject : bricksAndColors.getColorPalette().getColors()) {
					lab1 = workingMosaic[mosaicRow][mosaicColumn];
					lab2 = colorObject.getLabColor();
					double lDiff = lab1.getL()-lab2.getL();
					double aDiff = lab1.getA()-lab2.getA();
					double bDiff = lab1.getB()-lab2.getB();
					differenceSquared = lDiff * lDiff + aDiff * aDiff + bDiff * bDiff;
					if (differenceSquared < minimumDifference) {
						minimumDifference = differenceSquared;
						closestColor = colorObject;
						newColor = lab2;
					}
				}
				//set color information to mosaic
				mosaic.setColor(mosaicRow, mosaicColumn, closestColor);
				//compute error vector
				double l = newColor.getL()-lab1.getL();
				double a = newColor.getA()-lab1.getA();
				double b = newColor.getB()-lab1.getB();
				//error diffusion
				//(caution: mosaic borders!)
				//pixel right: 7/16
				if (!(mosaicColumn == mosaic.getMosaicWidth() - 1)) {
					updateErrorDistribution(workingMosaic[mosaicRow][mosaicColumn + 1], (7.0 / 16.0), l, a, b);
				}
				if (!(mosaicRow == mosaic.getMosaicHeight() - 1)) {
					//pixel bottom: 5/16
					updateErrorDistribution(workingMosaic[mosaicRow + 1][mosaicColumn], (5.0 / 16.0), l, a, b);
					if (!(mosaicColumn == mosaic.getMosaicWidth() - 1)) {
						//pixel bottom right: 1/16
						updateErrorDistribution(workingMosaic[mosaicRow + 1][mosaicColumn + 1], (1.0 / 16.0), l, a, b);
					}
					if (!(mosaicColumn == 0)) {
						//pixel bottom left: 3/16
						updateErrorDistribution(workingMosaic[mosaicRow + 1][mosaicColumn - 1], (3.0 / 16.0), l, a, b);
					}
				}
			}
	    }
	}
	
	/**
	 * computes a color vector with the error vector and a special factor than cuts the results to
	 * the cielab color space
	 * 
	 * @param color
	 * @param factor
	 * @param l
	 * @param a
	 * @param b
	 */
	private void updateErrorDistribution(Lab color, double factor, double l, double a, double b) {
		color.setL(MathUtils.clamp(color.getL() - (l * factor), 0.0, 100.0));
//		color.setA(MathUtils.clamp(color.getA() - (a * factor), -128.0, 128.0));
//		color.setB(MathUtils.clamp(color.getB() - (b * factor), -128.0, 128.0));
	}

}