package org.raiderrobotix.vision;

import java.io.IOException;

public abstract class LinuxExecuter {

	// Execute commands on Linux OS command line.

	public static void execute(String command) throws IOException {
		Runtime.getRuntime().exec(command);
	}

	public static void executeAll(String[] cmds) throws IOException {
		for (String i : cmds) {
			execute(i);
		}
	}

}
