package org.raiderrobotix.autonhelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.raiderrobotix.frc2017.CameraSetup;
import org.raiderrobotix.frc2017.Constants;
import org.raiderrobotix.frc2017.Drivebase;
import org.raiderrobotix.frc2017.GearCollector;

public final class AutonomousMode extends ArrayList<Instruction> {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public AutonomousMode() throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(Constants.AUTON_FILE_PATH));
		for (Instruction i : (ArrayList<Instruction>) in.readObject()) {
			this.add(i);
		}
		in.close();
	}

	public AutonomousMode(ArrayList<Instruction> autonList) {
		for (Instruction i : autonList) {
			this.add(i);
		}
	}

	public double auton(double time) {
		try {
			Drivebase drives = Drivebase.getInstance();
			CameraSetup camera = CameraSetup.getInstance();
			GearCollector collector = GearCollector.getInstance();
			Instruction i = this.get(0);
			switch (Integer.parseInt(i.getNext())) {
			case Mechanism.GEAR_COLLECTOR:
				switch (Integer.parseInt(i.getNext())) {
				case Mechanism.Collector.OPEN:
					collector.openCollector();
					break;
				case Mechanism.Collector.CLOSE:
					collector.closeCollector();
					break;
				}
				break;
			case Mechanism.LINE_UP:
				if (camera.linedUpToScore()) {
					time = 0.0;
					this.remove(0);
				}
				break;
			case Mechanism.DRIVES:
				if (drives.brakesAreOn()) {
					drives.brakesOff();
				}
				String fn = i.getNext();
				double value = Double.parseDouble(i.getNext());
				double speed = Double.parseDouble(i.getNext());
				switch (Integer.parseInt(fn)) {
				case Mechanism.Drives.STRAIGHT:
					if (drives.driveStraight(value, speed)) {
						time = 0.0;
						this.remove(0);
					}
					break;
				case Mechanism.Drives.TURN:
					if (drives.turnToAngle(value, speed)) {
						time = 0.0;
						this.remove(0);
					}
					break;
				}
				break;
			case Mechanism.WAIT:
				if (time >= Double.parseDouble(i.getNext())) {
					time = 0.0;
					this.remove(0);
				}
				break;
			case Mechanism.BRAKES:
				drives.setSpeed(0.0);
				if (Integer.parseInt(i.getNext()) == Mechanism.Drives.BRAKES_ON) {
					drives.brakesOn();
				} else {
					drives.brakesOff();
				}
				time = 0.0;
				this.remove(0);
			}
		} catch (NumberFormatException e) {
			System.err.println("Number Format Exception");
			this.remove(0);
		}
		return time;
	}

}
