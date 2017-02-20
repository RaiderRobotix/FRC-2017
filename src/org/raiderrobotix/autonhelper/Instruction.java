package org.raiderrobotix.autonhelper;

import java.util.ArrayList;

public final class Instruction extends ArrayList<String> {

	private static final long serialVersionUID = 1L;

	private int m_counter = -1;
	private double m_expirationTime = 20.0;

	public Instruction() {
	}

	public Instruction(String s) {
		this.add(s);
	}

	public Instruction(ArrayList<String> list) {
		for (String i : list) {
			this.add(i);
		}
	}

	public String getNext() {
		m_counter++;
		return this.get(m_counter % (this.size()));
	}

	public void setExpirationTime(double expirationTime) {
		m_expirationTime = expirationTime;
	}

	public double getExpirationTime() {
		return m_expirationTime;
	}

	public boolean hasExpirationTime() {
		return m_expirationTime < 15.0;
	}

}
