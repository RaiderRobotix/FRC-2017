package org.raiderrobotix.frc2017;

import edu.wpi.first.wpilibj.TalonSRX;

public final class FuelHandler {

	private static FuelHandler m_instance;

	private final TalonSRX m_intake;
	private final TalonSRX m_shooter;

	private FuelHandler() {
		m_intake = new TalonSRX(Constants.INTAKE_CAN_ADDRESS);
		m_shooter = new TalonSRX(Constants.SHOOTER_CAN_ADDRESS);
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

	public void intakeFuel(boolean override) {
		m_intake.set(Constants.FUEL_INTAKE_SPEED * (override ? -1.0 : 1.0));
	}

	public void stopIntake() {
		m_intake.setSpeed(0.0);
	}

}
