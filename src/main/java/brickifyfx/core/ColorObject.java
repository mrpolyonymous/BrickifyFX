package brickifyfx.core;

import java.io.Serializable;

import javafx.scene.paint.Color;

/**
 * A named color.
 * 
 * @author Tobias Reichling
 */
public class ColorObject implements Serializable {

	/* Full color palette as per LDraw definitions */
	public static final ColorObject Black = new ColorObject("Black", "0", "Black", "26", Color.web("#05131D"));
	public static final ColorObject Bright_Blue = new ColorObject("Blue", "1", "Bright Blue", "23", Color.web("#0055BF"));
	public static final ColorObject Dark_Green = new ColorObject("Green", "2", "Dark Green", "28", Color.web("#257A3E"));
	public static final ColorObject Dark_Turquoise = new ColorObject("Dark_Turquoise", "3", "Bright Bluish Green",",107", Color.web("#00838F"));
	public static final ColorObject Bright_Red = new ColorObject("Red", "4", "Bright Red", "21", Color.web("#C91A09"));
	public static final ColorObject Bright_Purple = new ColorObject("Dark_Pink", "5", "Bright Purple", "221", Color.web("#C870A0"));
	public static final ColorObject Brown = new ColorObject("Brown", "6", "Brown", "217", Color.web("#583927"));
	public static final ColorObject Grey = new ColorObject("Light_Gray", "7", "Grey", "2", Color.web("#9BA19D"));
	public static final ColorObject Dark_Grey = new ColorObject("Dark_Gray", "8", "Dark Grey", "27", Color.web("#6D6E5C"));
	public static final ColorObject Light_Blue = new ColorObject("Light_Blue", "9", "Light Blue", "45", Color.web("#B4D2E3"));
	public static final ColorObject Bright_Green = new ColorObject("Bright_Green", "10", "Bright Green", "37", Color.web("#4B9F4A"));
	public static final ColorObject Medium_Bluish_Green = new ColorObject("Light_Turquoise", "11", "Medium Bluish Green", "116", Color.web("#55A5AF"));
	public static final ColorObject Brick_Red = new ColorObject("Salmon", "12", "Brick Red", "4", Color.web("#F2705E"));
	public static final ColorObject Light_Reddish_Violet = new ColorObject("Pink", "13", "Light Reddish Violet", "9", Color.web("#FC97AC"));
	public static final ColorObject Bright_Yellow = new ColorObject("Yellow", "14", "Bright Yellow", "24", Color.web("#F2CD37"));
	public static final ColorObject White = new ColorObject("White", "15", "White", "1", Color.web("#FFFFFF"));
	public static final ColorObject Light_Green = new ColorObject("Light_Green", "17", "Light Green", "6", Color.web("#C2DAB8"));
	public static final ColorObject Light_Yellow = new ColorObject("Light_Yellow", "18", "Light Yellow", "3", Color.web("#FBE696"));
	public static final ColorObject Brick_Yellow = new ColorObject("Tan", "19", "Brick Yellow", "5", Color.web("#E4CD9E"));
	public static final ColorObject Light_Bluish_Violet = new ColorObject("Light_Violet", "20", "Light Bluish Violet", "39", Color.web("#C9CAE2"));
	public static final ColorObject Bright_Violet = new ColorObject("Purple", "22", "Bright Violet", "104", Color.web("#81007B"));
	public static final ColorObject Dark_Royal_Blue = new ColorObject("Dark_Blue_Violet", "23", "Dark Royal Blue", "196", Color.web("#2032B0"));
	public static final ColorObject Bright_Orange = new ColorObject("Orange", "25", "Bright Orange", "106", Color.web("#FE8A18"));
	public static final ColorObject Bright_Reddish_Violet = new ColorObject("Magenta", "26", "Bright Reddish Violet", "124", Color.web("#923978"));
	public static final ColorObject Bright_Yellowish_Green = new ColorObject("Lime", "27", "Bright Yellowish Green", "119", Color.web("#BBE90B"));
	public static final ColorObject Sand_Yellow = new ColorObject("Dark_Tan", "28", "Sand Yellow", "138", Color.web("#958A73"));
	public static final ColorObject Light_Purple = new ColorObject("Bright_Pink", "29", "Light Purple", "222", Color.web("#E4ADC8"));
	// TODO - LDD doesn't understand this color in LDraw format. It can't import it or export it.
	public static final ColorObject Medium_Lavender = new ColorObject("Medium_Lavender", "30", "Medium Lavender", "324", Color.web("#AC78BA"));
	public static final ColorObject Lavender = new ColorObject("Lavender", "31", "Lavender", "325", Color.web("#E1D5ED"));
	public static final ColorObject Light_Yellowish_Orange = new ColorObject("Very_Light_Orange", "68", "Light Yellowish Orange", "36", Color.web("#F3CF9B"));
	public static final ColorObject Bright_Reddish_Lilac = new ColorObject("Light_Purple", "69", "Bright Reddish Lilac", "198", Color.web("#CD6298"));
	public static final ColorObject Reddish_Brown = new ColorObject("Reddish_Brown", "70", "Reddish Brown", "192", Color.web("#582A12"));
	public static final ColorObject Medium_Stone_Grey = new ColorObject("Light_Bluish_Gray", "71", "Medium Stone Grey", "194", Color.web("#A0A5A9"));
	public static final ColorObject Dark_Stone_Grey = new ColorObject("Dark_Bluish_Gray", "72", "Dark Stone Grey", "199", Color.web("#6C6E68"));
	public static final ColorObject Medium_Blue = new ColorObject("Medium_Blue", "73", "Medium Blue", "102", Color.web("#5C9DD1"));
	public static final ColorObject Medium_Green = new ColorObject("Medium_Green", "74", "Medium Green", "29", Color.web("#73DCA1"));
	public static final ColorObject Light_Pink = new ColorObject("Light_Pink", "77", "Light Pink", "223", Color.web("#FECCCF"));
	public static final ColorObject Light_Nougat = new ColorObject("Light_Flesh", "78", "Light Nougat", "283", Color.web("#F6D7B3"));
	// interesting - according to LDraw apparently there are two colors with LEGO name "Dark Orange", with different RGB values
	public static final ColorObject Medium_Dark_Flesh = new ColorObject("Medium_Dark_Flesh", "84", "Dark Orange", "38", Color.web("#CC702A"));
	public static final ColorObject Medium_Lilac = new ColorObject("Dark_Purple", "85", "Medium Lilac", "268", Color.web("#3F3691"));
	public static final ColorObject Medium_Nougat = new ColorObject("Dark_Flesh", "86", "Medium Nougat", "312", Color.web("#7C503A"));
	public static final ColorObject Medium_Royal_Blue = new ColorObject("Blue_Violet", "89", "Medium Royal Blue", "195", Color.web("#4C61DB"));
	public static final ColorObject Nougat = new ColorObject("Flesh", "92", "Nougat", "18", Color.web("#D09168"));
	public static final ColorObject Light_Red = new ColorObject("Light_Salmon", "100", "Light Red", "100", Color.web("#FEBABD"));
	public static final ColorObject Bright_Bluish_Violet = new ColorObject("Violet", "110", "Bright Bluish Violet", "110", Color.web("#4354A3"));
	public static final ColorObject Medium_Bluish_Violet = new ColorObject("Medium_Violet", "112", "Medium Bluish Violet", "112", Color.web("#6874CA"));
	public static final ColorObject Medium_Yellowish_Green = new ColorObject("Medium_Lime", "115", "Medium Yellowish Green", "115", Color.web("#C7D23C"));
	public static final ColorObject Light_Bluish_Green = new ColorObject("Aqua", "118", "Light Bluish Green", "118", Color.web("#B3D7D1"));
	public static final ColorObject Light_Yellowish_Green = new ColorObject("Light_Lime", "120", "Light Yellowish Green", "120", Color.web("#D9E4A7"));
	public static final ColorObject Light_Orange = new ColorObject("Light_Orange", "125", "Light Orange", "125", Color.web("#F9BA61"));
	public static final ColorObject Light_Stone_Grey = new ColorObject("Very_Light_Bluish_Gray", "151", "Light Stone Grey", "208", Color.web("#E6E3E0"));
	public static final ColorObject Flame_Yellowish_Orange = new ColorObject("Bright_Light_Orange", "191", "Flame Yellowish Orange", "191", Color.web("#F8BB3D"));
	public static final ColorObject Light_Royal_Blue = new ColorObject("Bright_Light_Blue", "212", "Light Royal Blue", "212", Color.web("#86C1E1"));
	public static final ColorObject Rust = new ColorObject("Rust", "216", "Rust", "216", Color.web("#B31004"));
	public static final ColorObject Cool_Yellow = new ColorObject("Bright_Light_Yellow", "226", "Cool Yellow", "226", Color.web("#FFF03A"));
	public static final ColorObject Dove_Blue = new ColorObject("Sky_Blue", "232", "Dove Blue", "232", Color.web("#56BED6"));
	public static final ColorObject Earth_Blue = new ColorObject("Dark_Blue", "272", "Earth Blue", "140", Color.web("#0D325B"));
	public static final ColorObject Earth_Green = new ColorObject("Dark_Green", "288", "Earth Green", "141", Color.web("#184632"));
	public static final ColorObject Dark_Brown = new ColorObject("Dark_Brown", "308", "Dark Brown", "308", Color.web("#352100"));
	public static final ColorObject Pastel_Blue = new ColorObject("Maersk_Blue", "313", "Pastel Blue", "11", Color.web("#54A9C8"));
	public static final ColorObject New_Dark_Red = new ColorObject("Dark_Red", "320", "New Dark Red", "154", Color.web("#720E0F"));
	public static final ColorObject Dark_Azur = new ColorObject("Dark_Azure", "321", "Dark Azur", "321", Color.web("#1498D7"));
	public static final ColorObject Medium_Azur = new ColorObject("Medium_Azure", "322", "Medium Azur", "322", Color.web("#3EC2DD"));
	// TODO - LDD doesn't understand this color in LDraw format. It can't import it or export it.
	public static final ColorObject Aqua = new ColorObject("Light_Aqua", "323", "Aqua", "323", Color.web("#BDDCD8"));
	public static final ColorObject Spring_Yellowish_Green = new ColorObject("Yellowish_Green", "326", "Spring Yellowish Green", "326", Color.web("#DFEEA5"));
	public static final ColorObject Olive_Green = new ColorObject("Olive_Green", "330", "Olive Green", "330", Color.web("#9B9A5A"));
	public static final ColorObject Sand_Red = new ColorObject("Sand_Red", "335", "Sand Red", "153", Color.web("#D67572"));
	public static final ColorObject Medium_Reddish_Violet = new ColorObject("Medium_Dark_Pink", "351", "Medium Reddish Violet", "22", Color.web("#F785B1"));
	public static final ColorObject Earth_Orange = new ColorObject("Earth_Orange", "366", "Earth Orange", "25", Color.web("#FA9C1C"));
	public static final ColorObject Sand_Violet = new ColorObject("Sand_Purple", "373", "Sand Violet", "136", Color.web("#845E84"));
	public static final ColorObject Sand_Green = new ColorObject("Sand_Green", "378", "Sand Green", "151", Color.web("#A0BCAC"));
	public static final ColorObject Sand_Blue = new ColorObject("Sand_Blue", "379", "Sand Blue", "135", Color.web("#597184"));
	public static final ColorObject Light_Orange_Brown = new ColorObject("Fabuland_Brown", "450", "Light Orange Brown", "12", Color.web("#B67B50"));
	public static final ColorObject Bright_Yellowish_Orange = new ColorObject("Medium_Orange", "462", "Bright Yellowish Orange", "105", Color.web("#FFA70B"));
	public static final ColorObject Dark_Orange = new ColorObject("Dark_Orange", "484", "Dark Orange", "38", Color.web("#A95500"));
	public static final ColorObject Light_Grey = new ColorObject("Very_Light_Gray", "503", "Light Grey", "103", Color.web("#E6E3DA"));
	
	private final String ldrawColorName;
	private final String ldrawColorNumber;
	private final String ddColorName;
	private final String ddColorNumber;
	private final Color rgbColor;
	private final Lab labColor;

	/**
	 * constructor
	 * 
	 */
	public ColorObject(String ldrawColorName, String ldrawColorNumber, String ddColorName, String ddColorNumber, Color rgbColor) {
		this.ldrawColorName = ldrawColorName;
		this.ldrawColorNumber = ldrawColorNumber;
		this.ddColorName = ddColorName;
		this.ddColorNumber = ddColorNumber;
		this.rgbColor = rgbColor;
		this.labColor = new Calculation().rgbToLab(rgbColor);
	}

	/**
	 * returns the LDraw name of the color object
	 * 
	 * @return name
	 */
	public String getName() {
		return ldrawColorName;
	}

	/**
	 * Return the LDraw color code for this color
	 */
	public String getLdrawColorNumber() {
		return ldrawColorNumber;
	}

	/**
	 * Return the Digital Designer color name
	 */
	public String getDdColorName() {
		return ddColorName;
	}

	/**
	 * Return the Digital Designer color number
	 */
	public String getDdColorNumber() {
		return ddColorNumber;
	}

	/**
	 * returns the RGB color of the color object
	 * 
	 */
	public Color getRGB() {
		return rgbColor;
	}

	/**
	 * returns the LAB color of the color object
	 * 
	 */
	public Lab getLabColor() {
		return labColor;
	}

	@Override
	public String toString() {
		return ldrawColorName + "(" + ldrawColorNumber + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ldrawColorNumber == null) ? 0 : ldrawColorNumber.hashCode());
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
		ColorObject other = (ColorObject) obj;
		if (ldrawColorNumber == null) {
			if (other.ldrawColorNumber != null)
				return false;
		} else if (!ldrawColorNumber.equals(other.ldrawColorNumber))
			return false;
		return true;
	}

}
