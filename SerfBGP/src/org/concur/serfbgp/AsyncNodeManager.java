package org.concur.serfbgp;



import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncNodeManager  extends RunnableMsg{
	private ExecutorService exec;
	private List<AsyncNode> ns = new LinkedList<AsyncNode>();
	private static Random rand = new Random(31);
	private static boolean nowAddState = true;
	private static long modifications;
	public static final int NODES=100;
	public AsyncNodeManager(ExecutorService e) {
		exec = e;
	}
	public void add(AsyncNode n) {
			exec.execute(n);
			ns.add(n);
	}
		
		
	@Override
	public String toString() { return "AsyncNodeManager, nodes:"+ns.size(); }

	@Override
	public void runMsg() throws InterruptedException {
		//System.out.println("---------------");
		
		for (AsyncNode n : ns) {
				System.out.print(n.toString());
				System.out.print(" #routes:"+n.getRouteTable().size()+
						//" avg route len:"+n.averageRouteLength() +
						" q size:"+n.inputEventsSize() +
						" neighbours:"+n.neighboursString());
				System.out.println(" ");
		}
		System.out.println("---------------"+NodeEvent.lastCounter()+" "+rand.nextInt(9)+
				" modifications:"+modifications);
		modifications = 0;
		
		if (rand.nextFloat()<0.5) for (int i=0;i<NODES*10;i++) dispatchLinks() ;
		
		TimeUnit.MILLISECONDS.sleep(1000);
	}
	
	public void dispatchLinks() throws InterruptedException {
		if (nowAddState) {
			if (rand.nextFloat()<0.025)  {nowAddState = false;}
		}else {if  (rand.nextFloat()<0.025)  nowAddState = true;}
		int i1 = rand.nextInt(NODES);
		int i2 = rand.nextInt(NODES);
		if (i1 != i2) {
			modifications++;
			if (nowAddState) ns.get(i1).linkTo(ns.get(i2));
					else     ns.get(i1).unlinkFrom(ns.get(i2));
		}
	}
	
	public void dispatchLinks1d() throws InterruptedException {
		if (nowAddState) {
			if (rand.nextFloat()<0.025)  {nowAddState = false;}}
			else {if  (rand.nextFloat()<0.025)  nowAddState = true;}
		int i = rand.nextInt(NODES-1);
		if (nowAddState) ns.get(i).linkTo(ns.get(i+1));
		else     ns.get(i).unlinkFrom(ns.get(i+1));
		modifications++;
		
	}
	
	private boolean checkB(int i) {
		return (i>=0) && (i<NODES);
	}
	public void dispatchLinks15d() throws InterruptedException {

		if (nowAddState) {
			if (rand.nextFloat()<0.025)  {nowAddState = false;}}
			else {if  (rand.nextFloat()<0.025)  nowAddState = true;}
		int i = rand.nextInt(NODES-1);
		int b =3;
		int j = i + rand.nextInt(2*b) - b;
		if ((i!=j) && checkB(j)) {
			modifications++;
			if (nowAddState) ns.get(i).linkTo(ns.get(j));
			else     ns.get(i).unlinkFrom(ns.get(j));
		}
		
	}
}


