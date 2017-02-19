package org.raiderrobotix.frc2017;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;

public final class Drivebase {

	private static Drivebase m_instance;

	private final VictorSP m_leftDrives;
	private final VictorSP m_rightDrives;
	private final Servo m_rightBrake;
	private final Servo m_leftBrake;
	private final Encoder m_leftEncoder;
	private final Encoder m_rightEncoder;
	private final AHRS m_navX;
	private final AnalogInput m_sonic;

	private boolean m_brakesOn;
	private boolean m_drivingStep;
	private double m_headingYaw;

	private Drivebase() {
		m_navX = new AHRS(Port.kMXP);
		m_headingYaw = 0.0;

		m_leftDrives = new VictorSP(Constants.LEFT_DRIVES_PWM);
		m_rightDrives = new VictorSP(Constants.RIGHT_DRIVES_PWM);

		m_leftBrake = new Servo(Constants.LEFT_BRAKE_PWM);
		m_rightBrake = new Servo(Constants.RIGHT_BRAKE_PWM);

		m_leftEncoder = new Encoder(Constants.LEFT_ENCODER_PWM_A, Constants.LEFT_ENCODER_PWM_B,
				Constants.LEFT_ENCODER_INVERTED);
		m_rightEncoder = new Encoder(Constants.RIGHT_ENCODER_PWM_A, Constants.RIGHT_ENCODER_PWM_B,
				Constants.RIGHT_ENCODER_INVERTED);

		m_leftEncoder.setDistancePerPulse(Constants.INCHES_PER_COUNT);
		m_rightEncoder.setDistancePerPulse(Constants.INCHES_PER_COUNT);

		m_drivingStep = false;

		m_sonic = new AnalogInput(Constants.ULTRASONIC_PWM);
	}

	public static Drivebase getInstance() {
		if (m_instance == null) {
			m_instance = new Drivebase();
		}
		return m_instance;
	}

	public void setSpeed(double speed) {
		setSpeed(speed, speed);
	}

	public void setSpeed(double leftSpeed, double rightSpeed) {
		m_leftDrives.set(leftSpeed * (Constants.RIGHT_DRIVE_MOTORS_INVERTED ? -1.0 : 1.0));
		m_rightDrives.set(rightSpeed * (Constants.LEFT_DRIVE_MOTORS_INVERTED ? -1.0 : 1.0));
	}

	public void brakesOn() {
		m_brakesOn = true;
		m_leftBrake.set(Constants.LEFT_BRAKES_ON);
		m_rightBrake.set(Constants.RIGHT_BRAKES_ON);
	}

	public void brakesOff() {
		m_brakesOn = false;
		m_leftBrake.set(Constants.LEFT_BRAKES_OFF);
		m_rightBrake.set(Constants.RIGHT_BRAKES_OFF);
	}

	public boolean brakesAreOn() {
		return m_brakesOn;
	}

	public double getLeftEncoderDistance() {
		return m_leftEncoder.getDistance() * (Constants.LEFT_ENCODER_INVERTED ? -1.0 : 1.0);
	}

	public double getRightEncoderDistance() {
		return m_rightEncoder.getDistance() * (Constants.RIGHT_ENCODER_INVERTED ? -1.0 : 1.0);
	}

	public double getAverageEncoderDistance() {
		// return (getLeftEncoderDistance() + getRightEncoderDistance()) / 2.0;
		return getRightEncoderDistance();
	}

	/*
	 * public void resetEncoders() { m_leftEncoder.reset();
	 * m_rightEncoder.reset(); }
	 */

	/**
	 * Have the robot turn to a specific angle at a specified speed.
	 * 
	 * @param angle
	 *            - The angle (in degrees) you would like to turn the robot to.
	 * @param speed
	 *            - The speed at which you would like the robot to turn.
	 * @return True, when complete.
	 */
	public boolean turnToAngle(double angle, double speed) {
		if (!m_drivingStep) {
			brakesOff();
			resetSensors();
			m_drivingStep = true;
		} else {
			speed = Math.abs(speed) * (angle / Math.abs(angle));
			setSpeed(speed, -speed);
			if (Math.abs(getGyroAngle()) > Math.abs(angle) - Constants.TURN_ANGLE_TOLERANCE) {
				m_drivingStep = false;
				setSpeed(0.0);
			}
		}
		return (!m_drivingStep);
	}

	/**
	 * Have the robot drive to a specific distance at a specified speed.
	 * 
	 * @param distance
	 *            - The distance (in inches) you would like the robot to drive
	 *            to.
	 * @param speed
	 *            - The speed at which you would like the robot to drive.
	 * @return True, when complete.
	 */
	public boolean driveStraight(double distance, double speed) {
		if (!m_drivingStep) {
			brakesOff();
			resetSensors();
			m_drivingStep = true;
		} else {
			speed = Math.abs(speed) * (distance / Math.abs(distance));
			double leftSpeed = speed;
			double rightSpeed = speed;
			if (getAverageEncoderDistance() >= distance - Constants.DRIVE_STRAIGHT_SLOW_RANGE) {
				// If within slow range, set to slow speed
				setToSlowSpeed(speed > 0.0);
			} else if (getAverageEncoderDistance() > distance + Constants.DRIVE_STRAIGHT_DISTANCE_TOLERANCE) {
				setToSlowSpeed(speed < 0.0);
			} else {
				if (Math.abs(getGyroAngle()) > Constants.DRIVE_STRAIGHT_ANGLE_TOLERANCE) {
					// Adjust speeds for incorrect angles
					if (getGyroAngle() > 0) {
						// Too far clockwise
						if (distance > 0) {
							leftSpeed -= Constants.DRIVE_STRAIGHT_SPEED_SUBTRACTION;
						} else {
							rightSpeed += Constants.DRIVE_STRAIGHT_SPEED_SUBTRACTION;
						}
					} else {
						// Too far anti-clockwise
						if (distance > 0) {
							rightSpeed -= Constants.DRIVE_STRAIGHT_SPEED_SUBTRACTION;
						} else {
							leftSpeed += Constants.DRIVE_STRAIGHT_SPEED_SUBTRACTION;
						}
					}
				}
				setSpeed(leftSpeed, rightSpeed);
			}
			if (Math.abs(getAverageEncoderDistance()) > Math.abs(distance)
					- Constants.DRIVE_STRAIGHT_DISTANCE_TOLERANCE) {
				setSpeed(0.0);
				m_drivingStep = false;
			}
		}
		return (!m_drivingStep);
	}

	public void setToSlowSpeed(boolean forward) {
		if (forward) {
			setSpeed(Constants.SLOW_SPEED_STRONG, Constants.SLOW_SPEED_WEAK);
		} else {
			setSpeed(-Constants.SLOW_SPEED_WEAK, -Constants.SLOW_SPEED_STRONG);
		}
	}

	public double getGyroAngle() {
		return m_navX.getAngle() - m_headingYaw;
	}

	/*
	 * public void resetNavX() { m_headingYaw = m_navX.getAngle(); }
	 */

	public double getSonicDistance() {
		return m_sonic.getValue() * Constants.ULTRASONIC_CONSTANT;
	}

	public void resetSensors() {
		m_headingYaw = m_navX.getAngle();
		m_leftEncoder.reset();
		m_rightEncoder.reset();

	}
}
