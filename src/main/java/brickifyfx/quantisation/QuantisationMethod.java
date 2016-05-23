package brickifyfx.quantisation;

public enum QuantisationMethod {
	VECTOR_ERROR_DIFFUSION("Dithering", VectorErrorDiffusion.class),
	NAIVE_QUANTISATION_LAB("Closest color (perceptual)", NaiveQuantisationLab.class),
	NAIVE_QUANTISATION_RGB("Closest color (RGB)", NaiveQuantisationRgb.class),
	SOLID_REGIONS("Solid Regions", SolidRegions.class),
	FLOYD_STEINBERG("Two color dithering", null),
	PATTERN_DITHERING("Pattern Dithering", PatternDithering.class),
	SLICING("Slicing", null),
	RESIZE_ONLY("Resize only (ignores color palette)", ResizeOnly.class)
	;

	private String name;
	private Class<? extends Quantisation> implementationClass;

	private QuantisationMethod(String name, Class<? extends Quantisation> implementationClass) {
		this.name = name;
		this.implementationClass = implementationClass;
	}

	public Quantisation getQuantisation() {
		Quantisation quantisation;
		try {
			quantisation = implementationClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return quantisation;
	}

	@Override
	public String toString() {
		return name;
	}
}
