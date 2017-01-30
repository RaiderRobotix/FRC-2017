package org.raiderrobotix.vision;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.hopding.jrpicam.RaspiCam;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;

public class ImageDataCollector implements Runnable {
	// Can be run as separate Thread.

	private RaspiCam m_camera;

	public ImageDataCollector() {
		try {
			m_camera = new RaspiCam(VisionConstants.DEFAULT_PIC_DIRECTORY);
		} catch (FailedToRunRaspistillException e) {
			System.err.println("Could Not Open Camera");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void run() {
		int count = 0;
		while (true) {
			System.out.printf("Starting Trial: #%d\n", ++count);
			BufferedImage image = null;
			try {
				image = m_camera.takeBufferedStill();
				System.out.println("got here");
				BufferedImageUtilities.setContrast(image, VisionConstants.CONTRAST_SCALE,
						VisionConstants.CONTRAST_OFFSET);
				new FRC2017ImageHandler(image).writeImageData();
				// Write Image data to roboRIO
			} catch (InterruptedException | IOException e) {
				System.err.println("Could Not Take Image");
			}
		}
	}

	public static void main(String[] args) {
		new ImageDataCollector().run();
	}

}
