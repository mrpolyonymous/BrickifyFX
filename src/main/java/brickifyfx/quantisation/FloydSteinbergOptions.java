package brickifyfx.quantisation;

import brickifyfx.core.ColorObject;

public class FloydSteinbergOptions {

	private ColorObject dark;
	private ColorObject light;
	private int method;

	public FloydSteinbergOptions() {

	}

	public FloydSteinbergOptions(ColorObject dark, ColorObject light, int method) {
		super();
		this.dark = dark;
		this.light = light;
		this.method = method;
	}

	public ColorObject getDark() {
		return dark;
	}

	public void setDark(ColorObject dark) {
		this.dark = dark;
	}

	public ColorObject getLight() {
		return light;
	}

	public void setLight(ColorObject light) {
		this.light = light;
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

}
