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
		int n = 0;
		if (m_switchBox.getRawButton(5)) {
			n += 1;
		}
		if (m_switchBox.getRawButton(12)) {
			n += 2;
		}
		if (m_switchBox.getRawButton(7)) {
			n += 3;
		}
		if (m_switchBox.getRawButton(11)) {
			n += 4;
		}
		if (m_switchBox.getRawButton(6)) {
			n += 5;
		}
		if (m_switchBox.getRawButton(8)) {
			n += 6;
		}
		return n;
	}

	/**
	 * Show the user a list of autonomous modes on the robot.
	 * 
	 * @return A SendableChooser of autonomous modes to send to the
	 *         SmartDashboard.
	 */
	public SendableChooser<Integer> getSendableChooser() {
		SendableChooser<Integer> ret = new SendableChooser<Integer>();
		ret.addObject("-1: Do Nothing", -1);
		for (String i : new File(Constants.AUTON_DATA_RIO_DIRECTORY).list()) {
			ret.addObject(i.substring(i.lastIndexOf("/") + 1, i.lastIndexOf(".")),
					Integer.parseInt(i.substring(i.lastIndexOf("/") + 1, i.lastIndexOf("-"))));
		}
		return ret;
	}

	public void useFTPFile(int autonNumber) {
		if (m_step == 0) {
			try {
				m_auton = new AutonomousMode(autonNumber, Constants.AUTON_DATA_LOCAL_DIRECTORY);
				m_timer.start();
				m_timer.reset();
				m_step++;
			} catch (Exception e) {
				System.out.println("FTP Auton Reading Exception");
			}
		} else {
			if (m_auton.auton(m_timer.get() + 0.001) == 0.0) {
				// Add tiny amount to ensure robot wont skip a step.
				m_timer.reset();
			}
		}
	}

	public void test() {
		if (m_step == 0) {
			m_drives.resetEncoders();
			m_drives.resetNavX();
			m_drives.brakesOff();
			m_step++;
		} else if (m_step == 1) {
			if (m_drives.driveStraight(12.0, 0.4)) {
				m_drives.setSpeed(0.0);
				m_step++;
			}
		} else {
			m_drives.setSpeed(0.0);
		}
	}

}
