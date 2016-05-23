package brickifyfx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import brickifyfx.core.BricksAndColors;
import brickifyfx.core.ColoredBrick;
import brickifyfx.core.Mosaic;

public class LdrawOutput {

	private PrintWriter printWriter;

	public void saveLdraw(File outputFile, BricksAndColors bricksAndColors, Mosaic mosaic) throws IOException {
		// TODO - only works for 1x1 top-down 
		if (!bricksAndColors.getBricks().isTopDown() || !bricksAndColors.getBricks().isBasicBrickOnly()) {
			System.out.println("This export is not going to work correctly");
		}
		
		printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
		
		writeLdrawLine("0 // Created by brickifyfx");
		writeLdrawLine("0 // output of a " + mosaic.getMosaicWidth() + " by " + mosaic.getMosaicHeight() + " mosaic");
		double xCoord = 0.0;
		double yCoord = 0.0;
		double zCoord = 10.0 + 20.0 * mosaic.getMosaicHeight();
		for (int row = 0; row < mosaic.getMosaicHeight(); ++row) {
			xCoord = 10;
			for (int column = 0; column < mosaic.getMosaicWidth(); ++column) {
				ColoredBrick coloredBrick = mosaic.getMosaic()[row][column];
				if (coloredBrick.isComplete()) {
					writeLdrawLine("1 " + coloredBrick.getColor().getLdrawColorNumber() + " " + xCoord + " " + yCoord + " " + zCoord
							+ " 1 0 0 0 1 0 0 0 1 " + coloredBrick.getBrick().getLdrawFile());
				}
				xCoord += 20.0;
			}
			zCoord -= 20.0;
		}
		
		printWriter.close();
		printWriter = null;
	}

	private void writeLdrawLine(String line) {
		System.out.println(line);
		if (printWriter != null) {
			printWriter.println(line);
		}
	}
}
