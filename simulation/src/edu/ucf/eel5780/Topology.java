package edu.ucf.eel5780;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.ucf.eel5780.util.GraphViz;

public class Topology {

	/**
	 * Ranks: How 'tall' the DAG should be.
	 */
	private static int MIN_RANKS = 3;
	private static int MAX_RANKS = 3;
	
	/**
	 * Nodes/Rank: How 'fat' the DAG should be.
	 */
	private static int MIN_PER_RANK = 1;
	private static int MAX_PER_RANK = 5;

	/**
	 * Chance of having an edge.
	 */
	private static double PERCENT = 0.5;

	private HashMap<Integer, Node> nodesHash = new HashMap<Integer, Node>();
	
	public Topology () {
		generate();
	}
	
	private void generate() {
		int nodes = 0;
		int ranks = MIN_RANKS + (int)(Math.random() * ((MAX_RANKS - MIN_RANKS) + 1));

		//System.out.print("digraph {\n");

		// create ranks (height)
		for (int i = 0; i < ranks; i++) {
			// new nodes of 'higher' rank than all nodes generated until now
			int newNodes = MIN_PER_RANK + (int)(Math.random() * ((MAX_PER_RANK - MIN_PER_RANK) + 1));

			// create edges from old nodes ('nodes') to new ones ('newNodes')
			for (int j = 0; j < nodes; j++) {
				
				for (int k = 0; k < newNodes; k++) {
					
					if (Math.random() < PERCENT) {
						// create an edge
						//System.out.println(String.format("  %d -> %d;\n", j, k + nodes));
						addNodeNeighbor(j, k + nodes);
					}
					
				}
				
			}
			
			// accumulate into old node set.
			nodes += newNodes;
		}

		//System.out.print("}\n");
	}
	
	private void addNodeNeighbor(int nodeNumber, int neighborNumber) {
		Node node = nodesHash.get(nodeNumber);
		if(node == null) {
			node = new Node(nodeNumber);
			nodesHash.put(nodeNumber, node);
		}
		
		Node neighbor = nodesHash.get(neighborNumber);
		if(neighbor == null) {
			neighbor = new Node(neighborNumber);
			nodesHash.put(neighborNumber, neighbor);
		}
		
		node.addNeighbor(neighbor);
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
	    
		Iterator iterator = nodesHash.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Map.Entry<Integer, Node> pairs = (Map.Entry) iterator.next();
			String[] nodeDotFormat = pairs.getValue().getDotFormat();
			
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
