package org.concur.serfbgp;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GraphDisplayer  extends JFrame {

	    public GraphDisplayer(Collection<DrawingNode> nodes) {

	        initUI(nodes);
	    }

	    private void initUI(Collection<DrawingNode> nodes) {

	        final Surface surface = new Surface(nodes);
	        add(surface);

	        addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e) {
	                Timer timer = surface.getTimer();
	                timer.stop();
	            }
	        });

	        setTitle("Points");
	        setSize(500, 450);
	        setLocationRelativeTo(null);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }
	    
}