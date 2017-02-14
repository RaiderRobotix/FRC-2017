package org.raiderrobotix.frc2017;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

public final class GearCollector {

	private static GearCollector m_instance;

	private final Solenoid m_cylinders;
	private final DigitalInput m_lineBreaker;
	private boolean m_isOut;

	private GearCollector() {
		m_cylinders = new Solenoid(Constants.PCM_CAN_ADDRESS, Constants.GEAR_CYLINDERS_SOLENOID_CHANNEL);
		m_lineBreaker = new DigitalInput(Constants.LINE_BREAKER_PWM);
		m_isOut = false;
	}

	public static GearCollector getInstance() {
		if (m_instance == null) {
			m_instance = new GearCollector();
		}
		return m_instance;
	}

	public boolean isOut() {
		return m_isOut;
	}

	public void openCollector() {
		m_isOut = true;
		m_cylinders.set(true);
	}

	public void closeCollector() {
		m_isOut = false;
		m_cylinders.set(false);
	}

	public boolean lineBroken() {
		return !m_lineBreaker.get();
	}
	
}
