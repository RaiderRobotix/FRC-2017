package org.raiderrobotix.vision;

public enum Size { // Different Size Constants
	TOO_SMALL(0), TOO_LARGE(2), IN_RANGE(1), ERROR(3), TOO_LEFT(TOO_SMALL.getByte()), TOO_RIGHT(
			TOO_LARGE.getByte()), TOO_CLOCKWISE(TOO_LARGE.getByte()), TOO_COUNTERCLOCKWISE(
					TOO_SMALL.getByte()), TOO_FAR(TOO_LARGE.getByte()), TOO_CLOSE(TOO_SMALL.getByte());

	private byte m_b;

	private Size(int b) {
		m_b = (byte) b;
	}

	public byte getByte() {
		return m_b;
	}

	public boolean equals(byte b) {
		return m_b == b;
	}

	public boolean equals(Size s) {
		return equals(s.getByte());
	}

}
