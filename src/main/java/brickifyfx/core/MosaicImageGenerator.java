package brickifyfx.core;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import brickifyfx.MathUtils;

/**
 * Generates images from a Mosaic
 * 
 * @author Dan
 * @author Tobias Reichling
 * 
 */
public class MosaicImageGenerator {
	
	private boolean threeDEffect;
	private int drawnBasicBrickPixelWidth;
	private int drawnBasicBrickPixelHeight;
	private boolean useOldMethod;
	private Map<ColoredBrick, byte[]> renderedBrick;
	private Map<Brick, boolean[][]> paddedBrickMatrices;
	private Map<Brick, Integer> leftOffsets;


	public MosaicImageGenerator(BricksAndColors bricksAndColors, boolean threeDEffect, boolean useOldMethod) {
		this.threeDEffect = threeDEffect;
		this.useOldMethod = useOldMethod;

		drawnBasicBrickPixelWidth = bricksAndColors.getBricks().getBasicBrickWidthRatio();
		drawnBasicBrickPixelHeight = bricksAndColors.getBricks().getBasicBrickHeightRatio();
		while (drawnBasicBrickPixelWidth < 10 || drawnBasicBrickPixelHeight < 10) {
			drawnBasicBrickPixelWidth = drawnBasicBrickPixelWidth * 2;
			drawnBasicBrickPixelHeight = drawnBasicBrickPixelHeight * 2;
		}

		// caches to speed up rendering
		renderedBrick = new HashMap<>();
		paddedBrickMatrices = new IdentityHashMap<>();
		leftOffsets = new IdentityHashMap<>();

	}

	
	/**
	 * returns an image of the mosaic
	 * 
	 * @param mosaic
	 */
	public Image generateMosaicImage(Mosaic mosaic) {

		int startX = 0;
		int startY = 0;

		int mosaicWidth = mosaic.getMosaicWidth();
		int mosaicHeight = mosaic.getMosaicHeight();
		ColoredBrick[][] mosaicMatrix = mosaic.getMosaic();

		if (useOldMethod) {
			Brick brick;
			ColorObject color;

			Canvas mosaicImage = new Canvas(mosaicWidth * drawnBasicBrickPixelWidth, mosaicHeight * drawnBasicBrickPixelHeight);
			GraphicsContext g2d = mosaicImage.getGraphicsContext2D();

			for (int row = 0; row < mosaicHeight; row++) {
				startY = row * drawnBasicBrickPixelHeight;
				for (int column = 0; column < mosaicWidth; column++) {
					if (mosaicMatrix[row][column].isComplete()) {
						brick = mosaicMatrix[row][column].getBrick();
						color = mosaicMatrix[row][column].getColor();
						startX = column * drawnBasicBrickPixelWidth;
						paintBrick(g2d, startX, startY, brick, color.getRGB(), drawnBasicBrickPixelWidth, drawnBasicBrickPixelHeight, threeDEffect);
					}
				}
			}

			SnapshotParameters params = new SnapshotParameters();
			return mosaicImage.snapshot(params, null);
			
		} else {

			WritableImage writableImage = new WritableImage(mosaicWidth * drawnBasicBrickPixelWidth, mosaicHeight * drawnBasicBrickPixelHeight);
			PixelWriter pixelWriter = writableImage.getPixelWriter();
			for (int row = 0; row < mosaicHeight; row++) {
				startY = row * drawnBasicBrickPixelHeight;
				for (int column = 0; column < mosaicWidth; column++) {
					if (mosaicMatrix[row][column].isComplete()) {
						startX = column * drawnBasicBrickPixelWidth;
						paintBrick2(pixelWriter, startX, startY, mosaicMatrix[row][column]);
					}
				}
			}

			return writableImage;
			
		}
	}
	
	
	/**
	 * paints a colored brick, starting at the given point in the output image
	 * 
	 * @param pixelWriter
	 * @param startX
	 * @param startY
	 * @param coloredBrick
	 */
	private void paintBrick2(PixelWriter pixelWriter, int startX, int startY, ColoredBrick coloredBrick) {
		if (coloredBrick.getBrick().isRectangular()) {
			paintRectangularBrick(pixelWriter, startX, startY, coloredBrick);
		} else {
			paintNonRectangularBrick(pixelWriter, startX, startY, coloredBrick);
		}
	}

	private void paintRectangularBrick(PixelWriter pixelWriter, int startX, int startY, ColoredBrick coloredBrick) {
		final int bytesPerPixel = 4;

		Brick brick = coloredBrick.getBrick();
		boolean[][] matrix = brick.getMatrix();
		final int pixelsPerRow = drawnBasicBrickPixelWidth * matrix[0].length;

		byte[] bgraRenderedBrick = renderedBrick.get(coloredBrick);
		if (bgraRenderedBrick == null) {
			
			Color rgb = coloredBrick.getColor().getRGB();
			byte r, g, b, a;
			r = MathUtils.colorComponentToByte(rgb.getRed());
			g = MathUtils.colorComponentToByte(rgb.getGreen());
			b = MathUtils.colorComponentToByte(rgb.getBlue());
			a = MathUtils.colorComponentToByte(rgb.getOpacity());
			
			bgraRenderedBrick = new byte[matrix[0].length * matrix.length * drawnBasicBrickPixelHeight * drawnBasicBrickPixelWidth * bytesPerPixel];
			renderedBrick.put(coloredBrick, bgraRenderedBrick);

			// fill with the regular color
			for (int i = 0; i < bgraRenderedBrick.length; i += bytesPerPixel) {
				bgraRenderedBrick[i] = b;
				bgraRenderedBrick[i + 1] = g;
				bgraRenderedBrick[i + 2] = r;
				bgraRenderedBrick[i + 3] = a;
			}

			if (threeDEffect) {
				Color colorDark = rgb.darker();
				Color colorLight = rgb.brighter();
				byte rDark, gDark, bDark, aDark;
				byte rLight, gLight, bLight, aLight;
				rDark = MathUtils.colorComponentToByte(colorDark.getRed());
				gDark = MathUtils.colorComponentToByte(colorDark.getGreen());
				bDark = MathUtils.colorComponentToByte(colorDark.getBlue());
				aDark = a;
				rLight = MathUtils.colorComponentToByte(colorLight.getRed());
				gLight = MathUtils.colorComponentToByte(colorLight.getGreen());
				bLight = MathUtils.colorComponentToByte(colorLight.getBlue());
				aLight = a;

				//border left
				fillRectBgra(bgraRenderedBrick, bLight, gLight, rLight, aLight, 0, 2, 2, drawnBasicBrickPixelHeight * matrix.length - 2, pixelsPerRow);

				//border top
				fillRectBgra(bgraRenderedBrick, bLight, gLight, rLight, aLight, 0, 0, pixelsPerRow - 2, 2, pixelsPerRow);

				//border right
				fillRectBgra(bgraRenderedBrick, bDark, gDark, rDark, aDark, pixelsPerRow - 2,
						0,
						2,
						drawnBasicBrickPixelHeight * matrix.length,
						pixelsPerRow);

				//border bottom
				fillRectBgra(bgraRenderedBrick, bDark, gDark, rDark, aDark, 2,
						drawnBasicBrickPixelHeight * matrix.length - 2,
						pixelsPerRow - 2,
						2,
						pixelsPerRow);

			}
		}
		PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();
		pixelWriter.setPixels(startX, startY, pixelsPerRow, matrix.length * drawnBasicBrickPixelHeight, pixelFormat, bgraRenderedBrick, 0, pixelsPerRow * bytesPerPixel);
	}

	private void paintNonRectangularBrick(PixelWriter pixelWriter,
			int startX,
			int startY,
			ColoredBrick coloredBrick) {
		final int bytesPerPixel = 4;

		Brick brick = coloredBrick.getBrick();
		boolean[][] matrix = brick.getMatrix();

		//surround the brick matrix with false values to avoid many, many
		// bounds check conditions during the rendering
		boolean[][] paddedMatrix = paddedBrickMatrices.get(brick);
		Integer leftOffset = leftOffsets.get(brick);
		if (paddedMatrix == null) {
			leftOffset = Integer.valueOf(-1);
			paddedMatrix = new boolean[brick.getHeight() + 2][brick.getWidth() + 2];
			for (int row = 1; row < (brick.getHeight() + 1); row++) {
				for (int column = 1; column < (brick.getWidth() + 1); column++) {
					if (matrix[row-1][column-1] && leftOffset.intValue() < 0){
						leftOffset = Integer.valueOf(column-1);
					}
					paddedMatrix[row][column] = matrix[row - 1][column - 1];
				}
			}
			paddedBrickMatrices.put(brick, paddedMatrix);
			leftOffsets.put(brick, leftOffset);
		}

		startX = startX - (leftOffset.intValue() * drawnBasicBrickPixelWidth);

		Color colorNormal = coloredBrick.getColor().getRGB();
		Color colorDark = colorNormal.darker();
		Color colorLight = colorNormal.brighter();

		byte r, g, b, a;
		r = MathUtils.colorComponentToByte(colorNormal.getRed());
		g = MathUtils.colorComponentToByte(colorNormal.getGreen());
		b = MathUtils.colorComponentToByte(colorNormal.getBlue());
		a = MathUtils.colorComponentToByte(colorNormal.getOpacity());
		
		byte[] bgraRenderedBrick = renderedBrick.get(coloredBrick);
		if (bgraRenderedBrick == null) {
			// Just create a simple array with room to draw one basic brick
			bgraRenderedBrick = new byte[drawnBasicBrickPixelHeight * drawnBasicBrickPixelWidth * bytesPerPixel];
			renderedBrick.put(coloredBrick, bgraRenderedBrick);

			// fill with the regular color
			for (int i = 0; i < bgraRenderedBrick.length; i += bytesPerPixel) {
				bgraRenderedBrick[i] = b;
				bgraRenderedBrick[i + 1] = g;
				bgraRenderedBrick[i + 2] = r;
				bgraRenderedBrick[i + 3] = a;
			}
		}

		PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();
		for (int row = 1; row < (brick.getHeight() + 1); row++) {
			for (int column = 1; column < (brick.getWidth() + 1); column++) {
				if (!paddedMatrix[row][column]) {
					continue;
				}
				pixelWriter.setPixels(startX + (column-1)*drawnBasicBrickPixelWidth,
						startY + (row-1) * drawnBasicBrickPixelHeight,
						drawnBasicBrickPixelWidth,
						drawnBasicBrickPixelHeight,
						pixelFormat,
						bgraRenderedBrick,
						0,
						drawnBasicBrickPixelWidth * bytesPerPixel);

				if (threeDEffect) {
					//border left
					if (!paddedMatrix[row][column - 1]) {
						fillRect(pixelWriter, colorLight,
								startX + drawnBasicBrickPixelWidth * (column - 1),
								startY + drawnBasicBrickPixelHeight * (row - 1) + 2,
								2,
								drawnBasicBrickPixelHeight - 4);
					}
					//border top
					if (!paddedMatrix[row - 1][column]) {
						fillRect(pixelWriter, colorLight,
								startX + drawnBasicBrickPixelWidth * (column - 1) + 2,
								startY + drawnBasicBrickPixelHeight * (row - 1),
								drawnBasicBrickPixelWidth - 4,
								2);
					}
					//corner top left - but only if this really is a square with nothing above or to the left
					if (!(paddedMatrix[row][column - 1] && paddedMatrix[row - 1][column] && paddedMatrix[row][column - 1])) {
						fillRect(pixelWriter, colorLight,
								startX + drawnBasicBrickPixelWidth * (column - 1),
								startY + drawnBasicBrickPixelHeight * (row - 1),
								2,
								2);
					}
					//corner bottom left - but only if this really is a square with nothing below or to the left
					if (!(paddedMatrix[row][column - 1] && paddedMatrix[row + 1][column] && paddedMatrix[row + 1][column - 1])) {
						Color fillColor = colorLight;
						if (paddedMatrix[row][column - 1]) {
							fillColor = colorDark;
						}
						fillRect(pixelWriter, fillColor,
								startX + drawnBasicBrickPixelWidth * (column - 1),
								startY + drawnBasicBrickPixelHeight * (row - 1) + drawnBasicBrickPixelHeight - 2,
								2,
								2);
					}
					//border right
					if (!paddedMatrix[row][column + 1]) {
						fillRect(pixelWriter, colorDark,
								startX + drawnBasicBrickPixelWidth * (column - 1) + drawnBasicBrickPixelWidth - 2,
								startY + drawnBasicBrickPixelHeight * (row - 1) + 2,
								2,
								drawnBasicBrickPixelHeight - 4);
					}
					//border bottom
					if (!paddedMatrix[row + 1][column]) {
						fillRect(pixelWriter, colorDark,
								startX + drawnBasicBrickPixelWidth * (column - 1) + 2,
								startY + drawnBasicBrickPixelHeight * (row - 1) + drawnBasicBrickPixelHeight - 2,
								drawnBasicBrickPixelWidth - 4,
								2);
					}
					//corner top right
					if (!(paddedMatrix[row][column + 1] && paddedMatrix[row - 1][column] && paddedMatrix[row - 1][column + 1])) {
						Color fillColor = colorDark;
						if (paddedMatrix[row][column + 1]) {
							fillColor = colorLight;
						}
						fillRect(pixelWriter, fillColor,
								startX + drawnBasicBrickPixelWidth * (column - 1) + drawnBasicBrickPixelWidth - 2,
								startY + drawnBasicBrickPixelHeight * (row - 1),
								2,
								2);
					}
					//corner bottom right
					if (!(paddedMatrix[row][column + 1] && paddedMatrix[row + 1][column] && paddedMatrix[row + 1][column + 1])) {
						fillRect(pixelWriter, colorDark,
								startX + drawnBasicBrickPixelWidth * (column - 1) + drawnBasicBrickPixelWidth - 2,
								startY + drawnBasicBrickPixelHeight * (row - 1) + drawnBasicBrickPixelHeight - 2,
								2,
								2);
					}
				}
			}
		}
	}


	private void fillRect(PixelWriter pixelWriter, Color color, int x, int y, int width, int height) {
		for (int yOffset = 0; yOffset < height; ++yOffset) {
			for (int xOffset = 0; xOffset < width; ++xOffset) {
				pixelWriter.setColor(x + xOffset, y + yOffset, color);
			}
		}
		
	}


	private void fillRectBgra(byte[] toFill, byte b, byte g, byte r, byte a, int x, int y, int width, int height, int pixelsPerRow) {
		final int bytesPerPixel = 4;
		for (int row = 0; row < height; row++) {
			int offset = ((y + row) * pixelsPerRow + x) * bytesPerPixel;
			for (int column = 0; column < width; column++) {
				toFill[offset] = b;
				toFill[offset + 1] = g;
				toFill[offset + 2] = r;
				toFill[offset + 3] = a;
				offset += 4;
			}
		}
	}


	/**
	 * paints an brick in a specified color
	 * @param            g2d (drawing area)
	 * @param            startX
	 * @param            startY
	 * @param            brick
	 * @param            rgb (color)
	 * @param            width
	 * @param            height
	 * @param            threeD (on/off)       
	 */
	private void paintBrick(GraphicsContext g2d, int startX, int startY, Brick brick, Color rgb, int width, int height, boolean threeD) {
		Color colorNormal = rgb;
		Color colorDark;
		Color colorLight;
		int leftIndicator = -1;
		colorDark = rgb.darker();
		colorLight = rgb.brighter();

		//surround the brick matrix with zero values
		boolean[][] matrix = brick.getMatrix();
		boolean[][] matrixNew = new boolean[brick.getHeight() + 2][brick.getWidth() + 2];
		for(int row = 0; row < (brick.getHeight()+2); row++){
			for(int column = 0; column < (brick.getWidth()+2); column++){
				if(row == 0 || column == 0){ 
					matrixNew[row][column] = false; 
				}else if(row == brick.getHeight()+1 || column == brick.getWidth()+1){ 
					matrixNew[row][column] = false; 
				}else{
					if (matrix[row-1][column-1] && leftIndicator < 0){
						leftIndicator = column-1;
					}
					matrixNew[row][column] = matrix[row-1][column-1];
				}
			}
		}
		if (leftIndicator != 0) {
			startX = startX - (leftIndicator * width);
		}
		for(int row = 0; row < (brick.getHeight()+2); row++){ 
			for(int column = 0; column < (brick.getWidth()+2); column++){ 
				if (matrixNew[row][column]){
					g2d.setFill(colorNormal);
					g2d.fillRect(startX+width*(column-1),startY+height*(row-1),width,height);
					if (threeD){
						g2d.setFill(colorLight);
						//border left
						if(!matrixNew[row][column-1]){
							g2d.fillRect(startX+width*(column-1),startY+height*(row-1)+2,2,height-4);
						}
						//border top
						if(!matrixNew[row-1][column]){
							g2d.fillRect(startX+width*(column-1)+2,startY+height*(row-1),width-4,2);
						}
						//corner top left
						if (!(matrixNew[row][column-1] && matrixNew[row-1][column] && matrixNew[row][column-1])){
							g2d.fillRect(startX+width*(column-1),startY+height*(row-1),2,2);
						}
						//corner bottom left
						if (!(matrixNew[row][column-1] && matrixNew[row+1][column] && matrixNew[row+1][column-1])){
							if(matrixNew[row][column-1]){
								g2d.setFill(colorDark);
							}
							g2d.fillRect(startX+width*(column-1),startY+height*(row-1)+height-2,2,2);
							g2d.setFill(colorLight);
						}
						g2d.setFill(colorDark);
						//border right
						if(!matrixNew[row][column+1]){
							g2d.fillRect(startX+width*(column-1)+width-2,startY+height*(row-1)+2,2,height-4);
						}
						//border bottom
						if(!matrixNew[row+1][column]){
							g2d.fillRect(startX+width*(column-1)+2,startY+height*(row-1)+height-2,width-4,2);
						}
						//corner top right
						if (!(matrixNew[row][column+1] && matrixNew[row-1][column] && matrixNew[row-1][column+1])){
							if(matrixNew[row][column+1]){
								g2d.setFill(colorLight);
							}
							g2d.fillRect(startX+width*(column-1)+width-2,startY+height*(row-1),2,2);
							g2d.setFill(colorDark);
						}
						//corner bottom right
						if (!(matrixNew[row][column+1] && matrixNew[row+1][column] && matrixNew[row+1][column+1])){
							g2d.fillRect(startX+width*(column-1)+width-2,startY+height*(row-1)+height-2,2,2);
						}
					}
				}
			}
		}
	}

}
