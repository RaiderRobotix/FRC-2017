package org.raiderrobotix.vision;

import java.io.File;
import java.lang.reflect.Field;

public abstract class VisionConstants {

	protected static final float CONTRAST = 750.0f;
	protected static final float TAPE_DISTANCE_LOWER_LIMIT = 0.55f;
	protected static final float TAPE_DISTANCE_UPPER_LIMIT = 0.6f;
	protected static final float CENTER_X_MAXIMUM_DEVIATION = 1.5f;
	protected static final float CENTER_Y_MAXIMUM_DEVIATION = 1.5f;
	protected static final float DISTORTION_DEADBAND = 0.0085f;
	protected static final int TAPE_LOWER_THRESHOLD = 240;
	protected static final String PICTURE_COMMAND = "python /home/pi/Vision/take_picture.py";
	protected static final File PICTURE_FILE = new File("/home/pi/Vision/res/imgs/captured_picture.jpg");
	protected static final String I2C_WRITE_COMMAND_INIT = "python /home/pi/Vision/write_to_i2c.py ";

	public static final int LINE_SPREAD = 0;
	public static final int CENTER_X = 1;
	public static final int DISTORTION = 2;
	public static final int ARRAY_INITIAL = 8;
	public static final int ARRAY_SIZE;
	
	static {
		int count = 0;
		for(Field i : VisionConstants.class.getFields()) {
			if(!(i.toString().contains("ARRAY"))) {
				count++;
			}
		}
		ARRAY_SIZE = count;
	}
	
}
