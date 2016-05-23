package brickifyfx.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates a particular list of colors, and gives it a name
 * 
 * @author Dan
 *
 */
public class ColorPalette {

	private static final List<ColorObject> LDD_COLOR_LIST = Arrays.asList(
			// Standard color set for Lego Digital Desginer, as of release 4.3.6
			ColorObject.Black,
			ColorObject.Dark_Stone_Grey,
			ColorObject.Medium_Stone_Grey,
			ColorObject.Light_Stone_Grey,
			ColorObject.White,
			ColorObject.New_Dark_Red,
			ColorObject.Bright_Red,
			ColorObject.Dark_Brown,
			ColorObject.Reddish_Brown,
			ColorObject.Sand_Yellow,
			ColorObject.Brick_Yellow,
			ColorObject.Dark_Orange,
			ColorObject.Nougat,
			ColorObject.Medium_Nougat,
			ColorObject.Bright_Orange,
			ColorObject.Flame_Yellowish_Orange,
			ColorObject.Light_Nougat,
			ColorObject.Bright_Yellow,
			ColorObject.Cool_Yellow,
			ColorObject.Earth_Green,
			ColorObject.Sand_Green,
			ColorObject.Dark_Green,
			ColorObject.Bright_Green,
			ColorObject.Bright_Yellowish_Green,
			ColorObject.Dark_Azur,
			ColorObject.Medium_Azur,
			ColorObject.Earth_Blue,
			ColorObject.Bright_Blue,
			ColorObject.Medium_Blue,
			ColorObject.Sand_Blue,
			ColorObject.Light_Royal_Blue,
			// Doesn't import in to LDD even though it is listed there: ColorObject.Aqua,
			ColorObject.Medium_Lilac,
			ColorObject.Bright_Reddish_Violet,
			// Doesn't import in to LDD even though it is listed there: ColorObject.Medium_Lavender,
			ColorObject.Bright_Purple,
			ColorObject.Light_Purple
			);

	private static final List<ColorObject> LDRAW_COLOR_LIST = Arrays.asList(
			ColorObject.Black,
			ColorObject.Bright_Blue,
			ColorObject.Dark_Green,
			ColorObject.Dark_Turquoise,
			ColorObject.Bright_Red,
			ColorObject.Bright_Purple,
			ColorObject.Brown,
			ColorObject.Grey,
			ColorObject.Dark_Grey,
			ColorObject.Light_Blue,
			ColorObject.Bright_Green,
			ColorObject.Medium_Bluish_Green,
			ColorObject.Brick_Red,
			ColorObject.Light_Reddish_Violet,
			ColorObject.Bright_Yellow,
			ColorObject.White,
			ColorObject.Light_Green,
			ColorObject.Light_Yellow,
			ColorObject.Brick_Yellow,
			ColorObject.Light_Bluish_Violet,
			ColorObject.Bright_Violet,
			ColorObject.Dark_Royal_Blue,
			ColorObject.Bright_Orange,
			ColorObject.Bright_Reddish_Violet,
			ColorObject.Bright_Yellowish_Green,
			ColorObject.Sand_Yellow,
			ColorObject.Light_Purple,
			ColorObject.Medium_Lavender,
			ColorObject.Lavender,
			ColorObject.Light_Yellowish_Orange,
			ColorObject.Bright_Reddish_Lilac,
			ColorObject.Reddish_Brown,
			ColorObject.Medium_Stone_Grey,
			ColorObject.Dark_Stone_Grey,
			ColorObject.Medium_Blue,
			ColorObject.Medium_Green,
			ColorObject.Light_Pink,
			ColorObject.Light_Nougat,
			ColorObject.Medium_Dark_Flesh,
			ColorObject.Medium_Lilac,
			ColorObject.Medium_Nougat,
			ColorObject.Medium_Royal_Blue,
			ColorObject.Nougat,
			ColorObject.Light_Red,
			ColorObject.Bright_Bluish_Violet,
			ColorObject.Medium_Bluish_Violet,
			ColorObject.Medium_Yellowish_Green,
			ColorObject.Light_Bluish_Green,
			ColorObject.Light_Yellowish_Green,
			ColorObject.Light_Orange,
			ColorObject.Light_Stone_Grey,
			ColorObject.Flame_Yellowish_Orange,
			ColorObject.Light_Royal_Blue,
			ColorObject.Rust,
			ColorObject.Cool_Yellow,
			ColorObject.Dove_Blue,
			ColorObject.Earth_Blue,
			ColorObject.Earth_Green,
			ColorObject.Dark_Brown,
			ColorObject.Pastel_Blue,
			ColorObject.New_Dark_Red,
			ColorObject.Dark_Azur,
			ColorObject.Medium_Azur,
			ColorObject.Aqua,
			ColorObject.Spring_Yellowish_Green,
			ColorObject.Olive_Green,
			ColorObject.Sand_Red,
			ColorObject.Medium_Reddish_Violet,
			ColorObject.Earth_Orange,
			ColorObject.Sand_Violet,
			ColorObject.Sand_Green,
			ColorObject.Sand_Blue,
			ColorObject.Light_Orange_Brown,
			ColorObject.Bright_Yellowish_Orange,
			ColorObject.Dark_Orange,
			ColorObject.Light_Grey
			);

	private static final List<ColorObject> GRAYSCALE_COLOR_LIST = Arrays.asList(
			ColorObject.Black,
			ColorObject.Dark_Stone_Grey,
			ColorObject.Medium_Stone_Grey,
			ColorObject.Light_Stone_Grey,
			ColorObject.White
			);

	
	
	public static final ColorPalette LDD_COLORS = new ColorPalette("LEGO Digital Designer", LDD_COLOR_LIST);
	public static final ColorPalette LDRAW_COLORS = new ColorPalette("LDraw", LDRAW_COLOR_LIST);
	public static final ColorPalette GRAYSCALE = new ColorPalette("Grayscale", GRAYSCALE_COLOR_LIST);

	/////////////////////////////////////////////////////////////////////////
	
	private final String name;
	private final List<ColorObject> colors;
	
	private ColorPalette(String name, List<ColorObject> colors) {
		this.name = name;
		this.colors = Collections.unmodifiableList(colors);
	}
	
	public List<ColorObject> getColors() {
		return colors;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colors == null) ? 0 : colors.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ColorPalette other = (ColorPalette) obj;
		if (colors == null) {
			if (other.colors != null)
				return false;
		} else if (!colors.equals(other.colors))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
}
