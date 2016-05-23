package brickifyfx.core;

/**
 * Represents the combination of a brick and a color for the brick.
 * 
 * @author Dan
 *
 */
public class ColoredBrick {

	private ColorObject color;
	private Brick brick;

	public ColoredBrick() {

	}

	public ColoredBrick(ColorObject color, Brick brick) {
		super();
		this.color = color;
		this.brick = brick;
	}

	public ColorObject getColor() {
		return color;
	}

	public void setColor(ColorObject color) {
		this.color = color;
	}

	public Brick getBrick() {
		return brick;
	}

	public void setBrick(Brick brick) {
		this.brick = brick;
	}
	
	public boolean isColorOnly() {
		return color != null && brick == null;
	}
	
	public boolean isEmptyOrCovered() {
		return (color == null && brick == null) || (color != null && brick != null);
	}

	public boolean isComplete() {
		return color != null && brick != null;
	}

	@Override
	public String toString() {
		return String.valueOf(brick) + " " + String.valueOf(color);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((brick == null) ? 0 : brick.hashCode());
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
		ColoredBrick other = (ColoredBrick) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (brick == null) {
			if (other.brick != null)
				return false;
		} else if (!brick.equals(other.brick))
			return false;
		return true;
	}

	/**
	 * Creates a shallow copy of a ColoredBrick
	 */
	public static ColoredBrick copy(ColoredBrick original) {
		if (original == null) {
			return null;
		}
		return new ColoredBrick(original.color, original.brick);
	}


}
