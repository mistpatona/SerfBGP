package org.concur.serfbgp;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;

public class SimulationMain {
	static AsyncNodeManager mgr;
	static AsyncNodeDisplayer dsp;
	
	public static void main(String[] args) throws Exception {
	    ExecutorService exec = Executors.newCachedThreadPool();
	    mgr = new AsyncNodeManager(exec);
	    dsp = new AsyncNodeDisplayer();
	    final ArrayList<DrawingNode> ns = new ArrayList<DrawingNode>(AsyncNodeManager.NODES);
	    Random rand = new Random();

	    for (int i=0; i<AsyncNodeManager.NODES; i++) {
	    	DrawingNode n = new DrawingNode();
	    	//n.setPositionCoords(new Point(rand.nextFloat(),rand.nextFloat())); // 0.0 to 1.0 in both coordinates
	    	n.setPositionCoords(new Point(
	    			(float)(((int)(i/10)+rand.nextFloat())/10.0+0.04),
	    			(float)((i%10 +rand.nextFloat())/10.0+0.03) ));
	    	ns.add(n); 
	    	mgr.add(n);
	    	dsp.add(n);
	    	}
	    exec.execute(dsp);
	    exec.execute(mgr);
	    EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                GraphDisplayer ex = new GraphDisplayer(ns);
                ex.setVisible(true);
            }
        });
	    	    
	    System.out.println("Press ÔEnterÕ to quit");
	    System.in.read();
	    exec.shutdownNow();  
	    
	}

}
