package org.raiderrobotix.vision;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageDataCollector implements Runnable {
	// Can be run as separate Thread.

	public void run() {
		while (true) {
			BufferedImage image = null;
			try {
				Runtime.getRuntime().exec("raspistill -o " + VisionConstants.IMAGE_FILE.getAbsolutePath());
				image = ImageIO.read(VisionConstants.IMAGE_FILE); // Read image
				BufferedImageUtilities.setContrast(image, VisionConstants.CONTRAST_SCALE,
						VisionConstants.CONTRAST_OFFSET);
				new FRC2017ImageHandler(image).writeImageData();
				// Write Image data to roboRIO
			} catch (IOException e) {
				System.err.println("Could Not Take Image");
			}
		}
	}

	public static void main(String[] args) {
		new ImageDataCollector().run();
	}

}
