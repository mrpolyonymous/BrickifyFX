package brickifyfx.core;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;

/**
 * Utility class with mathematical methods
 * @author           Tobias Reichling / Adrian Schuetz
 */
public class Calculation {
	
	private static RandomFactory randomFactory = new BasicRandomFactory();
	
	public static void setRandomFactory(RandomFactory randomFactory_) {
		randomFactory = randomFactory_;
	}
	
	/**
	 * computes a s-rgb color object from a given cie-lab color object
	 * @param            lab
	 * @return           rgb
	 */
	public Color labToRgb(Lab lab){
		double x,y,z,r,g,b;		
		//----------------------------------------------------
		y = (lab.getL() + 16.0) / 116.0;
		x = lab.getA() / 500.0 + y;
		z = y - lab.getB() / 200.0;
		//----------------------------------------------------
		if(java.lang.Math.pow(y, 3.0) > 0.008856){
			y = java.lang.Math.pow(y, 3.0);
		}else{
			y = ( y - 16.0 / 116.0 ) / 7.787;
		}
		if(java.lang.Math.pow(x, 3.0) > 0.008856){
			x = java.lang.Math.pow(x, 3.0);
		}else{
			x = ( x - 16.0 / 116.0 ) / 7.787;
		}
		if(java.lang.Math.pow(z, 3.0) > 0.008856){
			z = java.lang.Math.pow(z, 3.0);
		}else{
			z = ( z - 16.0 / 116.0 ) / 7.787;
		}
		//----------------------------------------------------
		x = x *  95.047 / 100.0;  //  >
		y = y * 100.000 / 100.0;  //   > reference values: CIE Observer= 2°, Illuminant= D65
		z = z * 108.883 / 100.0;  //  >
		//----------------------------------------------------
		r = x *  3.2406 + y * -1.5372 + z * -0.4986;
		g = x * -0.9689 + y * 1.8758  + z *  0.0415;
		b = x *  0.0557 + y * -0.2040 + z *  1.0570;
		//----------------------------------------------------
		if (r > 0.0031308){
			r = 1.055 * java.lang.Math.pow(r, (1.0 / 2.4)) - 0.055;
		}else{
			r = 12.92 * r;
		}
		if (g > 0.0031308){
			g = 1.055 * java.lang.Math.pow(g, (1.0 / 2.4)) - 0.055;
		}else{
			g = 12.92 * g;
		}
		if (b > 0.0031308){
			b = 1.055 * java.lang.Math.pow(b, (1.0 / 2.4)) - 0.055;
		}else{
			b = 12.92 * b;
		}
		//----------------------------------------------------
		Color rgb = new Color(r, g, b, 1.0);
		return rgb;
	}
	
	/**
	 * computes a cie-lab color object from a given s-rgb color object
	 * @param            rgb
	 * @return           lab
	 */
	public Lab rgbToLab(Color rgb) {
		return rgbToLab(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
	}
	
	private static final double INT_COMPONENT_TO_DOUBLE = 1.0 / 255.0;

	/**
	 * computes a cie-lab color object from integer s-rgb color components in the range [0..255]
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return lab
	 */
	public Lab rgbToLab(int r, int g, int b) {
		return rgbToLab(r * INT_COMPONENT_TO_DOUBLE, g * INT_COMPONENT_TO_DOUBLE, b * INT_COMPONENT_TO_DOUBLE);
	}

	/**
	 * computes a cie-lab color object from s-rgb color components in the range [0..1]
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return lab
	 */
	public Lab rgbToLab(double r, double g, double b) {
		double x, y, z;

		if (r > 0.04045) {
			r = java.lang.Math.pow(((r + 0.055) / 1.055), 2.4);
		} else {
			r = r / 12.92;
		}
		if (g > 0.04045) {
			g = java.lang.Math.pow(((g + 0.055) / 1.055), 2.4);
		} else {
			g = g / 12.92;
		}
		if (b > 0.04045) {
			b = java.lang.Math.pow(((b + 0.055) / 1.055), 2.4);
		} else {
			b = b / 12.92;
		}
		//----------------------------------------------------
		r = r * 100.0;
		g = g * 100.0;
		b = b * 100.0;
		//----------------------------------------------------
		x = r * 0.412424 + g * 0.357579 + b * 0.180464;
		y = r * 0.212656 + g * 0.715158 + b * 0.072186;
		z = r * 0.019332 + g * 0.119193 + b * 0.950444;
		//----------------------------------------------------
		x = x /  95.047;  //   >
		y = y / 100.000;  //    > reference values: CIE Observer= 2°, Illuminant= D65
		z = z / 108.883;  //   >
		//----------------------------------------------------
		if (x > 0.008856) {
			x = java.lang.Math.pow(x, (1.0 / 3.0));
		} else {
			x = (7.787 * x) + (16.0 / 116.0);
		}
		if (y > 0.008856) {
			y = java.lang.Math.pow(y, (1.0 / 3.0));
		} else {
			y = (7.787 * y) + (16.0 / 116.0);
		}
		if (z > 0.008856) {
			z = java.lang.Math.pow(z, (1.0 / 3.0));
		} else {
			z = (7.787 * z) + (16.0 / 116.0);
		}
		//----------------------------------------------------
		Lab lab = new Lab((116.0 * y ) - 16.0,
				500.0 * ( x - y ),
				200.0 * ( y - z ));
		return lab;
	}
	
	/**
	 * scaling a picture
	 * @param            image                  source image
	 * @param            destinationWidth       destination image width
	 * @param            destinationHeight      destination image height
	 * @param            interpolation          1=bicubic, 2=bilinear, 3=nearestneighbor 
	 * @return           destination image
	 */
	public BufferedImage scale(BufferedImage image, int destinationWidth, int destinationHeight, InterpolationMethod interpolation){
		double factorX = ((double)destinationWidth/(double)image.getWidth());
		double factorY = ((double)destinationHeight/(double)image.getHeight());
		BufferedImage imageScaled = new BufferedImage(destinationWidth, destinationHeight, image.getType());
		Graphics2D g2 = imageScaled.createGraphics();
		switch (interpolation){
		case BICUBIC:
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			break;

		case BILINEAR:
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			break;

		case NEARESTNEIGHBOR:
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			break;

		default:
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			break;
		}
		AffineTransform at = AffineTransform.getScaleInstance(factorX, factorY);
		g2.drawRenderedImage(image, at);
		return imageScaled;
	}
	
	/**
	 * computes random coordinates
	 * 
	 * @param width
	 *            mosaic width
	 * @param height
	 *            mosaic height
	 * @return random coordinates as vector
	 */
	public List<Integer> randomCoordinates(int width, int height) {
		List<Integer> randomCoordinates = new ArrayList<>(2 * height * width);
		Random random = randomFactory.getRandom();
		int[][] coordinates = new int[2][(height*width)];
		int position;
		int tempX, tempY;
		//initialize coordinates
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				coordinates[0][((i*width)+j)] = i;
				coordinates[1][((i*width)+j)] = j;
	    }}
		//randomize coordinates
		for (int i = 0; i < (width*height); i++) {
		    position = random.nextInt(width*height);
		    tempX = coordinates[0][i];
		    tempY = coordinates[1][i];
		    coordinates[0][i] = coordinates[0][position];
		    coordinates[1][i] = coordinates[1][position];
		    coordinates[0][position] = tempX;
		    coordinates[1][position] = tempY;
		}
		//add coordinates to the vector
		for (int x = 0; x < (height*width); x++){
			randomCoordinates.add(Integer.valueOf(coordinates[0][x]));
			randomCoordinates.add(Integer.valueOf(coordinates[1][x]));
		}
		return randomCoordinates;
	}
	
	/**
	 * computes coordinates along a hilbert curve
	 * @param            mosaicWidth
	 * @param            mosaicHeight
	 * @return           hilbert coordinates as vector
	 */
	public List<Integer> hilbertCoordinates(int mosaicWidth, int mosaicHeight) {
		List<Integer> hilbertCoordinates = new ArrayList<>();
		int length = 0;
		//length = largest mosaic dimension
		if (mosaicWidth > mosaicHeight) {
			length = mosaicWidth;
		} else {
			length = mosaicHeight;
		}
		//computes the recursion depth from the length
		int n = 1;
		while (Math.pow(2, n) < length) {
			n++;
		}
		//raise the length to the power of 2 because
		//we need a square (2^int x 2^int) to compute the hilbert coordinates
		length = (int)Math.pow(2, n);
		//
		//  type 0    type 1    type 2    type 3
		//  +------+  /\     +  +------+  <------+  
		//  |         |      |  |      |         |    
		//  |         |      |  |      |         |  
		//  +------>  +------+  +     \/  +------+  
		//
		//start recursion with type 0
		hilbert(0,0,0,length,n,hilbertCoordinates, mosaicWidth, mosaicHeight);
		return hilbertCoordinates;
	}

	/**
	 * computes coordinates along a hilbert curve (recursion)
	 * @param            mosaicWidth
	 * @param            mosaicHeight
	 */
	private void hilbert(int type,int x,int y,int length,int n, List<Integer> coordinates, int mosaicWidth, int mosaicHeight){
		//l2 = half length
		//for computing the four subsquares
		int l2=length/2;
		//add the coordinates to the vector when the lowest recursions depth is reached
		if(n==0){
			//coordinates are interchanged because we need them
			//to index values in an array
			//add only coordinates which are positioned in the original mosaic dimensions
			if(y < mosaicHeight && x < mosaicWidth){
				coordinates.add(Integer.valueOf(y));
				coordinates.add(Integer.valueOf(x));
			}
		}
		//if not the lowest recursions depth is reached, call deeper recursions
		else{
			//decrements the recursions depth
			n=n-1;
			//
			//  type 0    type 1    type 2    type 3
			//  +------+  /\     +  +------+  <------+  
			//  |         |      |  |      |         |    
			//  |         |      |  |      |         |  
			//  +------>  +------+  +     \/  +------+  
			//
			if (type == 0) {
				hilbert(1, x+l2, y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(0, x,    y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(0, x,    y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(2, x+l2, y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
			}
			if (type == 1) {
				hilbert(0, x+l2, y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(1, x+l2, y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(1, x,    y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(3, x,    y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
			}
			if (type == 2) { 
				hilbert(3, x,    y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(2, x,    y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(2, x+l2, y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(0, x+l2, y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
			}
			if (type == 3) {   
				hilbert(2, x,    y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(3, x+l2, y+l2, l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(3, x+l2, y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
				hilbert(1, x,    y,    l2, n, coordinates, mosaicWidth, mosaicHeight);
			}
		}
	}
}
