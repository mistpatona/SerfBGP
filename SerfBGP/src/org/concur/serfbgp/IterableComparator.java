package org.concur.serfbgp;

import java.util.Comparator;
import java.util.Iterator;

public class IterableComparator<T> implements Comparator<Iterable<T>>{

	Comparator<T> c;
	
	public IterableComparator(Comparator<T> c) {
		super();
		this.c = c;
	}

	@Override
	public int compare(Iterable<T> arg0, Iterable<T> arg1) {
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
