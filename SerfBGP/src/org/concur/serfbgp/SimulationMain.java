package org.concur.serfbgp;

import java.util.concurrent.*;

public class SimulationMain {
	static AsyncNodeManager mgr;
	public static void main(String[] args) throws Exception {
	    ExecutorService exec = Executors.newCachedThreadPool();
	    mgr = new AsyncNodeManager(exec);
	    
	    System.out.println("Press ‘Enter’ to quit");
	    System.in.read();
	    exec.shutdownNow();  
	    
	}

}
