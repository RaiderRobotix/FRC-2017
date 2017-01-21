package org.raiderrobotix.frc2017;

import org.raiderrobotix.autonhelper.Auton;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public final class AutonController {

	private static AutonController m_instance;

	private int m_step;
	private final Drivebase m_drives;
	private final Timer m_timer;
	private final Joystick m_switchBox;
	private Auton m_auton;

	private AutonController() {
		m_drives = Drivebase.getInstance();
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

	public void useFTPFile() {
		if (m_step == 0) {
			try {
				m_auton = new Auton();
				m_timer.start();
				m_timer.reset();
				m_step++;
			} catch (Exception e) {
				System.out.println("FTP Auton Reading Exception");
			}
		} else {
			if (m_auton.auton(m_timer.get()) == 0.0) {
				m_timer.reset();
			}
		}
	}

}
