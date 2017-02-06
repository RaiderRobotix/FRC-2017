package org.raiderrobotix.frc2017;

public abstract class Constants { // TODO: get ALL values

	// FTP Information
	public static final String AUTON_FILE_PATH = "/home/lvuser/auton.dat";
	public static final String FTP_PREFIX = "ftp://roboRIO-25-frc.local";

	// Extra Mechanism Speed Constants TODO: get
	public static final double FUEL_INTAKE_SPEED = 1.0;
	public static final double CLIMB_SPEED = 1.0;
	public static final double SHOOTER_HIGH_SPEED = 1.0; // TODO: get
	public static final double SHOOTER_LOW_SPEED = 0.5; // TODO: get

	// Auto-Driving Constants
	public static final double TURN_ANGLE_TOLERANCE = 1.0; // (In Degrees)
	public static final double DRIVE_STRAIGHT_ANGLE_TOLERANCE = 1.0;
	public static final double DRIVE_STRAIGHT_SPEED_SUBTRACTION = 0.15; // TODO:
																		// get
	public static final double DRIVE_STRAIGHT_DISTANCE_TOLERANCE = 1.0; // TODO:
																		// get
	public static final double SLOW_SPEED_WEAK = 0.12;
	public static final double SLOW_SPEED_LARGE = 0.18;
	public static final double DRIVE_STRAIGHT_SLOW_RANGE = 18.0;

	// PWMs (Control) TODO: get
	public static final int LEFT_DRIVES_PWM = 8;
	public static final int RIGHT_DRIVES_PWM = 0;
	public static final int LEFT_BRAKE_PWM = 9;
	public static final int RIGHT_BRAKE_PWM = 1;

	// Brake Positions
	public static final double LEFT_BRAKES_ON = 0.42;
	public static final double LEFT_BRAKES_OFF = 0.57;
	public static final double RIGHT_BRAKES_ON = 0.55;
	public static final double RIGHT_BRAKES_OFF = 0.34;

	// Digital Sensors
	public static final int LEFT_ENCODER_PWM_A = 9; // TODO: Get Values
	public static final int LEFT_ENCODER_PWM_B = 4;
	public static final int RIGHT_ENCODER_PWM_A = 6;
	public static final int RIGHT_ENCODER_PWM_B = 7;

	// Auton Information
	private static final double TIRE_DIAMETER = 8.938; // (In Inches)
	private static final double TIRE_CIRCUMFERENCE = TIRE_DIAMETER * Math.PI;
	private static final double COUNTS_PER_REVOLUTION = 85.75; // TODO: Fix
	private static final double GEAR_RATIO = 0.068182; // (Driver: Encoder Gear,
														// Driven: Wheel Gear)
	private static final double INCHES_PER_REVOLUTION = GEAR_RATIO * TIRE_CIRCUMFERENCE;
	public static final double INCHES_PER_COUNT = INCHES_PER_REVOLUTION / COUNTS_PER_REVOLUTION;

	// Camera Constants
	public static final double VISION_TURN_SPEED = 0.2; // TODO: adjust values
	public static final double VISION_DRIVE_SPEED = 0.4;
	public static final double VISION_DRIVE_FIX_LENGTH = 6.0;
	public static final double VISION_TURN_FIX_ANGLE = 7.5;
	public static final double VISION_FORWARD_ADJUSTMENT_SPEED = 0.35; // TODO:
																		// get
	public static final String CAMERA_NETWORK_TABLE = "IMPLEMENT"; // TODO:
																	// implement

	// Joysticks
	public static final int LEFT_JOYSTICK_PORT = 0;
	public static final int RIGHT_JOYSTICK_PORT = 1;
	public static final int OPERATOR_JOYSTICK_PORT = 2;
	public static final int SWITCH_BOX_PORT = 3;
	public static final double JOYSTICK_DEADBAND = 0.2;
	public static final int OPERATOR_OVERRIDE_BTN = 7;

	// CAN Addresses TODO: Get Values
	public static final int INTAKE_CAN_ADDRESS = 0; // TODO: get
	public static final int SHOOTER_CAN_ADDRESS = 1; // TODO: get
	public static final int CLIMBER_CAN_ADDRESS = 2; // TODO: get

	// Inversions TODO: get
	public static final boolean RIGHT_DRIVE_MOTORS_INVERTED = false;
	public static final boolean LEFT_DRIVE_MOTORS_INVERTED = true;
	public static final boolean LEFT_ENCODER_INVERTED = true;
	public static final boolean RIGHT_ENCODER_INVERTED = false;
	public static final boolean INTAKE_MOTOR_INVERTED = true;
	public static final boolean SHOOTER_MOTOR_INVERTED = false;
	public static final boolean CLIMB_MOTOR_INVERTED = false;

	// Pneumatic Constants TODO: get
	public static final int GEAR_CYLINDERS_SOLENOID_CHANNEL = 0;

}
