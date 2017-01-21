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
		m_speedPanel.setLayout(m_dataPanelLayout);
		m_speedPanel.add(m_speedLabel);
		m_speedPanel.add(m_speedField);
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

		// Create Mechanism Drop-Down menu
		m_mechanismDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_extraDataPanel.removeAll();
				switch (m_mechanismMapping.get((String) m_mechanismDropDown.getSelectedItem()).get(0)) {
				case Mechanism.DRIVES:
					m_extraDataPanel.add(m_valuePanel);
					m_extraDataPanel.add(m_speedPanel);
					break;
				case Mechanism.BRAKES:
					m_extraDataPanel.add(new JLabel(""));
					m_extraDataPanel.add(new JLabel(""));
					break;
				case Mechanism.WAIT:
					m_extraDataPanel.add(m_valuePanel);
					m_extraDataPanel.add(new JLabel(""));
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
		m_extraDataPanel.add(m_valuePanel);
		m_extraDataPanel.add(new JLabel(""));
		this.add(m_extraDataPanel);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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

		// Turn To Angle
		n = new ArrayList<Integer>();
		n.add(Mechanism.DRIVES);
		n.add(Mechanism.Drives.TURN);
		m_mechanismMapping.put("Drive- Spin", n);

		n = new ArrayList<Integer>();
		n.add(Mechanism.LINE_UP);
		m_mechanismMapping.put("Line Up For Gear", n);

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
		}
		ret.add("$");
		return ret;
	}

}
