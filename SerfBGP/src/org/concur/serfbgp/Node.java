package org.concur.serfbgp;
import java.util.ArrayList;
import java.util.Collection;


public class Node {
	private static int node_counter = 0;
	private int id = node_counter++;
	public int getId() {return id;}
	public String toString() {return "N"+id;}
	private ArrayList<Node> neighbours;
	public int getNeighbourPrice(Node n) { return 100;}// for now, price is frozen
	void addNeighbours(Collection<Node> ns) {
		neighbours.addAll(ns);
	}
	public Node(){
		neighbours = new ArrayList<Node>(5);
	}
	public Node(Collection<Node> ns){
		neighbours = new ArrayList<Node>(ns);
	}

}
