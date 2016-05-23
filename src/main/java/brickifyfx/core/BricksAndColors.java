package brickifyfx.core;


/**
 * Contains information about the combination of bricks and colors to use to create a mosaic
 */
public class BricksAndColors {

	private final BrickSet brickSet;
	private final ColorPalette colorPalette;

	/**
	 * Constructor
	 * 
	 * @param brickSet
	 *            the set of bricks to use
	 * @param colorPalette
	 *            colors to use
	 */
	public BricksAndColors(BrickSet brickSet, ColorPalette colorPalette) {
		this.brickSet = brickSet;
		this.colorPalette = colorPalette;
	}

	/**
	 * Return the basic brick
	 */
	public Brick getBasicBrick() {
		return brickSet.getBasicBrick();
	}

	/**
	 * returns a collection of all colors
	 * 
	 * @return a collection of all colors
	 */
	public ColorPalette getColorPalette() {
		return colorPalette;
	}

	/**
	 * Returns a collection of all bricks
	 * 
	 * @return a collection of all bricks
	 */
	public BrickSet getBricks() {
		return brickSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brickSet == null) ? 0 : brickSet.hashCode());
		result = prime * result + ((colorPalette == null) ? 0 : colorPalette.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BricksAndColors other = (BricksAndColors) obj;
		if (brickSet == null) {
			if (other.brickSet != null)
				return false;
		} else if (!brickSet.equals(other.brickSet))
			return false;
		if (colorPalette == null) {
			if (other.colorPalette != null)
				return false;
		} else if (!colorPalette.equals(other.colorPalette))
			return false;
		return true;
	}

}