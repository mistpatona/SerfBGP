package org.concur.serfbgp;

public class NodeEvent {
	Node sender;
	public Node getNode() {
		return sender;
	}
	public String getMessage() {
		return message;
	}
	String message;
	public NodeEvent(Node n, String msg){
		sender = n;
		message = msg;
	}
	public String toString() {
		return sender.toShortString() + ":" + message;
	}
}
