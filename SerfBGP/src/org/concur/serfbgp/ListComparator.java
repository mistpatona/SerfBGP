package org.concur.serfbgp;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ListComparator<T> implements Comparator<List<T>>{

	Comparator<T> c;
	
	public ListComparator(Comparator<T> c) {
		super();
		this.c = c;
	}

	@Override
	public int compare(List<T> arg0, List<T> arg1) {
		Iterator<T> i0 = arg0.iterator();
		Iterator<T> i1 = arg1.iterator();
		while (i0.hasNext() && i1.hasNext()) {
			int p = c.compare(i0.next(),i1.next());  // i0.next().compareTo(i1.next());
			if (p !=0) return p;
		}
		boolean n0=i0.hasNext();
		if (n0 || i1.hasNext()) {if (n0) {return 1;} else return (-1);}
		else return 0;
	}

	
	

}
