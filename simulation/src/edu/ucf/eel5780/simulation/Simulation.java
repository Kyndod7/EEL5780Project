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
			LOGGER.fine("=== Tick "  + i + "===");
			topology.tick();
		}
		
		LOGGER.info("Number of nodes = " + topology.getNumberOfNodes());
		LOGGER.info("Total packet created = " + Packet.getCreated());
		LOGGER.info("Total packet dropped = " + Packet.getDropped());
		LOGGER.info("Total packet arrived = " + Packet.getArrived());
		LOGGER.info("Average delay = " + Packet.getAverageDelay());
		LOGGER.info("Average reception = " + Packet.getAverageReception());
	}

}
