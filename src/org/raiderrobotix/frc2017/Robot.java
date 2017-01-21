package org.raiderrobotix.frc2017;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class Robot extends IterativeRobot {

	// ====== Robot Classes ======
	private AutonController m_autonController;
	private OI m_OI;
	private Drivebase m_drives;
	private CameraSetup m_camera;

	// ====== Auton Logic ======
	private SendableChooser<Integer> m_autonChooser;
	private int m_autonChosen;

	public void robotInit() {
		// ===== ROBOT MECHANISMS =====
		m_autonController = AutonController.getInstance();
		m_OI = OI.getInstance();
		m_drives = Drivebase.getInstance();
		m_camera = CameraSetup.getInstance();

		// ===== RESETS =====
		m_drives.resetNavX();
		m_drives.resetEncoders();

		// ===== AUTON STUFF ===== TODO: Fix Autons
		m_autonChooser = new SendableChooser<Integer>();
		m_autonChooser.addObject("-1: Do Nothing", -1);
		m_autonChooser.addObject("21: Use FTP'd File", 21);
		SmartDashboard.putData("Auton Key", m_autonChooser);
	}

	private void update() {
		// Send data to the Smart Dashboard
		SmartDashboard.putNumber("Left Encoder",
				m_drives.getLeftEncoderDistance());
		SmartDashboard.putNumber("Right Encoder",
				m_drives.getRightEncoderDistance());
		SmartDashboard.putNumber("Gyro", m_drives.getGyroAngle());
		SmartDashboard.putNumber("Auton Chosen",
				SmartDashboard.getNumber("Choose Auton", -1));

		if (this.isDisabled() || this.isAutonomous()) {
			m_autonChosen = m_autonController.getAutonChosen();
			System.out.println("Auton Chosen: " + m_autonChosen);
		}
	}

	public void autonomousInit() {
		m_drives.brakesOff();
		m_autonController.resetStep();
		m_drives.resetNavX();
		m_camera.lightOn();
	}

	public void autonomousPeriodic() {
		if (m_autonChosen == 21) {
			m_autonController.useFTPFile();
		}
		update();
	}

	public void teleopInit() {
		m_drives.brakesOff();
	}

	public void teleopPeriodic() {
		m_OI.enableTeleopControls();
		update();
	}

	public void disabledPeriodic() {
		update();
	}

	public void testPeriodic() {
		m_drives.brakesOff();
	}

}
