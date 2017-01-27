package org.raiderrobotix.vision;

import java.util.Properties;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class ImageProperties extends Properties {

	private static final long serialVersionUID = 1L;
	private static ImageProperties m_instance;

	private final NetworkTable m_imageSizeTable;

	private ImageProperties() {
		m_imageSizeTable = NetworkTable.getTable(VisionConstants.CAMERA_NETWORK_TABLE);
	}

	public static ImageProperties getInstance() {
		if (m_instance == null) {
			m_instance = new ImageProperties();
		}
		return m_instance;
	}

	public byte getSizeProperty(String key) {
		return Byte.parseByte(super.getProperty(key));
	}

	public boolean containsError() {
		boolean ret = false;
		for (Object i : this.keySet()) {
			if (getSizeProperty(i.toString()) == Size.ERROR.getByte()) {
				ret = true;
			}
		}
		return ret;
	}

	public void updateProperties() {
		updateProperty(VisionConstants.CENTER_X);
		updateProperty(VisionConstants.DISTORTION);
		updateProperty(VisionConstants.LINE_SPREAD);
	}

	private void updateProperty(String key) {
		super.setProperty(key, m_imageSizeTable.getString(key, Byte.toString(Size.ERROR.getByte())));
	}

	public void setProperty(String key, byte value) {
		m_imageSizeTable.putString(key, Byte.toString(value));
	}

}
