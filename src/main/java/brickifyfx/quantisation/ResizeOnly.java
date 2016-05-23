package brickifyfx.quantisation;

import javafx.scene.paint.Color;
import brickifyfx.core.BricksAndColors;
import brickifyfx.core.ColorObject;
import brickifyfx.core.Mosaic;

public class ResizeOnly implements Quantisation {

	@Override
	public void quantisation(BricksAndColors bricksAndColors, Mosaic mosaic, int[] rgbImagePixels) {
		int mosaicWidth = mosaic.getMosaicWidth();
		int mosaicHeight = mosaic.getMosaicHeight();
		
		//color matching
		int red, green, blue;
		int offset = 0;
		for (int mosaicRow = 0; mosaicRow < mosaicHeight; mosaicRow++) {
			for (int mosaicColumn = 0; mosaicColumn < mosaicWidth; mosaicColumn++) {
				red = (rgbImagePixels[offset] >> 16) & 0xFF;
				green = (rgbImagePixels[offset] >> 8) & 0xFF;
				blue = rgbImagePixels[offset] & 0xFF;
				++offset;

				// just create a copy of the color
				ColorObject closestColor = new ColorObject("Not an LDraw color", red + "," + green + "," + blue, "Not an LDD color","-1", Color.rgb(red, green, blue));
				//set color to the mosaic
				mosaic.setColor(mosaicRow, mosaicColumn, closestColor);
			}
		}

	}

}
