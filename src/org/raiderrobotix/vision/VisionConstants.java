package org.raiderrobotix.vision;

import java.awt.Color;

import org.raiderrobotix.frc2017.Constants;

public abstract class VisionConstants extends Constants {

	static final int CONTRAST_OFFSET = -450;
	static final float CONTRAST_SCALE = 3.25f;
	static final float TAPE_DISTANCE_LOWER_LIMIT = 0.55f;
	static final float TAPE_DISTANCE_UPPER_LIMIT = 0.6f;
	static final float CENTER_X_MAXIMUM_DEVIATION = 1.5f;
	static final float CENTER_Y_MAXIMUM_DEVIATION = 1.5f;
	static final float DISTORTION_DEADBAND = 0.0085f;
	static final Color TAPE_LOWER_THRESHOLD_COLOR = new Color(230, 245, 230);

	public static final String LINE_SPREAD = "lineSpread";
	public static final String CENTER_X = "centerX";
	public static final String DISTORTION = "distortion";

}
