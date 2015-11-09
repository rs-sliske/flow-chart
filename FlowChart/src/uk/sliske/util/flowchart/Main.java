package uk.sliske.util.flowchart;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import uk.sliske.graphics.rendering.software.Frame;
import uk.sliske.graphics.rendering.software.GraphicsMGR;
import uk.sliske.util.flowchart.nodes.Node;

public class Main implements KeyListener {

	private Node	root;
	private Frame	frame;
	private long startTime;
	private long timeOffset;


	private Main(final String title) {
		root = new Node(title, Node.TYPE.TITLE, null);
		
		Node inside = new Node("inside a dungeon", Node.TYPE.CONDITION, root);
		new Node("in waiting area", Node.TYPE.CONDITION, inside);
		new Node("do floor", Node.TYPE.ACTION, inside);

		GraphicsMGR mgr = new GraphicsMGR(1280, 960);

		frame = new Frame("dg flow chart", mgr.getCanvas());
		mgr.getCanvas().addKeyListener(this);

		startTime = System.currentTimeMillis();
		timeOffset = 0;
		
		mgr.getContext().clear(0x00);

		while (loop(mgr) && frame.frame.isActive()) {
			
		}
	}

	private boolean loop(GraphicsMGR mgr) {
		if(!frame.frame.isVisible()){
			return false;
		}
		long currentTime = System.currentTimeMillis();
		long delta = currentTime - startTime;
		
		update(mgr);
		if(delta > timeOffset){
			render(mgr);
			timeOffset += 1000;
		}
		
		return frame.frame.isVisible();
	}

	private void update(GraphicsMGR mgr) {

	}

	private void render(GraphicsMGR mgr) {
		root.render(mgr);
		mgr.update();
	}

	public static void main(String[] args) {
		new Main("DG");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				root.move(-1, 0, true);
				break;
			case KeyEvent.VK_RIGHT:
				root.move(1, 0, true);
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
