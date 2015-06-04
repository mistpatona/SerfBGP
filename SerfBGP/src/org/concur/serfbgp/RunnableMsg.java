package org.concur.serfbgp;
// copied from my "Teller" project

	// runs code in a terminable thread 
	// and prints some description on that thread status
public abstract class RunnableMsg implements Runnable {
	public abstract String toString();
	public abstract void runMsg() throws InterruptedException;
	public void run() {
			System.out.println(this.toString() + " started");
			try {
		       while(!Thread.interrupted()) {
		    	   runMsg();
		       }
		    }
			catch(InterruptedException e) {
		    	      System.out.println(this.toString() + " interrupted");
		    }
		    System.out.println(this.toString() + " terminating");
	}

}
