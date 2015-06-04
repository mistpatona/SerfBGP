package org.concur.serfbgp;

public class NodeEvent {
	private static long counter = 0;
	private long id = counter++;
	
	Node sender;
	String message;
	
	public Node getNode() {
		return sender;
	}
	public String getMessage() {
		return message;
	}
	
	public NodeEvent(Node n, String msg){
		sender = n;
		message = msg;
	}
	public String toString() {
		return sender.toShortString() + ":" + id;
	}
}
