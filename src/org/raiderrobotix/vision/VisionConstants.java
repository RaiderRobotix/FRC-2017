package org.raiderrobotix.vision;

import java.awt.Color;
import java.io.File;

import org.raiderrobotix.frc2017.Constants;

public abstract class VisionConstants extends Constants {

	static final int CONTRAST_OFFSET = -450; // TODO: fix all of these
	static final float CONTRAST_SCALE = 3.25f;
	static final Color TAPE_LOWER_THRESHOLD_COLOR = new Color(230, 245, 230);
	static final File IMAGE_FILE = new File("/home/pi/Vision/img.jpg");
	static final float DISTORTION_THRESHOLD = 0.085f;
	static final float TAPE_SIZE_LIMIT = 0.1f;
	static final float ALLOWABLE_CENTER_RANGE_X = 0.085f;
	static final float ALLOWABLE_CENTER_RANGE_Y = 0.085f;
	static final float CENTER_X_POINT = 0.5f;
	static final float CENTER_Y_POINT = 0.5f;

	public static final String CENTER_X = "centerX";
	public static final String CENTER_Y = "centerY";
	public static final String DISTORTION = "distortion";

}
