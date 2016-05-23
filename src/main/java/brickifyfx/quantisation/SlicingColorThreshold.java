package brickifyfx.quantisation;

import brickifyfx.core.ColorObject;

public class SlicingColorThreshold {

	private ColorObject color;
	private int threshold;

	public SlicingColorThreshold(ColorObject color, int threshold) {
		super();
		this.color = color;
		this.threshold = threshold;
	}

	public ColorObject getColor() {
		return color;
	}

	public void setColor(ColorObject color) {
		this.color = color;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

}
