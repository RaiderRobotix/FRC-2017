package org.raiderrobotix.frc2017;

import com.ctre.CANTalon;

public final class FuelHandler {

	private static FuelHandler m_instance;

	private final CANTalon m_intake;
	private final CANTalon m_shooter;

	private FuelHandler() {
		m_intake = new CANTalon(Constants.INTAKE_CAN_ADDRESS);
		m_shooter = new CANTalon(Constants.SHOOTER_CAN_ADDRESS);
	}

	public static FuelHandler getInstance() {
		if (m_instance == null) {
			m_instance = new FuelHandler();
		}
		return m_instance;
	}

	public void setIntakeSpeed(double speed) {
		m_intake.set(speed * (Constants.INTAKE_MOTOR_INVERTED ? -1.0 : 1.0));
	}

	public void setShooterSpeed(double speed) {
		m_shooter.set(speed * (Constants.SHOOTER_MOTOR_INVERTED ? -1.0 : 1.0));
	}

	public void intakeFuel() {
		setIntakeSpeed(Constants.FUEL_INTAKE_SPEED);
	}

	public void reverseIntake() {
		setIntakeSpeed(-Constants.FUEL_INTAKE_SPEED);
	}

	public void stopIntake() {
		m_intake.set(0.0);
	}

}
