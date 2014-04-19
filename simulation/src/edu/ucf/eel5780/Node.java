package edu.ucf.eel5780;

import java.util.ArrayList;
import java.util.List;

public class Node {
	
	private int nodeNumber;
	private List<Node> neighbors;
	private Packet[] buffer; 
	
	public Node(int nodeNumber) {
		this.nodeNumber = nodeNumber;
		neighbors = new ArrayList<Node>();
	}
	
	public int getNumber() {
		return nodeNumber;
	}
	
	public void addNeighbor(Node neighbor) {
		neighbors.add(neighbor);
	}
	
	public String[] getDotFormat() {
		String[] dotFormatLine = new String[neighbors.size()];
		
		for(int i = 0; i < neighbors.size(); i ++) {
			dotFormatLine[i] = getNumber() + " -> " + neighbors.get(i).getNumber();
		}
		
		return dotFormatLine;
	}

}
