package brickifyfx.quantisation;

import java.util.Random;

import brickifyfx.core.ColorObject;
import brickifyfx.core.Lab;

class PatternDitherColor {

	private final Lab mixedColor;
	private final ColorObject color1;
	private final ColorObject color2;
	private final ColorObject color3;
	private final ColorObject color4;

	public PatternDitherColor(
			ColorObject color1,
			ColorObject color2,
			ColorObject color3,
			ColorObject color4) {
		this.color1 = color1;
		this.color2 = color2;
		this.color3 = color3;
		this.color4 = color4;
		this.mixedColor = computeMixedColorValue(color1, color2, color3, color4);
	}

	/**
	 * computes the average color value from a color pattern ( color 1 + color 2 + color 3 + color 4 ) / 4
	 */
	private static Lab computeMixedColorValue(ColorObject color1, ColorObject color2, ColorObject color3, ColorObject color4) {
		Lab lab1 = color1.getLabColor();
		Lab lab2 = color2.getLabColor();
		Lab lab3 = color3.getLabColor();
		Lab lab4 = color4.getLabColor();
		return new Lab((lab1.getL() + lab2.getL() + lab3.getL() + lab4.getL()) * 0.25,
				(lab1.getA() + lab2.getA() + lab3.getA() + lab4.getA()) * 0.25,
				(lab1.getB() + lab2.getB() + lab3.getB() + lab4.getB()) * 0.25);
	}

	public Lab getMixedColor() {
		return mixedColor;
	}

	public ColorObject getColor1() {
		return color1;
	}

	public ColorObject getColor2() {
		return color2;
	}

	public ColorObject getColor3() {
		return color3;
	}

	public ColorObject getColor4() {
		return color4;
	}

	public ColorObject getRandomColor(Random random) {
		int r = random.nextInt() & 0x3;
		if (r == 0) {
			return color1;
		} else if (r == 1) {
			return color2;
		} else if (r == 2) {
			return color3;
		} else  {
			return color4;
		}
	}

}
