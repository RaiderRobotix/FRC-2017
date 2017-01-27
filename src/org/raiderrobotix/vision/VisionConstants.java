package org.raiderrobotix.vision;

import java.io.File;

import org.raiderrobotix.frc2017.Constants;

public abstract class VisionConstants extends Constants {

	static final float CONTRAST = 750.0f;
	static final float TAPE_DISTANCE_LOWER_LIMIT = 0.55f;
	static final float TAPE_DISTANCE_UPPER_LIMIT = 0.6f;
	static final float CENTER_X_MAXIMUM_DEVIATION = 1.5f;
	static final float CENTER_Y_MAXIMUM_DEVIATION = 1.5f;
	static final float DISTORTION_DEADBAND = 0.0085f;
	static final int TAPE_LOWER_THRESHOLD = 240;
	static final String PICTURE_COMMAND = "python /home/pi/Vision/take_picture.py";
	static final File PICTURE_FILE = new File("/home/pi/Vision/res/imgs/captured_picture.jpg");

	public static final String LINE_SPREAD = "lineSpread";
	public static final String CENTER_X = "centerX";
	public static final String DISTORTION = "distortion";

}
