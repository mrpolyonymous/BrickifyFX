package brickifyfx.core;

/**
 * Encapsulation of CIE-LAB color values
 * 
 * @author Tobias Reichling
 */
public class Lab {

	private double l, a, b;

	/**
	 * constructor
	 */
	public Lab() {
		this.l = 0.0;
		this.a = 0.0;
		this.b = 0.0;
	}

	/**
	 * constructor
	 * 
	 * @param l
	 *            l-value LAB
	 * @param a
	 *            a-value LAB
	 * @param b
	 *            b-value LAB
	 */
	public Lab(double l, double a, double b) {
		this.l = l;
		this.a = a;
		this.b = b;
	}

	/**
	 * sets the l value
	 */
	public void setL(double l) {
		this.l = l;
	}

	/**
	 * returns the l value
	 * 
	 * @return l value
	 */
	public double getL() {
		return this.l;
	}

	/**
	 * sets the a value
	 * 
	 * @param a
	 *            a value
	 */
	public void setA(double a) {
		this.a = a;
	}

	/**
	 * returns the a value
	 * 
	 * @return a value
	 */
	public double getA() {
		return this.a;
	}

	/**
	 * sets the b value
	 * 
	 * @param b
	 *            b value
	 */
	public void setB(double b) {
		this.b = b;
	}

	/**
	 * returns the b value
	 * 
	 * @return b value
	 */
	public double getB() {
		return this.b;
	}

	@Override
	public String toString() {
		return "(L=" + l + ",a="+a+",b="+b+")";
	}
}
