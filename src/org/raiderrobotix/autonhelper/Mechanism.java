package org.raiderrobotix.autonhelper;

public abstract class Mechanism {

	public static final int WAIT = -1;
	public static final int DRIVES = 0;
	public static final int BRAKES = 1;
	public static final int LINE_UP = 2;

	public abstract class Drives {

		public static final int STRAIGHT = 0;
		public static final int TURN = 1;

		public static final int BRAKES_ON = 1;
		public static final int BRAKES_OFF = 2;

	}

}
