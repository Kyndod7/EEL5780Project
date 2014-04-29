package edu.ucf.eel5780.simulation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.ucf.eel5780.util.GraphViz;
import edu.ucf.eel5780.util.SimLogger;
import edu.ucf.eel5780.util.SimProperties;

public class Topology {

	private static final SimLogger LOGGER = SimLogger.getInstance();
	private List<Node> nodeList;
	
	public Topology () {
		initialize();
	}
	
	private void initialize() {
		this.nodeList = new ArrayList<Node>();
		// node container during topology generation
		HashMap<Integer, Node> nodeHash = new HashMap<Integer, Node>();
		
		generate(nodeHash);
		
		Iterator iterator = nodeHash.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Map.Entry<Integer, Node> keyValuePair = (Map.Entry) iterator.next();
			nodeList.add(keyValuePair.getValue());
		}
		
		GraphViz graphViz = print();
		LOGGER.finer(graphViz.getDotSource());
//		printToImage();
	}
	
	private void generate(HashMap<Integer, Node> nodeHash) {
		// initialize
		int nodes = 0;
		SimProperties properties = SimProperties.getInstance();
		// ranks: how 'long' the DAG should be
		int minRanks = properties.getMinRanks();
		int maxRanks = properties.getMaxRanks();
		// nodes/rank: how 'thick' the DAG should be
		int minPerRanks = properties.getMinPerRank();
		int maxPerRanks = properties.getMaxPerRank();
		// chance of having an edge
		double edgeProb = properties.getEdgeProb();
		
		int ranks = minRanks + (int)(Math.random() * ((maxRanks - minRanks) + 1));

		// create ranks (height)
		for (int i = 0; i < ranks; i++) {
			// new nodes of 'higher' rank than all nodes generated until now
			int newNodes = minPerRanks + (int)(Math.random() * ((maxPerRanks - minPerRanks) + 1));
			
			// create edges from old nodes ('nodes') to new ones ('newNodes')
			for (int j = 0; j < nodes; j++) {
				for (int k = 0; k < newNodes; k++) {
					if (Math.random() < edgeProb) {
						// create an edge
						//System.out.println(String.format("  %d -> %d;\n", j, k + nodes));
						addNodeNeighbor(j, k + nodes, nodeHash);
					}
				}
			}
			
			// accumulate into old node set
			nodes += newNodes;
		}

	}
	
	private void addNodeNeighbor(int nodeNumber, int neighborNumber, HashMap<Integer, Node> nodeHash) {
		Node node = getNode(nodeNumber, nodeHash);
		Node neighbor = getNode(neighborNumber, nodeHash);
		node.addNeighbor(neighbor);
	}
	
	private Node getNode(int nodeNumber, HashMap<Integer, Node> nodeHash) {
		Node node = nodeHash.get(nodeNumber);
		if(node == null) {
			node = new Node(nodeNumber, new Scheme());
			nodeHash.put(nodeNumber, node);
		}
		return node;
	}
	
	public int getNumberOfNodes() {
		return nodeList.size();
	}
	
	public int getNumberOfDataSinks() {
		int numDataSinks = 0;
		for(Node node : nodeList) {
			if(node.isDataSink()) {
				numDataSinks++;
			}
		}
		return numDataSinks;
	}

	public void tick() {
		List<Node> nodes = this.nodeList;
		List<Node> eventNodes = new ArrayList<Node>();
		
		// prevents nodes that gained packets during this tick to sent it out immediately
		for (Node node : nodes) {
			if(!node.isBufferEmpty()) {
				eventNodes.add(node);
			}
			node.tick();
		}
		
		for (Node eventNode : eventNodes)
			eventNode.sendPacket();
	}
	
	public void printToConsole() {
		GraphViz gv = print();
		System.out.println(gv.getDotSource());
	}
	
	public void printToImage() {
		GraphViz gv = print();
		File out = new File("out.png");
	    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "png" ), out );
	}
	
	private GraphViz print() {
	    GraphViz gv = new GraphViz();
	    gv.addln(gv.start_graph());
	    List<Node> nodeList = this.nodeList;
	    
		Iterator<Node> iterator = nodeList.iterator();
		
		while(iterator.hasNext()) {
			String[] nodeDotFormat = iterator.next().getDotFormat();
			
			if(nodeDotFormat.length > 0) {
				for(int i = 0; i < nodeDotFormat.length; i++) {
					gv.addln(nodeDotFormat[i] + ";");
				}
			}
		}
		
		gv.addln(gv.end_graph());
		return gv;
	}

}
