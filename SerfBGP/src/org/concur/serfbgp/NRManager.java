package org.concur.serfbgp;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class NRManager extends RunnableMsg{
	private ExecutorService exec;
	private List<NodeRunner> nrs = new LinkedList<NodeRunner>();
	
	public void add(NodeRunner nr) {
		exec.execute(nr);
		nrs.add(nr);
	}
	
	public void add(Node n) {
		add(new NodeRunner(n));
	}
	
	@Override
	public String toString() { return "NodeRunnerManager, nodes:"+nrs.size(); }

	@Override
	public void runMsg() throws InterruptedException {
		System.out.println("---------------");
		for (NodeRunner nr : nrs) {
			System.out.print(nr.toString());
			System.out.print(" #routes:"+nr.getNode().getRouteTable().size());
			System.out.println("*");
		}
		System.out.println("---------------");
		TimeUnit.MILLISECONDS.sleep(3000);
	}
}
