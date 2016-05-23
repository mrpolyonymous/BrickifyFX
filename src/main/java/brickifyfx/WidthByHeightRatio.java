package brickifyfx;

class WidthByHeightRatio extends CropRatio {
	private double widthToHeightMultiplier;
	private double heightToWidthToMultiplier;

	public WidthByHeightRatio(String label, double width, double height) {
		super(label);
		widthToHeightMultiplier = height / width;
		heightToWidthToMultiplier = 1.0 / widthToHeightMultiplier;
	}

	private WidthByHeightRatio(String label) {
		super(label);
	}

	@Override
	public double convertWidthToHeight(double width) {
		return width * widthToHeightMultiplier;
	}

	@Override
	public double convertHeightToWidth(double height) {
		return height * heightToWidthToMultiplier;
	}

}