package org.raiderrobotix.autonhelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.raiderrobotix.frc2017.Drivebase;
import org.raiderrobotix.frc2017.FuelHandler;
import org.raiderrobotix.frc2017.GearCollector;

public final class AutonomousMode extends ArrayList<Instruction> {

	private static final long serialVersionUID = 1L;
	private final Drivebase m_drives;
	private final GearCollector m_gearCollector;
	private final FuelHandler m_fuelHandler;

	@SuppressWarnings("unchecked")
	public AutonomousMode(int autonNumber, File dir) throws IOException, ClassNotFoundException {
		m_drives = Drivebase.getInstance();
		m_gearCollector = GearCollector.getInstance();
		m_fuelHandler = FuelHandler.getInstance();
		String filePath = "";
		File[] fileList = dir.listFiles();
		checkLoop: for (File file : fileList) {
			// Look for correct autonomous mode.
			if (file.isFile()) {
				String path = file.getAbsolutePath();
				try {
					path = path.substring(path.lastIndexOf("/") + 1, path.indexOf("_"));
					if (Integer.parseInt(path) == autonNumber) {
						filePath = file.getAbsolutePath();
						break checkLoop;
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
		for (Instruction i : (ArrayList<Instruction>) in.readObject()) {
			this.add(i);
		}
		in.close();
	}

	/**
	 * Take control of robot with .dat file.
	 * 
	 * @param time
	 *            The seconds passed on the current step on the robots timer.
	 *            This is turned to zero when the new step should start.
	 * @return The time passed into it. Returns 0.0 when complete.
	 */
	public double auton(double time) {
		try {
			if (this.size() > 0) {
				Instruction instruction = this.get(0);
				switch (Integer.parseInt(instruction.getNext())) {
				// Determine Mechanism requires and use it.
				case Mechanism.INTAKE:
					switch (Integer.parseInt(instruction.getNext())) {
					case Mechanism.Intake.INTAKE_IN:
						m_fuelHandler.intakeFuel(false);
						break;
					case Mechanism.Intake.INTAKE_OFF:
						m_fuelHandler.stopIntake();
						break;
					}
					break;
				case Mechanism.SHOOTER:
					m_fuelHandler.setShooterSpeed(Double.parseDouble(instruction.getNext()));
					break;
				case Mechanism.GEAR_COLLECTOR:
					switch (Integer.parseInt(instruction.getNext())) {
					case Mechanism.Collector.OPEN:
						m_gearCollector.openCollector();
						break;
					case Mechanism.Collector.CLOSE:
						m_gearCollector.closeCollector();
						break;
					}
					break;
				case Mechanism.DRIVES:
					if (m_drives.brakesAreOn()) {
						m_drives.brakesOff();
					}
					String fn = instruction.getNext();
					double value = Double.parseDouble(instruction.getNext());
					double speed = Double.parseDouble(instruction.getNext());
					switch (Integer.parseInt(fn)) {
					case Mechanism.Drives.STRAIGHT:
						if (m_drives.driveStraight(value, speed)) {
							time = 0.0;
							this.remove(0);
						}
						break;
					case Mechanism.Drives.TURN:
						if (m_drives.turnToAngle(value, speed)) {
							time = 0.0;
							this.remove(0);
						}
						break;
					}
					break;
				case Mechanism.WAIT:
					if (time >= Double.parseDouble(instruction.getNext())) {
						time = 0.0;
						this.remove(0);
					}
					break;
				case Mechanism.BRAKES:
					m_drives.setSpeed(0.0);
					if (Integer.parseInt(instruction.getNext()) == Mechanism.Drives.BRAKES_ON) {
						m_drives.brakesOn();
					} else {
						m_drives.brakesOff();
					}
					time = 0.0;
					this.remove(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.remove(0);
		}
		return time;
	}

}
