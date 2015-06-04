package org.concur.serfbgp;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;


public class NodeRunner extends RunnableMsg {
	
	private Node node;
	private ArrayBlockingQueue<NodeEvent> inputEvents = new ArrayBlockingQueue<NodeEvent>(25);
	public static final String routeTableChanged = "route table changed"; 
	public static final String somethingUpdated = "something updated";
	
	public NodeRunner() {
		node = new Node(); 
	}
	
	public NodeRunner(Node n) {
		node = n; 
	}
	
	public void linkTo(NodeRunner nr) throws InterruptedException{
		Node n = nr.getNode();
		node.linkTo(n);
		n.notifySomethingUpdated(node);
	}
	
	public Node getNode() {
		return node;
	}
	@Override
	public String toString() {
		return "R "+node.toString()+":"+node.neighboursString();
	}

	@Override
	public void runMsg() throws InterruptedException {
		checkRoutesChanged();
		synchronized(this) {wait();}
	}
	
	public void notifyRoutesChanged(Node n) throws InterruptedException{
		inputEvents.put(new NodeEvent(n,routeTableChanged));
	}
	public void notifySomethingUpdated(Node n) throws InterruptedException{
		inputEvents.put(new NodeEvent(n,somethingUpdated));
	}
	public void checkRoutesChanged() throws InterruptedException {
		Set<Node> s = new HashSet<Node>();
		while (!inputEvents.isEmpty()) {
			NodeEvent e = inputEvents.take();
			if (e.getMessage() == routeTableChanged) s.add(e.getNode());// compress repeating events
			if (e.getMessage() == somethingUpdated) s.add(e.getNode());// compress repeating events
		}
		for (Node n : s) node.updateReceivedRoutes(n);
		node.updateRouteTable();
		for (Node n : neighbours) n.notifyRoutesChanged(this);
		
	}
}
