package org.raiderrobotix.frc2017;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class Robot extends IterativeRobot {

	// ====== Robot Classes ======
	private AutonController m_autonController;
	private OI m_OI;
	private Drivebase m_drives;
	private GearCollector m_gearCollector;
	private Compressor m_compressor;
	private PowerDistributionPanel m_pdp;

	// ====== Auton Logic ======
	private int m_autonChosen;

	public void robotInit() {

		// Code to delete ALL autonomous modes stored on the RoboRio
		// for(File i : new
		// File(Constants.AUTON_DATA_RIO_DIRECTORY).listFiles()) {
		// i.delete();
		// }

		// ===== ROBOT MECHANISMS =====
		m_autonController = AutonController.getInstance();
		m_OI = OI.getInstance();
		m_drives = Drivebase.getInstance();
		m_gearCollector = GearCollector.getInstance();
		m_compressor = new Compressor(Constants.PCM_CAN_ADDRESS);
		m_pdp = new PowerDistributionPanel(Constants.PDP_CAN_ADDRESS);

		// ===== RESETS =====
		m_drives.resetSensors();
		m_compressor.setClosedLoopControl(true);

		// ===== AUTON STUFF =====
		SmartDashboard.putData("Auton Key", m_autonController.getSendableChooser());
	}

	private void update() {
		// Send data to the Smart Dashboard
		SmartDashboard.putNumber("Left Encoder", m_drives.getLeftEncoderDistance());
		SmartDashboard.putNumber("Right Encoder", m_drives.getRightEncoderDistance());
		SmartDashboard.putNumber("Gyro Heading", m_drives.getGyroAngle());
		SmartDashboard.putNumber("Auton Chosen", m_autonChosen);
		SmartDashboard.putBoolean("Gear Collector Out?", m_gearCollector.isOut());
		SmartDashboard.putBoolean("Brakes In?", m_drives.brakesAreOn());
		SmartDashboard.putNumber("Battery Voltage", m_pdp.getVoltage());
		SmartDashboard.putNumber("Battery Current", m_pdp.getTotalCurrent());
		SmartDashboard.putNumber("Climber Current", m_pdp.getCurrent(13));

		if (this.isDisabled() || this.isAutonomous()) {
			m_autonChosen = m_autonController.getAutonChosen();
			System.out.printf("Auton Chosen: %d\n", m_autonChosen);
		}
	}

	public void autonomousInit() {
		m_drives.brakesOff();
		m_autonController.resetStep();
		m_drives.resetSensors();
		m_gearCollector.closeCollector();
	}

	public void autonomousPeriodic() {
		if (m_autonChosen != 0) {
			m_autonController.useFTPFile(m_autonChosen);
		}
		update();
	}

	public void teleopInit() {
		m_drives.brakesOff();
	}

	public void teleopPeriodic() {
		m_OI.setAutoIntakeOn(m_autonController.getSwitch(5));
		m_OI.setAutoLineBreakOn(m_autonController.getSwitch(6));
		m_OI.enableTeleopControls();
		update();
	}

	public void disabledPeriodic() {
		update();
	}

	public void testPeriodic() {
		m_drives.brakesOff();
		update();
	}

}
