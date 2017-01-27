package org.raiderrobotix.autonhelper;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.raiderrobotix.frc2017.Constants;

public abstract class Utility {

	/**
	 * Get a file from the user.
	 * 
	 * @return File that was chosen
	 */
	public static File getFile() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			// Do nothing, as look and feel are just decorative.
		}
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose a File");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int a = JFileChooser.APPROVE_OPTION - 1;
		while (a != JFileChooser.APPROVE_OPTION || fileChooser.getSelectedFile() == null)
			a = fileChooser.showDialog(null, "Use");
		return fileChooser.getSelectedFile();
	}

	/**
	 * Get a name for the new autonomous mode.
	 * 
	 * @return A string- name of the new auton.
	 */
	public static String getName() {
		String a = "";
		boolean ran = false;
		while (a.length() == 0 || a.indexOf(" ") >= 0) {
			if (ran) {
				JOptionPane.showMessageDialog(null,
						"Please write a name with at least one character that contains no spaces, please.");
			}
			a = JOptionPane.showInputDialog(null, "Enter the method name", "Enter name", JOptionPane.PLAIN_MESSAGE);
			ran = true;
		}
		return a;
	}

	/**
	 * Get a speed value from the user.
	 * 
	 * @return A double value that will work with wpilibj motor setting.
	 */
	public static double getSpeed() {
		final JSlider slider = new JSlider(0, 100, 50);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		final JLabel value = new JLabel("50%");
		value.setHorizontalAlignment(SwingConstants.CENTER);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				value.setText(Integer.toString(slider.getValue()) + "%");
			}
		});
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1, 0, 8));
		panel.add(slider);
		panel.add(value);
		JOptionPane.showMessageDialog(null, panel, "Choose Motor Speed", JOptionPane.PLAIN_MESSAGE);
		return ((double) slider.getValue()) / 100.0;
	}

	/**
	 * Send over auton information to the robot.
	 * 
	 * @param auton
	 *            List of instructions to send to the robot.
	 * @throws IOException
	 *             Will throw if there is an error with the FTP connection.
	 */
	public static void sendOverFile(ArrayList<Instruction> auton) throws IOException {
		URL url = new URL(Constants.FTP_PREFIX + Constants.AUTON_FILE_PATH);
		URLConnection conn = url.openConnection();
		ObjectOutputStream out = new ObjectOutputStream(conn.getOutputStream());
		out.writeObject(auton);
		out.close();
		JOptionPane.showMessageDialog(null, new JLabel("<html>Transfer Successful!</html>", SwingConstants.CENTER),
				"Message", JOptionPane.PLAIN_MESSAGE);
	}

}
