package org.concur.serfbgp;



import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncNodeManager  extends RunnableMsg{
	private ExecutorService exec;
	private List<AsyncNode> ns = new LinkedList<AsyncNode>();
		
	public void add(AsyncNode n) {
			exec.execute(n);
			ns.add(n);
	}
		
		
	@Override
	public String toString() { return "AsyncNodeManager, nodes:"+ns.size(); }

	@Override
	public void runMsg() throws InterruptedException {
		System.out.println("---------------");
		for (AsyncNode n : ns) {
				System.out.print(n.toString());
				System.out.print(" #routes:"+n.getRouteTable().size());
				System.out.println("*");
		}
		System.out.println("---------------");
		TimeUnit.MILLISECONDS.sleep(3000);
	}
}


