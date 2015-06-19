package org.concur.serfbgp;

public class PEdge<T> {
	T a,b;
	PEdge(T p,T q) {
		a=p;
		b=q;
	}
	public T getA() { return a;}
	public T getB() { return b;}
	public String toString() {
		return a + "->" + b;
	}
}
