package org.raiderrobotix.vision;

import java.awt.image.BufferedImage;

/**
 * FRC-2017 specific Image Handler to produce image data.
 */
public final class FRC2017ImageHandler extends BufferedImageUtilities {

	private BufferedImage m_image;
	private int m_leftCenterX;
	private int m_rightCenterX;
	private int m_centerX;
	private int m_centerY;
	private float m_distortion;
	private int m_width;
	private int m_height;
	private final NetworkImageProperties m_props;

	public FRC2017ImageHandler(BufferedImage img) {
		m_image = img;
		m_props = NetworkImageProperties.getInstance();
		update();
	}

	public void update() {
		m_width = m_image.getWidth();
		m_height = m_image.getHeight();
		int threshold = VisionConstants.TAPE_LOWER_THRESHOLD_COLOR.getRGB();
		int[] center = getCenter(getTruthTable(m_image, threshold));
		m_centerX = center[0];
		m_centerY = center[1];
		BufferedImage left = xCut(m_image, 0, m_centerX);
		m_leftCenterX = getCenter(getTruthTable(left, threshold))[0];
		BufferedImage right = xCut(m_image, m_centerX, m_width - 1);
		m_rightCenterX = getCenter(getTruthTable(right, threshold))[0];
		m_distortion = percentAboveThreshold(left, threshold) - percentAboveThreshold(right, threshold);
	}

	public void update(BufferedImage img) {
		m_image = img;
		update();
	}

	public Size getDistortion() {
		if (m_distortion > VisionConstants.DISTORTION_THRESHOLD) {
			return Size.TOO_CLOCKWISE;
		} else if (m_distortion < VisionConstants.DISTORTION_THRESHOLD) {
			return Size.TOO_COUNTERCLOCKWISE;
		} else {
			return Size.IN_RANGE;
		}
	}

	public Size getCenterX() {
		float leftCenterX = ((float) m_leftCenterX) / ((float) m_width);
		float rightCenterX = ((float) m_rightCenterX) / ((float) m_width);
		float centerX = ((float) m_centerX) / ((float) m_width);
		if (rightCenterX - leftCenterX <= VisionConstants.TAPE_SIZE_LIMIT) {
			if (centerX > 0.5f) {
				return Size.TOO_LEFT;
			} else {
				return Size.TOO_RIGHT;
			}
		} else {
			if (centerX > 0.5f + VisionConstants.ALLOWABLE_CENTER_RANGE_X) {
				return Size.TOO_RIGHT;
			} else if (centerX < 0.5f - VisionConstants.ALLOWABLE_CENTER_RANGE_X) {
				return Size.TOO_LEFT;
			} else {
				return Size.IN_RANGE;
			}
		}
	}

	public Size getCenterY() {
		float centerY = ((float) m_centerY) / ((float) m_height);
		if (centerY > VisionConstants.CENTER_Y_POINT + VisionConstants.ALLOWABLE_CENTER_RANGE_Y) {
			return Size.TOO_FAR;
		} else if (centerY < VisionConstants.CENTER_Y_POINT - VisionConstants.ALLOWABLE_CENTER_RANGE_Y) {
			return Size.TOO_CLOSE;
		} else {
			return Size.IN_RANGE;
		}
	}

	public void writeImageData() {
		m_props.setProperty(VisionConstants.DISTORTION, getDistortion().getByte());
		m_props.setProperty(VisionConstants.CENTER_X, getCenterX().getByte());
		m_props.setProperty(VisionConstants.CENTER_Y, getCenterY().getByte());
	}

}