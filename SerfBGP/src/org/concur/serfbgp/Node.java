package org.concur.serfbgp;
//import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;





public class Node {
	private static Integer node_counter = 0;
	private Integer id = node_counter++;

	private ConcurrentHashMap<Node,NodeInfo> neighbours = 
			new ConcurrentHashMap<Node,NodeInfo>();
	public Set<Node> getNeighbours() {
		return neighbours.keySet();
	}
	public int getNeighbourCount() {
		return neighbours.size();
	}
	private Map<Integer,RouteList> receivedRoutes = new ConcurrentHashMap<Integer,RouteList>();
	private RouteList routeTable = new RouteList();
	public ReentrantLock inputEventsFlag = new ReentrantLock();

	public Integer getId() {return id;}
	public String toString() {return "N"+id;}
	public String toShortString() {return "N"+id;}
	public String neighboursString() {
		StringBuilder result = new StringBuilder();
		for (Node n : getNeighbours()) result.append(n.toShortString()+";");
		return result.toString();
	}
	public Integer getNeighbourPrice(Node n) { return 100; }// for now, price is frozen
	
	public void removeNeighbour(Node n) {
		if (neighbours.remove(n) == null) return; 
		receivedRoutes.remove(n.getId());
		updateRouteTable();
	}
	public void unlinkFrom(Node n){
		removeNeighbour(n);
		n.removeNeighbour(this);
	}
	public  void addNeighbour(Node n) {
		if ((!neighbours.containsKey(n)) && (!n.equals(this)))   
			neighbours.put(n,new NodeInfo()); // TODO: NodeInfo is a dummy plug now
	}
	public void linkTo(Node n) {
		addNeighbour(n);
		n.addNeighbour(this);
	}
	/*public Node(){
		neighbours = new ArrayList<Node>(5);
	}*/
	private RouteList listFromNode(Node n, RouteList rs){
		return rs.taxRoutes(getNeighbourPrice(n));
	}
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
	public boolean updateRouteTable(){
		RouteList old = routeTable;
		RouteList young = bestRouteList();//essentially from "receivedRoutes" Map
		synchronized(routeTable) { routeTable = young; }
		return (!old.eqTo(young));
	}
	public RouteList getRouteTable(){
		RouteList r = routeTable.mkExportRouteList(getId());
		r.add(RouteRecord.mkOwnRecord(getId()));
		return r; 
	}
	public RouteList getRouteTable0(){
		return routeTable; 
	}
	public void updateReceivedRoutes(){
		Map<Integer,RouteList> tmp = new ConcurrentHashMap<Integer,RouteList>();
		for (Node n : getNeighbours()){
			RouteList rs = listFromNode(n,n.getRouteTable());
			tmp.put(n.getId(), rs);
		}
		receivedRoutes = tmp;
	}
	public void updateReceivedRoutes(Node n){
		RouteList rs = listFromNode(n,n.getRouteTable());
		receivedRoutes.put(n.getId(), rs);
	}
	public void updateRoutes(){
		updateReceivedRoutes();
		updateRouteTable();
	}
	public void updateRoutes(Node n){
		updateReceivedRoutes(n);
		updateRouteTable();
	}
	
	public float averageRouteLength() {
		int c=0;
		float s=0;
		for (RouteRecord r: getRouteTable0()) {
			c++;
			s=s+r.path.size();
		}
		if (c==0) return 0;
		return s/c;
	}
	
	
}
