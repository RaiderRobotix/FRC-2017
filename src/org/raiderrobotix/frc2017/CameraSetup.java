package org.raiderrobotix.frc2017;

import org.raiderrobotix.vision.Size;
import org.raiderrobotix.vision.VisionConstants;

import edu.wpi.first.wpilibj.I2C;

public final class CameraSetup {

	private static CameraSetup m_instance;

	private final I2C m_pi;
	private final Drivebase m_drives;
	private int m_step = 0;
	private byte[] m_byteList;

	private CameraSetup() {
		m_pi = new I2C(I2C.Port.kOnboard, Constants.I2C_CAMERA_ADDRESS);
		m_drives = Drivebase.getInstance();
		m_byteList = new byte[VisionConstants.ARRAY_SIZE];
	}

	public static CameraSetup getInstance() {
		if (m_instance == null) {
			m_instance = new CameraSetup();
		}
		return m_instance;
	}

	public boolean linedUpToScore() {
		updateData();
		if (m_byteList[1] == Size.ERROR.getByte()) {
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
			if (Size.IN_RANGE.equals(m_byteList[VisionConstants.DISTORTION])) {
				m_drives.setSpeed(0.0);
				m_step++;
			} else {
				double leftSpeed = Math.abs(Constants.VISION_TURN_SPEED)
						* (Size.TOO_CLOCKWISE.equals(m_byteList[VisionConstants.DISTORTION]) ? -1.0 : 1.0);
				m_drives.setSpeed(leftSpeed, -leftSpeed);
			}
		} else if (m_step == 2) {
			if (Size.IN_RANGE.equals(m_byteList[VisionConstants.CENTER_X])) {
				m_drives.setSpeed(0.0);
				m_step += 2;
			}
			m_step++;
		} else if (m_step == 3) {
			if (m_drives.turnToAngle(
					Constants.VISION_TURN_FIX_ANGLE
							* (Size.TOO_LEFT.equals(m_byteList[VisionConstants.CENTER_X]) ? 1.0 : -1.0),
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
			if (Size.IN_RANGE.equals(m_byteList[VisionConstants.LINE_SPREAD])) {
				m_drives.setSpeed(0.0);
				m_step++;
			} else {
				m_drives.setSpeed(Math.abs(Constants.VISION_DRIVE_SPEED)
						* (Size.TOO_SMALL.equals(m_byteList[VisionConstants.LINE_SPREAD]) ? -1.0 : 1.0));
			}
		} else {
			m_drives.setSpeed(0.0);
			m_step = 0;
			return true;
		}
		return false;
	}

	private void updateData() {
		int readListSize = (VisionConstants.ARRAY_SIZE * 2) + 1;
		byte[] readList = new byte[readListSize];
		m_pi.readOnly(readList, readListSize);
		int index;
		for (index = 0; index < readListSize; index++) {
			if (readList[index] == VisionConstants.ARRAY_INITIAL) {
				index++;
				break;
			}
		}
		for (int i = index; i < (i + VisionConstants.ARRAY_SIZE) - 1; i++) {
			m_byteList[i - index] = readList[index];
		}
	}

	public void lightOn() {
		// TODO: implement light logic
	}

	public void lightOff() {
		// TODO: implement light logic
	}

}
