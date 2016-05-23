package brickifyfx;

abstract class CropRatio {
	private String label;

	public CropRatio(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public boolean hasRatio() {
		return true;
	}

	@Override
	public String toString() {
		return label;
	}

	public abstract double convertWidthToHeight(double width);
	public abstract double convertHeightToWidth(double height);
	
}