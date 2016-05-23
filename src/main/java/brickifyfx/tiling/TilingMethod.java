package brickifyfx.tiling;


public enum TilingMethod {
	BASIC_BRICKS_ONLY("Basic Bricks", BasicBricksOnly.class),
	BRICK_SIZE_OPTIMISATION("Use biggest bricks", BrickSizeOptimisation.class),
	COSTS_OPTIMISATION("Cost optimization", null),
	STABILITY_OPTIMISATION("Stability optimization", null);

	private String name;
	private Class<? extends Tiling> implementationClass;

	private TilingMethod(String name, Class<? extends Tiling> implementationClass) {
		this.name = name;
		this.implementationClass = implementationClass;
	}

	public Tiling getTiling() {
		Tiling tiling;
		try {
			tiling = implementationClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return tiling;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
