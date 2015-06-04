package org.concur.serfbgp;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

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
		//return "A"+super.toString()+":"+neighboursString();
		return super.toString();
	}
	
	public void linkTo(AsyncNode n) throws InterruptedException{
		super.linkTo(n);
		n.notifySomethingUpdated(this);
		notifySomethingUpdated(n);  // notify myself ?! suppose it's better way than
									// to call updating routes explicitly
	}
	public void unlinkFrom(AsyncNode nd) throws InterruptedException{
		super.unlinkFrom(nd);
		nd.notifySomethingUpdated(this);
		for (Node n : getNeighbours()) ((AsyncNode) n).notifySomethingUpdated(this);
		for (Node n : nd.getNeighbours()) ((AsyncNode) n).notifySomethingUpdated(nd);

	}
	
	public void runMsg() throws InterruptedException {
		checkRoutesChanged();
		//wait();
		//synchronized(this) {wait();}
		TimeUnit.MILLISECONDS.sleep(1500);
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
		Set<Node> nei = new HashSet<Node>(getNeighbours());
		//System.out.println("[" +super.toShortString()+"] updates from: "+s+" #");
		// TODO: what is set intersection operation? we need (s 'intersect' nei) -- retainAll(getNeighbours())
		for (Node n : s) if (nei.contains(n)) {
			//System.out.println("[" +super.toShortString()+"] updates from: "+n+" ");
			updateReceivedRoutes(n);
		}
		if (updateRouteTable()) {
			//System.out.println("[" +super.toShortString()+"] routes UPDated");
				for (Node n : getNeighbours()) 
					((AsyncNode) n).notifySomethingUpdated(this);
		} else {
			//System.out.println("[" +super.toShortString()+"] routes NO UPDATES");
		}
		//notifyAll();
	}
}
