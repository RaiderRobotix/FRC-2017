package org.raiderrobotix.frc2017;

import edu.wpi.first.wpilibj.Joystick;

public final class OI {

	private static OI m_instance;

	private boolean m_lastOperatorTriggerValue;

	// ===== Robot Mechanisms =====
	private final Drivebase m_drives;
	private final GearCollector m_collector;

	// ===== Joysticks =====
	private final Joystick m_leftStick;
	private final Joystick m_rightStick;
	private final Joystick m_operatorStick;

	public OI() {
		m_drives = Drivebase.getInstance();
		m_collector = GearCollector.getInstance();

		m_leftStick = new Joystick(Constants.LEFT_JOYSTICK_PORT);
		m_rightStick = new Joystick(Constants.RIGHT_JOYSTICK_PORT);
		m_operatorStick = new Joystick(Constants.OPERATOR_JOYSTICK_PORT);

		m_lastOperatorTriggerValue = false;
	}

	public static OI getInstance() {
		if (m_instance == null) {
			m_instance = new OI();
		}
		return m_instance;
	}

	public void enableTeleopControls() {
		// =========== RESETS ===========
		if (getOperatorButton(2)) { // TODO: change
			m_drives.resetEncoders();
			m_drives.resetNavX();
		}
		m_lastOperatorTriggerValue = getOperatorTrigger();

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

		// =========== GEAR COLLECTOR ===========
		if (operatorTriggerIsFalling()) {
			if (m_collector.isOut()) {
				m_collector.closeCollector();
			} else {
				m_collector.openCollector();
			}
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

	public boolean operatorTriggerIsFalling() {
		return m_operatorStick.getTrigger() && (!m_lastOperatorTriggerValue);
	}

	public boolean getOperatorButton(int btn) {
		return m_operatorStick.getRawButton(btn);
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
