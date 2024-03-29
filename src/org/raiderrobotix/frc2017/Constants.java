package org.raiderrobotix.frc2017;

import java.io.File;

public abstract class Constants {

	// FTP Information
	public static final String FTP_PREFIX = "ftp://roboRIO-25-frc.local";
	public static final String AUTON_DATA_RIO_DIRECTORY = "/home/lvuser/autons/";
	public static final File AUTON_DATA_LOCAL_DIRECTORY = new File(
			"C:/Users/raiderrobotix/Desktop/Workspace/FRC-2017/autons/");

	// Extra Mechanism Speed Constants
	public static final double FUEL_INTAKE_SPEED = 1.0;
	public static final double CLIMB_SPEED = 1.0;
	public static final double SHOOTER_HIGH_SPEED = 1.0;
	public static final double SHOOTER_LOW_SPEED = 0.55;
	public static final double SHOOTER_REVERSE_SPEED = -0.475;

	// Auto-Driving Constants
	public static final double TURN_ANGLE_TOLERANCE = 1.0; // (In Degrees)
	public static final double DRIVE_STRAIGHT_ANGLE_TOLERANCE = 1.0;
	public static final double DRIVE_STRAIGHT_SPEED_SUBTRACTION = 0.15;
	public static final double DRIVE_STRAIGHT_DISTANCE_TOLERANCE = 1.0;
	public static final double SLOW_SPEED_WEAK = 0.12;
	public static final double SLOW_SPEED_STRONG = 0.18;
	public static final double DRIVE_STRAIGHT_SLOW_RANGE = 24.0;

	// PWMs (Control)
	public static final int LEFT_DRIVES_PWM = 3;
	public static final int RIGHT_DRIVES_PWM = 4;
	public static final int LEFT_BRAKE_PWM = 0;
	public static final int RIGHT_BRAKE_PWM = 1;

	// Brake Positions
	public static final double LEFT_BRAKES_ON = 0.63;
	public static final double LEFT_BRAKES_OFF = 0.41;
	public static final double RIGHT_BRAKES_ON = 0.18;
	public static final double RIGHT_BRAKES_OFF = 0.46;

	// Digital Sensors
	public static final int LEFT_ENCODER_PWM_A = 0;
	public static final int LEFT_ENCODER_PWM_B = 1;
	public static final int RIGHT_ENCODER_PWM_A = 3;
	public static final int RIGHT_ENCODER_PWM_B = 4;
	public static final int LINE_BREAKER_PWM = 6;

	// Auton Information
	private static final double TIRE_CIRCUMFERENCE = 29.898; // (In Inches)
	private static final double COUNTS_PER_REVOLUTION = 128;
	private static final double GEAR_RATIO = 0.068182; // (Driver: Encoder Gear,
														// Driven: Wheel Gear)
	private static final double INCHES_PER_REVOLUTION = GEAR_RATIO * TIRE_CIRCUMFERENCE;
	public static final double INCHES_PER_COUNT = INCHES_PER_REVOLUTION / COUNTS_PER_REVOLUTION;

	// Joysticks
	public static final int LEFT_JOYSTICK_PORT = 0;
	public static final int RIGHT_JOYSTICK_PORT = 1;
	public static final int OPERATOR_JOYSTICK_PORT = 2;
	public static final int SWITCH_BOX_PORT = 3;
	public static final double JOYSTICK_DEADBAND = 0.2;

	// CAN Addresses
	public static final int INTAKE_CAN_ADDRESS = 14;
	public static final int SHOOTER_CAN_ADDRESS = 13;
	public static final int CLIMBER_CAN_ADDRESS = 11;
	public static final int PCM_CAN_ADDRESS = 12;
	public static final int PDP_CAN_ADDRESS = 15;

	// Inversions
	public static final boolean RIGHT_DRIVE_MOTORS_INVERTED = false;
	public static final boolean LEFT_DRIVE_MOTORS_INVERTED = true;
	public static final boolean LEFT_ENCODER_INVERTED = true;
	public static final boolean RIGHT_ENCODER_INVERTED = false;
	public static final boolean INTAKE_MOTOR_INVERTED = false;
	public static final boolean SHOOTER_MOTOR_INVERTED = false;
	public static final boolean CLIMB_MOTOR_INVERTED = true;

	// Pneumatics Constants
	public static final int GEAR_CYLINDERS_SOLENOID_CHANNEL = 4;
	public static final int FLAPPER_SOLENOID_CHANNEL = 7;
	public static final double SHOOTER_WARMUP_TIME_HIGH = 0.4;
	public static final double SHOOTER_WARMUP_TIME_LOW = 0.3;
	public static final double SHOOTER_REVERSE_WAIT_TIME = 0.5;

	// Assorted Constants
	public static final double LINE_BREAKER_CLOSE_WAIT_TIME = 1.5;
	public static final int ULTRASONIC_PWM = 0;
	public static final double ULTRASONIC_CONSTANT = 1.0;

}
