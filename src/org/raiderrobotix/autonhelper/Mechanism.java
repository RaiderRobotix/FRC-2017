package org.raiderrobotix.autonhelper;

public abstract class Mechanism {

	public static final int WAIT = -1;
	public static final int DRIVES = 0;
	public static final int BRAKES = 1;
	public static final int GEAR_COLLECTOR = 2;
	public static final int INTAKE = 3;
	public static final int SHOOTER = 4;
	public static final int LINE_BREAKER = 5;

	public abstract class Drives {

		public static final int STRAIGHT = 0;
		public static final int TURN = 1;

		public static final int BRAKES_ON = 1;
		public static final int BRAKES_OFF = 2;

	}

	public abstract class Collector {

		public static final int OPEN = 0;
		public static final int CLOSE = 1;

	}

	public abstract class Intake {

		public static final int INTAKE_IN = 0;
		public static final int INTAKE_OFF = 1;

	}

	public abstract class LineBreaker {

		public static final int BROKEN = 0;
		public static final int UNBROKEN = 1;

	}

}
