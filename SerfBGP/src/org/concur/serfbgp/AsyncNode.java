package org.concur.serfbgp;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

public class AsyncNode extends Node implements Runnable {
	
	private ArrayBlockingQueue<NodeEvent> inputEvents = new ArrayBlockingQueue<NodeEvent>(25);
	public static final String routeTableChanged = "route table changed"; 
	public static final String somethingUpdated = "something updated";
	
	

/*	public AsyncNode() {
		// TODO Auto-generated constructor stub
		super();
	}*/
	@Override
	public String toString() {
		return "A"+super.toString()+":"+neighboursString();
	}
	
	public void linkTo(AsyncNode n) throws InterruptedException{
		super.linkTo(n);
		n.notifySomethingUpdated(this);
		notifySomethingUpdated(n);  // notify myself ?! suppose it's better way than
									// to call updating routes explicitly
	}
	
	public void runMsg() throws InterruptedException {
		checkRoutesChanged();
		synchronized(this) {wait();}
	}
	@Override
	public void run() {
		System.out.println(this.toString() + " started");
		try {
	       while(!Thread.interrupted()) {
	    	   runMsg();
	       }
	    }
		catch(InterruptedException e) {
	    	      System.out.println(this.toString() + " interrupted");
	    }
	    System.out.println(this.toString() + " terminating");
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
		for (Node n : s) updateReceivedRoutes(n);
		updateRouteTable();
		for (Node n : getNeighbours()) ((AsyncNode) n).notifySomethingUpdated(this);
		
	}
}
