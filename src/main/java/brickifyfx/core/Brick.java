package brickifyfx.core;

import java.util.Arrays;

/**
 * contains information about a brick
 * 
 * @author Tobias Reichling
 */
public class Brick {

	private final int width;
	private final int height;
	private final boolean[][] matrix;
	private final boolean isRectangular;
	private final String name;
	private final int stability;
	private final String ldrawFile;

	/**
	 * constructor
	 * 
	 * @param name
	 * @param matrix
	 * @param stability
	 */
	public Brick(String name, boolean[][] matrix, int stability, String ldrawFile) {
		assertMatrixOkay(matrix);
		this.width = matrix[0].length;
		this.height = matrix.length;

		boolean rectangular = true;
		for (int row = 0; row < matrix.length; ++row) {
			for(int column = 0; column < matrix[0].length; ++column) {
				if (!matrix[row][column]) {
					rectangular = false;
				}
			}
		}
		this.isRectangular = rectangular;
		this.matrix = matrix;
		this.name = name;
		this.stability = stability;
		if (ldrawFile.endsWith(".dat")) {
			this.ldrawFile = ldrawFile;
		} else {
			this.ldrawFile = ldrawFile + ".dat";
		}
	}
	
	/**
	 * Make sure the first row of the matrix has a "true" value, and the first column of the matrix also has a "true" value
	 */
	private static void assertMatrixOkay(boolean[][] matrix) {
		for (int row = 1; row < matrix.length; ++row) {
			if (matrix[row].length != matrix[0].length) {
				throw new IllegalArgumentException("matrix is not rectangular");
			}
		}
		
		boolean firstRowOkay = false, firstColumnOkay = false, lastRowOkay = false, lastColumnOkay = false;

		for (int col = 0; !firstRowOkay && col < matrix[0].length; ++col) {
			firstRowOkay |= matrix[0][col];
		}
		for (int col = 0; !lastRowOkay && col < matrix[0].length; ++col) {
			lastRowOkay |= matrix[matrix.length-1][col];
		}

		for (int rowNumber = 0; !firstColumnOkay && rowNumber < matrix.length; ++rowNumber) {
			boolean[] row = matrix[rowNumber];
			if (row[0]) {
				firstColumnOkay = true;
			}
		}
		for (int rowNumber = 0; !lastColumnOkay && rowNumber < matrix.length; ++rowNumber) {
			boolean[] row = matrix[rowNumber];
			if (row[row.length-1]) {
				lastColumnOkay = true;
			}
		}
		if (!firstRowOkay || !firstColumnOkay || !lastRowOkay || !lastColumnOkay) {
			throw new IllegalArgumentException("Invalid brick matrix");
		}
		
		// TODO other checks: matrix is connected
	}
	
	/**
	 * returns the width of the brick in terms of brick units/studs
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * returns the name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns the height
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * returns the matrix
	 * 
	 * @return msatrix
	 */
	public boolean[][] getMatrix() {
		return matrix;
	}

	/**
	 * returns the stability
	 * 
	 * @return stability
	 */
	public int getStability() {
		return stability;
	}

	/**
	 * Return whether or not this brick is rectangular, i.e. it's an n by m brick with no gaps or holes
	 */
	public boolean isRectangular() {
		return isRectangular;
	}

	public String getLdrawFile() {
		return ldrawFile;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + Arrays.hashCode(matrix);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + stability;
		result = prime * result + width;
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
		Brick other = (Brick) obj;
		if (height != other.height)
			return false;
		if (!Arrays.deepEquals(matrix, other.matrix))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (stability != other.stability)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

}
