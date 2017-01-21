package org.raiderrobotix.frc2017;

import edu.wpi.first.wpilibj.Joystick;

public final class OI {

	private static OI m_instance;

	// ===== Robot Mechanisms =====
	private final Drivebase m_drives;

	// ===== Joysticks =====
	private final Joystick m_leftStick;
	private final Joystick m_rightStick;
	private final Joystick m_operatorStick;

	public OI() {
		m_drives = Drivebase.getInstance();

		m_leftStick = new Joystick(Constants.LEFT_JOYSTICK_PORT);
		m_rightStick = new Joystick(Constants.RIGHT_JOYSTICK_PORT);
		m_operatorStick = new Joystick(Constants.OPERATOR_JOYSTICK_PORT);
	}

	public static OI getInstance() {
		if (m_instance == null) {
			m_instance = new OI();
		}
		return m_instance;
	}

	public void enableTeleopControls() {
		// =========== RESET ===========
		if (getOperatorTrigger()) { // TODO: change
			m_drives.resetEncoders();
			m_drives.resetNavX();
		}

		// =========== DRIVES ===========
		if (getLeftTrigger()) {
			m_drives.brakesOn();
		} else if (getLeftButton(2)) {
			m_drives.brakesOff();
		}

		if (!m_drives.brakesAreOn()) {
			m_drives.setSpeed(getLeftY(), getRightY());
		} else {
			m_drives.setSpeed(0.0);
		}
	}

	public double getLeftY() {
		double yval = m_leftStick.getY();
		if (Math.abs(yval) < Constants.JOYSTICK_DEADBAND) {
			yval = 0.0;
		}
		return yval;
	}

	public double getRightY() {
		double yval = m_rightStick.getY();
		if (Math.abs(yval) < Constants.JOYSTICK_DEADBAND) {
			yval = 0.0;
		}
		return yval;
	}

	public boolean getOperatorTrigger() {
		return m_operatorStick.getTrigger();
	}

	public boolean getRightTrigger() {
		return m_rightStick.getTrigger();
	}

	public boolean getLeftTrigger() {
		return m_leftStick.getTrigger();
	}

	public boolean getRightButton(int btn) {
		return m_rightStick.getRawButton(btn);
	}

	public boolean getLeftButton(int btn) {
		return m_leftStick.getRawButton(btn);
	}
}
