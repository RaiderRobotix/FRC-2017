package org.raiderrobotix.autonhelper;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class InstructionSet extends ArrayList<InstructionPanel> {

	private static final long serialVersionUID = 1L;
	private static InstructionSet m_instance;

	private InstructionSet() {
	}

	public static InstructionSet getInstance() {
		if (m_instance == null) {
			m_instance = new InstructionSet();
		}
		return m_instance;
	}

	/**
	 * Updates and creates JPanel from instruction panel(s).
	 * 
	 * @return A usable JPanel using ArrayList information.
	 */
	public JPanel getPanel() {
		JPanel nonScroll = new JPanel();
		nonScroll.setLayout(new GridLayout(this.size(), 1, 0, 15));
		for (InstructionPanel i : this) {
			i.update();
			nonScroll.add(i);
			i.setPreferredSize(new Dimension(790, 56));
		}
		JScrollPane scroll = new JScrollPane(nonScroll, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		if (this.size() > 7) {
			scroll.setPreferredSize(new Dimension(829, 510));
		}
		scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JPanel ret = new JPanel();
		ret.add(scroll);
		return ret;
	}

	/**
	 * Refinement of the remove method.
	 */
	public InstructionPanel remove(int index) {
		InstructionPanel r = super.remove(index);
		for (int i = 0; i < this.size(); i++) {
			this.get(i).step = i + 1;
		}
		AutonUI.getInstance().updateUI(false);
		return r;
	}

	/**
	 * Refinement of the add method.
	 */
	public boolean add(InstructionPanel e) {
		super.add(e);
		e.update();
		return true;
	}

	/**
	 * Make plain-text code from the user's current selections.
	 * 
	 * @param methodName
	 *            The name of the method to use in the code.
	 * @return The plain-text code that you can copy into the robot files.
	 */
	public String getCode(String methodName) {
		// Set Up Initial Variables
		String ret = "public void " + methodName + "() {\n";
		ret += "if(m_step == 0) {\n";
		int stepCounter = 1;
		boolean extraIndentExists = false;

		// Add Resets and Close Last Step
		for (InstructionPanel i : this) {
			switch (Integer.parseInt(i.getInstruction().getNext())) {
			case Mechanism.WAIT:
				ret += "m_timer.start();\n";
				ret += "m_timer.reset();\n";
				break;
			case Mechanism.DRIVES:
				ret += "m_drives.brakesOff();\n";
				ret += "m_drives.resetNavX();\n";
				if (Integer.parseInt(i.getInstruction().get(1)) == Mechanism.Drives.STRAIGHT) {
					ret += "m_drives.resetEncoders();\n";
				}
				break;
			case Mechanism.BRAKES:
				ret += "m_drives.setSpeed(0.0);\n";
				break;
			}
			ret += "m_step++;\n";
			if (extraIndentExists) {
				ret += "}\n";
				extraIndentExists = false;
			}

			// Create Current Step
			ret += "} else if (m_step == " + Integer.toString(stepCounter) + ") {\n";
			stepCounter++;
			Instruction instruction = i.getInstruction();
			switch (Integer.parseInt(instruction.getNext())) {
			case Mechanism.INTAKE:
				switch (Integer.parseInt(instruction.getNext())) {
				case Mechanism.Intake.INTAKE_IN:
					ret += "m_fuelHandler.intakeFuel(false);\n";
					break;
				case Mechanism.Intake.INTAKE_OFF:
					ret += "m_fuelHandler.stopIntake();\n";
					break;
				}
				break;
			case Mechanism.SHOOTER:
				ret += "m_fuelHandler.setShooterSpeed(" + instruction.getNext() + ");\n";
				break;
			case Mechanism.GEAR_COLLECTOR:
				switch (Integer.parseInt(instruction.getNext())) {
				case Mechanism.Collector.OPEN:
					ret += "m_collector.openCollector();\n";
					break;
				case Mechanism.Collector.CLOSE:
					ret += "m_collector.closeCollector();\n";
					break;
				}
				break;
			case Mechanism.LINE_UP:
				ret += "if (m_camera.linedUpToScore()) {\n";
				extraIndentExists = true;
				break;
			case Mechanism.WAIT:
				ret += "if (m_timer.get() > " + instruction.getNext() + ") {\n";
				extraIndentExists = true;
				break;
			case Mechanism.DRIVES:
				switch (Integer.parseInt(instruction.getNext())) {
				case Mechanism.Drives.STRAIGHT:
					ret += "if (m_drives.driveStraight(" + instruction.getNext() + ", " + instruction.getNext()
							+ ")) {\n";
					break;
				case Mechanism.Drives.TURN:
					ret += "if (m_drives.turnToAngle(" + instruction.getNext() + ", " + instruction.getNext()
							+ ")) {\n";
					break;
				}
				extraIndentExists = true;
				break;
			case Mechanism.BRAKES:
				if (Integer.parseInt(instruction.getNext()) == Mechanism.Drives.BRAKES_ON) {
					ret += "m_drives.brakesOn();\n";
				} else {
					ret += "m_drives.brakesOff();\n";
				}
				break;
			}
		}

		// End Auton Method
		if (extraIndentExists) {
			ret += "}\n";
			extraIndentExists = false;
		}
		ret += "} else {\n";
		ret += "m_drives.setSpeed(0.0);\n";
		ret += "}\n";
		ret += "}\n";
		return ret;
	}

}
