package edu.ucf.eel5780.simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.ucf.eel5780.util.SimLogger;
import edu.ucf.eel5780.util.SimProperties;

public class Node {
	
	private static final SimLogger LOGGER = SimLogger.getInstance();
	private int nodeNumber;
	private Scheme scheme;
	private int bufferSize;
	private double packetGenProp;
	private double reception;
	private LinkedList<Node> neighbors;
	private LinkedList<Packet> buffer;
	
	public Node(int nodeNumber, Scheme scheme) {
		this.nodeNumber = nodeNumber;
		this.scheme = scheme;
		initialize();
	}
	
	public void addNeighbor(Node neighbor) {
		neighbors.add(neighbor);
	}
	
	public int getBufferStatus() {
		return buffer.size();
	}
	
	public String[] getDotFormat() {
		List<String> dotFormatNode = new ArrayList<String>(); 
		
		for(int i = 0; i < neighbors.size(); i ++) {
			dotFormatNode.add(getNumber() + " -> " + neighbors.get(i).getNumber());
		}
		
		if(isDataSink()) {
			dotFormatNode.add(getNumber() + " [shape=box, style=filled, color=\".7 .3 1.0\"]");
		}
		
		return dotFormatNode.toArray(new String[dotFormatNode.size()]);
	}
	
	public int getNumber() {
		return this.nodeNumber;
	}
	
	public double getReception() {
		return this.reception;
	}
	
	public boolean isBufferEmpty() {
		if(getBufferStatus() == 0) {
			return true;
		}
		return false;
	}
	
	public boolean isDataSink() {
		if(neighbors.size() == 0) {
			return true;
		}
		return false;
	}
	
	public void receivePacket(Packet packet) {
		LOGGER.fine("Node" + getNumber() + " received packet " + packet.getId());
		packet.addHop(getReception());
		// data sinks do not forward packets
		if (isDataSink()) {
			packet.arrive();
			return;
		}
		// enqueues packet
		addPacket(packet);
	}
	
	/**
	 * Send next packet in the queue to the BEST neighbor
	 */
	public void sendPacket() {
		// data sinks do not forward packets
		if (isDataSink()) {
			return;
		}
		if (buffer.size() == 0) {
			return;
		}
		
		Packet nextPacket = buffer.remove();
		Node bestNeighbor = this.scheme.selectNeighbor(this.neighbors);
		LOGGER.fine("Node" + getNumber() + " sent packet " + nextPacket.getId() + " to Node" + bestNeighbor.getNumber());
		bestNeighbor.receivePacket(nextPacket); 
	}
	
	public void tick() {
		// data sinks do not generate or buffer packets
		if (isDataSink()) {
			return;
		}
		refreshBuffer();
		generatePacket();
	}
	
	private void addPacket(Packet packet) {
		// check weather to dropped the packet
		if (getBufferStatus() >= bufferSize) {
			LOGGER.fine("Node" + getNumber() + " dropped packet " + packet.getId()); 
			packet.drop();
			return;
		} 
		LOGGER.finer("Node" + getNumber() + " added packet " + packet.getId());
		buffer.add(packet);
	}
	
	/**
	 * Generate packet with probability.
	 */
	private void generatePacket() {
		double random = Math.random();
		if (random < packetGenProp) {
			Packet newPacket = Packet.getInstance();
			LOGGER.fine("Node" + getNumber() + " generated packet " + newPacket.getId()); 
			addPacket(newPacket);
		}
	}
	
	private void initialize() {
		SimProperties properties = SimProperties.getInstance();
		this.bufferSize = properties.getNodeBufferSize();
		this.packetGenProp = properties.getNodePacketGenProb();
		this.neighbors = new LinkedList<Node>();
		this.buffer = new LinkedList<Packet>();
		this.reception = Math.random();
	}
	
	private void refreshBuffer() {
		for(Packet packet : buffer) {
			packet.tick();
		}
	}

}
