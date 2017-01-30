package org.raiderrobotix.frc2017;

import org.raiderrobotix.vision.NetworkImageProperties;
import org.raiderrobotix.vision.Size;
import org.raiderrobotix.vision.VisionConstants;

public final class CameraSetup {

	private static CameraSetup m_instance;

	private final NetworkImageProperties m_props;
	private final Drivebase m_drives;
	private int m_step = 0;

	private CameraSetup() {
		m_props = NetworkImageProperties.getInstance();
		m_drives = Drivebase.getInstance();
	}

	public static CameraSetup getInstance() {
		if (m_instance == null) {
			m_instance = new CameraSetup();
		}
		return m_instance;
	}

	public boolean linedUpToScore() {
		m_props.updateProperties();
		if (m_props.containsError()) {
			// If can't connect to camera, don't use it.
			m_drives.setSpeed(0.0);
			m_step = 0;
			return true;
		}
		if (m_step == 0) {
			m_drives.brakesOff();
			m_drives.setSpeed(0.0);
			m_step++;
		} else if (m_step == 1) {
			if (Size.IN_RANGE.equals(m_props.getSizeProperty(VisionConstants.DISTORTION))) {
				m_drives.setSpeed(0.0);
				m_step++;
			} else {
				double leftSpeed = Math.abs(Constants.VISION_TURN_SPEED)
						* (Size.TOO_CLOCKWISE.equals(m_props.getSizeProperty(VisionConstants.DISTORTION)) ? -1.0 : 1.0);
				m_drives.setSpeed(leftSpeed, -leftSpeed);
			}
		} else if (m_step == 2) {
			if (Size.IN_RANGE.equals(m_props.getSizeProperty(VisionConstants.CENTER_X))) {
				m_drives.setSpeed(0.0);
				m_step += 2;
			}
			m_step++;
		} else if (m_step == 3) {
			if (m_drives.turnToAngle(
					Constants.VISION_TURN_FIX_ANGLE
							* (Size.TOO_LEFT.equals(m_props.getSizeProperty(VisionConstants.CENTER_X)) ? 1.0 : -1.0),
					Constants.VISION_TURN_SPEED)) {
				m_drives.setSpeed(0.0);
				m_step++;
			}
		} else if (m_step == 4) {
			if (m_drives.driveStraight(Constants.VISION_DRIVE_FIX_LENGTH, Constants.VISION_DRIVE_SPEED)) {
				m_drives.setSpeed(0.0);
				m_step -= 3;
			}
		} else if (m_step == 5) {
			if (Size.IN_RANGE.equals(m_props.getSizeProperty(VisionConstants.LINE_SPREAD))) {
				m_drives.setSpeed(0.0);
				m_step++;
			} else {
				m_drives.setSpeed(Math.abs(Constants.VISION_DRIVE_SPEED)
						* (Size.TOO_SMALL.equals(m_props.getSizeProperty(VisionConstants.LINE_SPREAD)) ? -1.0 : 1.0));
			}
		} else {
			m_drives.setSpeed(0.0);
			m_step = 0;
			return true;
		}
		return false;
	}

	public void lightOn() {
		// TODO: implement light logic
	}

	public void lightOff() {
		// TODO: implement light logic
	}

}
