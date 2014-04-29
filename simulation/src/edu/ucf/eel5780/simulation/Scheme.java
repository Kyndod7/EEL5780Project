package edu.ucf.eel5780.simulation;

import java.util.List;

public class Scheme {
	
	public Scheme () {
		// do nothing
	}
	
	public Node selectNeighbor(List<Node> neighbors) {
		//TODO implement, handle 0 neightbors
		//TODO if any of neighbors sink send to it?
		if(neighbors.size() == 0) {
			return null;
		}
		return neighbors.get(0);
	}

}
