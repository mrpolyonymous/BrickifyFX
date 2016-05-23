package brickifyfx;

class NoCrop extends CropRatio {

	public NoCrop(String label) {
		super(label);
	}

	@Override
	public double convertWidthToHeight(double width) {
		return width;
	}
	@Override
	public double convertHeightToWidth(double height) {
		return height;
	}

	@Override
	public boolean hasRatio() {
		return false;
	}

}