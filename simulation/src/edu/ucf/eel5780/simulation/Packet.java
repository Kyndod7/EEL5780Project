package edu.ucf.eel5780.simulation;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Packet {
	
	private static SecureRandom random = new SecureRandom();
	private static int arrived;
	private static int created;
	private static int dropped;
	private static double totalDelay;
	private static double totalAverageReception;
	
	public static Packet getInstance() {
		created++;
		return new Packet();
	}
	
	private static void dropped() {
		dropped++;
	}
	
	private static void arrived(int delay, double averageReception) {
		arrived++;
		totalDelay += delay;
		totalAverageReception += averageReception;
	}
	
	public static int getArrived() {
		return arrived;
	}
	
	public static double getAverageDelay() {
		return totalDelay/arrived; 
	}
	
	public static double getAverageReception() {
		return totalAverageReception/arrived;
	}
	
	public static int getCreated() {
		return created;
	}
	
	public static int getDropped() {
		return dropped;
	}

	private String id;
	private int hops;
	private int delay;
	private double reception;
	
	private Packet() {
		this.id = new BigInteger(25, random).toString(32);
	}
	
	public void addHop(double reception) {
		this.hops++;
		this.reception += reception;
	}
	
	public void arrive() {
		arrived(this.delay, this.reception / (double) this.hops);
	}
	
	public String getId() {
		return this.id;
	}
	
	public void drop() {
		dropped();
	}
	
	public void tick() {
		this.delay++;
	}

}
