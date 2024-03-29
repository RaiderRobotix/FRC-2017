package org.raiderrobotix.autonhelper;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public final class InstructionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// Components of an Instruction Panel
	private JLabel m_stepLabel = new JLabel("", SwingConstants.CENTER);
	private JButton m_removeButton = new JButton("X");
	private JButton m_reorderButtonUp = new JButton("<html>&#9650<html>");
	private JButton m_reorderButtonDown = new JButton("<html>&#9660<html>");
	private JButton m_expirationButton = new JButton("Expiration");
	private JComboBox<Object> m_mechanismDropDown;
	private JLabel m_speedLabel = new JLabel("Speed: ");
	private JTextField m_speedField = new JTextField();
	private JPanel m_speedPanel = new JPanel();
	private JLabel m_valueLabel = new JLabel("Value: ");
	private JTextField m_valueField = new JTextField();
	private JPanel m_valuePanel = new JPanel();
	private JPanel m_extraDataPanel = new JPanel();
	private JPanel m_reorderPanel;

	// Instruction Panel Resources
	protected int step;
	private InstructionSet m_instructionSet;
	private HashMap<String, ArrayList<Integer>> m_mechanismMapping;
	private GridLayout m_dataPanelLayout = new GridLayout(1, 2, 5, 5);
	private long m_lastTimeClickedSpeed;

	public InstructionPanel(int initStep) {
		// Assign Variables
		step = initStep;
		mapMechanisms();
		m_lastTimeClickedSpeed = System.currentTimeMillis() - 2000;
		m_mechanismDropDown = new JComboBox<Object>((m_mechanismMapping.keySet().toArray()));
		m_instructionSet = InstructionSet.getInstance();

		// Create panels For Extra Auton Data
		m_speedField.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
				if (m_lastTimeClickedSpeed + 300 >= System.currentTimeMillis()) {
					m_speedField.setText(Double.toString(Utility.getSpeed()));
					m_lastTimeClickedSpeed = System.currentTimeMillis() - 2000;
				} else {
					m_lastTimeClickedSpeed = System.currentTimeMillis();
				}
			}
		});

		m_speedPanel.setLayout(m_dataPanelLayout);
		m_speedPanel.add(m_speedLabel);
		m_speedPanel.add(m_speedField);
		m_valuePanel.setLayout(m_dataPanelLayout);
		m_valuePanel.add(m_valueLabel);
		m_valuePanel.add(m_valueField);
		m_extraDataPanel.setLayout(new GridLayout(2, 1, 5, 5));

		// Create Accessory Buttons and Functions for Instruction Panels
		m_removeButton.setForeground(Color.RED);
		m_removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_instructionSet.remove(step - 1);
			}
		});
		final InstructionPanel ip = this;
		m_reorderButtonUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_instructionSet.add(step - 2, ip);
				m_instructionSet.remove(step);
			}
		});
		m_reorderButtonDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_instructionSet.add(step + 1, ip);
				m_instructionSet.remove(step - 1);
			}
		});

		m_expirationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (JOptionPane.showOptionDialog(null,
						new JLabel("<html>Adjust the expiration time?</html>", SwingConstants.CENTER), "Option",
						JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[] { "Set", "Remove" }, -1)) {
				case 0:
					getNumberLoop: while (true) {
						try {
							double d = Double.parseDouble(JOptionPane.showInputDialog(null,
									new JLabel("<html>Enter expiration time.</html>", SwingConstants.CENTER),
									"Expiration,", JOptionPane.PLAIN_MESSAGE));
							m_expirationButton.setText(String.format("Exp.: %f", d));
							break getNumberLoop;
						} catch (NumberFormatException nfe) {
							// Do nothing as loop will repeat.
						}
					}
					break;
				case 1:
					m_expirationButton.setText("Expiration");
				}
			}
		});

		// Create Mechanism Drop-Down menu
		m_mechanismDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_extraDataPanel.removeAll();
				switch (m_mechanismMapping.get((String) m_mechanismDropDown.getSelectedItem()).get(0)) {
				case Mechanism.DRIVES:
					m_extraDataPanel.add(m_valuePanel);
					JPanel expirationSpeedPanel = new JPanel();
					expirationSpeedPanel.setLayout(m_dataPanelLayout);
					JPanel expirationPaneEast = new JPanel();
					expirationPaneEast.setLayout(m_dataPanelLayout);
					expirationPaneEast.add(m_expirationButton);
					expirationPaneEast.add(m_speedLabel);
					expirationSpeedPanel.add(expirationPaneEast);
					expirationSpeedPanel.add(m_speedField);
					m_extraDataPanel.add(expirationSpeedPanel);
					break;
				case Mechanism.BRAKES:
					m_extraDataPanel.add(new JLabel(""));
					m_extraDataPanel.add(new JLabel(""));
					break;
				case Mechanism.LINE_BREAKER:
					m_extraDataPanel.add(new JLabel(""));
					m_extraDataPanel.add(new JLabel(""));
					break;
				case Mechanism.WAIT:
					m_extraDataPanel.add(m_valuePanel);
					m_extraDataPanel.add(new JLabel(""));
					break;
				case Mechanism.GEAR_COLLECTOR:
					m_extraDataPanel.add(new JLabel(""));
					m_extraDataPanel.add(new JLabel(""));
					break;
				case Mechanism.INTAKE:
					m_extraDataPanel.add(new JLabel(""));
					m_extraDataPanel.add(new JLabel(""));
					break;
				case Mechanism.SHOOTER:
					m_extraDataPanel.add(new JLabel(""));
					m_extraDataPanel.add(m_speedPanel);
					break;
				}
				AutonUI.getInstance().updateUI(false);
			}
		});
		m_mechanismDropDown.setBackground(Color.WHITE);

		// Assign Layout and Components
		this.setLayout(m_dataPanelLayout);
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new GridLayout(2, 1, 2, 2));
		JPanel northWesternPanel = new JPanel();
		northWesternPanel.setLayout(m_dataPanelLayout);
		northWesternPanel.add(m_stepLabel);
		JPanel northWesternSubPanel = new JPanel();
		northWesternSubPanel.setLayout(m_dataPanelLayout);
		northWesternSubPanel.add(m_removeButton);
		m_reorderPanel = new JPanel();
		m_reorderPanel.setLayout(m_dataPanelLayout);
		m_reorderPanel.add(step == 1 ? new JLabel("") : m_reorderButtonUp);
		m_reorderPanel.add(step == InstructionSet.getInstance().size() ? new JLabel("") : m_reorderButtonDown);
		northWesternSubPanel.add(m_reorderPanel);
		northWesternPanel.add(northWesternSubPanel);
		westPanel.add(northWesternPanel);
		westPanel.add(m_mechanismDropDown);
		this.add(westPanel);
		m_extraDataPanel.add(new JLabel(""));
		m_extraDataPanel.add(new JLabel(""));
		this.add(m_extraDataPanel);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	public InstructionPanel(int initStep, Instruction i) {
		this(initStep);
		int m = Integer.parseInt(i.get(0));
		for (String j : m_mechanismMapping.keySet()) {
			if (m == m_mechanismMapping.get(j).get(0)) {
				if (!(m == Mechanism.BRAKES || m == Mechanism.DRIVES || m == Mechanism.INTAKE
						|| m == Mechanism.GEAR_COLLECTOR)) {
					m_mechanismDropDown.setSelectedItem(j);
					break;
				} else if (Integer.parseInt(i.get(1)) == m_mechanismMapping.get(j).get(1)) {
					m_mechanismDropDown.setSelectedItem(j);
					break;
				}
			}
		}
		m_mechanismDropDown.getActionListeners()[0].actionPerformed(null);
		int i0 = Integer.parseInt(i.getNext());
		if (i0 == Mechanism.BRAKES || i0 == Mechanism.DRIVES || i0 == Mechanism.INTAKE
				|| i0 == Mechanism.GEAR_COLLECTOR) {
			i.getNext();
		}
		if (i0 != Mechanism.DRIVES) {
			if (!(m_extraDataPanel.getComponent(0) instanceof JLabel)) {
				m_valueField.setText(i.getNext());
			}
			if (!(m_extraDataPanel.getComponent(1) instanceof JLabel)) {
				m_speedField.setText(i.getNext());
			}
		} else {
			m_valueField.setText(i.getNext());
			m_speedField.setText(i.getNext());
			if (i.hasExpirationTime()) {
				m_expirationButton.setText(String.format("Exp.: %f", i.getExpirationTime()));
			} else {
				m_expirationButton.setText("Expiration");
			}
		}
	}

	/**
	 * Assign drop down menu items to the correct mechanism IDs for shorthand
	 * code.
	 */
	private void mapMechanisms() {
		m_mechanismMapping = new HashMap<String, ArrayList<Integer>>();
		// Drive Straight
		ArrayList<Integer> n = new ArrayList<Integer>();
		n.add(Mechanism.DRIVES);
		n.add(Mechanism.Drives.STRAIGHT);
		m_mechanismMapping.put("Drive Straight", n);

		// Line Breaker Broken
		n = new ArrayList<Integer>();
		n.add(Mechanism.LINE_BREAKER);
		n.add(Mechanism.LineBreaker.BROKEN);
		m_mechanismMapping.put("Wait For Line Breaker Broken", n);

		// Line Breaer Unbroken
		n = new ArrayList<Integer>();
		n.add(Mechanism.LINE_BREAKER);
		n.add(Mechanism.LineBreaker.UNBROKEN);
		m_mechanismMapping.put("Wait For Line Breaker Unbroken", n);

		// Turn To Angle
		n = new ArrayList<Integer>();
		n.add(Mechanism.DRIVES);
		n.add(Mechanism.Drives.TURN);
		m_mechanismMapping.put("Drive- Spin", n);

		// Open Gear Collector
		n = new ArrayList<Integer>();
		n.add(Mechanism.GEAR_COLLECTOR);
		n.add(Mechanism.Collector.OPEN);
		m_mechanismMapping.put("Open Gear Collector", n);

		// Close Gear Collector
		n = new ArrayList<Integer>();
		n.add(Mechanism.GEAR_COLLECTOR);
		n.add(Mechanism.Collector.CLOSE);
		m_mechanismMapping.put("Close Gear Collector", n);

		// Brakes On
		n = new ArrayList<Integer>();
		n.add(Mechanism.BRAKES);
		n.add(Mechanism.Drives.BRAKES_ON);
		m_mechanismMapping.put("Brakes In", n);

		// Brakes Off
		n = new ArrayList<Integer>();
		n.add(Mechanism.BRAKES);
		n.add(Mechanism.Drives.BRAKES_OFF);
		m_mechanismMapping.put("Brakes Out", n);

		// Timer- Wait
		n = new ArrayList<Integer>();
		n.add(Mechanism.WAIT);
		m_mechanismMapping.put("Timer- Wait", n);

		// Intake- In
		n = new ArrayList<Integer>();
		n.add(Mechanism.INTAKE);
		n.add(Mechanism.Intake.INTAKE_IN);
		m_mechanismMapping.put("Intake- in", n);

		// Intake- Off
		n = new ArrayList<Integer>();
		n.add(Mechanism.INTAKE);
		n.add(Mechanism.Intake.INTAKE_IN);
		m_mechanismMapping.put("Intake- Off", n);

		// Set Shooter
		n = new ArrayList<Integer>();
		n.add(Mechanism.SHOOTER);
		m_mechanismMapping.put("Set Shooter", n);
	}

	/**
	 * Update instruction panel's appearance for minor changes.
	 */
	public void update() {
		m_stepLabel.setText("Step " + Integer.toString(step));
		m_reorderPanel.removeAll();
		m_reorderPanel.setLayout(m_dataPanelLayout);
		m_reorderPanel.add(step == 1 ? new JLabel("") : m_reorderButtonUp);
		m_reorderPanel.add(step == InstructionSet.getInstance().size() ? new JLabel("") : m_reorderButtonDown);
	}

	/**
	 * Make shorthand code from user input on UI.
	 * 
	 * @return Instruction information made from what the user enters into the
	 *         panel.
	 */
	public Instruction getInstruction() {
		Instruction ret = new Instruction();
		for (int i : m_mechanismMapping.get((String) m_mechanismDropDown.getSelectedItem())) {
			ret.add(Integer.toString(i));
		}
		switch (m_mechanismMapping.get((String) m_mechanismDropDown.getSelectedItem()).get(0)) {
		case Mechanism.DRIVES:
			ret.add(m_valueField.getText());
			ret.add(m_speedField.getText());
			break;
		case Mechanism.WAIT:
			ret.add(m_valueField.getText());
			break;
		case Mechanism.SHOOTER:
			ret.add(m_speedField.getText());
			break;
		}
		String expirationText = m_expirationButton.getText();
		if (expirationText.indexOf("Expiration") < 0) {
			ret.setExpirationTime(Double.parseDouble(expirationText.substring(expirationText.indexOf(" ") + 1)));
		}
		return ret;
	}

}
