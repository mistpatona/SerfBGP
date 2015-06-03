package org.concur.serfbgp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;



public class Node {
	private static Integer node_counter = 0;
	private Integer id = node_counter++;
	public static final String routeTableChanged = "route table changed"; 
	private ArrayList<Node> neighbours;
	private Map<Integer,RouteList> receivedRoutes = new HashMap<Integer,RouteList>();
	private RouteList routeTable = new RouteList();
	private ArrayBlockingQueue<NodeEvent> inputEvents = new ArrayBlockingQueue<NodeEvent>(25);

	public Integer getId() {return id;}
	public String toString() {return "N"+id;}
	public Integer getNeighbourPrice(Node n) { return 100;}// for now, price is frozen
	public void addNeighbours(Collection<Node> ns) {
		neighbours.addAll(ns);
	}
	public void addNeighbour(Node n) {
		if ((!neighbours.contains(n)) && (!n.equals(this)))   neighbours.add(n);
	}
	public void linkTo(Node n) {
		addNeighbour(n);
		n.addNeighbour(this);
	}
	public Node(){
		neighbours = new ArrayList<Node>(5);
	}
	/*public Node(Node n){
		neighbours = new ArrayList<Node>(5);
		neighbours.add(n);
	}
	public Node(Collection<Node> ns){
		neighbours = new ArrayList<Node>(ns);
	}*/
	private RouteList listFromNode(Node n, RouteList rs){
		return rs.taxRoutes(getNeighbourPrice(n));
	}
	/*private void importRoutesFromNode(Node n, RouteList rs){
		receivedRoutes.put(n.getId(), rs);
	}*/
	public Map<Integer,RouteRecord> routesByDst(){
		RouteList allRs = new RouteList();
		for (RouteList x : receivedRoutes.values())
			allRs.addAll(x.filterOutNode(getId())); // concat.filter(not includesMe)
		Map<Integer,RouteList> m = new HashMap<Integer,RouteList>();
		for (RouteRecord r : allRs) {
			Integer i = r.dstNode;
			RouteList rs = m.get(i);
			if (rs != null) { 
				rs.add(r);
			    m.put(i,rs); 
			  }
			 else { 
				m.put(i,RouteList.mkRouteList(r));
			 } // put all into map indexed by node numbers
		}
		Map<Integer,RouteRecord> m1 = new HashMap<Integer,RouteRecord>();
		for (Integer i : m.keySet())
			m1.put(i, m.get(i).sortedByPrice().get(0)); // map head.sort -- choose cheapest route
		return m1;
	}
	public RouteList bestRouteList() {
		return RouteList.mkRouteList(routesByDst().values());
	}
	public void updateRouteTable(){
		routeTable = bestRouteList();//essentially from "receivedRoutes" Map
	}
	public RouteList getRouteTable(){
		RouteList r = routeTable.mkExportRouteList(getId());
		r.add(RouteRecord.mkOwnRecord(getId()));
		return r; 
	}
	public void updateReceivedRoutes(){
		Map<Integer,RouteList> tmp = new HashMap<Integer,RouteList>();
		for (Node n : neighbours){
			RouteList rs = listFromNode(n,n.getRouteTable());
			tmp.put(n.getId(), rs);
		}
		receivedRoutes = tmp;
	}
	public void updateReceivedRoutes(Node n){
		Map<Integer,RouteList> tmp = receivedRoutes;
		    RouteList rs = listFromNode(n,n.getRouteTable());
			tmp.put(n.getId(), rs);
		receivedRoutes = tmp;
	}
	public void updateRoutes(){
		updateReceivedRoutes();
		updateRouteTable();
	}
	public void updateRoutes(Node n){
		updateReceivedRoutes(n);
		updateRouteTable();
	}
	public void notifyRoutesChanged(Node n) throws InterruptedException{
		inputEvents.put(new NodeEvent(n,routeTableChanged));
	}
	public void checkRoutesChanged() throws InterruptedException {
		Set<Node> s = new HashSet<Node>();
		while (!inputEvents.isEmpty()) {
			NodeEvent e = inputEvents.take();
			if (e.getMessage() == routeTableChanged) s.add(e.getNode());// compress repeating events
		}
		for (Node n : s) updateReceivedRoutes(n);
		updateRouteTable();
		for (Node n : neighbours) n.notifyRoutesChanged(this);
		
	}
	
}
