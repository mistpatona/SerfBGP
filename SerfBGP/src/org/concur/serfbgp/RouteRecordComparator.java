package org.concur.serfbgp;

import java.util.Comparator;
import java.util.ListIterator;

public class RouteRecordComparator implements Comparator<RouteRecord> {

	@Override
	public int compare(RouteRecord r0, RouteRecord r1) {
		int p = r0.compareTo(r1);
		if (p !=0) return p; // first, use simplest method of previous generation
		//now compare pathes
		ListIterator<Integer> i0 = r0.path.listIterator();
		ListIterator<Integer> i1 = r1.path.listIterator();
	
		while (i0.hasNext() && i1.hasNext()) {
			p = i0.next().compareTo(i1.next());
			if (p !=0) return p;
		}
		boolean n0=i0.hasNext();
		if (n0 || i1.hasNext()) {if (n0) {return 1;} else return (-1);}
		else return 0;
	}

}
