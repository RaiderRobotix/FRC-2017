package org.raiderrobotix.vision;

import java.awt.image.BufferedImage;

public final class ImageHandler extends ImageUtilities {

	private BufferedImage m_image;
	private float m_leftCenterX;
	private float m_rightCenterX;
	private float m_centerX;
	private float m_distortion;
	private float m_lineSpread;
	private int m_width;

	public ImageHandler(BufferedImage img) {
		m_image = img;
		update();
	}

	public void update() {
		m_width = m_image.getWidth();
		m_leftCenterX = ((float) getCenter( // Get the centers of the two
											// different pieces of tape.
				getTruthTable(xCut(m_image, 0, (m_width / 2) - 1), VisionConstants.TAPE_LOWER_THRESHOLD))[0])
				/ ((float) m_width);
		m_rightCenterX = ((float) m_width + getCenter(
				getTruthTable(xCut(m_image, (m_width / 2), m_width - 1), VisionConstants.TAPE_LOWER_THRESHOLD))[0])
				/ ((float) m_width);
		m_centerX = ((float) getCenter(getTruthTable(m_image, VisionConstants.TAPE_LOWER_THRESHOLD))[0])
				/ ((float) m_width); // Center of tape structure
		m_distortion = percentAboveThreshold( // amount turned
				xCut(m_image, 0, (m_width / 2) - 1), VisionConstants.TAPE_LOWER_THRESHOLD)
				- percentAboveThreshold(xCut(m_image, m_width / 2, m_width - 1), VisionConstants.TAPE_LOWER_THRESHOLD);
		m_lineSpread = m_rightCenterX - m_leftCenterX; // length between centers
														// of tape pieces
	}

	public void update(BufferedImage img) {
		m_image = img;
		update();
	}

	public Size getLineSpread() {
		if (m_lineSpread < VisionConstants.TAPE_DISTANCE_LOWER_LIMIT) {
			return Size.TOO_SMALL;
		}
		if (m_lineSpread > VisionConstants.TAPE_DISTANCE_UPPER_LIMIT) {
			return Size.TOO_LARGE;
		}
		if (m_lineSpread < 0.0f) {
			return Size.ERROR;
		}
		return Size.IN_RANGE;
	}

	public Size getCenterX() {
		if (m_centerX < (m_width / 2) - VisionConstants.CENTER_X_MAXIMUM_DEVIATION) {
			return Size.TOO_LEFT;
		}
		if (m_centerX > (m_width / 2) + VisionConstants.CENTER_X_MAXIMUM_DEVIATION) {
			return Size.TOO_RIGHT;
		}
		return Size.IN_RANGE;
	}

	public Size getDistortion() {
		if (m_distortion > VisionConstants.DISTORTION_DEADBAND) {
			return Size.TOO_CLOCKWISE;
		}
		if (m_distortion < (-1 * VisionConstants.DISTORTION_DEADBAND)) {
			return Size.TOO_COUNTERCLOCKWISE;
		}
		return Size.IN_RANGE;
	}

	public void writeImageData() {
		// Create command to write image
		StringBuilder command = new StringBuilder(VisionConstants.I2C_WRITE_COMMAND_INIT);
		command.append(Integer.toString(VisionConstants.ARRAY_INITIAL));
		Size[] numsToAdd = new Size[] { getLineSpread(), getCenterX(), getDistortion() };
		for (Size i : numsToAdd) {
			command.append(" " + Byte.toString(i.getByte()));
		}
		LinuxExecuter.execute(command.toString()); // Write image to I2C via
													// Python
	}

}