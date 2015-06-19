package org.concur.serfbgp;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

public class AsyncNode extends Node implements Runnable {

	private ArrayBlockingQueue<NodeEvent> inputEvents = new ArrayBlockingQueue<NodeEvent>(
			125);
	private int inputEventsMaxSize;
	public static final String routeTableChanged = "route table changed";
	public static final String somethingUpdated = "something updated";
	private static Random rand = new Random(31);
	private Point positionCoords;

	public Point getPositionCoords() {
		return positionCoords;
	}
	
	public float getGeomDistanceTo (AsyncNode n)  {
		return this.getPositionCoords().distanceTo(n.getPositionCoords());
	}

	public void setPositionCoords(Point positionCoords) {
		this.positionCoords = positionCoords;
	}

	@Override
	public String toString() {
		// return "A"+super.toString()+":"+neighboursString();
		return super.toString();
	}

	public int inputEventsSize() {
		return inputEventsMaxSize;// inputEvents.size();
	}

	public void linkTo(AsyncNode n) throws InterruptedException {
		super.linkTo(n);
		n.notifySomethingUpdated(this);
		notifySomethingUpdated(n); // notify myself ?! suppose it's better way
									// than
									// to call updating routes explicitly
	}

	public void unlinkFrom(AsyncNode nd) throws InterruptedException {
		super.unlinkFrom(nd);
		nd.notifySomethingUpdated(this);
		for (Node n : getNeighbours())
			((AsyncNode) n).notifySomethingUpdated(this);
		for (Node n : nd.getNeighbours())
			((AsyncNode) n).notifySomethingUpdated(nd);

	}
	
	public void runMsg() throws InterruptedException {
		checkRoutesChanged();
		synchronized(inputEventsFlag) {inputEventsFlag.wait(10+rand.nextInt(600));}
	}
	@Override
	public void run() {
		System.out.println(this.toString() + " started");
		try {
			while (!Thread.interrupted()) {
				runMsg();
			}
		} catch (InterruptedException e) {
			System.out.println(this.toString() + " interrupted");
		}
		System.out.println(this.toString() + " terminating");
	}

	public void notifySomethingUpdated(Node n) throws InterruptedException {
		inputEvents.put(new NodeEvent(n, somethingUpdated));
		synchronized (inputEventsFlag) {
			inputEventsFlag.notify();
		}
	}

	public void checkRoutesChanged() throws InterruptedException {
		Set<Node> s = new HashSet<Node>();
		int p = 5;
		int q = 17;
		inputEventsMaxSize = (inputEvents.size() * p + inputEventsMaxSize * q)
				/ (p + q);
		while (!inputEvents.isEmpty()) {
			NodeEvent e = inputEvents.take();
			if (e.getMessage() == routeTableChanged)
				s.add(e.getNode());// compress repeating events
			if (e.getMessage() == somethingUpdated)
				s.add(e.getNode());// compress repeating events
		}
		Set<Node> nei = new HashSet<Node>(getNeighbours());
		// TODO: what is set intersection operation? we need (s 'intersect' nei)
		// -- retainAll(getNeighbours())
		for (Node n : s)
			if (nei.contains(n)) {
				updateReceivedRoutes(n);
			}
		if (updateRouteTable()) {
			for (Node n : getNeighbours())
				((AsyncNode) n).notifySomethingUpdated(this);
		} else {
		}

	}
}
