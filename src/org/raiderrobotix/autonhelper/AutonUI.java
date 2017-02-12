package org.raiderrobotix.autonhelper;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.raiderrobotix.frc2017.Constants;

public final class AutonUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private static AutonUI m_instance;

	private Container m_pane;
	private InstructionSet m_is;
	private JButton m_addButton = new JButton("Add Step");
	private JButton m_helpButton = new JButton("Constants");
	private JButton m_ftpButton = new JButton("Send Code to Robot");
	private JButton m_copyButton = new JButton("Copy Code to Clipboard");
	private JButton m_openButton = new JButton("Open Auton");

	private AutonUI() {
		super("Auton Helper");
		m_pane = this.getContentPane();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_pane.setLayout(new BorderLayout());
		m_is = InstructionSet.getInstance();
		m_addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_is.add(new InstructionPanel(m_is.size() + 1));
				updateUI(false);
			}
		});
		m_openButton.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				try {
					while (m_is.size() != 0) {
						m_is.remove(0);
					}
					int c = 0;
					for (Instruction i : (ArrayList<Instruction>) new ObjectInputStream(
							new FileInputStream(Utility.getFile(Constants.AUTON_DATA_LOCAL_DIRECTORY))).readObject()) {
						m_is.add(new InstructionPanel(++c, i));
					}
					updateUI(false);
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		m_helpButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						new JLabel(
								"<html>Constants.SHOOTER_HIGH_SPEED = " + Double.toString(Constants.SHOOTER_HIGH_SPEED)
										+ ";<br>Constants.SHOOTER_LOW_SPEED = "
										+ Double.toString(Constants.SHOOTER_LOW_SPEED) + ";</html>",
								SwingConstants.CENTER),
						"Constants", JOptionPane.PLAIN_MESSAGE);
			}
		});
		m_ftpButton.addActionListener(new ActionListener() {
			@SuppressWarnings("resource")
			public void actionPerformed(ActionEvent e) {
				ArrayList<Instruction> instructions = new ArrayList<Instruction>();
				for (InstructionPanel i : m_is) {
					instructions.add(i.getInstruction());
				}
				try {
					String name = Utility.getName();
					name += ".dat";
					Utility.sendOverFile(instructions, name);
					new ObjectOutputStream(new FileOutputStream(
							new File(Constants.AUTON_DATA_LOCAL_DIRECTORY.getAbsolutePath(), name).getAbsolutePath()))
									.writeObject(instructions);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null,
							new JLabel("<html>There was an error<br>sending the file.</html>", SwingConstants.CENTER),
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		m_copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String methodName = Utility.getName();
					Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
					StringSelection code = new StringSelection(m_is.getCode(methodName));
					c.setContents(code, code);
					JOptionPane.showMessageDialog(null, new JLabel("Copy Successful!", SwingConstants.CENTER),
							"Message", JOptionPane.PLAIN_MESSAGE);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,
							new JLabel("<html>There was an error<br>copying the code.</html>", SwingConstants.CENTER),
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		updateUI(true);
		this.setSize(850, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static AutonUI getInstance() {
		if (m_instance == null) {
			m_instance = new AutonUI();
		}
		return m_instance;
	}

	/**
	 * Repaints and revalidates all components and puts them together if this is
	 * the first time.
	 * 
	 * @param init
	 *            Is this the first time the UI is running for the user?
	 */
	protected void updateUI(boolean init) {
		if (init) {
			m_pane.removeAll();
			JPanel northernButtonPanel = new JPanel();
			northernButtonPanel.setLayout(new GridLayout(1, 5, 10, 10));
			northernButtonPanel.add(m_addButton);
			northernButtonPanel.add(m_ftpButton);
			northernButtonPanel.add(m_copyButton);
			northernButtonPanel.add(m_helpButton);
			northernButtonPanel.add(m_openButton);
			m_pane.add(northernButtonPanel, BorderLayout.NORTH);
		} else {
			try {
				m_pane.remove(1);
			} catch (Exception e) {
			}
		}
		m_pane.add(m_is.getPanel(), BorderLayout.CENTER);
		this.revalidate();
		this.repaint();
	}

	public static void main(String[] args) {
		 AutonUI.getInstance();
	}

}