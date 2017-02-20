package org.raiderrobotix.frc2017;

import java.io.File;

import org.raiderrobotix.autonhelper.AutonomousMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public final class AutonController {

	private static AutonController m_instance;

	private int m_step;
	private final Drivebase m_drives;
	@SuppressWarnings("unused")
	private final GearCollector m_collector;
	private final Timer m_timer;
	private final Joystick m_switchBox;
	private AutonomousMode m_auton;

	private AutonController() {
		m_drives = Drivebase.getInstance();
		m_collector = GearCollector.getInstance();
		m_switchBox = new Joystick(Constants.SWITCH_BOX_PORT);
		m_timer = new Timer();
		m_step = 0;
	}

	public static AutonController getInstance() {
		if (m_instance == null) {
			m_instance = new AutonController();
		}
		return m_instance;
	}

	public void resetStep() {
		m_step = 0;
	}

	public int getAutonChosen() {
		int ret = 0;
		for (int i = 1; i <= 6; i++) {
			ret += (getSwitch(i) ? i : 0);
		}
		return ret;
	}

	/**
	 * Show the user a list of autonomous modes on the robot.
	 * 
	 * @return A SendableChooser of autonomous modes to send to the
	 *         SmartDashboard.
	 */
	public SendableChooser<Integer> getSendableChooser() {
		SendableChooser<Integer> ret = new SendableChooser<Integer>();
		ret.addObject("0: Do Nothing", -1);
		String[] autonFiles = new File(Constants.AUTON_DATA_RIO_DIRECTORY).list();
		for (String file : autonFiles) {
			ret.addObject(file.substring(0, file.lastIndexOf(".")),
					Integer.parseInt(file.substring(0, file.lastIndexOf("_"))));
		}
		return ret;
	}

	/**
	 * Use one of the saved file in the roboRIO's autonomous mode directory.
	 * 
	 * @param autonNumber
	 *            The number of the autonomous mode you wish to use.
	 */
	public void useFTPFile(int autonNumber) {
		if (m_step == 0) {
			try {
				m_auton = new AutonomousMode(autonNumber, new File(Constants.AUTON_DATA_RIO_DIRECTORY));
				m_timer.start();
				m_timer.reset();
				m_drives.resetSensors();
				m_step++;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("FTP Auton Reading Exception");
			}
		} else {
			if (m_auton.auton(m_timer.get() + 0.001) == 0.0) {
				// Add tiny amount to ensure robot wont skip a step.
				m_timer.reset();
				m_drives.resetSensors();
			}
		}
	}

	/**
	 * Tested and works. Not on SendableChooser.
	 */
	public void redLeftGear() {
		if (m_step == 0) {
			m_drives.brakesOff();
			m_drives.resetSensors();
			m_step++;
		} else if (m_step == 1) {
			if (m_drives.driveStraight(96.0, 0.7)) {
				m_drives.brakesOff();
				m_drives.resetSensors();
				m_step++;
			}
		} else if (m_step == 2) {
			if (m_drives.turnToAngle(60.0, 0.5)) {
				m_drives.brakesOff();
				m_drives.resetSensors();
				m_step++;
			}
		} else if (m_step == 3) {
			if (m_drives.driveStraight(31.0, 0.7)) {
			}
		} else {
			m_drives.setSpeed(0.0);
		}
	}

	/**
	 * Tested and works. Not in SendableChooser
	 */
	public void redGearRight() {
		if (m_step == 0) {
			m_drives.brakesOff();
			m_drives.resetSensors();
			m_step++;
		} else if (m_step == 1) {
			if (m_drives.driveStraight(90.0, 0.7)) {
				m_drives.brakesOff();
				m_drives.resetSensors();
				m_step++;
			}
		} else if (m_step == 2) {
			if (m_drives.turnToAngle(-60.0, 0.5)) {
				m_drives.brakesOff();
				m_drives.resetSensors();
				m_step++;
			}
		} else if (m_step == 3) {
			if (m_drives.driveStraight(36.0, 0.7)) {
			}
		} else {
			m_drives.setSpeed(0.0);
		}
	}

	public boolean getSwitch(int n) {
		switch (n) {
		case 1:
			return m_switchBox.getRawButton(5);
		case 2:
			return m_switchBox.getRawButton(12);
		case 3:
			return m_switchBox.getRawButton(7);
		case 4:
			return m_switchBox.getRawButton(11);
		case 5:
			return m_switchBox.getRawButton(6);
		case 6:
			return m_switchBox.getRawButton(8);
		default:
			return false;
		}
	}

}
