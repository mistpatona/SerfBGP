package org.concur.serfbgp;

public class RouteRecord implements Comparable<RouteRecord>{
	public static RouteRecord mkOwnRecord(int myNode){
		int[] p = {myNode};
		return (new RouteRecord(p,0) );
	}
	public int dstNode;
	public int[] path;
	public int price;
	public RouteRecord(int d,int[] pth, int pri){
		dstNode = d;
		path = pth;
		price = pri;
		assert path[path.length - 1] == dstNode;
	}
	public RouteRecord(int[] pth, int pri){
		path = pth;
		price = pri;
		dstNode = path[path.length - 1]; //  last element
 	}
	@Override
	public int compareTo(RouteRecord rt) {
		int my = this.price;
		int his = rt.price;
		return (my < his) ? -1 : ( (my == his) ? 0 : 1);
	}
	private String showPath() {
		StringBuilder result = new StringBuilder();
		for (int i=0;i<path.length;i++) 
			          result.append(":"+path[i]);
	    return result.toString();
	}
	public String toString() {
		return "("+dstNode+")" + showPath() + " " + price;
	}
	public RouteRecord addTax(int p) {
		return new RouteRecord(dstNode,path,price+p);
	}
	public boolean pathIncludes(int n){
		for(int i=0;i<path.length;i++)
			if (path[i]==n) return true;
		return false;
	}
	
}
