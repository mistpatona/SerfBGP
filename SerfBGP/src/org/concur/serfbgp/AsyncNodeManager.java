package org.concur.serfbgp;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncNodeManager  extends RunnableMsg{
	private ExecutorService exec;
	private List<AsyncNode> ns = new LinkedList<AsyncNode>();
	private static Random rand = new Random(31);
	private static boolean nowAddState = true;
	static volatile long modifications;
	public static final int NODES=100;
	public AsyncNodeManager(ExecutorService e) {
		exec = e;
	}
	public synchronized void add(AsyncNode n) {
			exec.execute(n);
			ns.add(n);
	}
			
	@Override
	public String toString() { return "AsyncNodeManager, nodes:"+ns.size(); }

	@Override
	public void runMsg() throws InterruptedException {
		if (rand.nextFloat()<0.5) for (int i=0;i<NODES*10;i++) dispatchLinks1dLimDist() ;
		TimeUnit.MILLISECONDS.sleep(400);
	}
	
	public void dispatchLinks() throws InterruptedException {
		if (nowAddState) {
			if (rand.nextFloat()<0.035)  {nowAddState = false;}
		}else {if  (rand.nextFloat()<0.025)  nowAddState = true;}
		int i1 = rand.nextInt(NODES);
		int i2 = rand.nextInt(NODES);
		// any to any
		if (i1 != i2) {
			modifications++;
			if (nowAddState) ns.get(i1).linkTo(ns.get(i2));
					else     ns.get(i1).unlinkFrom(ns.get(i2));
		}
	}
	
	public void dispatchLinks1d() throws InterruptedException {
		if (nowAddState) {
			if (rand.nextFloat()<0.025)  {nowAddState = false;}}
			else {if  (rand.nextFloat()<0.025)  nowAddState = true;}
		int i = rand.nextInt(NODES-1);
		// only next to next
		if (nowAddState) ns.get(i).linkTo(ns.get(i+1));
		else     ns.get(i).unlinkFrom(ns.get(i+1));
		modifications++;
		
	}
	
	public void dispatchLinks15d() throws InterruptedException {
		if (nowAddState) {
			if (rand.nextFloat()<0.025)  {nowAddState = false;}}
			else {if  (rand.nextFloat()<0.025)  nowAddState = true;}
		int b = 7;
		int i = rand.nextInt(NODES-b);
		int j = i + rand.nextInt(b)+1;
		// only those close enough, within "b" nodes near
		modifications++;
			if (nowAddState) ns.get(i).linkTo(ns.get(j));
			else     ns.get(i).unlinkFrom(ns.get(j));
	}
	
	public void dispatchLinks1dLim() throws InterruptedException {
		if (nowAddState) {
			if (rand.nextFloat() < 0.025) {
				nowAddState = false;
			}
		} else {
			if (rand.nextFloat() < 0.055)
				nowAddState = true;
		}
		int lim = 5;
		int b = 5;
		int i = rand.nextInt(NODES - b);
		int j = i + rand.nextInt(b) + 1;
		// only next to next 
		if (nowAddState) {
			AsyncNode n1 = ns.get(i);
			AsyncNode n2 = ns.get(j);
			if ((n1.getNeighbourCount() < lim)
					&& (n2.getNeighbourCount() < lim)) {
				n1.linkTo(n2);
				modifications++;
			}
		} else {
			ns.get(i).unlinkFrom(ns.get(j));
			modifications++;
		}

	}
	public void dispatchLinks1dLimDist() throws InterruptedException {
		if (nowAddState) {
			if (rand.nextFloat() < 0.010) {
				nowAddState = false;
			}
		} else {
			if (rand.nextFloat() < 0.055)
				nowAddState = true;
		}
		int lim = 25;
		int b = 15;
		//int i = rand.nextInt(NODES - b);
		//int j = i + rand.nextInt(b) + 1;
		int i = rand.nextInt(NODES);
		int j = i + rand.nextInt(b) + 1;
		j = j % NODES;
		// only next to next //getGeomDistanceTo
		if (nowAddState) {
			AsyncNode n1 = ns.get(i);
			AsyncNode n2 = ns.get(j);
			if ((n1.getNeighbourCount() < lim)
					&& (n2.getNeighbourCount() < lim)
					&&  (n1.getGeomDistanceTo(n2) < 0.205)) {
				n1.linkTo(n2);
				modifications++;
			}
		} else {
			ns.get(i).unlinkFrom(ns.get(j));
			modifications++;
		}

	}
	
}


