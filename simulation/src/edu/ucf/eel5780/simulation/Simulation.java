package edu.ucf.eel5780.simulation;

import edu.ucf.eel5780.util.SimLogger;
import edu.ucf.eel5780.util.SimProperties;

public class Simulation {

	private static final SimLogger LOGGER = SimLogger.getInstance();
	
	public Simulation() {
	}

	public void run() {
		Topology topology = new Topology();
		int frames = SimProperties.getInstance().getNumFrames();

		for (int i = 0; i < frames; i++) {
			LOGGER.fine("tick "  + i);
			topology.tick();
		}
	}

}
