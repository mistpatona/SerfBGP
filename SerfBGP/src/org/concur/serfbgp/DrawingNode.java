package org.concur.serfbgp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;



public class DrawingNode extends AsyncNode {
	public Integer getNeighbourPrice(Node n) { //return 100; 
		//return Math.round(this.getGeomDistanceTo((DrawingNode) n)*50);
		return Math.round(this.getBiasedDistanceTo((DrawingNode) n)*50);
	}
	
	
	public void doDrawing(Graphics g, JComponent jc) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.black);

       	final Point c = this.getDrawingCoords(jc);
       	int x = (int) c.getX();
       	int y = (int) c.getY();
       	int a = 5;
       	g2d.setColor(Color.black);
       	g2d.drawLine(x-a, y, x+a, y);
       	g2d.drawLine(x, y-a, x, y+a);
       	g2d.setColor(Color.gray);
       	g2d.drawString(this.toShortString(), x+a, y+a);
    }
	
	public void drawNeiLinks(Graphics g, JComponent jc) {
		Graphics2D g2d = (Graphics2D) g;
		Point p = this.getDrawingCoords(jc);
		int x1 = (int) p.getX();
		int y1 = (int) p.getY();
		for(Node nn : this.getNeighbours()) {
			DrawingNode n = (DrawingNode)nn;// not so fine
			g2d.setColor(Color.green);
			p = n.getDrawingCoords(jc);
			int x2 = (int) p.getX();
			int y2 = (int) p.getY();
			g2d.drawLine(x1,y1,x2,y2);
		}
	}
	
	public void drawRoutes(ArrayList<DrawingNode> ns, Graphics g, JComponent jc) {
		ArrayList<RouteRecord> rs = new ArrayList<RouteRecord>(this.getRouteTable0());//make copy
		PTree<Integer> t = new PTree<Integer>(this.getId()); // creating root of tree
		for (RouteRecord r : rs)	t.addPath(r.path.listIterator());
		for (PEdge<Integer> e : t.getAllEdges()) {
			DrawingNode n1 = ns.get(e.getA());
			DrawingNode n2 = ns.get(e.getB());
			n1.drawDirectLink(n2, g, jc);
		}
		
	}
	
	public void drawDirectLink(DrawingNode n, Graphics g, JComponent jc){
		Graphics2D g2d = (Graphics2D) g;
		Point p = this.getDrawingCoords(jc);
		int x1 = (int) p.getX();
		int y1 = (int) p.getY();
		p = n.getDrawingCoords(jc);
		int x2 = (int) p.getX();
		int y2 = (int) p.getY();
		g2d.setColor(Color.blue);
		g2d.drawLine(x1,y1,x2,y2);
	}
	
	public Point getDrawingCoords(JComponent jc) {
		int w = jc.getWidth();
        int h = jc.getHeight();
       	final Point c = this.getPositionCoords();
       	float x = (float) (c.getX() * w * 0.9);
       	float y = (float) (c.getY() * h * 0.9);
       	return new Point(x,y);
	}

}
