package brickifyfx.core;

/**
 * Special placeholder subclass of ColoredBrick used as a sentinel for areas of the mosaic covered
 * by larger bricks in other rows or columns, instead of null.
 * 
 * @author Dan
 * 
 */
public class EmptyColoredBrick extends ColoredBrick {

	public static final EmptyColoredBrick EMPTY_COLORED_BRICK = new EmptyColoredBrick();

	private EmptyColoredBrick() {
		// private constructor
	}

	@Override
	public ColorObject getColor() {
		return null;
	}

	@Override
	public void setColor(ColorObject color) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Brick getBrick() {
		return null;
	}

	@Override
	public void setBrick(Brick brick) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isColorOnly() {
		return false;
	}

	@Override
	public boolean isEmptyOrCovered() {
		return true;
	}

	@Override
	public boolean isComplete() {
		return false;
	}

	@Override
	public String toString() {
		return "empty";
	}
}
