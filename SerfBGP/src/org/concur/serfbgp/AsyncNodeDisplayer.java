package org.concur.serfbgp;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AsyncNodeDisplayer extends RunnableMsg{
	private List<AsyncNode> ns = new LinkedList<AsyncNode>(); 
	public void add(AsyncNode n) {
		ns.add(n);
	}
	public String toString() { return "AsyncNodeDisplayer, nodes:"+ns.size(); }
	
	@Override
	public void runMsg() throws InterruptedException {
		for (AsyncNode n : ns) {
				System.out.print(n.toString());
				System.out.print(" #routes:"+n.getRouteTable().size()+
						//" avg route len:"+n.averageRouteLength() +
						" q size:"+n.inputEventsSize() 
						//+" neighbours:"+n.neighboursString()
						);
				System.out.println(" ");
		}
		System.out.println("---------------"+NodeEvent.lastCounter()+" "+
				" modifications:"+AsyncNodeManager.modifications);
		TimeUnit.MILLISECONDS.sleep(3000);
	}

}
