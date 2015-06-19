package org.concur.serfbgp;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.*;

@SuppressWarnings("serial")
class Surface extends JPanel implements ActionListener {

    private final int DELAY = 1000;
    private Timer timer;
    private ArrayList<DrawingNode> points =
    		new ArrayList<DrawingNode>(); // gonna pull some readonly shit from it
    
    public void addPoint(DrawingNode n) {
    	points.add(n);
    }
    public Surface(Collection<DrawingNode> nodes) {
    	points = new ArrayList<DrawingNode>(nodes);
        initTimer();
    }   
    

    public Surface() {

        initTimer();
    }

    private void initTimer() {

        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    public Timer getTimer() {
        
        return timer;
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(Color.black);

        int w = getWidth();
        int h = getHeight();
        
        String str = "Size: " +w+"x"+h;

        for (DrawingNode n : points) {
        /*	final Point p = n.getPositionCoords();
        	int x = (int) (p.getX() * w * 0.9);
        	int y = (int) (p.getY() * h * 0.9);
        	int a = 5;
        	g2d.setColor(Color.black);
        	g2d.drawLine(x-a, y, x+a, y);
        	g2d.drawLine(x, y-a, x, y+a);
        	g2d.setColor(Color.gray);
        	g2d.drawString(n.toShortString(), x+a, y+a); */
        	n.doDrawing(g, this);
        	n.drawNeiLinks(g, this);
        }
        
        DrawingNode n = points.get(22);
        n.drawNeiLinks(g, this);

        g2d.drawString(str, 10, 10);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
