package org.raiderrobotix.frc2017;

import edu.wpi.first.wpilibj.Solenoid;

public class GearCollector {

	private static GearCollector m_instance;

	private final Solenoid m_leftCylinder;
	private final Solenoid m_rightCylinder;
	private boolean m_isOut;

	private GearCollector() {
		m_leftCylinder = new Solenoid(Constants.LEFT_CYLINDER_SOLENOID_CHANNEL);
		m_rightCylinder = new Solenoid(Constants.RIGHT_CYLINDER_SOLENOID_CHANNEL);
		m_isOut = false;
	}
	
	public static GearCollector getInstance() {
		if(m_instance == null) {
			m_instance = new GearCollector();
		}
		return m_instance;
	}
	
	public boolean isOut() {
		return m_isOut;
	}

	public void openCollector() {
		m_isOut = true;
		m_leftCylinder.set(true);
		m_rightCylinder.set(true);
	}
	
	public void closeCollector() {
		m_isOut = false;
		m_leftCylinder.set(false);
		m_rightCylinder.set(false);
	}
	
}
