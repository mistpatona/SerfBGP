package org.concur.serfbgp;

import java.util.ArrayList;
import java.util.Arrays;



@SuppressWarnings("serial")
public class RouteList extends ArrayList<RouteRecord> {
	public RouteList filterDst(int d) {
		RouteList ans = new RouteList();
		for (RouteRecord r:this) if (r.dstNode == d) ans.add(r);
		return ans;
	}
	public RouteList() {}
	public static RouteList mkRouteList(RouteRecord[] rs) {
		RouteList ans = new RouteList();
		for (RouteRecord r:rs) ans.add(r);
		return ans;
	}
	public static RouteList mkRouteList(RouteRecord r) {
		RouteList ans = new RouteList();
		ans.add(r);
		return ans;
	}
	public RouteRecord[] asArray() {
		RouteRecord[] ans = new RouteRecord[this.size()];
		int i=0;
		for (RouteRecord r:this) ans[i++]=r;
		return ans;
	}
	public RouteRecord[] asArraySortedByPrice(){
		RouteRecord[] tmp = this.asArray();
		Arrays.sort(tmp);
		return tmp; 
	}
	public RouteList sortedByPrice(){
		return mkRouteList(this.asArraySortedByPrice()); 
	}
	public RouteList taxRoutes(int p){
		RouteList ans = new RouteList();
		for (RouteRecord r:this) 
			ans.add(r.addTax(p));
		return ans;
	}
}
