package org.concur.serfbgp;

import java.util.ArrayList;
import java.util.List;

public class RouteRecord implements Comparable<RouteRecord>{
	public static RouteRecord mkOwnRecord(Integer myNode){
		ArrayList<Integer> a = new ArrayList<Integer>(); 
		a.add(myNode);
		return (new RouteRecord(a,0) );
	}
	public Integer dstNode;
	public ArrayList<Integer> path;
	public Integer price;
	public RouteRecord(Integer d,List<Integer> pth, Integer pri){
		dstNode = d;
		path = new ArrayList<Integer>(pth);
		price = pri;
		// TODO: assert path[path.length - 1] == dstNode;
	}
	public RouteRecord(List<Integer> pth, Integer pri){
		path = new ArrayList<Integer>(pth);
		price = pri;
		dstNode = path.get(path.size()-1);  //  last element
 	}
	@Override
	public int compareTo(RouteRecord rt) {
		int my = this.price;
		int his = rt.price;
		return (my < his) ? -1 : ( (my == his) ? 0 : 1);
	}
	public boolean eqTo (RouteRecord rt) {
		if (this.price != rt.price) return false;
		return (this.path.equals(rt.path));
	}
	private String showPath() {
		StringBuilder result = new StringBuilder();
		for (Integer i : path) 
			          result.append(":"+i);
	    return result.toString();
	}
	public String toString() {
		return "("+dstNode+")" + showPath() + " " + price;
	}
	public RouteRecord plusTax(Integer p) {
		return new RouteRecord(dstNode,path,price+p);
	}
	public boolean pathIncludes(Integer n){
		return path.contains(n);
	}
	public void prependPath(Integer n) {
		path.add(0,n);
	}
	public RouteRecord copy() {
		return new RouteRecord(dstNode,path,price);
	}
	public int pathSize() {
		return path.size();
	}
	
}
