package org.raiderrobotix.frc2017;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TalonSRX;

public final class Climber {

	private static Climber m_instance;

	private final TalonSRX m_climbMotor;
	private final Solenoid m_gripper;

	private Climber() {
		m_climbMotor = new TalonSRX(Constants.CLIMBER_CAN_ADDRESS);
		m_gripper = new Solenoid(Constants.GRIPPER_SOLENOID_CHANNEL);
	}

	public static Climber getInstance() {
		if (m_instance == null) {
			m_instance = new Climber();
		}
		return m_instance;
	}

	private void setSpeed(double speed) {
		m_climbMotor.set(speed * (Constants.CLIMB_MOTOR_INVERTED ? -1.0 : 1.0));
	}

	public void startMotor(boolean override) {
		setSpeed(Constants.CLIMB_SPEED * (override ? -1.0 : 1.0));
	}

	public void stopMotor() {
		setSpeed(0.0);
	}

	public void closeGripper() {
		m_gripper.set(true);
	}

	public void openGripper() {
		m_gripper.set(false);
	}

}
