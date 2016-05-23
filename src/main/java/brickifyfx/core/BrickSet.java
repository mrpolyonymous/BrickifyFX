package brickifyfx.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A set of Bricks that always includes a basic 1 by 1 brick
 */
public class BrickSet {

	private static final Brick PLATE_BASIC_TOP = new Brick("Plate 1 x 1", new boolean[][]{ { true } }, 1, "3024.dat");
	private static final Brick PLATE_BASIC_SIDE = new Brick("Plate 1 x 1", new boolean[][]{ { true } }, 7, "3024.dat");

	private static final Brick[] TOP_BRICKS_1_BY_1;
	private static final Brick[] TOP_BRICKS_SMALL;
	private static final Brick[] TOP_BRICKS;
	private static final Brick[] SIDE_BRICKS_1_BY_1;
	private static final Brick[] SIDE_BRICKS;

	static {
		boolean[] TRUE1 = {true};
		boolean[] TRUE2 = {true, true};
		boolean[] TRUE3 = {true, true, true};
		boolean[] TRUE4 = {true, true, true, true};
		boolean[] TRUE6 = {true, true, true, true, true, true};
		boolean[] TRUE8 = {true, true, true, true, true, true, true, true};
		boolean[][] plate_2x1			= 	{TRUE2};
		boolean[][] plate_1x2			= 	{TRUE1,TRUE1};
		boolean[][] plate_3x1			= 	{TRUE3};
		boolean[][] plate_1x3			= 	{TRUE1,TRUE1,TRUE1};
		boolean[][] plate_4x1			= 	{TRUE4};
		boolean[][] plate_1x4			= 	{TRUE1,TRUE1,TRUE1,TRUE1};
		boolean[][] plate_6x1			= 	{TRUE6};
		boolean[][] plate_1x6			= 	{TRUE1,TRUE1,TRUE1,TRUE1,TRUE1,TRUE1};
		boolean[][] plate_8x1			= 	{TRUE8};
		boolean[][] plate_1x8			= 	{TRUE1,TRUE1,TRUE1,TRUE1,TRUE1,TRUE1,TRUE1,TRUE1};
		boolean[][] plate_2x2			= 	{TRUE2,TRUE2};
		boolean[][] plate_2x2_Corner		= 	{TRUE2,{true,false}};
		boolean[][] plate_2x2_Corner_90	= 	{TRUE2,{false,true}};
		boolean[][] plate_2x2_Corner_180	= 	{{false,true},TRUE2};
		boolean[][] plate_2x2_Corner_270	= 	{{true,false},TRUE2};
		boolean[][] plate_3x2			= 	{TRUE3,TRUE3};
		boolean[][] plate_2x3			= 	{TRUE2,TRUE2,TRUE2};
		boolean[][] plate_4x2			= 	{TRUE4,TRUE4};
		boolean[][] plate_2x4			= 	{TRUE2,TRUE2,TRUE2,TRUE2};
		boolean[][] plate_6x2			= 	{TRUE6,TRUE6};
		boolean[][] plate_2x6			= 	{TRUE2,TRUE2,TRUE2,TRUE2,TRUE2,TRUE2};
		boolean[][] plate_8x2			= 	{TRUE8,TRUE8};
		boolean[][] plate_2x8			= 	{TRUE2,TRUE2,TRUE2,TRUE2,TRUE2,TRUE2,TRUE2,TRUE2};
		boolean[][] plate_4x4			= 	{TRUE4,TRUE4,TRUE4,TRUE4};
		boolean[][] plate_4x4_Corner		= 	{TRUE4,TRUE4,{true,true,false,false},{true,true,false,false}};
		boolean[][] plate_4x4_Corner_90	= 	{TRUE4,TRUE4,{false,false,true,true},{false,false,true,true}};
		boolean[][] plate_4x4_Corner_180	= 	{{false,false,true,true},{false,false,true,true},TRUE4,TRUE4};
		boolean[][] plate_4x4_Corner_270	= 	{{true,true,false,false},{true,true,false,false},TRUE4,TRUE4};
		boolean[][] plate_6x4			= 	{TRUE6,TRUE6,TRUE6,TRUE6};
		boolean[][] plate_4x6			= 	{TRUE4,TRUE4,TRUE4,TRUE4,TRUE4,TRUE4};
		boolean[][] plate_8x4			= 	{TRUE8,TRUE8,TRUE8,TRUE8};
		boolean[][] plate_4x8			= 	{TRUE4,TRUE4,TRUE4,TRUE4,TRUE4,TRUE4,TRUE4,TRUE4};
		boolean[][] plate_6x6			= 	{TRUE6,TRUE6,TRUE6,TRUE6,TRUE6,TRUE6};
		boolean[][] plate_8x6			= 	{TRUE8,TRUE8,TRUE8,TRUE8,TRUE8,TRUE8};
		boolean[][] plate_6x8			= 	{TRUE6,TRUE6,TRUE6,TRUE6,TRUE6,TRUE6,TRUE6,TRUE6};
		boolean[][] plate_8x8			= 	{TRUE8,TRUE8,TRUE8,TRUE8,TRUE8,TRUE8,TRUE8,TRUE8};

		TOP_BRICKS_1_BY_1 = new Brick[] {
				PLATE_BASIC_TOP
		};
		TOP_BRICKS_SMALL = new Brick[] {
				new Brick("Plate 2 x 1", plate_2x1, 1, "3023"),
				new Brick("Plate 1 x 2", plate_1x2, 1, "3023"),
				new Brick("Plate 3 x 1", plate_3x1, 1, "3623"),
				new Brick("Plate 1 x 3", plate_1x3, 1, "3623"),
				new Brick("Plate 4 x 1", plate_4x1, 1, "3710"),
				new Brick("Plate 1 x 4", plate_1x4, 1, "3710"),
				new Brick("Plate 2 x 2 Corner", plate_2x2_Corner, 1, "2420"),
				new Brick("Plate 2 x 2 Corner 90", plate_2x2_Corner_90, 1, "2420"),
				new Brick("Plate 2 x 2 Corner 180", plate_2x2_Corner_180, 1, "2420"),
				new Brick("Plate 2 x 2 Corner 270", plate_2x2_Corner_270, 1, "2420"),
				new Brick("Plate 2 x 2", plate_2x2, 1, "3022"),
		};
		TOP_BRICKS = new Brick[] {
			new Brick("Plate 2 x 1", plate_2x1, 1, "3023"),
			new Brick("Plate 1 x 2", plate_1x2, 1, "3023"),
			new Brick("Plate 3 x 1", plate_3x1, 1, "3623"),
			new Brick("Plate 1 x 3", plate_1x3, 1, "3623"),
			new Brick("Plate 4 x 1", plate_4x1, 1, "3710"),
			new Brick("Plate 1 x 4", plate_1x4, 1, "3710"),
			new Brick("Plate 6 x 1", plate_6x1, 1, "3666"),
			new Brick("Plate 1 x 6", plate_1x6, 1, "3666"),
			new Brick("Plate 8 x 1", plate_8x1, 1, "3460"),
			new Brick("Plate 1 x 8", plate_1x8, 1, "3460"),
			new Brick("Plate 2 x 2 Corner", plate_2x2_Corner, 1, "2420"),
			new Brick("Plate 2 x 2 Corner 90", plate_2x2_Corner_90, 1, "2420"),
			new Brick("Plate 2 x 2 Corner 180", plate_2x2_Corner_180, 1, "2420"),
			new Brick("Plate 2 x 2 Corner 270", plate_2x2_Corner_270, 1, "2420"),
			new Brick("Plate 2 x 2", plate_2x2, 1, "3022"),
			new Brick("Plate 3 x 2", plate_3x2, 1, "3021"),
			new Brick("Plate 2 x 3", plate_2x3, 1, "3021"),
			new Brick("Plate 4 x 2", plate_4x2, 1, "3020"),
			new Brick("Plate 2 x 4", plate_2x4, 1, "3020"),
			new Brick("Plate 6 x 2", plate_6x2, 1, "3795"),
			new Brick("Plate 2 x 6", plate_2x6, 1, "3795"),
			new Brick("Plate 8 x 2", plate_8x2, 1, "3034"),
			new Brick("Plate 2 x 8", plate_2x8, 1, "3034"),
			new Brick("Plate 4 x 4", plate_4x4, 1, "3031"),
			new Brick("Plate 4 x 4 Corner", plate_4x4_Corner, 1, "2639"),
			new Brick("Plate 4 x 4 Corner 90", plate_4x4_Corner_90, 1, "2639"),
			new Brick("Plate 4 x 4 Corner 180", plate_4x4_Corner_180, 1, "2639"),
			new Brick("Plate 4 x 4 Corner 270", plate_4x4_Corner_270, 1, "2639"),
			new Brick("Plate 6 x 4", plate_6x4, 1, "3032"),
			new Brick("Plate 4 x 6", plate_4x6, 1, "3032"),
			new Brick("Plate 8 x 4", plate_8x4, 1, "3035"),
			new Brick("Plate 4 x 8", plate_4x8, 1, "3035"),
			new Brick("Plate 6 x 6", plate_6x6, 1, "3958"),
			new Brick("Plate 8 x 6", plate_8x6, 1, "3036"),
			new Brick("Plate 6 x 8", plate_6x8, 1, "3036"),
			new Brick("Plate 8 x 8", plate_8x8, 1, "41539"),
		};
		

		// Side-on
		boolean[][] brick_1x1			= 	{TRUE1,TRUE1,TRUE1};
		boolean[][] brick_1x2			= 	{TRUE2,TRUE2,TRUE2};
		boolean[][] brick_1x3			= 	{TRUE3,TRUE3,TRUE3};
		boolean[][] brick_1x4			= 	{TRUE4,TRUE4,TRUE4};
		boolean[][] brick_1x6			= 	{TRUE6,TRUE6,TRUE6};
		boolean[][] brick_1x8			= 	{TRUE8,TRUE8,TRUE8};
		SIDE_BRICKS_1_BY_1 = new Brick[] {
				PLATE_BASIC_SIDE
		};
		SIDE_BRICKS = new Brick[] {
				new Brick("Plate 1 x 2", plate_2x1, 8, "3023"),
				new Brick("Plate 1 x 3", plate_3x1, 9, "3623"),
				new Brick("Plate 1 x 4", plate_4x1, 10, "3710"),
				new Brick("Plate 1 x 6", plate_6x1, 11, "3666"),
				new Brick("Plate 1 x 8", plate_8x1, 12, "3460"),
				new Brick("Brick 1 x 1", brick_1x1, 1, "3005"),
				new Brick("Brick 1 x 2", brick_1x2, 2, "3004"),
				new Brick("Brick 1 x 3", brick_1x3, 3, "3622"),
				new Brick("Brick 1 x 4", brick_1x4, 4, "3010"),
				new Brick("Brick 1 x 6", brick_1x6, 5, "3009"),
				new Brick("Brick 1 x 8", brick_1x8, 6, "3008")
				
		};
	}

	public static final BrickSet TOP_DOWN_1_BY_1_PLATE = new BrickSet("Top-down 1 by 1 plate", true, PLATE_BASIC_TOP, 1, 1, Arrays.asList(TOP_BRICKS_1_BY_1));
	public static final BrickSet TOP_DOWN_SMALL = new BrickSet("Top-down small plates", true, PLATE_BASIC_TOP, 1, 1, Arrays.asList(TOP_BRICKS_SMALL));
	public static final BrickSet TOP_DOWN = new BrickSet("Top-down plates", true, PLATE_BASIC_TOP, 1, 1, Arrays.asList(TOP_BRICKS));
	public static final BrickSet SIDE_ON_1_BY_1_PLATE = new BrickSet("Side-on 1 by 1 plate", false, PLATE_BASIC_SIDE, 5, 2, Arrays.asList(SIDE_BRICKS_1_BY_1));
	public static final BrickSet SIDE_ON = new BrickSet("Side-on plates and bricks", false, PLATE_BASIC_SIDE, 5, 2, Arrays.asList(SIDE_BRICKS));

	//////////////////////////////////////////////////////////////
	
	private final String name;
	private final boolean topDown;
	private final Brick basicBrick;
	private final List<Brick> bricks;
	private final int basicBrickWidthRatio;
	private final int basicBrickHeightRatio;
	
	private BrickSet(String name, boolean topDown, Brick basicBrick, int basicBrickWidthRatio, int basicBrickHeightRatio, List<Brick> bricks) {
		this.name = name;
		this.topDown = topDown;
		this.basicBrick = basicBrick;
		this.basicBrickWidthRatio = basicBrickWidthRatio;
		this.basicBrickHeightRatio = basicBrickHeightRatio;
		List<Brick> initialBrickList = new ArrayList<>(bricks);
		if (!initialBrickList.contains(basicBrick)) {
			initialBrickList.add(basicBrick);
		}
		this.bricks = Collections.unmodifiableList(initialBrickList);
	}

	public boolean isTopDown() {
		return topDown;
	}

	public int getBasicBrickWidthRatio() {
		return basicBrickWidthRatio;
	}

	public int getBasicBrickHeightRatio() {
		return basicBrickHeightRatio;
	}

	public Brick getBasicBrick() {
		return basicBrick;
	}
	
	public Collection<Brick> getAllBricks() {
		return bricks;
	}
	
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Does this brick set consist solely of a basic brick?
	 */
	public boolean isBasicBrickOnly() {
		return bricks.size() == 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((basicBrick == null) ? 0 : basicBrick.hashCode());
		result = prime * result + basicBrickHeightRatio;
		result = prime * result + basicBrickWidthRatio;
		result = prime * result + ((bricks == null) ? 0 : bricks.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (topDown ? 1231 : 1237);
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
		BrickSet other = (BrickSet) obj;
		if (basicBrick == null) {
			if (other.basicBrick != null)
				return false;
		} else if (!basicBrick.equals(other.basicBrick))
			return false;
		if (basicBrickHeightRatio != other.basicBrickHeightRatio)
			return false;
		if (basicBrickWidthRatio != other.basicBrickWidthRatio)
			return false;
		if (bricks == null) {
			if (other.bricks != null)
				return false;
		} else if (!bricks.equals(other.bricks))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (topDown != other.topDown)
			return false;
		return true;
	}

	
}
