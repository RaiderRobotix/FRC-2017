package org.raiderrobotix.frc2017;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public final class OI {

	private static OI m_instance;

	// ===== Robot Mechanisms =====
	private final Drivebase m_drives;
	private final GearCollector m_gearCollector;
	private final Climber m_climber;
	private final FuelHandler m_fuelHandler;

	// ===== Joysticks =====
	private final Joystick m_leftStick;
	private final Joystick m_rightStick;
	private final Joystick m_operatorStick;

	// ===== Automatic Constants =====
	private final Timer m_teleopTimer;
	private double m_lastCloseTime = -Constants.LINE_BREAKER_CLOSE_WAIT_TIME;
	private boolean m_autoIntakeOn = true;
	private boolean m_autoLineBreak = true;
	private double m_warmupFinishTime = 0.0;
	private double m_reverseFinishTime = 0.0;
	private boolean m_lastReverse = false;
	private boolean m_shooterIsRevving = false;

	public OI() {
		m_drives = Drivebase.getInstance();
		m_gearCollector = GearCollector.getInstance();
		m_climber = Climber.getInstance();
		m_fuelHandler = FuelHandler.getInstance();

		m_teleopTimer = new Timer();
		m_teleopTimer.start();
		m_teleopTimer.reset();

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
		// =========== RESETS ===========
		if (getRightButton(8)) {
			m_drives.resetSensors();
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

		// =========== GEAR COLLECTOR ===========
		if (getOperatorTrigger()) {
			m_gearCollector.openCollector();
		} else if (getOperatorButton(3)) {
			m_gearCollector.closeCollector();
		} else if (m_autoLineBreak) {
			if (getOperatorButton(10) || getOperatorButton(12)) {
				m_gearCollector.closeCollector();
			} else if (m_gearCollector.lineBroken()) {
				m_gearCollector.closeCollector();
				m_lastCloseTime = m_teleopTimer.get();
			} else if (m_teleopTimer.get() > m_lastCloseTime + Constants.LINE_BREAKER_CLOSE_WAIT_TIME) {
				m_gearCollector.openCollector();
			}
		}

		// =========== CLIMBER ===========
		if (getOperatorButton(10)) {
			m_climber.runForward();
		} else if (getOperatorButton(12)) {
			m_climber.runBackwards();
		} else {
			m_climber.stopMotor();
		}

		// =========== FUEL HANDLER ===========
		boolean shootHigh = getOperatorButton(6); // Shooter: Joysticks
		boolean shootLow = getOperatorButton(4);
		boolean reverseShooter = getOperatorButton(5);

		if (getRightButton(2) || shootHigh || shootLow) { // Intake
			m_fuelHandler.intakeFuel();
		} else if (getOperatorButton(7)) {
			m_fuelHandler.stopIntake();
		} else if (getOperatorButton(8)) {
			m_fuelHandler.reverseIntake();
		} else if (m_autoIntakeOn && (getRightY() > 0.0 || getLeftY() > 0.0)) {
			m_fuelHandler.intakeFuel();
		} else {
			m_fuelHandler.stopIntake();
		}

		if (shootHigh) { // Shooter: Control
			m_fuelHandler.setShooterSpeed(Constants.SHOOTER_HIGH_SPEED);
		} else if (shootLow) {
			m_fuelHandler.setShooterSpeed(Constants.SHOOTER_LOW_SPEED);
		} else if (reverseShooter && m_teleopTimer.get() >= m_reverseFinishTime) {
			m_fuelHandler.setShooterSpeed(Constants.SHOOTER_REVERSE_SPEED);
		} else {
			m_shooterIsRevving = false;
			m_fuelHandler.setShooterSpeed(0.0);
		}

		if (!m_shooterIsRevving) { // Flapper: Warm-Up Speeds
			if (shootHigh) {
				m_warmupFinishTime = m_teleopTimer.get() + Constants.SHOOTER_WARMUP_TIME_HIGH;
				m_shooterIsRevving = true;
			} else if (shootLow) {
				m_warmupFinishTime = m_teleopTimer.get() + Constants.SHOOTER_WARMUP_TIME_LOW;
				m_shooterIsRevving = true;
			} else if (reverseShooter) {
				m_warmupFinishTime = m_teleopTimer.get();
				m_shooterIsRevving = true;
			}
		}

		if ((!m_lastReverse) && reverseShooter) { // Reverse Shooter- Wait Time
			m_reverseFinishTime = m_teleopTimer.get() + Constants.SHOOTER_REVERSE_WAIT_TIME;
		}

		if (reverseShooter) { // Flapper- Control
			m_fuelHandler.openFlapper();
		} else if ((m_shooterIsRevving && m_teleopTimer.get() >= m_warmupFinishTime && getRightTrigger())
				|| getRightButton(5)) {
			m_fuelHandler.openFlapper();
		} else {
			m_fuelHandler.closeFlapper();
		}

		m_lastReverse = reverseShooter;
	}

	public double getLeftY() {
		double yval = -m_leftStick.getY();
		if (Math.abs(yval) < Constants.JOYSTICK_DEADBAND) {
			yval = 0.0;
		}
		return yval;
	}

	public double getRightY() {
		double yval = -m_rightStick.getY();
		if (Math.abs(yval) < Constants.JOYSTICK_DEADBAND) {
			yval = 0.0;
		}
		return yval;
	}

	public boolean getOperatorTrigger() {
		return m_operatorStick.getTrigger();
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

	public void setAutoIntakeOn(boolean on) {
		m_autoIntakeOn = on;
	}

	public void setAutoLineBreakOn(boolean on) {
		m_autoLineBreak = on;
	}
}
