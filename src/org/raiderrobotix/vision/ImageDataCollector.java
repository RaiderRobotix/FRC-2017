package org.raiderrobotix.vision;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageDataCollector implements Runnable {

	public void run() {
		while (true) {
			BufferedImage image;
			try {
				image = takePicture();
				ImageUtilities.setContrast(image, VisionConstants.CONTRAST);
			} catch (IOException e) {
				System.err.println("Could Not Find Captured Image");
				image = null;
			}
			new ImageHandler(image).writeImageData(); // Write Image data to
														// roboRIO
		}
	}

	public BufferedImage takePicture() throws IOException {
		LinuxExecuter.execute(VisionConstants.PICTURE_COMMAND);
		return ImageIO.read(VisionConstants.PICTURE_FILE);
	}

	public static void main(String[] args) {
		new ImageDataCollector().run();
	}

}
