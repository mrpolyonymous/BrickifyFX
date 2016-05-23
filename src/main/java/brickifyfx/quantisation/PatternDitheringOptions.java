package brickifyfx.quantisation;

public class PatternDitheringOptions {

	private int luminanceLimit;

	public PatternDitheringOptions(int luminanceLimit) {
		super();
		this.luminanceLimit = luminanceLimit;
	}

	public PatternDitheringOptions() {
	}

	public int getLuminanceLimit() {
		return luminanceLimit;
	}

	public void setLuminanceLimit(int luminanceLimit) {
		this.luminanceLimit = luminanceLimit;
	}

}
