/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package brickifyfx;

/**
 *
 * @author Dan
 */
public class MathUtils {

	public static double clamp(double val, double min, double max) {
		if (max < min) {
			return val;
		} else if (val < min) {
			return min;
		} else if (val > max) {
			return max;
		} else {
			return val;
		}
	}
	
	public static double min(double a, double b, double c) {
		return Math.min(Math.min(a, b), c);
	}
	
	public static double max(double a, double b, double c) {
		return Math.max(Math.max(a, b), c);
	}
	
	/**
	 * Takes an r, g, or b component from a JavaFX Color object and
	 * converts it to an equivalent byte value for an 8 bit per channel
	 * image.
	 */
	public static byte colorComponentToByte(double component) {
		if (component <= 0.0) {
			return 0;
		} else if (component >= 1.0) {
			return (byte) 255;
		} else {
			int converted = (int)(component * 255.0);
			return (byte)converted;
		}
	}
}
