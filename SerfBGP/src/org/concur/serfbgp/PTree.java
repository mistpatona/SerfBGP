package org.concur.serfbgp;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PTree<T> { // Path Tree
	private final T data; // leave null for root
	private final ArrayList<PTree<T>> children = new ArrayList<PTree<T>>();

	public PTree(T t) { // give null for root
		data = t;
	}

	public static <T> PTree<T> root() {
		return new PTree<T>(null);
	}

	public List<PEdge<T>> getAllEdges() {
		List<PEdge<T>> result = new ArrayList<PEdge<T>>();
		for (PTree<T> t : children) {
			result.add(new PEdge<T>(data, t.getData()));
			result.addAll(t.getAllEdges());
		}
		return result;
	}

	public boolean matches(final T t) {
		return (t == data);
	}

	public boolean addPath(Iterator<T> i) {
		// "data" need not to be checked:
		// it obviously WAS in i one step before
		if (!i.hasNext())
			return false;
		T n = i.next();
		for (PTree<T> t : children) {
			if (t.matches(n))
				return t.addPath(i);
		}// if we got here, we could not add anywhere.
			// so add the new element:
		PTree<T> q = new PTree<T>(n);
		children.add(q);
		q.addPath(i); // no matter what result is
		return true;
	}

	public T getData() {
		return data;
	}

	public List<PTree<T>> getChildren() {
		return children;
	}

	public boolean isStub() {
		return children.size() == 0;
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append((null == data) ? "root" : data);
		if (children.size() > 0) {
			result.append("[");
			for (PTree<T> t : children) {
				result.append(t);
			}
			result.append("]");
		} else {
			result.append(";");
		}
		return result.toString();
	}

}

