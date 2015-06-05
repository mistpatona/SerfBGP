package org.concur.serfbgp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;



@SuppressWarnings("serial")
public class RouteList extends ArrayList<RouteRecord> {
	public RouteList filterDst(Integer d) {
		RouteList ans = new RouteList();
		for (RouteRecord r:this) if (r.dstNode == d) ans.add(r);
		return ans;
	}
	public RouteList filterOutNode(Integer n) {
		RouteList ans = new RouteList();
		for (RouteRecord r:this) if (!r.pathIncludes(n)) ans.add(r);
		return ans;
	}
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (RouteRecord r:this)
			result.append(r.toString()+"\n");
	    return result.toString();
	}
	public RouteList() {}
	public static RouteList mkRouteList(RouteRecord[] rs) {
		RouteList ans = new RouteList();
		for (RouteRecord r:rs) ans.add(r);
		return ans;
	}
	public static RouteList mkRouteList(Collection<RouteRecord> rs) {
		RouteList ans = new RouteList();
		for (RouteRecord r:rs) ans.add(r);
		return ans;
	}
	public RouteList mkExportRouteList(Integer myNode) {
		RouteList ans = new RouteList();
		for (RouteRecord r:this) {
			RouteRecord s = r.copy();
			s.prependPath(myNode);
			ans.add(s);
		}
		return ans;
	}
	public static RouteList mkRouteList(RouteRecord r) {
		RouteList ans = new RouteList();
		ans.add(r);
		return ans;
	}
	private RouteRecord[] asArray() {
		RouteRecord[] ans = new RouteRecord[this.size()];
		int i=0;
		for (RouteRecord r:this) ans[i++]=r;
		return ans;
	}
	private RouteRecord[] asArraySortedByPrice(){
		RouteRecord[] tmp = this.asArray();
		Arrays.sort(tmp);
		return tmp; 
	}
	public RouteList sortedByPrice(){
		return mkRouteList(this.asArraySortedByPrice()); 
	}
	public RouteRecord[] asSortedArray(){
		RouteRecord[] tmp = this.asArray();
		Arrays.sort(tmp,new RouteRecordComparator());
		return tmp; 
	}
	public boolean eqTo(RouteList other) {
		RouteList s1 = RouteList.mkRouteList(this.asSortedArray());
		RouteList s2 = RouteList.mkRouteList(other.asSortedArray());

		ListComparator<RouteRecord> ccc = 
				new ListComparator<RouteRecord>(new RouteRecordComparator());

		return (ccc.compare(s1,s2) == 0);
		
	}
	public RouteList taxRoutes(Integer p){
		RouteList ans = new RouteList();
		for (RouteRecord r:this) 
			ans.add(r.plusTax(p));
		return ans;
	}
	public Map<Integer,Integer> pathLengthStats() {
		HashMap<Integer,Integer> ans = new HashMap<Integer,Integer>(3); 
		for (RouteRecord r:this){
			Integer i = r.pathSize();
			if (ans.containsKey(i)) {ans.put(i,ans.get(i)+1);}
			else {ans.put(i,1);}
		}
		return ans;
	}
}
