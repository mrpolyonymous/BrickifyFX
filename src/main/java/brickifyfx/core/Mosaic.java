package brickifyfx.core;

/**
 * The class that stores the rectangular matrix of bricks that make up the mosaic.
 * 
 * @author Tobias Reichling
 */
public class Mosaic {
	
	private int mosaicWidth;
	private int mosaicHeight;
	private ColoredBrick[][] mosaicMatrix;

	/**
	 * @param width
	 * 	The number of building bricks wide to make the mosaic 
	 * @param height
	 * 	The number of building bricks high to make the mosaic 
	 */
	public Mosaic(int width, int height)
	{
		this.mosaicWidth = width;
		this.mosaicHeight = height;
		this.mosaicMatrix = new ColoredBrick[height][width];
		// Initialize the mosaic matrix with the special "empty" brick 
		for (int row = 0; row < height; row++){
			for (int column = 0; column < width; column++){
				this.mosaicMatrix[row][column] = EmptyColoredBrick.EMPTY_COLORED_BRICK;
			}
		}
	}

	/**
	 * Reinitializes the brick at a given position in the mosaic matrix to be empty
	 * 
	 * @param row
	 * @param column
	 */
	public void reinitialize(int row, int column) {
		this.mosaicMatrix[row][column] = EmptyColoredBrick.EMPTY_COLORED_BRICK;
	}

	/**
	 * Sets the colour of the mosaic at a given position in the mosaic matrix
	 * 
	 * @param row
	 * @param column
	 * @param color
	 */
	public void setColor(int row, int column, ColorObject color) {
		if (mosaicMatrix[row][column] == EmptyColoredBrick.EMPTY_COLORED_BRICK) {
			mosaicMatrix[row][column] = new ColoredBrick();
		}
		this.mosaicMatrix[row][column].setColor(color);
	}

	/**
	 * Sets the brick used to build the mosaic at a given position in the mosaic matrix
	 * 
	 * @param row
	 * @param column
	 * @param brick
	 */
	public void setBrick(int row, int column, Brick brick) {
		if (mosaicMatrix[row][column] == EmptyColoredBrick.EMPTY_COLORED_BRICK) {
			mosaicMatrix[row][column] = new ColoredBrick();
		}
		mosaicMatrix[row][column].setBrick(brick);
	}

	/**
	 * Returns the mosaic matrix
	 * 
	 * @return mosaic matrix
	 */
	public ColoredBrick[][] getMosaic() {
		return this.mosaicMatrix;
	}

	/**
	 * Returns the width of the mosaic in terms of number building bricks.
	 */
	public int getMosaicWidth() {
		return this.mosaicWidth;
	}

	/**
	 * Returns the height of the mosaic in terms of number building bricks.
	 */
	public int getMosaicHeight() {
		return this.mosaicHeight;
	}

}
