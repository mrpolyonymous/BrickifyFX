package brickifyfx.core;

import java.util.Random;

public class BasicRandomFactory implements RandomFactory {

	@Override
	public Random getRandom() {
		return new Random();
	}
}