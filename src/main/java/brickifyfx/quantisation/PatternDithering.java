package brickifyfx.quantisation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import brickifyfx.core.Calculation;
import brickifyfx.core.ColorObject;
import brickifyfx.core.BricksAndColors;
import brickifyfx.core.Lab;
import brickifyfx.core.Mosaic;

/**
 * pattern dithering with 2x2 color pattern
 * 
 * @author Adrian Schuetz
 */
public class PatternDithering implements Quantisation {

	private Calculation calculation;
	private int luminanceLimit;

	/**
	 * constructor
	 */
	public PatternDithering() {
		this.calculation = new Calculation();
		this.luminanceLimit = 25;
	}

	/**
	 * constructor
	 */
	public PatternDithering(PatternDitheringOptions selection) {
		this.calculation = new Calculation();
		this.luminanceLimit = selection.getLuminanceLimit();
	}

	/**
	 * Pattern dithering with 2x2 color pattern
	 */
	@Override
	public void quantisation(BricksAndColors bricksAndColors, Mosaic mosaic, int[] rgbPixels) {
		int mosaicWidth = mosaic.getMosaicWidth();
		int mosaicHeight = mosaic.getMosaicHeight();

		//lab color vector
		List<ColorObject> labColors = new ArrayList<>(bricksAndColors.getColorPalette().getColors());

		Random random = new Random(System.currentTimeMillis());

		//compute a vector with color-combinations (each with 2 colors)
		List<PatternDitherColor> ditherColors = new ArrayList<>();
		while (!labColors.isEmpty()) {
			//delete first color from the list
			ColorObject color = labColors.remove(0);
			//compute lab color
			//pattern with only one color
			ditherColors.add(new PatternDitherColor(color, color, color, color));

			//compute mixed color values
			for (ColorObject secondColor : labColors) {
				//computes color values if the luminance values of the 2 colors
				//do not differentiate more than the given limit
				if (isWithinLuminanceLimit(color, secondColor)) {
					//3x fist color, 1x second color
					ditherColors.add(new PatternDitherColor(color, color, color, secondColor));
					//2x fist color, 2x second color
					ditherColors.add(new PatternDitherColor(color, secondColor, secondColor, color));
					//1x fist color, 3x second color
					ditherColors.add(new PatternDitherColor(secondColor, secondColor, secondColor, color));
				}
			}
		}

		//Number of dither colors
		int ditherColorNumber = ditherColors.size();

		//color matching
		Lab lab;
		double differenceSquared;
		int offset = 0;
		for (int mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {

			for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
				double minimumDifferenceSquared = 500000.0;
				//color values
				Lab sourceImageColor = calculation.rgbToLab((rgbPixels[offset] >> 16) & 0xFF,
						(rgbPixels[offset] >> 8) & 0xFF,
						rgbPixels[offset] & 0xFF);
				double l = sourceImageColor.getL();
				double a = sourceImageColor.getA();
				double b = sourceImageColor.getB();
				++offset;

				PatternDitherColor closestColor = null;
				for (int x = 0; x < ditherColorNumber; x++) {
					PatternDitherColor ditherColor = ditherColors.get(x);
					lab = ditherColor.getMixedColor();
					//compute difference
					double lDiff = lab.getL() - l;
					double aDiff = lab.getA() - a;
					double bDiff = lab.getB() - b;
					differenceSquared = lDiff * lDiff + aDiff * aDiff + bDiff * bDiff;
					if (differenceSquared < minimumDifferenceSquared) {
						minimumDifferenceSquared = differenceSquared;
						closestColor = ditherColor;
					}
				}

				//set color
				//choose the color from the color pattern using the position information
				//if the mosaic row is even (odd) take the color from the pattern position where the row is even (odd)
				//column: see above
				mosaic.setColor(mosaicRow, mosaicColumn, closestColor.getRandomColor(random));
				/*
				if ((mosaicRow & 1) == 0) {
					//mosaicRow even
					if ((mosaicColumn & 1) == 0) {
						//mosaicColumn even
						mosaic.setColor(mosaicRow, mosaicColumn, closestColor.getColor1());
					} else {
						//mosaicColumn odd
						mosaic.setColor(mosaicRow, mosaicColumn, closestColor.getColor2());
					}
				} else {
					//mosaicRow odd
					if ((mosaicColumn & 1) == 0) {
						//mosaicColumn even
						mosaic.setColor(mosaicRow, mosaicColumn, closestColor.getColor3());
					} else {
						//mosaicColumn odd
						mosaic.setColor(mosaicRow, mosaicColumn, closestColor.getColor4());
					}
				}
				*/
			}
		}

	}
	
	/**
	 * checks if a luminance difference is allowed
	 */
	private boolean isWithinLuminanceLimit(ColorObject color1, ColorObject color2) {
		Lab lab1 = color1.getLabColor();
		Lab lab2 = color2.getLabColor();
		if (Math.abs(lab1.getL() - lab2.getL()) > luminanceLimit || Math.abs(lab1.getA() - lab2.getA()) > 50  || Math.abs(lab1.getB() - lab2.getB()) > 50 ) {
			return false;
		}
		return true;
	}
}