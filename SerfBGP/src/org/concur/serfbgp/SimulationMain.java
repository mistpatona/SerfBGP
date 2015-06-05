package org.concur.serfbgp;

import java.util.ArrayList;
import java.util.concurrent.*;

public class SimulationMain {
	static AsyncNodeManager mgr;
	static AsyncNodeDisplayer dsp;
	
	public static void main(String[] args) throws Exception {
	    ExecutorService exec = Executors.newCachedThreadPool();
	    mgr = new AsyncNodeManager(exec);
	    dsp = new AsyncNodeDisplayer();
	    ArrayList<AsyncNode> ns = new ArrayList<AsyncNode>();

	    for (int i=0; i<AsyncNodeManager.NODES; i++) {
	    	AsyncNode n = new AsyncNode();
	    	ns.add(n); 
	    	mgr.add(n);
	    	dsp.add(n);
	    	}
	    exec.execute(dsp);
	    exec.execute(mgr);
	    	    
	    System.out.println("Press ÔEnterÕ to quit");
	    System.in.read();
	    exec.shutdownNow();  
	    
	}

}
