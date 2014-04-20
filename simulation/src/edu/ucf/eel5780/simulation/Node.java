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
	private LinkedList<Node> neighbors;
	private LinkedList<Packet> buffer;
	
	public Node(int nodeNumber, Scheme scheme) {
		this.nodeNumber = nodeNumber;
		this.scheme = scheme;
		initialize();
	}
	
	private void initialize() {
		SimProperties properties = SimProperties.getInstance();
		this.bufferSize = properties.getNodeBufferSize();
		this.packetGenProp = properties.getNodePacketGenProb();
		this.neighbors = new LinkedList<Node>();
		this.buffer = new LinkedList<Packet>();
	}
	
	public void addNeighbor(Node neighbor) {
		neighbors.add(neighbor);
	}
	
	public void addPacket(Packet packet) {
		// data sinks do not forward or generate packets
		if (isDataSink()) {
			return;
		}
		// check weather to dropped the packet
		if (getBufferStatus() >= bufferSize) {
			return;
		}
		// enqueues packet
		buffer.add(packet);
		LOGGER.fine("Node " + getNumber() + " added packet"); 
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
		return nodeNumber;
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
	
	public void tick() {
		// data sinks do not forward or generate packets
		if (isDataSink()) {
			return;
		}
		//send next packet in the queue to the BEST neighbor
		sentPacket();
		//generate packet with probability
		generatePacket();
	}
	
	private void sentPacket() {
		if (buffer.size() == 0) {
			return;
		}
		
		Packet nextPacket = buffer.remove();
		Node bestNeighbor = getRoute();
		bestNeighbor.addPacket(nextPacket);
		LOGGER.fine("Node " + getNumber() + "sent packet"); 
	}
	
	private Node getRoute() {
		return this.scheme.selectNeighbor(this.neighbors);
	}
	
	private void generatePacket() {
		double random = Math.random();
		if (random < packetGenProp) {
			addPacket(new Packet());
			LOGGER.fine("Node " + getNumber() + " generated a packet"); 
		}
	}

}
