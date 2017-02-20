package org.raiderrobotix.frc2017;

import com.ctre.CANTalon;

public final class Climber {

	private static Climber m_instance;

	private final CANTalon m_climbMotor;

	private Climber() {
		m_climbMotor = new CANTalon(Constants.CLIMBER_CAN_ADDRESS);
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

	public void runForward() {
		setSpeed(Constants.CLIMB_SPEED);
	}

	public void runBackwards() {
		setSpeed(Constants.CLIMB_SPEED);
	}

	public void stopMotor() {
		setSpeed(0.0);
	}

}
