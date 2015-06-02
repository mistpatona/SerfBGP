package org.concur.serfbgp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;



public class Node {
	private static int node_counter = 0;
	private int id = node_counter++;
	private ArrayList<Node> neighbours;
	private Map<Integer,RouteList> receivedRoutes = new HashMap<Integer,RouteList>();
	private RouteList routeTable = new RouteList();

	public int getId() {return id;}
	public Integer getIntegerId() {return (Integer)id;}
	public String toString() {return "N"+id;}
	public int getNeighbourPrice(Node n) { return 100;}// for now, price is frozen
	public void addNeighbours(Collection<Node> ns) {
		neighbours.addAll(ns);
	}
	public void addNeighbour(Node n) {
		neighbours.add(n);
	}
	public Node(){
		neighbours = new ArrayList<Node>(5);
	}
	public Node(Node n){
		neighbours = new ArrayList<Node>(5);
		neighbours.add(n);
	}
	public Node(Collection<Node> ns){
		neighbours = new ArrayList<Node>(ns);
	}
	public RouteList listFromNode(Node n, RouteList rs){
		return rs.taxRoutes(getNeighbourPrice(n));
	}
	public void importRoutesFromNode(Node n, RouteList rs){
		receivedRoutes.put(n.getIntegerId(), rs);
	}
	public Map<Integer,RouteRecord> routesByDst(){
		RouteList allRs = new RouteList();
		for (RouteList x : receivedRoutes.values())
			allRs.addAll(x.filterOutNode(getId())); // concat.filter(includesMe)
		Map<Integer,RouteList> m = new HashMap<Integer,RouteList>();
		for (RouteRecord r : allRs) {
			Integer i = (Integer)(r.dstNode);
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
			m1.put(i, (m.get(i).asArraySortedByPrice())[0]); // map head.sort -- choose cheapest route
		return m1;
	}
	public RouteList bestRouteList() {
		return RouteList.mkRouteList(routesByDst().values());
	}
	public void updateRouteTable(){
		routeTable = bestRouteList();
	}
	public RouteList getRouteTable(){
		RouteList r = RouteList.mkRouteList(routeTable);
		r.add(RouteRecord.mkOwnRecord(getId()));
		return r; 
	}
	public void updateReceivedRoutes(){
		Map<Integer,RouteList> tmp = new HashMap<Integer,RouteList>();
		RouteList rs = new RouteList();
		for (Node n : neighbours){
			rs = listFromNode(n,n.getRouteTable());
			tmp.put(n.getIntegerId(), rs);
		}
		receivedRoutes = tmp;
	}
}
