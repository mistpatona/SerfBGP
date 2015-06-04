package org.concur.serfbgp;
import java.util.ArrayList;
//import java.util.Collection;
import java.util.HashMap;

import java.util.Map;





public class Node {
	private static Integer node_counter = 0;
	private Integer id = node_counter++;

	private ArrayList<Node> neighbours;
	public ArrayList<Node> getNeighbours() {
		return neighbours;
	}
	private Map<Integer,RouteList> receivedRoutes = new HashMap<Integer,RouteList>();
	private RouteList routeTable = new RouteList();
	

	public Integer getId() {return id;}
	public String toString() {return "_N"+id;}
	public String toShortString() {return "N"+id;}
	public String neighboursString() {
		StringBuilder result = new StringBuilder();
		for (Node n : neighbours) result.append(n.toShortString()+";");
		return result.toString();
	}
	public Integer getNeighbourPrice(Node n) { return 100;}// for now, price is frozen
	/*public void addNeighbours(Collection<Node> ns) {
		neighbours.addAll(ns);
	}*/
	public  void addNeighbour(Node n) {
		synchronized (neighbours) {
		if ((!neighbours.contains(n)) && (!n.equals(this)))   neighbours.add(n);
		}
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
	

	
	
}
