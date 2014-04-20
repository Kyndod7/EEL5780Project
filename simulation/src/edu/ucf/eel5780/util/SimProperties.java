package edu.ucf.eel5780.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SimProperties {
	
	private static SimProperties singleton;
	
	public static SimProperties getInstance() {
		if (singleton == null) {
			singleton = new SimProperties();
		}
		return singleton;
	}
	
	private int numFrames;
	private int nodeBufferSize;
	private double nodePacketGenProb;
	private int minRanks;
	private int maxRanks;
	private int minPerRank;
	private int maxPerRank;
	private double edgeProb;
	
	private SimProperties() {
		initialize();
	}
	
	private void initialize() {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("simulation.properties"));
			loadProperties(properties);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private void loadProperties(Properties properties) {
		numFrames = Integer.parseInt(properties.getProperty("simulation.num_frames"));
		nodeBufferSize = Integer.parseInt(properties.getProperty("simulation.node_buffer_size"));
		nodePacketGenProb = Double.parseDouble(properties.getProperty("simulation.node_packet_gen_prob"));
		minRanks = Integer.parseInt(properties.getProperty("topology.min_ranks"));
		maxRanks = Integer.parseInt(properties.getProperty("topology.max_ranks"));
		minPerRank = Integer.parseInt(properties.getProperty("topology.min_per_rank"));
		maxPerRank = Integer.parseInt(properties.getProperty("topology.max_per_rank"));
		edgeProb = Double.parseDouble(properties.getProperty("topology.edge_prob"));
	}

	public int getNumFrames() {
		return numFrames;
	}

	public int getNodeBufferSize() {
		return nodeBufferSize;
	}

	public double getNodePacketGenProb() {
		return nodePacketGenProb;
	}

	public int getMinRanks() {
		return minRanks;
	}

	public int getMaxRanks() {
		return maxRanks;
	}

	public int getMinPerRank() {
		return minPerRank;
	}

	public int getMaxPerRank() {
		return maxPerRank;
	}

	public double getEdgeProb() {
		return edgeProb;
	}
	
}
