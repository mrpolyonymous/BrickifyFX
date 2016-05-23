package brickifyfx.quantisation;

import java.util.ArrayList;
import java.util.List;

public class SlicingOptions {

	private List<SlicingColorThreshold> thresholds = new ArrayList<>();

	public SlicingOptions() {

	}

	public void addThreshold(SlicingColorThreshold threshold) {
		thresholds.add(threshold);
	}

	public List<SlicingColorThreshold> getThresholds() {
		return thresholds;
	}
}
