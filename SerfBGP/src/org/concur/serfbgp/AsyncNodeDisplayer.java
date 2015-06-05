package org.concur.serfbgp;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
				RouteList rs = n.getRouteTable();
				System.out.print(" #routes:"+rs.size()+
						//" avg route len:"+n.averageRouteLength() +
						" Qsize:"+n.inputEventsSize() 
						+" Nneighbours:"+n.getNeighbours().size()
						+" Path lengths: "+mapStats(rs.pathLengthStats())
						);
				System.out.println(" ");
		}
		System.out.println("---------------"+NodeEvent.lastCounter()+" "+
				" modifications:"+AsyncNodeManager.modifications);
		TimeUnit.MILLISECONDS.sleep(3000);
	}
	
	public String mapStats(Map<Integer,Integer> m) {
		StringBuilder ans = new StringBuilder();
		for (Entry<Integer,Integer> e : m.entrySet()) {
			ans.append(e.getKey());
			ans.append(":");
			ans.append(e.getValue());
			ans.append(" ");
		}
		ans.append(".");
		return ans.toString();
	}
}
